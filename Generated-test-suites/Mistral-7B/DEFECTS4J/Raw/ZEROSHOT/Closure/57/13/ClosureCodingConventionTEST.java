import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import java.util.List;

public class ClosureCodingConventionTest {

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = createFunctionType("ParentCtor", ImmutableSet.of());
    FunctionType childCtor = createFunctionType("ChildCtor", ImmutableSet.of());
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals(parentCtor.getPrototype(), childCtor.getDeclaredProperty("superClass_"));
    assertEquals(childCtor, childCtor.getDeclaredProperty("constructor"));
  }

  @org.junit.Test
  public void testApplySubclassRelationshipMixin() {
    FunctionType parentPrototype = createFunctionType("ParentPrototype", ImmutableSet.of());
    FunctionType childPrototype = createFunctionType("ChildPrototype", ImmutableSet.of());
    SubclassType type = SubclassType.MIXIN;

    ClosureCodingConvention.applySubclassRelationship(parentPrototype, childPrototype, type);

    assertEquals(parentPrototype, childPrototype.getPrototype());
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    String propertyName = "superClass_";

    assertTrue(ClosureCodingConvention.isSuperClassReference(propertyName));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = createNode("goog.provide('X')");

    assertEquals("X", ClosureCodingConvention.extractClassNameIfProvide(node, null));
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = createNode("goog.require('X')");

    assertEquals("X", ClosureCodingConvention.extractClassNameIfRequire(node, null));
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty",
        ClosureCodingConvention.getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol",
        ClosureCodingConvention.getExportSymbolFunction());
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createNode("goog.addDependency('X')");

    List<String> result = ClosureCodingConvention.identifyTypeDeclarationCall(node);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("X", result.get(0));
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node node = createNode("goog.isDef(x)");

    assertTrue(ClosureCodingConvention.isPropertyTestFunction(node));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node node = createNode("goog.reflect.object.cast(x, MyType)");
    ObjectLiteralCast expected = new ObjectLiteralCast("MyType", node.getNext());

    ObjectLiteralCast actual = ClosureCodingConvention.getObjectLiteralCast(
        new NodeTraversal(null), node);

    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    Node node = createNode("function f(p) {}");

    assertFalse(ClosureCodingConvention.isOptionalParameter(node.getChildren().get(0)));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    Node node = createNode("function f(p, args) {}");

    assertFalse(ClosureCodingConvention.isVarArgsParameter(node.getChildren().get(1)));
  }

  @org.junit.Test
  public void testIsPrivate() {
    assertFalse(ClosureCodingConvention.isPrivate("foo"));
  }

  @org.junit.Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> functions = ClosureCodingConvention.getAssertionFunctions();

    for (AssertionFunctionSpec function : functions) {
      System.out.println(function.getName() + "(" + function.getType() + ")");
    }
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    Node node = createNode("goog.bind(function f(x) {}, thisValue, args)");

    Bind expected = new Bind(
        node.getFirstChild(), node.getFirstChild().getNext(), node.getLastChild());

    Bind actual = ClosureCodingConvention.describeFunctionBind(node);

    assertEquals(expected.getFunction(), actual.getFunction());
    assertEquals(expected.getThisValue(), actual.getThisValue());
    assertEquals(expected.getParameters(), actual.getParameters());
  }

  @org.junit.Test
  public void testDescribeFunctionBindPartial() {
    Node node = createNode("goog.partial(function f(x, y) {}, y, args)");

    Bind expected = new Bind(
        node.getFirstChild(), null, node.getLastChild());

    Bind actual = ClosureCodingConvention.describeFunctionBind(node);

    assertEquals(expected.getFunction(), actual.getFunction());
    assertEquals(expected.getThisValue(), actual.getThisValue());
    assertEquals(expected.getParameters(), actual.getParameters());
  }

  private FunctionType createFunctionType(String name, ImmutableSet<String> arguments) {
    ObjectType objectType = new ObjectType(name, ImmutableList.of());
    FunctionType result = new FunctionType(name, arguments, null, null, null,
        null, null, null, null, objectType, null, null, null);

    return result;
  }

  private Node createNode(String jsCode) {
    Node node = new Node(Token.SCRIPTLET, null, null, null);
    NodeTraversal traversal = new NodeTraversal(node);
    NodeUtil.parse(traversal, jsCode, null);

    return node;
  }
}