import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.SubclassType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import java.util.List;

public class ClosureCodingConventionTest {

  @org.junit.Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType("ParentCtor", null, ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", null, ImmutableList.of());
    SubclassType type = SubclassType.INHERITS;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType expectedParentPrototype = new FunctionType("ParentCtor.prototype", null, ImmutableList.of());
    FunctionType expectedChildPrototype = new FunctionType("ChildCtor.prototype", null, ImmutableList.of());

    assertEquals(expectedParentPrototype, childCtor.getPrototype());
    assertEquals(expectedChildPrototype, parentCtor.getPrototype());
  }

  @org.junit.Test
  public void testApplySubclassRelationship_Mixin() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType("ParentCtor", null, ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", null, ImmutableList.of());
    SubclassType type = SubclassType.MIXIN;

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType expectedParentPrototype = new FunctionType("ParentCtor.prototype", null, ImmutableList.of());
    FunctionType expectedChildPrototype = new FunctionType("ChildCtor.prototype", null, ImmutableList.of());

    assertEquals(expectedParentPrototype, childCtor.getPrototype());
    assertEquals(expectedChildPrototype, parentCtor.getPrototype());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_InvalidCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType parentCtor = new FunctionType("ParentCtor", null, ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", null, ImmutableList.of());

    codingConvention.applySubclassRelationship(parentCtor, childCtor, null);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String expected = "MyClass";
    Node node = Node.newNode(Token.CALL, new Node[]{"goog.require",
        Node.newNode(Token.STRING, expected)});

    String result = codingConvention.extractClassNameIfRequire(node, null);

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire_WithParent() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String expected = "MyClass";
    Node node = Node.newNode(Token.CALL, new Node[]{
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Node.newNode(Token.STRING, "require"),
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "MyClass"))});

    String result = codingConvention.extractClassNameIfRequire(node, Node.newNode(Token.NAME, "MyModule"));

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String expected = "MyClass";
    Node node = Node.newNode(Token.CALL, new Node[]{
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Node.newNode(Token.STRING, "provide"),
        Node.newNode(Token.ARRAYLIT, new Node[]{
            Node.newNode(Token.STRING, "MyClass")})});

    String result = codingConvention.extractClassNameIfProvide(node, null);

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String expected = "goog.exportProperty";

    String result = codingConvention.getExportPropertyFunction();

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String expected = "MySingletonGetter";
    Node node = Node.newNode(Token.CALL, new Node[]{
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Node.newNode(Token.STRING, "addSingletonGetter"),
        Node.newNode(Token.STRING, expected)});

    String result = codingConvention.getSingletonGetterClassName(node);

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = Node.newNode(Token.CALL, new Node[]{
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Node.newNode(Token.STRING, "addDependency"),
        Node.newNode(Token.ARRAYLIT, new Node[]{
            Node.newNode(Token.STRING, "MyClass"),
            Node.newNode(Token.ARRAYLIT, new Node[]{
                Node.newNode(Token.STRING, "MyType1"),
                Node.newNode(Token.STRING, "MyType2")})})});

    List<String> result = codingConvention.identifyTypeDeclarationCall(node);

    List<String> expected = ImmutableList.of("MyType1", "MyType2");

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = Node.newNode(Token.CALL, new Node[]{
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Node.newNode(Token.STRING, "isDef")});

    boolean result = codingConvention.isPropertyTestFunction(node);

    assertTrue(result);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction_InvalidCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    Node node = Node.newNode(Token.CALL, new Node[]{
        Node.newNode(Token.NAME, "myFunction"),
        Node.newNode(Token.STRING, "isDef")});

    boolean result = codingConvention.isPropertyTestFunction(node);

    assertFalse(result);
  }

  @org.junit.Test
  public void testObjectLiteralCast() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    NodeTraversal traversal = new NodeTraversal() {};
    Node node = Node.newNode(Token.CALL, new Node[]{
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Node.newNode(Token.STRING, "reflect"),
        Node.newNode(Token.STRING, "object"),
        Node.newNode(Token.OBJECTLIT, new Node[]{
            Node.newNode(Token.STRING, "name"),
            Node.newNode(Token.NUMBER, 42)})});

    ObjectLiteralCast result = codingConvention.getObjectLiteralCast(traversal, node);

    assertEquals("goog.reflect.object", result.getTypeName());
    assertEquals("name", result.getPropertyName());
  }

  @org.junit.Test
  public void testObjectLiteralCast_Null() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    NodeTraversal traversal = new NodeTraversal() {};
    Node node = Node.newNode(Token.CALL, new Node[]{
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog")),
        Node.newNode(Token.STRING, "reflect"),
        Node.newNode(Token.STRING, "object"),
        Node.newNode(Token.NULL)});

    ObjectLiteralCast result = codingConvention.getObjectLiteralCast(traversal, node);

    assertNull(result);
  }

  @org.junit.Test
  public void testPropertyTestFunctions() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    List<String> result = codingConvention.getAssertionFunctions().stream()
        .map(assertionFunctionSpec -> assertionFunctionSpec.getName()).collect(Collectors.toList());

    List<String> expected = ImmutableList.of(
        "goog.asserts.assert",
        "goog.asserts.assertNumber",
        "goog.asserts.assertString",
        "goog.asserts.assertFunction",
        "goog.asserts.assertObject",
        "goog.asserts.assertArray",
        "goog.asserts.assertInstanceof");

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testSingletonGetter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    FunctionType getterType = new FunctionType("MyGetter", null, ImmutableList.of());
    ObjectType objectType = new ObjectType("MyObject", null);

    codingConvention.applySingletonGetter(NodeUtil.createFunctionType("MyFunction", null, ImmutableList.of()),
        getterType, objectType);

    FunctionType expectedFunctionType = new FunctionType("MyFunction", null, ImmutableList.of());
    FunctionType expectedGetterType = new FunctionType("MyFunction.getInstance", null, ImmutableList.of());
    ObjectType expectedObjectType = new ObjectType("MyFunction.instance_", null);

    assertEquals(expectedFunctionType, expectedFunctionType);
    assertEquals(expectedGetterType, getterType);
    assertEquals(expectedObjectType, objectType);
  }
}