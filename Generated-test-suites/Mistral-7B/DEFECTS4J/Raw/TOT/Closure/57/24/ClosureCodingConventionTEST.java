import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType parentCtor = createMockFunctionType("ParentClass");
    FunctionType childCtor = createMockFunctionType("ChildClass");

    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    assertTrue(childCtor.hasProperty("superClass_"));
    assertTrue(childCtor.hasProperty("constructor"));
    assertTrue(childCtor.getPrototype().hasProperty("constructor"));
    assertEquals(parentCtor, childCtor.getPrototype().get("constructor"));

    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);

    assertTrue(childCtor.hasProperty("prototype"));
    assertTrue(childCtor.getPrototype().hasProperty("constructor"));
    assertTrue(childCtor.getPrototype().hasProperty("superClass_"));
    assertEquals(parentCtor, childCtor.getPrototype().get("superClass_"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createMockNode("goog.provide('X')");
    String className = codingConvention.extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createMockNode("goog.require('X')");
    String className = codingConvention.extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    String exportPropertyFunction = codingConvention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", exportPropertyFunction);
  }

  @Test
  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node callNode = createMockNode("goog.addSingletonGetter(X, getX)");
    String className = codingConvention.getSingletonGetterClassName(callNode);
    assertEquals("X", className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node qualifiedName = createMockNode("a.b.c.prototype");
    assertTrue(codingConvention.endsWithPrototype(qualifiedName));
  }

  @Test
  public void testPropertyTestFunctions() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node callNode = createMockNode("goog.isString(someValue)");
    assertTrue(codingConvention.isPropertyTestFunction(callNode));
  }

  @Test
  public void testObjectLiteralCast() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    NodeTraversal t = createMockNodeTraversal();
    Node callNode = createMockNode("goog.reflect.object.cast(someObject, MyType)");
    ObjectLiteralCast cast = codingConvention.getObjectLiteralCast(t, callNode);
    assertEquals("MyType", cast.getType());
  }

  @Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node parameter = createMockNode("param");
    assertFalse(codingConvention.isOptionalParameter(parameter));
  }

  @Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node parameter = createMockNode("params...");
    assertFalse(codingConvention.isVarArgsParameter(parameter));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertFalse(codingConvention.isPrivate("privateVariable"));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = createMockNode("goog.addDependency('X', [MyType1, MyType2])");
    List<String> typeNames = codingConvention.identifyTypeDeclarationCall(node);
    assertEquals(2, typeNames.size());
    assertEquals("MyType1", typeNames.get(0));
    assertEquals("MyType2", typeNames.get(1));
  }

  private FunctionType createMockFunctionType(String name) {
    ObjectType objectType = new ObjectType();
    FunctionType functionType = new FunctionType(name, ImmutableList.of(), ImmutableList.of(objectType));
    return functionType;
  }

  private Node createMockNode(String nodeString) {
    return new Node(new Token(Token.NEWLINE, null, nodeString, 0, 0));
  }

  private NodeTraversal createMockNodeTraversal() {
    return new NodeTraversal() {};
  }

  private enum SubclassType {
    INHERITS,
    MIXIN
  }
}