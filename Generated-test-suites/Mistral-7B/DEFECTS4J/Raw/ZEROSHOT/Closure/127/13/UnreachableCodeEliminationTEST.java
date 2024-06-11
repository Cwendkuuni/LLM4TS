import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilerContext;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.ElaboratedNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private CompilerContext context;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    context = new CompilerContext();
    context.setOption(Compiler.Option.DEBUG, false);
    compiler.init(context);

    rootNode = Node.parse("function foo() {" +
        "if (true) {" +
        "  return; // Unreachable return statement." +
        "  alert('unreachable');" +
        "}" +
        "}");
  }

  @Test
  public void testUnreachableCodeElimination() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, rootNode);

    // Check that the return statement and the alert statement have been removed.
    ElaboratedNode fooFunction = context.getFunction("foo");
    assertTrue(fooFunction.getBody().isEmpty());
  }

  @Test
  public void testRemoveNoOpStatements() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    Node rootNodeWithNoOpStatement = Node.parse("function foo() {" +
        "if (true) {" +
        "  alert('unreachable');" + // No side effects.
        "}" +
        "}");

    pass.process(null, rootNodeWithNoOpStatement);

    ElaboratedNode fooFunctionWithNoOpStatement = context.getFunction("foo");
    assertTrue(fooFunctionWithNoOpStatement.getBody().isEmpty());
  }

  @Test
  public void testDontRemoveReachableCode() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node rootNodeWithReachableCode = Node.parse("function foo() {" +
        "if (true) {" +
        "  return;" + // Reachable return statement.
        "  alert('reachable');" +
        "}" +
        "}");

    pass.process(null, rootNodeWithReachableCode);

    ElaboratedNode fooFunctionWithReachableCode = context.getFunction("foo");
    Node body = fooFunctionWithReachableCode.getBody();
    assertFalse(body.isBlock() || body.isEmpty());
    assertTrue(body.getFirstChild().isReturn());
    assertTrue(body.getLastChild().isStatement());
    assertTrue(body.getLastChild().isCall() &&
        NodeUtil.isStringLiteral(body.getLastChild().getFirstChild()));
  }

  @Test
  public void testRemoveUnconditionalBranching() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node rootNodeWithUnconditionalBranching = Node.parse("function foo() {" +
        "while (true) {" +
        "  break;" + // Unconditional break statement.
        "}" +
        "}");

    pass.process(null, rootNodeWithUnconditionalBranching);

    ElaboratedNode fooFunctionWithUnconditionalBranching = context.getFunction("foo");
    assertTrue(fooFunctionWithUnconditionalBranching.getBody().isBlock());
    assertTrue(fooFunctionWithUnconditionalBranching.getBody().getLastChild()
        .isStatement());
    assertTrue(fooFunctionWithUnconditionalBranching.getBody().getLastChild()
        .isReturn());
  }

  @Test
  public void testRemoveDeadCodeInsideFunctionCall() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node rootNodeWithDeadCodeInsideFunctionCall = Node.parse("function foo() {" +
        "function bar() {" +
        "  if (true) {" +
        "    return;" + // Unreachable return statement.
        "    alert('unreachable');" +
        "  }" +
        "}" +
        "bar();" +
        "}");

    pass.process(null, rootNodeWithDeadCodeInsideFunctionCall);

    ElaboratedNode fooFunctionWithDeadCodeInsideFunctionCall = context.getFunction("foo");
    Node body = fooFunctionWithDeadCodeInsideFunctionCall.getBody();
    assertTrue(body.getFirstChild().isFunction());
    assertTrue(body.getFirstChild().getFirstChild().isIdentifier("bar"));
    assertTrue(body.getFirstChild().getLastChild().isStatement());
  }

  @Test
  public void testRemoveDeadCodeInsideTryCatchBlock() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    Node rootNodeWithDeadCodeInsideTryCatchBlock = Node.parse("function foo() {" +
        "try {" +
        "  if (true) {" +
        "    throw new Error('unreachable');" + // Unreachable throw statement.
        "  }" +
        "}" +
        "catch (e) {" +
        "  alert('catch');" + // Reachable statement in the catch block.
        "}" +
        "}");

    pass.process(null, rootNodeWithDeadCodeInsideTryCatchBlock);

    ElaboratedNode fooFunctionWithDeadCodeInsideTryCatchBlock = context.getFunction("foo");
    Node body = fooFunctionWithDeadCodeInsideTryCatchBlock.getBody();
    assertTrue(body.getFirstChild().isTry());
    assertTrue(body.getFirstChild().getLastChild().isStatement());
    assertTrue(body.getFirstChild().getLastChild().isCall() &&
        NodeUtil.isFunctionExpressionIdentifier("catch", body.getLastChild()));
  }
}