import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class FunctionRewriterTest {
  private NodeFactory nodeFactory;
  private FunctionRewriter functionRewriter;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    functionRewriter = new FunctionRewriter(null);
  }

  @Test
  public void testEmptyFunctionReduction() {
    Node emptyFunctionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunction(Token.NAME, "emptyFunction", null,
            ImmutableList.of(), null, null, ImmutableList.of()));

    Node rewrittenNode = functionRewriter.process(null, emptyFunctionNode).getLastChild();
    assertNotNull(rewrittenNode);
    assertTrue(rewrittenNode.getClass().getName().equals(
        "com.google.javascript.jscomp.Node$Call"));
  }

  @Test
  public void testIdentityFunctionReduction() {
    Node identityFunctionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunction(
            Token.NAME, "identityFunction", null, ImmutableList.of(),
            null, null, ImmutableList.of(
                nodeFactory.createName("value"))));

    Node rewrittenNode = functionRewriter.process(null, identityFunctionNode).getLastChild();
    assertNotNull(rewrittenNode);
    assertTrue(rewrittenNode.getClass().getName().equals(
        "com.google.javascript.jscomp.Node$Call"));
    assertTrue(rewrittenNode.getFirstChild().getString().equals(
        "JSCompiler_identityFn"));
  }

  @Test
  public void testReturnConstantReduction() {
    Node returnConstantFunctionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunction(Token.NAME, "returnConstantFunction", null,
            ImmutableList.of(), null, null, ImmutableList.of(
                nodeFactory.createNumber(10))));

    Node rewrittenNode = functionRewriter.process(null, returnConstantFunctionNode).getLastChild();
    assertNotNull(rewrittenNode);
    assertTrue(rewrittenNode.getClass().getName().equals(
        "com.google.javascript.jscomp.Node$Call"));
    assertTrue(rewrittenNode.getFirstChild().getString().equals(
        "JSCompiler_returnArg"));
    assertTrue(rewrittenNode.getLastChild().hasType(Token.NUMBER));
    assertTrue(rewrittenNode.getLastChild().getInt().equals(10));
  }

  @Test
  public void testGetterReduction() {
    Node getterFunctionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunction(
            Token.NAME, "getPropertyGetFunction", null, ImmutableList.of(),
            null, null, ImmutableList.of(
                nodeFactory.createName("_name"))));

    Node rewrittenNode = functionRewriter.process(null, getterFunctionNode).getLastChild();
    assertNotNull(rewrittenNode);
    assertTrue(rewrittenNode.getClass().getName().equals(
        "com.google.javascript.jscomp.Node$Call"));
    assertTrue(rewrittenNode.getFirstChild().getString().equals(
        "JSCompiler_get"));
    assertTrue(rewrittenNode.getLastChild().hasType(Token.NAME));
    assertTrue(rewrittenNode.getLastChild().getString().equals("_name"));
  }

  @Test
  public void testSetterReduction() {
    Node setterFunctionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunction(
            Token.NAME, "setPropertySetFunction", null, ImmutableList.of(
                nodeFactory.createName("_name")),
            null, null, ImmutableList.of(
                nodeFactory.createName("_value"))));

    Node rewrittenNode = functionRewriter.process(null, setterFunctionNode).getLastChild();
    assertNotNull(rewrittenNode);
    assertTrue(rewrittenNode.getClass().getName().equals(
        "com.google.javascript.jscomp.Node$Call"));
    assertTrue(rewrittenNode.getFirstChild().getString().equals(
        "JSCompiler_set"));
    assertTrue(rewrittenNode.getLastChild().hasType(Token.NAME));
    assertTrue(rewrittenNode.getLastChild().getString().equals("_name"));
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalFunctionReduction() {
    Node illegalFunctionNode = nodeFactory.createExpressionStatement(
        nodeFactory.createFunction(Token.NAME, "illegalFunction", null,
            ImmutableList.of(), null, null, ImmutableList.of()));

    functionRewriter.process(null, illegalFunctionNode);
  }
}