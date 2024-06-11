import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.enterScope;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.exitScope;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.process;
import static com.google.javascript.jscomp.NodeTraversal.ScopedCallback.visit;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestBaseCompiler;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = new TestBaseCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    // Test empty case
    Node root = Node.createTree("{}");
    process(deadAssignmentsElimination, root, null);
    assertFalse(NodeUtil.has(root, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment
    Node root1 = Node.createTree("function foo() { var x = 1; x = 2; }");
    process(deadAssignmentsElimination, root1, null);
    assertTrue(NodeUtil.isEmpty( NodeUtil.getFirstChild(root1) ));

    // Test case with dead assignment in if statement
    Node root2 = Node.createTree("function foo() { var x = 1; if (false) { x = 2; } }");
    process(deadAssignmentsElimination, root2, null);
    assertFalse(NodeUtil.has(root2, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment in for loop
    Node root3 = Node.createTree("function foo() { for (var i = 0; i < 10; i++) { i = i + 1; } }");
    process(deadAssignmentsElimination, root3, null);
    assertFalse(NodeUtil.has(NodeUtil.getFirstChild(root3), DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment in while loop
    Node root4 = Node.createTree("function foo() { var i = 0; while (true) { i = i + 1; } }");
    process(deadAssignmentsElimination, root4, null);
    assertFalse(NodeUtil.has(NodeUtil.getFirstChild(root4), DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment in if statement with assignment operator
    Node root5 = Node.createTree("function foo() { var x = 1; if (x = 2) { x = 3; } }");
    process(deadAssignmentsElimination, root5, null);
    assertFalse(NodeUtil.has(root5, DeadAssignmentsElimitation.matchRemovableAssigns));

    // Test case with dead assignment in simple expression
    Node root6 = Node.createTree("function foo() { 1 + (x = 2); }");
    process(deadAssignmentsElimination, root6, null);
    assertFalse(NodeUtil.has(root6, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment in ternary operator
    Node root7 = Node.createTree("function foo() { x = 1; y = x > 0 ? x : y = 2; }");
    process(deadAssignmentsElimination, root7, null);
    assertFalse(NodeUtil.has(root7, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment in switch statement
    Node root8 = Node.createTree("function foo() { switch (x) { case 1: x = 2; break; } }");
    process(deadAssignmentsElimination, root8, null);
    assertFalse(NodeUtil.has(root8, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment in for-in loop
    Node root9 = Node.createTree("function foo() { for (var key in obj) { obj[key] = 2; } }");
    process(deadAssignmentsElimination, root9, null);
    assertFalse(NodeUtil.has(NodeUtil.getFirstChild(root9), DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment in try-catch statement
    Node root10 = Node.createTree("function foo() { try { x = 1; } catch (e) { x = 2; } }");
    process(deadAssignmentsElimination, root10, null);
    assertFalse(NodeUtil.has(root10, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test case with dead assignment in return statement
    Node root11 = Node.createTree("function foo() { return x = 1; }");
    process(deadAssignmentsElimination, root11, null);
    assertTrue(NodeUtil.isReturnStatement(root11));
    assertEquals(NodeUtil.getFirstChild(root11), Node.createIntLiteral(1));

    // Test case with dead assignment in expression statement
    Node root12 = Node.createTree("x = 1;");
    process(deadAssignmentsElimination, root12, null);
    assertTrue(NodeUtil.isExpressionStatement(root12));
    assertEquals(NodeUtil.getExpression(root12), Node.createAssign(NodeUtil.getFirstChild(root12), Node.createIntLiteral(1)));
  }

  @Test
  public void testDeadAssignmentsEliminationEdgesCases() {
    AbstractCompiler compiler = new TestBaseCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    // Test dead assignment in assignment statement with same variable
    Node root13 = Node.createTree("function foo() { x = x; }");
    process(deadAssignmentsElimination, root13, null);
    assertFalse(NodeUtil.has(root13, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with different variables
    Node root14 = Node.createTree("function foo() { x = y; y = 1; }");
    process(deadAssignmentsElimination, root14, null);
    assertFalse(NodeUtil.has(root14, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with different types
    Node root15 = Node.createTree("function foo() { x = 1; y = [1]; }");
    process(deadAssignmentsElimination, root15, null);
    assertFalse(NodeUtil.has(root15, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with array index
    Node root16 = Node.createTree("function foo() { a[i] = b[i]; }");
    process(deadAssignmentsElimination, root16, null);
    assertFalse(NodeUtil.has(root16, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with object property
    Node root17 = Node.createTree("function foo() { obj.prop = 1; obj.prop = 2; }");
    process(deadAssignmentsElimination, root17, null);
    assertFalse(NodeUtil.has(root17, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with function call
    Node root18 = Node.createTree("function foo() { x = bar(); }");
    process(deadAssignmentsElimination, root18, null);
    assertFalse(NodeUtil.has(root18, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with conditional statement
    Node root19 = Node.createTree("function foo() { x = (y > 0) ? 1 : 2; y = 3; }");
    process(deadAssignmentsElimination, root19, null);
    assertFalse(NodeUtil.has(root19, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with switch statement
    Node root20 = Node.createTree("function foo() { switch (x) { case 1: x = 2; break; } }");
    process(deadAssignmentsElimination, root20, null);
    assertFalse(NodeUtil.has(root20, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with for loop
    Node root21 = Node.createTree("function foo() { for (var i = 0; i < 10; i++) { x = i; } }");
    process(deadAssignmentsElimination, root21, null);
    assertFalse(NodeUtil.has(NodeUtil.getFirstChild(root21), DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with while loop
    Node root22 = Node.createTree("function foo() { var i = 0; while (true) { x = i; } }");
    process(deadAssignmentsElimination, root22, null);
    assertFalse(NodeUtil.has(NodeUtil.getFirstChild(root22), DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with try-catch statement
    Node root23 = Node.createTree("function foo() { try { x = 1; } catch (e) { x = 2; } }");
    process(deadAssignmentsElimination, root23, null);
    assertFalse(NodeUtil.has(root23, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in assignment statement with return statement
    Node root24 = Node.createTree("function foo() { return x = 1; }");
    process(deadAssignmentsElimination, root24, null);
    assertTrue(NodeUtil.isReturnStatement(root24));
    assertEquals(NodeUtil.getFirstChild(root24), Node.createAssign(NodeUtil.getFirstChild(root24), Node.createIntLiteral(1)));

    // Test dead assignment in expression statement with same variable
    Node root25 = Node.createTree("x = x;");
    process(deadAssignmentsElimination, root25, null);
    assertFalse(NodeUtil.has(root25, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with different variables
    Node root26 = Node.createTree("x = y; y = 1;");
    process(deadAssignmentsElimination, root26, null);
    assertFalse(NodeUtil.has(root26, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with different types
    Node root27 = Node.createTree("x = 1; y = [1];");
    process(deadAssignmentsElimination, root27, null);
    assertFalse(NodeUtil.has(root27, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with array index
    Node root28 = Node.createTree("a[i] = b[i];");
    process(deadAssignmentsElimination, root28, null);
    assertFalse(NodeUtil.has(root28, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with object property
    Node root29 = Node.createTree("obj.prop = 1; obj.prop = 2;");
    process(deadAssignmentsElimination, root29, null);
    assertFalse(NodeUtil.has(root29, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with function call
    Node root30 = Node.createTree("x = bar();");
    process(deadAssignmentsElimination, root30, null);
    assertFalse(NodeUtil.has(root30, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with conditional statement
    Node root31 = Node.createTree("x = (y > 0) ? 1 : 2; y = 3;");
    process(deadAssignmentsElimination, root31, null);
    assertFalse(NodeUtil.has(root31, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with switch statement
    Node root32 = Node.createTree("switch (x) { case 1: x = 2; break; } ");
    process(deadAssignmentsElimination, root32, null);
    assertFalse(NodeUtil.has(root32, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with for loop
    Node root33 = Node.createTree("for (var i = 0; i < 10; i++) { x = i; }");
    process(deadAssignmentsElimination, root33, null);
    assertFalse(NodeUtil.has(NodeUtil.getFirstChild(root33), DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with while loop
    Node root34 = Node.createTree("var i = 0; while (true) { x = i; }");
    process(deadAssignmentsElimination, root34, null);
    assertFalse(NodeUtil.has(NodeUtil.getFirstChild(root34), DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with try-catch statement
    Node root35 = Node.createTree("try { x = 1; } catch (e) { x = 2; }");
    process(deadAssignmentsElimination, root35, null);
    assertFalse(NodeUtil.has(root35, DeadAssignmentsElimination.matchRemovableAssigns));

    // Test dead assignment in expression statement with return statement
    Node root36 = Node.createTree("return x = 1;");
    process(deadAssignmentsElimination, root36, null);
    assertTrue(NodeUtil.isReturnStatement(root36));
    assertEquals(NodeUtil.getFirstChild(root36), Node.createAssign(NodeUtil.getFirstChild(root36), Node.createIntLiteral(1)));
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsEliminationException() {
    AbstractCompiler compiler = new TestBaseCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = Node.createTree("function foo() { try { x = 1; } catch (e) { x = throw new Error(); } }");
    process(deadAssignmentsElimination, root, null);
  }

  @Test
  public void testDeadAssignmentsEliminationExitScope() {
    AbstractCompiler compiler = new TestBaseCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = Node.createTree("function foo() { var x = 1; if (false) { x = 2; } }");
    NodeTraversal t = new NodeTraversal(root, enterScope, visit, exitScope, process);
    t.traverse();
  }
}