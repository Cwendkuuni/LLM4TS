package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Token;
import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @org.junit.Test
  public void testProcess_NoUselessCode() {
    Node root = createNode("var x = 1;");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @org.junit.Test
  public void testProcess_UselessCodeAssignment() {
    Node root = createNode("var s = \"this string is \";");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. This code lacks side-effects. Is there a bug?");
  }

  @org.junit.Test
  public void testProcess_UselessCodeFunctionCall() {
    Node root = createNode("x = foo();");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. Is there a missing '='?");
  }

  @org.junit.Test
  public void testProcess_UselessCodeEmptyStatement() {
    Node root = createNode(";");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @org.junit.Test
  public void testProcess_UselessCodeComments() {
    Node root = createNode("// Comment\n// This is a comment\n");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @org.junit.Test
  public void testProcess_UselessCodeSemicolonAtEndOfFile() {
    Node root = createNode("var x = 1;");
    root.setNext(createNode(Token.SEMICOLON));
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @org.junit.Test
  public void testProcess_UselessCodeFunctionCallWithEmptyStatement() {
    Node root = createNode("foo();;");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. Probably just a stray-semicolon. Doesn't hurt to check though");
  }

  @org.junit.Test
  public void testProcess_UselessCodeMultipleStatements() {
    Node root = createNode("var s = \"this string is \";\nx = foo();");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. This code lacks side-effects. Is there a bug?");
    verifyWarning("Suspicious code. Is there a missing '='?");
  }

  @org.junit.Test
  public void testProcess_UselessCodeComplexStatement() {
    Node root = createNode("var x = 1 + 2 * 3;\nvar y = 4;\n");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @org.junit.Test
  public void testProcess_UselessCodeAssignmentWithSimpleOperator() {
    Node root = createNode("x += 1;");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @org.junit.Test
  public void testProcess_ProtectSideEffectFreeCode() {
    Node root = createNode("var s = \"this string is \";\nvar x = s.length;");
    checkSideEffects.process(true, root);
    verifyNoWarnings();
  }

  private Node createNode(int nodeType) {
    return new Node(nodeType);
  }

  private Node createNode(String string) {
    return new Node(Token.STRING_LITERAL, null, string);
  }

  private void verifyWarning(String message) {
    Node warning = checkSideEffects.problemNodes.get(0);
    String warningMessage =
        String.format("Expected warning message '{0}'\nActual warning message: {1}", message, warning.getJSDocInfo().getSummary());
    assertThat(warning.getJSDocInfo().getSummary()).contains(message);
  }

  private void verifyNoWarnings() {
    assertThat(checkSideEffects.problemNodes.size()).isZero();
  }

  private void verifyNoWarnings(String message) {
    Node warning = checkSideEffects.problemNodes.get(0);
    String warningMessage = String.format("Expected no warning message or message '{0}'\nActual warning message: {1}", message, warning.getJSDocInfo().getSummary());
    assertNull(warning);
  }
}

class NodeUtilTest extends AbstractPostOrderCallback {

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    // Empty implementation
  }

  public static boolean isExpressionResultUsed(Node node) {
    return false;
  }

  public static boolean isSimpleOperatorType(int type) {
    return false;
  }

  public static boolean mayHaveSideEffects(Node node, AbstractCompiler compiler) {
    return false;
  }
}

class CheckSideEffectsProtectionTest extends AbstractPostOrderCallback {

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isCall() && n.getLastChild().isName() && n.getLastChild().getString().equals(CheckSideEffects.PROTECTOR_FN)) {
      Node replacement = IR.call(IR.name(CheckSideEffects.PROTECTOR_FN));
      replacement.putBooleanProp(Node.FREE_CALL, true);
      n.getParent().replaceChild(n, replacement);
      replacement.addChildToBack(n);
    }
  }
}