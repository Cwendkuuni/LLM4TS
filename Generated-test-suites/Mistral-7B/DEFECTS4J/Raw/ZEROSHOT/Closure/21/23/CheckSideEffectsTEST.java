import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private Node root;

  @Before
  public void setup() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler = new AbstractCompiler() {
      @Override
      public NodeFactory getFactory() {
        return NodeFactory.instance();
      }

      @Override
      public CompilerInput getSourceInput() {
        return input;
      }
    };

    input = new CompilerInput("test.js", "test.js", null);
    root = NodeFactory.createProgram();
  }

  @Test
  public void testCheckSideEffects() {
    // Test case for a simple expression with no side-effects.
    Node expression = NodeFactory.createBinary(Token.OP_PLUS,
        NodeFactory.createString("This is a "),
        NodeFactory.createString("test."));

    Node statement = NodeFactory.createExpressionStatement(expression);
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, true).process(input, root);
    assertTrue(root.getChildCount() == 1);

    // Test case for a void statement.
    statement = NodeFactory.createExpressionStatement(NodeFactory.createVOID());
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, true).process(input, root);
    assertTrue(root.getChildCount() == 1);

    // Test case for a statement with a side-effect.
    statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(
        NodeFactory.createName("console.log"),
        NodeFactory.createString("Hello World!")));
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, true).process(input, root);
    assertEquals(1, root.getChildCount());
    assertEquals(1, problemNodes.size());

    Node problemNode = problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
    assertEquals("console.log", problemNode.getFirstChild().getString());

    // Test case for a statement with a side-effect but with a protection
    // call to prevent dead code removal.
    statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(
        NodeFactory.createName(PROTECTOR_FN), NodeFactory.createString("Hello World!")));
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, true).process(input, root);
    assertEquals(1, root.getChildCount());
    assertEquals(0, problemNodes.size());

    // Test case for a statement with a side-effect and without protection
    // call. The statement should still be removed as it doesn't change the
    // behavior of the program.
    problemNodes.clear();
    statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(
        NodeFactory.createName("console.log"),
        NodeFactory.createString("Hello World!")));
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, false).process(input, root);
    assertEquals(0, root.getChildCount());
    assertEquals(1, problemNodes.size());

    problemNode = problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
    assertEquals("console.log", problemNode.getFirstChild().getString());
  }

  @Test
  public void testProtectSideEffects() {
    // Test case for protecting side-effect free expressions.
    Node expression = NodeFactory.createBinary(Token.OP_PLUS,
        NodeFactory.createString("This is a "),
        NodeFactory.createString("test."));

    Node statement = NodeFactory.createExpressionStatement(expression);
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, true).process(input, root);
    new StripProtection(compiler).process(input, root);
    assertTrue(root.getChildCount() == 1);

    // Test case for protecting a void statement.
    statement = NodeFactory.createExpressionStatement(NodeFactory.createVOID());
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, true).process(input, root);
    new StripProtection(compiler).process(input, root);
    assertTrue(root.getChildCount() == 1);

    // Test case for protecting a statement with a side-effect.
    problemNodes.clear();
    statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(
        NodeFactory.createName("console.log"),
        NodeFactory.createString("Hello World!")));
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, true).process(input, root);
    new StripProtection(compiler).process(input, root);
    assertEquals(1, root.getChildCount());
    assertEquals(1, problemNodes.size());

    problemNode = problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
    assertEquals("console.log", problemNode.getFirstChild().getString());

    // Test case for protecting a statement with a side-effect and without
    // protection call. The statement should still be protected.
    problemNodes.clear();
    statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(
        NodeFactory.createName("console.log"),
        NodeFactory.createString("Hello World!")));
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, false).process(input, root);
    new StripProtection(compiler).process(input, root);
    assertEquals(0, root.getChildCount());
    assertEquals(1, problemNodes.size());

    problemNode = problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
    assertEquals("console.log", problemNode.getFirstChild().getString());
  }

  @Test(expected = IllegalStateException.class)
  public void testProtectSideEffectsWithDeadCodeRemoval() {
    // Test case for protecting side-effect free expressions with dead code
    // removal enabled. This should result in an error as dead code removal
    // should not be performed on protected code.
    Node expression = NodeFactory.createBinary(Token.OP_PLUS,
        NodeFactory.createString("This is a "),
        NodeFactory.createString("test."));

    Node statement = NodeFactory.createExpressionStatement(expression);
    root.addChild(statement);

    new CheckSideEffects(compiler, WARNING, true).process(input, root);
    compiler.setOption(CompilerOptions.LanguageMode.ECMASCRIPT6, true);
    compiler.setOption(CompilerOptions.DeadCodeRemoval, true);
    new StripProtection(compiler).process(input, root);
    assertTrue(root.getChildCount() == 1);

    // The following line should throw an IllegalStateException as dead code
    // removal is not allowed on protected code.
    new CheckSideEffects(compiler, WARNING, true).process(input, root);
  }
}