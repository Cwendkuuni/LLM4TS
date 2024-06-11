import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestUtils;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  private static Node getNode(String jsCode) {
    return ParserTestUtils.parseJs(jsCode, null, null).getRoot();
  }

  @org.junit.Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CheckerFactory checkerFactory = new CheckerFactory();
    JSErrorReporter reporter = new JSErrorReporter();

    // Test case 1: Simple assignment
    String code1 = "var x = 1;";
    Node node1 = getNode(code1);
    NodeTraversal traversal1 = new NodeTraversal(compiler, checkerFactory, reporter);
    traversal1.traverse(node1);
    assertEquals(0, reporter.getDiagnostics().size());

    // Test case 2: Function call with no side effect
    String code2 = "function add(a, b) { return a + b; }";
    Node node2 = getNode(code2);
    NodeTraversal traversal2 = new NodeTraversal(compiler, checkerFactory, reporter);
    traversal2.traverse(node2);
    assertEquals(0, reporter.getDiagnostics().size());

    // Test case 3: Expression with no side effect
    String code3 = "var x = true ? 1 : 2;";
    Node node3 = getNode(code3);
    NodeTraversal traversal3 = new NodeTraversal(compiler, checkerFactory, reporter);
    traversal3.traverse(node3);
    assertEquals(0, reporter.getDiagnostics().size());

    // Test case 4: Statement with no side effect
    String code4 = "if (true) {}";
    Node node4 = getNode(code4);
    NodeTraversal traversal4 = new NodeTraversal(compiler, checkerFactory, reporter);
    traversal4.traverse(node4);
    assertEquals(0, reporter.getDiagnostics().size());

    // Test case 5: Statement with side effect
    String code5 = "var x = 1;\nx++;";
    Node node5 = getNode(code5);
    NodeTraversal traversal5 = new NodeTraversal(compiler, checkerFactory, reporter);
    traversal5.traverse(node5);
    List<DiagnosticType> diagnostics = reporter.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.warning, diagnostics.get(0).getSeverity());

    // Test case 6: Function call with side effect
    String code6 = "function sideEffectFunction() { document.write('Hello World!'); }";
    Node node6 = getNode(code6);
    NodeTraversal traversal6 = new NodeTraversal(compiler, checkerFactory, reporter);
    traversal6.traverse(node6);
    diagnostics = reporter.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.warning, diagnostics.get(0).getSeverity());
  }

  @org.junit.Test
  public void testCheckSideEffectsProtectSideEffects() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);

    // Test case 1: Simple assignment
    String code1 = "var x = 1;";
    Node node1 = getNode(code1);
    checkSideEffects.process(null, node1);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 2: Function call with no side effect
    String code2 = "function add(a, b) { return a + b; }";
    Node node2 = getNode(code2);
    checkSideEffects.process(null, node2);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 3: Expression with no side effect
    String code3 = "var x = true ? 1 : 2;";
    Node node3 = getNode(code3);
    checkSideEffects.process(null, node3);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 4: Statement with no side effect
    String code4 = "if (true) {}";
    Node node4 = getNode(code4);
    checkSideEffects.process(null, node4);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 5: Statement with side effect
    String code5 = "var x = 1;\nx++;";
    Node node5 = getNode(code5);
    checkSideEffects.process(null, node5);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    // Test case 6: Function call with side effect
    String code6 = "function sideEffectFunction() { document.write('Hello World!'); }";
    Node node6 = getNode(code6);
    checkSideEffects.process(null, node6);
    assertFalse(checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testCheckSideEffectsRemoveProtection() {
    AbstractCompiler compiler = new Compiler();
    CheckLevel level = CheckLevel.ERROR;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, true);
    StripProtection stripProtection = new StripProtection(compiler);

    // Test case 1: Simple assignment with protection
    String code1 = "var JSCOMPILER_PRESERVE x = 1;";
    Node node1 = getNode(code1);
    checkSideEffects.process(null, node1);
    assertEquals(1, checkSideEffects.problemNodes.size());

    stripProtection.process(null, node1);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, node1);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 2: Function call with no side effect and protection
    String code2 = "function JSCOMPILER_PRESERVE add(a, b) { return a + b; }";
    Node node2 = getNode(code2);
    checkSideEffects.process(null, node2);
    assertEquals(1, checkSideEffects.problemNodes.size());

    stripProtection.process(null, node2);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, node2);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 3: Expression with no side effect and protection
    String code3 = "var JSCOMPILER_PRESERVE x = true ? 1 : 2;";
    Node node3 = getNode(code3);
    checkSideEffects.process(null, node3);
    assertEquals(1, checkSideEffects.problemNodes.size());

    stripProtection.process(null, node3);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, node3);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 4: Statement with no side effect and protection
    String code4 = "if (JSCOMPILER_PRESERVE true) {}";
    Node node4 = getNode(code4);
    checkSideEffects.process(null, node4);
    assertEquals(1, checkSideEffects.problemNodes.size());

    stripProtection.process(null, node4);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, node4);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 5: Statement with side effect and protection
    String code5 = "var JSCOMPILER_PRESERVE x = 1;\nJSCOMPILER_PRESERVE x++;";
    Node node5 = getNode(code5);
    checkSideEffects.process(null, node5);
    assertEquals(2, checkSideEffects.problemNodes.size());

    stripProtection.process(null, node5);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, node5);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Test case 6: Function call with side effect and protection
    String code6 = "function JSCOMPILER_PRESERVE sideEffectFunction() { document.write('Hello World!'); }";
    Node node6 = getNode(code6);
    checkSideEffects.process(null, node6);
    assertEquals(1, checkSideEffects.problemNodes.size());

    stripProtection.process(null, node6);
    checkSideEffects = new CheckSideEffects(compiler, level, false);
    checkSideEffects.process(null, node6);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }
}