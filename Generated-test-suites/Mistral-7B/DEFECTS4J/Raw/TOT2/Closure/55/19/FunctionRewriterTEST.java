import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FunctionRewriterTest {
  private FunctionRewriter functionRewriter;
  private Node rootNode;

  @Before
  public void setUp() {
    functionRewriter = new FunctionRewriter(new NoOpCompiler());
    rootNode = createTestNode();
  }

  @Test
  public void testIdentityFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpCompiler());
    Node identityFunctionNode = createIdentityFunctionNode();
    Node rewrittenFunctionNode = functionRewriter.process(null, identityFunctionNode).getFirstChild();
    Assert.assertEquals("expected identity function to be replaced with JSCompiler_identityFn",
                        NodeUtil.isCall(rewrittenFunctionNode, "JSCompiler_identityFn"), true);
  }

  @Test
  public void testReturnConstantFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpCompiler());
    Node returnConstantFunctionNode = createReturnConstantFunctionNode();
    Node rewrittenFunctionNode = functionRewriter.process(null, returnConstantFunctionNode).getFirstChild();
    Assert.assertEquals("expected return constant function to be replaced with JSCompiler_returnArg",
                        NodeUtil.isCall(rewrittenFunctionNode, "JSCompiler_returnArg"), true);
  }

  @Test
  public void testGetterFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpCompiler());
    Node getterFunctionNode = createGetterFunctionNode();
    Node rewrittenFunctionNode = functionRewriter.process(null, getterFunctionNode).getFirstChild();
    Assert.assertEquals("expected getter function to be replaced with JSCompiler_get",
                        NodeUtil.isCall(rewrittenFunctionNode, "JSCompiler_get"), true);
  }

  @Test
  public void testSetterFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpCompiler());
    Node setterFunctionNode = createSetterFunctionNode();
    Node rewrittenFunctionNode = functionRewriter.process(null, setterFunctionNode).getFirstChild();
    Assert.assertEquals("expected setter function to be replaced with JSCompiler_set",
                        NodeUtil.isCall(rewrittenFunctionNode, "JSCompiler_set"), true);
  }

  @Test
  public void testEmptyFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpCompiler());
    Node emptyFunctionNode = createEmptyFunctionNode();
    Node rewrittenFunctionNode = functionRewriter.process(null, emptyFunctionNode).getFirstChild();
    Assert.assertEquals("expected empty function to be replaced with JSCompiler_emptyFn",
                        NodeUtil.isCall(rewrittenFunctionNode, "JSCompiler_emptyFn"), true);
  }

  @Test
  public void testFunctionWithArguments() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpCompiler());
    Node functionWithArgumentsNode = createFunctionWithArgumentsNode();
    Node rewrittenFunctionNode = functionRewriter.process(null, functionWithArgumentsNode).getFirstChild();
    Assert.assertEquals("expected function with arguments to not be replaced",
                        rewrittenFunctionNode.getType(), Token.FUNCTION);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullNode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpCompiler());
    functionRewriter.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRootNode() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpCompiler());
    functionRewriter.process(null, rootNode);
  }

  private Node createTestNode() {
    Node testNode = new Node(Token.BLOCK, 1, 1);
    testNode.addChildToBack(createIdentityFunctionNode());
    testNode.addChildToBack(createReturnConstantFunctionNode());
    testNode.addChildToBack(createGetterFunctionNode());
    testNode.addChildToBack(createSetterFunctionNode());
    testNode.addChildToBack(createEmptyFunctionNode());
    testNode.addChildToBack(createFunctionWithArgumentsNode());
    return testNode;
  }

  private Node createIdentityFunctionNode() {
    Node identityFunctionNode = new Node(Token.FUNCTION, 1, 1);
    identityFunctionNode.setProperty("name", "identityFunction");
    identityFunctionNode.addChildToBack(new Node(Token.ARGLIST, 1, 1));
    identityFunctionNode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    identityFunctionNode.addChildToBack(new Node(Token.RETURN, 1, 1));
    identityFunctionNode.getLastChild().addChildToBack(
        new Node(Token.NAME, 1, 1, "this"));
    identityFunctionNode.getLastChild().addChildToBack(
        new Node(Token.GETPROP, 1, 1, "identityFunction"));
    return identityFunctionNode;
  }

  private Node createReturnConstantFunctionNode() {
    Node returnConstantFunctionNode = new Node(Token.FUNCTION, 0, 1);
    returnConstantFunctionNode.setProperty("name", "returnConstantFunction");
    returnConstantFunctionNode.addChildToBack(new Node(Token.ARGLIST, 0, 1));
    returnConstantFunctionNode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    returnConstantFunctionNode.addChildToBack(new Node(Token.RETURN, 1, 1));
    returnConstantFunctionNode.getLastChild().setInteger(Token.INT, 42);
    return returnConstantFunctionNode;
  }

  private Node createGetterFunctionNode() {
    Node getterFunctionNode = new Node(Token.FUNCTION, 0, 1);
    getterFunctionNode.setProperty("name", "getterFunction");
    getterFunctionNode.addChildToBack(new Node(Token.ARGLIST, 0, 1));
    getterFunctionNode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    getterFunctionNode.addChildToBack(new Node(Token.RETURN, 1, 1));
    getterFunctionNode.getLastChild().addChildToBack(new Node(Token.NAME, 1, 1, "this"));
    getterFunctionNode.getLastChild().addChildToBack(new Node(Token.GETPROP, 1, 1, "getterFunctionValue"));
    Node getterFunctionValueNode = new Node(Token.NAME, 1, 1, "getterFunctionValue");
    getterFunctionValueNode.setProperty("value", "getterFunctionValue");
    getterFunctionNode.getLastChild().addChildToBack(getterFunctionValueNode);
    return getterFunctionNode;
  }

  private Node createSetterFunctionNode() {
    Node setterFunctionNode = new Node(Token.FUNCTION, 1, 1);
    setterFunctionNode.setProperty("name", "setterFunction");
    setterFunctionNode.addChildToBack(new Node(Token.ARGLIST, 1, 1));
    setterFunctionNode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    setterFunctionNode.addChildToBack(new Node(Token.EXPRSTMT, 1, 1));
    Node exprStmtNode = setterFunctionNode.getLastChild();
    Node assignNode = exprStmtNode.getFirstChild();
    Node lhs = assignNode.getFirstChild();
    Node rhs = assignNode.getLastChild();
    lhs.setProperty("name", "setterFunctionThis");
    lhs.addChildToBack(new Node(Token.GETPROP, 1, 1, "setterFunctionProperty"));
    rhs.setProperty("name", "setterFunctionValue");
    return setterFunctionNode;
  }

  private Node createEmptyFunctionNode() {
    Node emptyFunctionNode = new Node(Token.FUNCTION, 0, 1);
    emptyFunctionNode.setProperty("name", "emptyFunction");
    emptyFunctionNode.addChildToBack(new Node(Token.ARGLIST, 0, 1));
    emptyFunctionNode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    return emptyFunctionNode;
  }

  private Node createFunctionWithArgumentsNode() {
    Node functionWithArgumentsNode = new Node(Token.FUNCTION, 1, 1);
    functionWithArgumentsNode.setProperty("name", "functionWithArguments");
    functionWithArgumentsNode.addChildToBack(new Node(Token.ARGLIST, 1, 1));
    functionWithArgumentsNode.addChildToBack(new Node(Token.BLOCK, 1, 1));
    functionWithArgumentsNode.addChildToBack(new Node(Token.EXPRSTMT, 1, 1));
    Node exprStmtNode = functionWithArgumentsNode.getLastChild();
    Node assignNode = exprStmtNode.getFirstChild();
    Node lhs = assignNode.getFirstChild();
    Node rhs = assignNode.getLastChild();
    lhs.setProperty("name", "functionWithArgumentsThis");
    lhs.addChildToBack(new Node(Token.GETPROP, 1, 1, "functionWithArgumentsProperty"));
    rhs.setProperty("name", "functionWithArgumentsValue");
    return functionWithArgumentsNode;
  }

  private static class NoOpCompiler implements AbstractCompiler {
    @Override
    public Node parseSyntheticCode(String name, String code) {
      return null;
    }

    @Override
    public Node getNodeForCodeInsertion(Node parent) {
      return null;
    }

    @Override
    public void reportCodeChange() {
    }
  }
}