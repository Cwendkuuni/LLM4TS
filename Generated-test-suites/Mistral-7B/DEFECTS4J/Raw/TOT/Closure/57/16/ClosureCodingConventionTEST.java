import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class ClosureCodingConventionTest {

  private static ClosureCodingConvention codingConvention = new ClosureCodingConvention();

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", null, null);
    FunctionType childCtor = new FunctionType("ChildCtor", null, null);
    SubclassType type = SubclassType.INHERITS;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertNotNull(childCtor.getPrototype().getProperty("constructor"));
    assertEquals(parentCtor, childCtor.getPrototype().getProperty("constructor"));
    assertNotNull(childCtor.getPrototype().getProperty("superClass_"));
    assertEquals(parentCtor, childCtor.getPrototype().getProperty("superClass_"));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('X')");
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('X')");
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
    assertFalse(codingConvention.isSuperClassReference("foo"));
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = codingConvention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", exportPropertyFunction);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("FunctionType", null, null);
    FunctionType getterType = new FunctionType("FunctionType.getInstance",
        new ObjectType("FunctionType"), null);
    ObjectType objectType = new ObjectType("FunctionType");

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    assertNotNull(functionType.getProperty("getInstance"));
    assertNotNull(functionType.getProperty("instance_"));
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createNode("goog.addDependency('X')");
    List<String> types = codingConvention.identifyTypeDeclarationCall(node);
    assertNotNull(types);
    assertEquals(1, types.size());
    assertEquals("X", types.get(0));
  }

  @org.junit.Test
  public void testGetAbstractMethodName() {
    String abstractMethodName = codingConvention.getAbstractMethodName();
    assertEquals("goog.abstractMethod", abstractMethodName);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node node = createNode("goog.addSingletonGetter(X, X)");
    String className = codingConvention.getSingletonGetterClassName(node);
    assertEquals("X", className);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = createNode("goog.isDef(foo)");
    assertTrue(codingConvention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testObjectLiteralCast() {
    Node node = createNode(
        "goog.reflect.object.getPrototypeOf(goog.reflect.object.cast(" +
        "    {'a': 1, 'b': 2}, FunctionType))");
    ObjectLiteralCast cast =
        codingConvention.getObjectLiteralCast(null, node);
    assertEquals(FunctionType.class.getName(), cast.getType());
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    Node parameter = createNode("foo?");
    assertFalse(codingConvention.isOptionalParameter(parameter));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    Node parameter = createNode("foo(...args)");
    assertFalse(codingConvention.isVarArgsParameter(parameter));
  }

  @org.junit.Test
  public void testIsPrivate() {
    String name = "foo_";
    assertFalse(codingConvention.isPrivate(name));
  }

  @org.junit.Test
  public void testPropertyTestFunctions() {
    Node node = createNode("goog.isNumber(1)");
    assertTrue(codingConvention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testBind() {
    Node node = createNode("goog.bind(function(x) { return x*2; }, this, 5)");
    Bind bind = codingConvention.describeFunctionBind(node);
    assertEquals(5, bind.getThisValue());
    assertEquals(1, bind.getArguments().size());
    assertEquals(1, bind.getArguments().get(0).getIndex());
  }

  private Node createNode(String code) {
    Node node = NodeOperations.parseJsExpression(code);
    return node;
  }

  private static final class SubclassType {
    static final SubclassType INHERITS = new SubclassType("INHERITS");
    static final SubclassType MIXIN = new SubclassType("MIXIN");

    private final String name;

    private SubclassType(String name) {
      this.name = name;
    }
  }

}