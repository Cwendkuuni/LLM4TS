import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createNumber;
import static com.google.javascript.rhino.NodeFactory.createStatement;
import static com.google.javascript.rhino.NodeFactory.createReturnStatement;
import static com.google.javascript.rhino.NodeFactory.createVariable;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createStatementList;
import static com.google.javascript.rhino.NodeFactory.createIfStatement;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createCallExpression;
import static com.google.javascript.rhino.NodeFactory.createBooleanLiteral;
import static com.google.javascript.rhino.NodeFactory.createExpressionStatement;
import static com.google.javascript.rhino.NodeFactory.createNodeTraversal;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.UnreachableCodeElimination.*;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private Node root;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    root = createScript("");
    traversal = new NodeTraversal(root);
  }

  // Test method: unreachableCodeElimination_emptyFunction
  @Test
  public void unreachableCodeElimination_emptyFunction() {
    Node emptyFunction = createFunction("test", createBlock(0));
    Node functionWrapper = createBlock(0, createStatementList(createStatement(emptyFunction)));

    root.setBody(functionWrapper);
    new EliminationPass(createControlFlowGraph(root)).visit(traversal, root, null);

    assertEquals("", root.toString());
  }

  // Test method: unreachableCodeElimination_returnStatement
  @Test
  public void unreachableCodeElimination_returnStatement() {
    Node function = createFunction("test", createBlock(0));
    Node returnStatement = createReturnStatement(createNumber(1));

    Node body = createBlock(0, createStatementList(createStatement(function), createStatement(returnStatement)));
    root.setBody(body);

    new EliminationPass(createControlFlowGraph(root)).visit(traversal, root, null);

    assertEquals("function test() { return 1; }", root.toString());
  }

  // Test method: unreachableCodeElimination_deadCodeAfterReturns
  @Test
  public void unreachableCodeElimination_deadCodeAfterReturns() {
    Node function = createFunction("test", createBlock(0));
    Node ifStatement = createIfStatement(createBooleanLiteral(false), createStatementList(), createStatementList());
    Node returnStatement = createReturnStatement(createNumber(1));
    Node deadCode = createStatement(createCallExpression(createName("alert"), createList(createString("unreachable"))));

    Node body = createBlock(0,
        createStatementList(createStatement(function),
            createStatementList(ifStatement, returnStatement),
            createStatement(deadCode)));

    root.setBody(body);

    new EliminationPass(createControlFlowGraph(root)).visit(traversal, root, null);

    assertEquals("function test() { return 1; }", root.toString());
  }

  // Test method: unreachableCodeElimination_deadCodeNoSideEffects
  @Test
  public void unreachableCodeElimination_deadCodeNoSideEffects() {
    Node function = createFunction("test", createBlock(0));
    Node a = createVariable("a");
    Node b = createVariable("b");

    Node statement1 = createStatement(createExpressionStatement(createCallExpression(createName("MyClass.prototype.propertyName"), createList(a))));
    Node statement2 = createStatement(createExpressionStatement(createBooleanLiteral(true)));

    Node body = createBlock(0, createStatementList(createStatement(function), createStatementList(statement1, statement2)));

    root.setBody(body);

    new EliminationPass(createControlFlowGraph(root)).visit(traversal, root, null);

    assertEquals("function test() {}\n" +
        "MyClass.prototype.propertyName = null;", root.toString());
  }

  // Test method: unreachableCodeElimination_breakStatements
  @Test
  public void unreachableCodeElimination_breakStatements() {
    Node function = createFunction("test", createBlock(0));
    Node outerLoop = createForStatement(createName("i"), createNumber(0), createNumber(5), createBooleanLiteral(true), null, null, null);
    Node innerLoop = createForStatement(createName("j"), createNumber(0), createNumber(3), createBooleanLiteral(true), null, null, null);
    Node breakStatement = createBreakStatement();

    Node body = createBlock(0,
        createStatementList(createStatement(function),
            createStatementList(outerLoop,
                createStatementList(createStatement(innerLoop), createStatement(breakStatement)))));

    root.setBody(body);

    new EliminationPass(createControlFlowGraph(root)).visit(traversal, root, null);

    assertEquals("function test() { for (var i = 0; i < 5; i++) { for (var j = 0; j < 3; j++) { break; } } }", root.toString());
  }

  // Test method: unreachableCodeElimination_continueStatements
  @Test
  public void unreachableCodeElimination_continueStatements() {
    Node function = createFunction("test", createBlock(0));
    Node outerLoop = createForStatement(createName("i"), createNumber(0), createNumber(5), createBooleanLiteral(true), null, null, null);
    Node innerLoop = createForStatement(createName("j"), createNumber(0), createNumber(3), createBooleanLiteral(true), null, null, null);
    Node continueStatement = createContinueStatement();

    Node body = createBlock(0,
        createStatementList(createStatement(function),
            createStatementList(outerLoop,
                createStatementList(createStatement(innerLoop), createStatement(continueStatement)))));

    root.setBody(body);

    new EliminationPass(createControlFlowGraph(root)).visit(traversal, root, null);

    assertEquals("function test() { for (var i = 0; i < 5; i++) { for (var j = 0; j < 3; j++) { continue; } } }", root.toString());
  }

  // Test method: unreachableCodeElimination_deadCodeInsideTryCatch
  @Test
  public void unreachableCodeElimination_deadCodeInsideTryCatch() {
    Node function = createFunction("test", createBlock(0));
    Node tryNode = createBlock(0, createStatementList(createStatement(function)));

    Node catchNode = createBlock(0, createStatementList(createStatement(createVariable("e"))));
    Node catchBody = createBlock(0, createStatementList(createStatement(createReturnStatement(createNumber(0)))));

    Node finallyNode = createBlock(0);

    Node body = createBlock(0,
        createStatementList(createStatement(tryNode),
            createStatementList(createStatement(createTryCatch(tryNode, catchNode, finallyNode))),
            createStatementList(createStatement(createExpressionStatement(createCallExpression(createName("alert"), createList(createString("unreachable"))))), catchBody, finallyNode)));

    root.setBody(body);

    new EliminationPass(createControlFlowGraph(root)).visit(traversal, root, null);

    assertEquals("function test() { try { } catch (e) { return 0; } finally { } }", root.toString());
  }

  // Test method: unreachableCodeElimination_deadCodeInsideForIn
  @Test
  public void unreachableCodeElimination_deadCodeInsideForIn() {
    Node function = createFunction("test", createBlock(0));
    Node array = createArrayLiteral(createNumber(1), createNumber(2), createNumber(3));

    Node forInStatement = createForInStatement(createName("i"), array);
    Node deadCode = createStatement(createExpressionStatement(createCallExpression(createName("alert"), createList(createString("unreachable")))));

    Node body = createBlock(0,
        createStatementList(createStatement(function),
            createStatementList(createStatement(forInStatement), createStatement(deadCode))));

    root.setBody(body);

    new EliminationPass(createControlFlowGraph(root)).visit(traversal, root, null);

    assertEquals("function test() {}\n" +
        "var a = [1, 2, 3];", root.toString());
  }
}