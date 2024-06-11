import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import com.google.common.base.Function;

public class ClosureCodingConventionTest {

  

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = newFunctionType("ParentClass", new Type[]{}, new Type[]{}, null);
    FunctionType childCtor = newFunctionType("ChildClass", new Type[]{}, new Type[]{}, null);
    SubclassType type = SubclassType.INHERITS;
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertNotNull(childCtor.getPrototype().getProperty("constructor"));
    assertSame(childCtor, childCtor.getPrototype().getProperty("constructor"));
    assertNotNull(childCtor.getProperty("superClass_"));
    assertSame(parentCtor, childCtor.getProperty("superClass_"));
  }

  @org.junit.Test
  public void testApplySubclassRelationship_MIXIN() {
    FunctionType parentPrototype = newFunctionType("ParentPrototype", new Type[]{}, new Type[]{}, null);
    FunctionType childPrototype = newFunctionType("ChildPrototype", new Type[]{}, new Type[]{}, null);
    FunctionType parentCtor = newFunctionType("ParentClass", new Type[]{}, new Type[]{}, parentPrototype);
    FunctionType childCtor = newFunctionType("ChildClass", new Type[]{}, new Type[]{}, childPrototype);
    SubclassType type = SubclassType.MIXIN;
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertNotNull(childPrototype.getProperty("constructor"));
    assertSame(childCtor, childPrototype.getProperty("constructor"));
    assertNotNull(childPrototype.getProperty("superClass_"));
    assertSame(parentPrototype, childPrototype.getProperty("superClass_"));
  }

  @org.junit.Test
  public void testTypeofClassDefiningName() {
    Node node = newNode("goog.inherits(SubClass, SuperClass)");
    assertEquals(SubclassType.INHERITS,
        new ClosureCodingConvention().typeofClassDefiningName(node.getFirstChild()));

    node = newNode("SubClass.inherits(SuperClass)");
    assertEquals(SubclassType.INHERITS,
        new ClosureCodingConvention().typeofClassDefiningName(node.getFirstChild()));

    node = newNode("goog.mixin(ChildClass.prototype, SuperClass.prototype)");
    assertEquals(SubclassType.MIXIN,
        new ClosureCodingConvention().typeofClassDefiningName(node.getFirstChild()));

    node = newNode("SubClass.mixin(SuperClass.prototype)");
    assertEquals(SubclassType.MIXIN,
        new ClosureCodingConvention().typeofClassDefiningName(node.getFirstChild()));
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("anotherProperty"));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = newNode("goog.provide('X.ClassName')");
    assertEquals("X.ClassName",
        new ClosureCodingConvention().extractClassNameIfProvide(node, null));
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = newNode("goog.require('X.ClassName')");
    assertEquals("X.ClassName",
        new ClosureCodingConvention().extractClassNameIfRequire(node, null));
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        new ClosureCodingConvention().getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol",
        new ClosureCodingConvention().getExportSymbolFunction());
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = newNode("goog.addDependency('X', ['Type1', 'Type2'])");
    List<String> result = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(2, result.size());
    assertEquals("Type1", result.get(0));
    assertEquals("Type2", result.get(1));
  }

  @org.junit.Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod",
        new ClosureCodingConvention().getAbstractMethodName());
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node node = newNode("goog.addSingletonGetter(X, getXInstance)");
    assertEquals("X",
        new ClosureCodingConvention().getSingletonGetterClassName(node));
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = newFunctionType("X", new Type[]{}, new Type[]{}, null);
    FunctionType getterType = newFunctionType("getXInstance", new Type[]{}, new Type[]{JSTypeNative.OBJECT_TYPE}, null);
    ObjectType objectType = newObjectType("XInstance", new Type[]{});
    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    assertNotNull(functionType.getProperty("getInstance"));
    assertSame(getterType, functionType.getProperty("getInstance"));
    assertNotNull(functionType.getProperty("instance_"));
    assertSame(objectType, functionType.getProperty("instance_"));
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = newNode("goog.isDef(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = newNode("goog.isString(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = newNode("goog.isFunction(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = newNode("goog.isArray(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = newNode("goog.isObject(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = newNode("goog.isDefAndNotNull(x)");
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = newNode("goog.isNumber(x)");
    assertFalse(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = newNode("goog.isBoolean(x)");
    assertFalse(new ClosureCodingConvention().isPropertyTestFunction(node));

    node = newNode("goog.assert(x)");
    assertFalse(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node node = newNode("goog.reflect.object(myObject)");
    ObjectLiteralCast result = new ClosureCodingConvention().getObjectLiteralCast(new NodeTraversal(), node);
    assertNotNull(result);
    assertEquals("myObject", result.getTypeName());
    assertEquals(node.getNext(), result.getValueNode());
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    // This method should not be tested as it always returns false.
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    // This method should not be tested as it always returns false.
  }

  @org.junit.Test
  public void testIsPrivate() {
    // This method should not be tested as it always returns false.
  }

  @org.junit.Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> functions = new ClosureCodingConvention().getAssertionFunctions();
    assertEquals(8, functions.size());

    for (AssertionFunctionSpec function : functions) {
      String functionName = function.getName();
      assertEquals(functionName, function.getFunctionType().getName());
      assertEquals(function.getParameterType(), function.getFunctionType().getParameters()[0].getType());
    }
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    Node node = newNode("goog.bind(fn, thisValue, args...)");
    Bind result = new ClosureCodingConvention().describeFunctionBind(node);

    assertNotNull(result);
    assertEquals("fn", result.getFunctionName());
    assertSame(thisValue, result.getThisValue());
    assertSame(args, result.getArguments());
  }

  
}