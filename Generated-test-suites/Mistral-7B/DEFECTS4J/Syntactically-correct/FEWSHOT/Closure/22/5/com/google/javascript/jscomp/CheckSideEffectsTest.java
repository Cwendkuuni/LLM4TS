package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.Compiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;

/**
 * Test cases for the CheckSideEffects class.
 */
public class CheckSideEffectsTest {

  private static final DiagnosticType USELESS_CODE_ERROR = DiagnosticType.warning("JSC_USELESS_CODE");

  @Test
  public void testCheckSideEffects() {
    Compiler compiler = mock(Compiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checker = new CheckSideEffects(compiler, level, false);

    Node root = mockNode("function foo() {}");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCompiler()).thenReturn(compiler);

    checker.process(null, root);

    verifyNoWarnings(compiler);

    // Add a useless statement
    Node useless = mockNode("x = 1;");
    root.addChildToBack(useless);

    checker.process(null, root);

    verifyWarnings(compiler, 1, "Suspicious code. This code lacks side-effects. Is there a bug?", root, useless);

    // Add a statement that does have side-effects
    Node sideEffect = mockNode("document.write('Hello World');");
    root.addChildToBack(sideEffect);

    checker.process(null, root);

    verifyNoWarnings(compiler);
  }

  @Test
  public void testProtectSideEffects() {
    Compiler compiler = mock(Compiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checker = new CheckSideEffects(compiler, level, true);

    Node root = mockNode("function foo() {}");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCompiler()).thenReturn(compiler);

    checker.process(null, root);

    verifyNoWarnings(compiler);

    // Add a useless statement
    Node useless = mockNode("x = 1;");
    root.addChildToBack(useless);

    checker.process(null, root);

    verifyProtectedUselessStatement(compiler, root, useless);

    // Add a statement that does have side-effects
    Node sideEffect = mockNode("document.write('Hello World');");
    root.addChildToBack(sideEffect);

    checker.process(null, root);

    verifyNoWarnings(compiler);
    verifySideEffectNodeExists(compiler, root, sideEffect);
  }

  @Test
  public void testRemoveProtection() {
    Compiler compiler = mock(Compiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checker = new CheckSideEffects(compiler, level, true);

    Node root = mockNode("function foo() {}");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCompiler()).thenReturn(compiler);

    checker.process(null, root);

    // Add a useless statement that was protected
    Node protectedUseless = mockProtectedNode("x = 1;");
    root.addChildToBack(protectedUseless);

    checker.process(null, root);

    verifyNoWarnings(compiler);
    verifyUselessStatementDeleted(compiler, root, protectedUseless);
  }

  private void verifyNoWarnings(Compiler compiler) {
    assertEquals(0, compiler.getDiagnostics().size());
  }

  private void verifyWarnings(Compiler compiler, int numberOfWarnings, String message, Node node, Node problemNode) {
    assertEquals(numberOfWarnings, compiler.getDiagnostics().size());
    for (Object diagnostic : compiler.getDiagnostics()) {
      DiagnosticInfo diagnosticInfo = (DiagnosticInfo) diagnostic;
      assertEquals(USELESS_CODE_ERROR, diagnosticInfo.getType());
      assertEquals(message, diagnosticInfo.getMessage(null));
      assertEquals(node, diagnosticInfo.getSourceNode());
      assertEquals(problemNode, diagnosticInfo.getLocation().getSourceNode());
    }
  }

  private void verifyProtectedUselessStatement(Compiler compiler, Node root, Node node) {
    assertEquals(1, root.getChildCount());
    Node protector = root.getFirstChild();
    assertEquals(Token.CALL, protector.getType());
    Node protectorName = protector.getFirstChild();
    assertEquals("JSCOMPILER_PRESERVE", protectorName.getString());
    Node protectedNode = protector.getLastChild();
    assertEquals(node, protectedNode);
  }

  private void verifySideEffectNodeExists(Compiler compiler, Node root, Node node) {
    assertEquals(1, root.getChildCount());
    Node protector = root.getFirstChild();
    assertFalse(protector.isCall());
    Node sideEffect = root.getLastChild();
    assertEquals(node, sideEffect);
  }

  private void verifyUselessStatementDeleted(Compiler compiler, Node root, Node node) {
    assertEquals(0, root.getChildCount());
  }

  private Node mockNode(String nodeString) {
    Node node = IR.parseNode(nodeString);
    return node;
  }

  private Node mockProtectedNode(String nodeString) {
    Node protector = mockNode("JSCOMPILER_PRESERVE(" + nodeString + ")");
    return protector.getLastChild();
  }
}