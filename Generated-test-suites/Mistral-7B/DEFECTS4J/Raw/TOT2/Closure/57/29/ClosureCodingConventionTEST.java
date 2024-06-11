import static com.google.javascript.jscomp.ClosureCodingConvention.*;
import static org.junit.Assert.*;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", new ObjectType("ParentClass"));
    FunctionType childCtor = new FunctionType("ChildCtor", new ObjectType("ChildClass"));
    SubclassType type = SubclassType.INHERITS;
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);
    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().constructor);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = new Node("goog.provide('X')");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = new Node("goog.require('X')");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = new Node("goog.inherits(Child, SuperClass)");
    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals("Child", relationship.getSubclass());
    assertEquals("SuperClass", relationship.getSuperclass());
  }

  @Test
  public void testExtractClassNameIfGoog() {
    Node node = new Node("goog.require('X')");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = new Node("a.b.c.prototype");
    boolean result = new ClosureCodingConvention().endsWithPrototype(qualifiedName);
    assertTrue(result);
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(new ClosureCodingConvention().isSuperClassReference("superClass_"));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = new Node("goog.addDependency('X', [Type1, Type2])");
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(2, typeNames.size());
    assertEquals("Type1", typeNames.get(0));
    assertEquals("Type2", typeNames.get(1));
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod", new ClosureCodingConvention().getAbstractMethodName());
  }

  @Test
  public void testObjectLiteralCast() {
    Node objectNode = new Node("{foo: 'bar'}");
    ObjectLiteralCast cast = new ClosureCodingConvention().getObjectLiteralCast(null, new Node("goog.reflect.object(X)"));
    assertEquals("X", cast.getType());
    assertEquals(objectNode, cast.getValue());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node call = new Node("goog.isDef(X)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(call));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("GetterFunction", new ObjectType("GetterFunctionClass"));
    FunctionType getterType = new FunctionType("Getter", new ObjectType("GetterClass"));
    ObjectType objectType = new ObjectType("ObjectClass");
    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);
    assertTrue(functionType.hasProperty("getInstance"));
    assertTrue(functionType.hasProperty("instance_"));
  }

  @Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", new ClosureCodingConvention().getGlobalObject());
  }
}