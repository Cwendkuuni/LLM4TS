import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  // Test EliminationPass.visit() method
  @Test
  public void testVisit() {
    // Typical use case: Code following a return statement
    String code1 = "function test1() { if (x) { return; alert('unreachable'); } }";
    String expected1 = "function test1() { if (x) { return; } }";
    assertEquals(expected1, processCode(code1));

    // Edge case: No code following a return statement
    String code2 = "function test2() { return; }";
    String expected2 = "function test2() { return; }";
    assertEquals(expected2, processCode(code2));

    // Typical use case: Statement without side effects
    String code3 = "function test3() { a.b.MyClass.prototype.propertyName; }";
    String expected3 = "function test3() { }";
    assertEquals(expected3, processCode(code3));

    // Edge case: Statement without side effects inside a block
    String code4 = "function test4() { if (x) { a.b.MyClass.prototype.propertyName; } }";
    String expected4 = "function test4() { if (x) { } }";
    assertEquals(expected4, processCode(code4));

    // Error scenario: Code not in CFG
    String code5 = "function test5() { someFunction(); }";
    String expected5 = "function test5() { someFunction(); }";
    assertEquals(expected5, processCode(code5));
  }

  // Test EliminationPass.tryRemoveUnconditionalBranching() method
  @Test
  public void testTryRemoveUnconditionalBranching() {
    // Typical use case: Remove unconditional branch node
    String code1 = "function test1() { while (true) { break; } }";
    String expected1 = "function test1() { while (true) { } }";
    assertEquals(expected1, processCode(code1));

    // Edge case: Retain unconditional branch node
    String code2 = "function test2() { while (true) { continue; } }";
    String expected2 = "function test2() { while (true) { continue; } }";
    assertEquals(expected2, processCode(code2));

    // Edge case: Recursive removal of useless branch nodes
    String code3 = "function test3() { while (true) { break; break; } }";
    String expected3 = "function test3() { while (true) { } }";
    assertEquals(expected3, processCode(code3));
  }

  // Test EliminationPass.computeFollowing() method
  @Test
  public void testComputeFollowing() {
    // Typical use case: Compute following node
    String code1 = "function test1() { if (x) { return; } else { alert('not unreachable'); } }";
    assertNotNull(computeFollowing(code1));

    // Edge case: No following node
    String code2 = "function test2() { while (true) { alert('unreachable'); } }";
    assertNull(computeFollowing(code2));
  }

  // Test UnreachableCodeElimination.removeDeadExprStatementSafely() method
  @Test
  public void testRemoveDeadExprStatementSafely() {
    // Typical use case: Remove dead expression statement
    String code1 = "function test1() { a.b.MyClass.prototype.propertyName; }";
    String expected1 = "function test1() { }";
    assertEquals(expected1, processCode(code1));

    // Edge case: Retain non-empty block
    String code2 = "function test2() { if (x) { alert('not unreachable'); } }";
    String expected2 = "function test2() { if (x) { alert('not unreachable'); } }";
    assertEquals(expected2, processCode(code2));

    // Edge case: Retain var declaration without children
    String code3 = "function test3() { var x; }";
    String expected3 = "function test3() { var x; }";
    assertEquals(expected3, processCode(code3));
  }

  // Helper method to process the code using the UnreachableCodeElimination class
  private String processCode(String code) {
    Compiler compiler = new Compiler();
    compiler.disableThreads();
    CompilerOptions options = new CompilerOptions();
    compiler.initOptions(options);
    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode(code);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, root);
    return compiler.toSource(root);
  }

  // Helper method to compute the following node using the EliminationPass class
  private Node computeFollowing(String code) {
    Compiler compiler = new Compiler();
    compiler.disableThreads();
    CompilerOptions options = new CompilerOptions();
    compiler.initOptions(options);
    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode(code);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    EliminationPass eliminationPass = pass.new EliminationPass(null);
    NodeTraversal.traverse(compiler, root, eliminationPass);
    return eliminationPass.computeFollowing(root);
  }
}