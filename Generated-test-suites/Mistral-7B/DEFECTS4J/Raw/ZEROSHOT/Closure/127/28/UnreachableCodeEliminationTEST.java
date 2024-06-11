import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.logging.Level;

public class UnreachableCodeEliminationTest {

  private static final Logger logger =
      Logger.getLogger(UnreachableCodeEliminationTest.class.getName());

  @ org.junit.Before
  public void setUp() {
    // Initialize logger to fine level for testing purpose
    logger.setLevel(Level.FINE);
  }

  // Test case for a simple function with dead code
  @org.junit.Test
  public void testSimpleFunctionWithDeadCode() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(Token.FUNCTION_DECLARATION, "test", null);
    root.setBody(new Node(Token.BLOCK, null));
    root.getBody().addChild(new Node(Token.RETURN, null, "result"));
    root.getBody().addChild(new Node(Token.LOGICAL_OR, null, new Node(Token.FALSE), new Node(Token.TRUE)));

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(logger.isLogged(Level.FINE, "Removing LOGICAL_OR node"));
    Node result = root.getBody().getFirstChild();
    assertFalse(root.getBody().getLastChild().isPresent());
  }

  // Test case for a function with unreachable break statement
  @org.junit.Test
  public void testFunctionWithUnreachableBreakStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(Token.FUNCTION_DECLARATION, "test", null);
    root.setBody(new Node(Token.BLOCK, null));
    Node loop = new Node(Token.FOR_INIT, null);
    loop.addChild(new Node(Token.VAR, null, "i", null));
    Node condition = new Node(Token.NUMBER_LITERAL, 5);
    Node loopBody = new Node(Token.BLOCK, null);
    Node nextStatement = new Node(Token.BREAK, null);
    Node skipStatement = new Node(Token.IF, null, condition, loopBody);
    loopBody.addChild(nextStatement);
    root.getBody().addChild(loop);
    root.getBody().addChild(skipStatement);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(logger.isLogged(Level.FINE, "Removing BREAK node"));
    Node iVar = root.getBody().getFirstChild().getFirstChild();
    Node loopNode = root.getBody().getChildAt(1);
    Node ifNode = root.getBody().getChildAt(2);
    Node bodyNode = ifNode.getChildAt(0);
    assertFalse(nextStatement.isPresentIn(root));
  }

  // Test case for a function with unreachable return statement
  @org.junit.Test
  public void testFunctionWithUnreachableReturnStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(Token.FUNCTION_DECLARATION, "test", null);
    root.setBody(new Node(Token.BLOCK, null));
    root.getBody().addChild(new Node(Token.RETURN, null, "result"));
    root.getBody().addChild(new Node(Token.LOGICAL_OR, null, new Node(Token.TRUE), new Node(Token.FALSE)));

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(logger.isLogged(Level.FINE, "Removing LOGICAL_OR node"));
    Node result = root.getBody().getFirstChild();
    assertFalse(root.getBody().getLastChild().isPresent());
  }

  // Test case for a function with unreachable continue statement
  @org.junit.Test
  public void testFunctionWithUnreachableContinueStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(Token.FUNCTION_DECLARATION, "test", null);
    root.setBody(new Node(Token.BLOCK, null));
    Node loop = new Node(Token.FOR, null, null, null, null, null, null, null);
    Node condition = new Node(Token.NUMBER_LITERAL, 5);
    Node loopBody = new Node(Token.BLOCK, null);
    Node nextStatement = new Node(Token.CONTINUE, null);
    Node skipStatement = new Node(Token.IF, null, condition, loopBody);
    loopBody.addChild(nextStatement);
    loop.setCondition(skipStatement);
    root.getBody().addChild(loop);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(logger.isLogged(Level.FINE, "Removing CONTINUE node"));
    Node loopNode = root.getBody().getChildAt(0);
    Node ifNode = loopNode.getFirstChild();
    Node bodyNode = ifNode.getChildAt(0);
    assertFalse(nextStatement.isPresentIn(root));
  }

  // Test case for a function with unreachable statement having no side effects
  @org.junit.Test
  public void testFunctionWithUnreachableStatementWithNoSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(Token.FUNCTION_DECLARATION, "test", null);
    root.setBody(new Node(Token.BLOCK, null));
    root.getBody().addChild(new Node(Token.LOGICAL_OR, null, new Node(Token.TRUE), new Node(Token.NULL)));

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(logger.isLogged(Level.FINE, "Removing LOGICAL_OR node"));
    Node result = root.getBody().getFirstChild();
    assertFalse(root.getBody().getLastChild().isPresent());
  }

  // Test case for a function with unreachable statement having side effects
  @org.junit.Test
  public void testFunctionWithUnreachableStatementWithSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(Token.FUNCTION_DECLARATION, "test", null);
    root.setBody(new Node(Token.BLOCK, null));
    root.getBody().addChild(new Node(Token.ASSIGNMENT, null, new Node(Token.VAR, null, "x"), new Node(Token.NUMBER_LITERAL, 10)));

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertFalse(logger.isLogged(Level.FINE, "Removing ASSIGNMENT node"));
    Node xVar = root.getBody().getFirstChild();
    assertTrue(root.getBody().getLastChild().isPresent());
  }

  // Test case for a function with unreachable statement and no-op statements
  @org.junit.Test
  public void testFunctionWithUnreachableStatementAndNoOpStatements() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(Token.FUNCTION_DECLARATION, "test", null);
    root.setBody(new Node(Token.BLOCK, null));
    root.getBody().addChild(new Node(Token.ASSIGNMENT, null, new Node(Token.VAR, null, "x"), new Node(Token.NUMBER_LITERAL, 10)));
    root.getBody().addChild(new Node(Token.LOGICAL_OR, null, new Node(Token.TRUE), new Node(Token.TRUE)));

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    assertFalse(logger.isLogged(Level.FINE, "Removing ASSIGNMENT node"));
    assertFalse(logger.isLogged(Level.FINE, "Removing LOGICAL_OR node"));
    Node xVar = root.getBody().getFirstChild();
    assertTrue(root.getBody().getLastChild().isPresent());
  }

  // Test case for a complex control flow with unreachable statements
  @org.junit.Test
  public void testComplexControlFlowWithUnreachableStatements() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(Token.FUNCTION_DECLARATION, "test", null);
    root.setBody(new Node(Token.BLOCK, null));

    Node ifStatement1 = new Node(Token.IF, null, new Node(Token.NUMBER_LITERAL, 1));
    Node ifBody1 = new Node(Token.BLOCK, null);
    Node ifElse = new Node(Token.ELSE, null);
    Node ifElseBody = new Node(Token.BLOCK, null);

    Node tryStatement = new Node(Token.TRY, null);
    Node catchStatement = new Node(Token.CATCH, null, new Node(Token.VAR, null, "e"), new Node(Token.BLOCK, null));

    ifBody1.addChild(new Node(Token.ASSIGNMENT, null, new Node(Token.VAR, null, "result"), new Node(Token.NUMBER_LITERAL, 1)));
    ifElseBody.addChild(new Node(Token.ASSIGNMENT, null, new Node(Token.VAR, null, "result"), new Node(Token.NUMBER_LITERAL, 2)));

    ifStatement1.setCondition(new Node(Token.LOGICAL_OR, null, new Node(Token.NUMBER_LITERAL, 0), new Node(Token.NUMBER_LITERAL, 1)));
    ifStatement1.setBody(ifBody1);
    ifElse.setBody(ifElseBody);
    ifStatement1.setElse(ifElse);

    tryStatement.setBody(ifStatement1);
    tryStatement.setHandler(catchStatement);
    root.getBody().addChild(tryStatement);

    Node returnStatement = new Node(Token.RETURN, null, new Node(Token.VAR, null, "result"));
    ifElseBody.addChild(returnStatement);

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    assertTrue(logger.isLogged(Level.FINE, "Removing RETURN node"));
    assertFalse(logger.isLogged(Level.FINE, "Removing ASSIGNMENT node in if statement"));
    assertFalse(logger.isLogged(Level.FINE, "Removing ASSIGNMENT node in else statement"));
    Node resultVar = root.getBody().getLastChild();
    assertTrue(resultVar.getType() == Token.VAR);
  }
}