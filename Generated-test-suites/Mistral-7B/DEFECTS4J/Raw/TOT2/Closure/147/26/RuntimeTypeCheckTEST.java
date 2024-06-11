import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {

  @Test
  public void testBasicFunctionType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node rootNode = Node.newScript("");
    runtimeTypeCheck.process(null, rootNode);

    FunctionType functionType = (FunctionType) NodeUtil.getFunctionType(
        rootNode, "testFunction");

    Node functionCallNode = Node.newExprStatement(
        new Node(Token.CALL, NodeUtil.getFunctionNode("testFunction"), null));

    Node result = NodeUtil.runScriptAndReturnRoot(
        NodeUtil.getSourceForNode(functionCallNode), rootNode);

    assertTrue(functionType.getReturnType().isInstanceOf(JSType.getType(Node.INT_TYPE)));

    // Basic functionality test
    Node argumentNode = Node.newNumberLiteral(10);
    Node functionCallNodeWithArgument = new Node(
        Token.CALL, NodeUtil.getFunctionNode("testFunction"), argumentNode);
    Node resultWithArgument = NodeUtil.runScriptAndReturnRoot(
        NodeUtil.getSourceForNode(functionCallNodeWithArgument), rootNode);

    assertEquals(10, resultWithArgument.getFirstChild().getNumber().intValueExact());
  }

  @Test(expected = AssertionError.class)
  public void testFunctionTypeWithWrongArgumentType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node rootNode = Node.newScript("");
    runtimeTypeCheck.process(null, rootNode);

    FunctionType functionType = (FunctionType) NodeUtil.getFunctionType(
        rootNode, "testFunction");

    Node functionCallNode = Node.newExprStatement(
        new Node(Token.CALL, NodeUtil.getFunctionNode("testFunction"), null));

    Node argumentNode = Node.newBooleanLiteral(false);
    Node functionCallNodeWithArgument = new Node(
        Token.CALL, NodeUtil.getFunctionNode("testFunction"), argumentNode);

    try {
      Node resultWithArgument = NodeUtil.runScriptAndReturnRoot(
          NodeUtil.getSourceForNode(functionCallNodeWithArgument), rootNode);
      fail("Expected AssertionError but got: " + resultWithArgument);
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testClassType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node rootNode = Node.newScript("");
    runtimeTypeCheck.process(null, rootNode);

    ObjectType objectType = (ObjectType) NodeUtil.getTypeByName(
        rootNode, "MyClass");

    Node instanceNode = Node.newObjectLiteral(Node.newQualifiedNameNode("MyClass"));
    Node functionCallNode = new Node(Token.CALL,
        jsCode("instanceChecker"), NodeUtil.getMemberExpression(instanceNode, "prototype"));

    Node result = NodeUtil.runScriptAndReturnRoot(
        NodeUtil.getSourceForNode(functionCallNode), rootNode);

    assertTrue(result.isTrue());

    // Basic functionality test
    Node anotherInstanceNode = Node.newObjectLiteral(Node.newQualifiedNameNode("MyClass"));
    Node anotherFunctionCallNode = new Node(Token.CALL,
        jsCode("instanceChecker"), NodeUtil.getMemberExpression(anotherInstanceNode, "prototype"));

    Node anotherResult = NodeUtil.runScriptAndReturnRoot(
        NodeUtil.getSourceForNode(anotherFunctionCallNode), rootNode);

    assertTrue(anotherResult.isTrue());

    // Edge case: null check
    Node nullInstanceNode = Node.newNull();
    Node nullFunctionCallNode = new Node(Token.CALL,
        jsCode("instanceChecker"), NodeUtil.getMemberExpression(nullInstanceNode, "prototype"));

    Node nullResult = NodeUtil.runScriptAndReturnRoot(
        NodeUtil.getSourceForNode(nullFunctionCallNode), rootNode);

    assertFalse(nullResult.isTrue());
  }

  @Test(expected = AssertionError.class)
  public void testClassTypeWithWrongInstance() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node rootNode = Node.newScript("");
    runtimeTypeCheck.process(null, rootNode);

    ObjectType objectType = (ObjectType) NodeUtil.getTypeByName(
        rootNode, "MyClass");

    Node instanceNode = Node.newObjectLiteral(Node.newQualifiedNameNode("OtherClass"));
    Node functionCallNode = new Node(Token.CALL,
        jsCode("instanceChecker"), NodeUtil.getMemberExpression(instanceNode, "prototype"));

    try {
      Node resultWithWrongInstance = NodeUtil.runScriptAndReturnRoot(
          NodeUtil.getSourceForNode(functionCallNode), rootNode);
      fail("Expected AssertionError but got: " + resultWithWrongInstance);
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testInterfaceType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node rootNode = Node.newScript("");
    runtimeTypeCheck.process(null, rootNode);

    ObjectType objectType = (ObjectType) NodeUtil.getTypeByName(
        rootNode, "MyInterface");

    Node implementingClassNode = Node.newQualifiedNameNode("MyClass");
    Node functionCallNode = new Node(Token.CALL,
        jsCode("implementsChecker"), NodeUtil.getMemberExpression(implementingClassNode, "prototype"));

    Node result = NodeUtil.runScriptAndReturnRoot(
        NodeUtil.getSourceForNode(functionCallNode), rootNode);

    assertTrue(result.isTrue());

    // Basic functionality test
    Node anotherImplementingClassNode = Node.newQualifiedNameNode("AnotherClass");
    Node anotherFunctionCallNode = new Node(Token.CALL,
        jsCode("implementsChecker"), NodeUtil.getMemberExpression(anotherImplementingClassNode, "prototype"));

    Node anotherResult = NodeUtil.runScriptAndReturnRoot(
        NodeUtil.getSourceForNode(anotherFunctionCallNode), rootNode);

    assertTrue(anotherResult.isTrue());

    // Edge case: null check
    Node nullImplementingClassNode = Node.newNull();
    Node nullFunctionCallNode = new Node(Token.CALL,
        jsCode("implementsChecker"), NodeUtil.getMemberExpression(nullImplementingClassNode, "prototype"));

    Node nullResult = NodeUtil.runScriptAndReturnRoot(
        NodeUtil.getSourceForNode(nullFunctionCallNode), rootNode);

    assertFalse(nullResult.isTrue());
  }

  @Test(expected = AssertionError.class)
  public void testInterfaceTypeWithWrongImplementer() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, null);
    Node rootNode = Node.newScript("");
    runtimeTypeCheck.process(null, rootNode);

    ObjectType objectType = (ObjectType) NodeUtil.getTypeByName(
        rootNode, "MyInterface");

    Node notImplementingClassNode = Node.newQualifiedNameNode("MyOtherClass");
    Node functionCallNode = new Node(Token.CALL,
        jsCode("implementsChecker"), NodeUtil.getMemberExpression(notImplementingClassNode, "prototype"));

    try {
      Node resultWithWrongImplementer = NodeUtil.runScriptAndReturnRoot(
          NodeUtil.getSourceForNode(functionCallNode), rootNode);
      fail("Expected AssertionError but got: " + resultWithWrongImplementer);
    } catch (AssertionError e) {
      // Expected
    }
  }

  private static Node jsCode(String prop) {
    return NodeUtil.newQualifiedNameNode("jscomp.typecheck." + prop, -1, -1);
  }
}