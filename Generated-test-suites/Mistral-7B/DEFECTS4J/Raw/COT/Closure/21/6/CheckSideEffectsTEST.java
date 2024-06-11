import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects_Empty() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();
    CompilerInput input = new CompilerInput();
    checkSideEffects.process(input.getAstRoot(compiler), input.getAstRoot(compiler));
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_VoidStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node voidStatement = NodeFactory.createEmpty();
    root.addChildToBack(voidStatement);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_SimpleExpression() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node expression = NodeFactory.createBinary(Token.OP_ADD, NodeFactory.createNumber(1), NodeFactory.createNumber(2));
    root.addChildToBack(expression);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffect() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node assignment = NodeFactory.createAssignment(Token.ASSIGN, NodeFactory.createName("x"), NodeFactory.createName("foo"));
    root.addChildToBack(assignment);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffectProtected() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node assignment = NodeFactory.createAssignment(Token.ASSIGN, NodeFactory.createName("x"), NodeFactory.createName("foo"));
    root.addChildToBack(assignment);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    NodeTraversal.traverse(compiler, root, checkSideEffects.stripProtection);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffects_StatementWithMissingPlus() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node stringLiteral = NodeFactory.createString("this string is ");
    Node assignment = NodeFactory.createAssignment(Token.ASSIGN, NodeFactory.createName("s"), stringLiteral);
    Node nextStatement = NodeFactory.createName("continued on the next line but you forgot the +");
    root.addChildToBack(assignment);
    root.addChildToBack(nextStatement);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffects_AssignmentWithFunctionCall() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node assignment = NodeFactory.createAssignment(Token.ASSIGN, NodeFactory.createName("x"), NodeFactory.createCall(NodeFactory.createName("foo")));
    root.addChildToBack(assignment);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
  }

  @Test
  public void testCheckSideEffects_StatementWithExtraSemicolon() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(NodeFactory.createName("foo")));
    Node extraSemicolon = NodeFactory.createSemicolon();
    root.addChildToBack(statement);
    root.addChildToBack(extraSemicolon);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithExtraSemicolonUsed() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(NodeFactory.createName("foo")));
    Node extraSemicolon = NodeFactory.createSemicolon();
    root.addChildToBack(NodeFactory.createExpressionStatement(NodeFactory.createCall(NodeFactory.createName("bar"))));
    root.addChildToBack(statement);
    root.addChildToBack(extraSemicolon);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithForLoop() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(NodeFactory.createName("foo")));
    Node forNode = NodeFactory.createForNode(
        NodeFactory.createName("i"),
        NodeFactory.createNodeList(NodeFactory.createNumber(0), NodeFactory.createNumber(10)),
        NodeFactory.createBlock(),
        NodeFactory.createBlock());
    root.addChildToBack(forNode);
    Node loopStatement = NodeFactory.createStatementList(statement).getFirstChild();
    forNode.setLastChild(loopStatement);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithForLoopProtected() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node statement = NodeFactory.createExpressionStatement(NodeFactory.createCall(NodeFactory.createName("foo")));
    Node forNode = NodeFactory.createForNode(
        NodeFactory.createName("i"),
        NodeFactory.createNodeList(NodeFactory.createNumber(0), NodeFactory.createNumber(10)),
        NodeFactory.createBlock(),
        NodeFactory.createBlock());
    root.addChildToBack(forNode);
    Node loopStatement = NodeFactory.createStatementList(statement).getFirstChild();
    forNode.setLastChild(loopStatement);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    NodeTraversal.traverse(compiler, root, checkSideEffects.stripProtection);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithReturnStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node statement = NodeFactory.createReturnStatement(NodeFactory.createCall(NodeFactory.createName("foo")));
    root.addChildToBack(statement);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffects_StatementWithReturnStatementProtected() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    AbstractCompiler compiler = new Compiler();

    Node root = NodeFactory.createBlock();
    Node statement = NodeFactory.createReturnStatement(NodeFactory.createCall(NodeFactory.createName("foo")));
    root.addChildToBack(statement);

    checkSideEffects.process(new CompilerInput().getAstRoot(compiler), root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    NodeTraversal.traverse(compiler, root, checkSideEffects.stripProtection);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  private static class Compiler extends AbstractCompiler {
    @Override
    public void reportCodeChange() {
      // Do nothing
    }
  }

  private static class StripProtection extends CheckSideEffects.StripProtection {
    StripProtection(AbstractCompiler compiler) {
      super(compiler);
    }

    @Override
    public boolean mayHaveSideEffects(Node n, AbstractCompiler compiler) {
      return false;
    }
  }
}