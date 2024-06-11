import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = newFunctionType("ParentClass", new ClassType[0]);
    FunctionType childCtor = newFunctionType("ChildClass", new ClassType[0]);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    ObjectType prototype = childCtor.getPrototype();
    assertEquals("constructor", childCtor, prototype.getDeclaredProperty("constructor"));
    assertEquals("superClass_", parentCtor, prototype.getDeclaredProperty("superClass_"));
  }

  @Test
  public void testSubclassRelationshipsWithPrototype() {
    FunctionType parentCtor = newFunctionType("ParentClass", new ClassType[0]);
    FunctionType childCtor = newFunctionType("ChildClass", new ClassType[0]);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(
        newNodeFromString("goog.inherits(ChildClass, ParentClass.prototype)"));
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("ChildClass", relationship.getSubclass());
    assertEquals("ParentClass", relationship.getSuperclass());
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = newFunctionType("getter", new ClassType[0]);
    ObjectType objectType = newObjectType("ObjectType");
    FunctionType functionType = newFunctionType("FunctionType", new ClassType[0]);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals("getInstance", functionType.getDeclaredProperty("getInstance"));
    assertNotNull(functionType.getDeclaredProperty("instance_"));
  }

  @Test
  public void testExtractClassNameIfGoogProvide() {
    Node node = newNodeFromString("goog.provide('MyClassName')");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("MyClassName", className);
  }

  @Test
  public void testExtractClassNameIfGoogRequire() {
    Node node = newNodeFromString("goog.require('MyClassName')");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("MyClassName", className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = newNodeFromString("a.b.c.prototype");
    assertTrue(new ClosureCodingConvention().endsWithPrototype(node));
  }

  @Test
  public void testExtractTypeDeclarationCall() {
    Node node = newNodeFromString("goog.addDependency(['MyClass', 'MyOtherClass'])");
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(2, typeNames.size());
    assertTrue(typeNames.contains("MyClass"));
    assertTrue(typeNames.contains("MyOtherClass"));
  }

  @Test
  public void testGetAbstractMethodName() {
    String abstractMethodName = new ClosureCodingConvention().getAbstractMethodName();
    assertEquals("goog.abstractMethod", abstractMethodName);
  }

  @Test
  public void testSingletonGetterClassName() {
    Node node = newNodeFromString("goog.addSingletonGetter(MyClassName, getInstance)");
    String className = new ClosureCodingConvention().getSingletonGetterClassName(node);
    assertEquals("getInstance", className);
  }

  @Test
  public void testApplySingletonGetterWrongOrder() {
    FunctionType getterType = newFunctionType("getter", new ClassType[0]);
    ObjectType objectType = newObjectType("ObjectType");
    FunctionType functionType = newFunctionType("FunctionType", new ClassType[0]);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    try {
      convention.applySingletonGetter(objectType, getterType, functionType);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = newNodeFromString("goog.isDef(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testObjectLiteralCast() {
    FunctionType objectType = newFunctionType("ObjectType", new ClassType[0]);
    Node node = newNodeFromString("goog.reflect.object.cast(x, ObjectType)");
    ObjectLiteralCast objectLiteralCast = new ClosureCodingConvention().getObjectLiteralCast(null, node);
    assertEquals("ObjectType", objectLiteralCast.getType());
    assertEquals("x", objectLiteralCast.getValue());
  }

  @Test(expected = AssertionError.class)
  public void testObjectLiteralCastNoObject() {
    FunctionType objectType = newFunctionType("ObjectType", new ClassType[0]);
    Node node = newNodeFromString("goog.reflect.object.cast(1, ObjectType)");
    new ClosureCodingConvention().getObjectLiteralCast(null, node);
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = newNodeFromString("function foo(x = 0) {}");
    assertFalse(new ClosureCodingConvention().isOptionalParameter(node.getFirstChild()));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = newNodeFromString("function foo(args) {}");
    assertFalse(new ClosureCodingConvention().isVarArgsParameter(node.getFirstChild()));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPrivate("myVariable"));
  }

  // Error scenarios

  @Test(expected = NullPointerException.class)
  public void testNullNode() {
    new ClosureCodingConvention().endsWithPrototype(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSubclassRelationship() {
    FunctionType parentCtor = newFunctionType("ParentClass", new ClassType[0]);
    FunctionType childCtor = newFunctionType("ChildClass", new ClassType[0]);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(childCtor, parentCtor, SubclassType.MIXIN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSubclassRelationshipWithPrototype() {
    FunctionType parentCtor = newFunctionType("ParentClass", new ClassType[0]);
    FunctionType childCtor = newFunctionType("ChildClass", new ClassType[0]);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(childCtor, parentCtor.getPrototype(), SubclassType.INHERITS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplySingletonGetterNullObjectType() {
    FunctionType getterType = newFunctionType("getter", new ClassType[0]);
    ObjectType objectType = null;
    FunctionType functionType = newFunctionType("FunctionType", new ClassType[0]);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplySingletonGetterNonFunctionType() {
    FunctionType getterType = newFunctionType("getter", new ClassType[0]);
    ObjectType objectType = newObjectType("ObjectType");
    ObjectType functionType = newObjectType("ObjectType");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(functionType, getterType, objectType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplySingletonGetterWrongNumberOfArguments() {
    FunctionType getterType = newFunctionType("getter", new ClassType[0]);
    ObjectType objectType = newObjectType("ObjectType");
    FunctionType functionType = newFunctionType("FunctionType", new ClassType[0]);
    FunctionType instanceType = newFunctionType("InstanceType", new ClassType[0]);

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(instanceType, getterType, functionType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplySingletonGetterWrongFunctionType() {
    FunctionType getterType = newFunctionType("getter", new ClassType[0]);
    ObjectType objectType = newObjectType("ObjectType");
    ObjectType functionType = newObjectType("ObjectType");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySingletonGetter(objectType, getterType, functionType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetObjectLiteralCastInvalidType() {
    FunctionType objectType = newFunctionType("ObjectType", new ClassType[0]);
    Node node = newNodeFromString("goog.reflect.object.cast(1, ObjectType)");
    new ClosureCodingConvention().getObjectLiteralCast(null, node);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetObjectLiteralCastInvalidValue() {
    FunctionType objectType = newFunctionType("ObjectType", new ClassType[0]);
    Node node = newNodeFromString("goog.reflect.object.cast(null, ObjectType)");
    new ClosureCodingConvention().getObjectLiteralCast(null, node);
  }
}