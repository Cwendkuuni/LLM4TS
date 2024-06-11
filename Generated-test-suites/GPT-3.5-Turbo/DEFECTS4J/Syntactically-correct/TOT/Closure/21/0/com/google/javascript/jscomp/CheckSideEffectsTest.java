import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.diagnostic.DiagnosticType;
import com.google.javascript.jscomp.testing.JSCompCorrespondences;
import com.google.common.collect.Iterables;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private Node externs;
  private Node root;
  private CheckSideEffects pass;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    externs = new Node(Token.EMPTY);
    root = new Node(Token.EMPTY);
    pass = new CheckSideEffects(compiler, null, false);
  }

  @Test
  public void testProcess_NoIssues_ReportsNoErrors() {
    CheckSideEffects pass = new CheckSideEffects(compiler, null, false);
    pass.process(externs, root);
    verify(compiler, never()).report(any());
  }

  @Test
  public void testProcess_IssueInCode_ReportsError() {
    Node codeNode = new Node(Token.EMPTY);
    Node parent = new Node(Token.BLOCK, codeNode);
    Node nullNode = new Node(Token.NULL);
    codeNode.addChildToFront(nullNode);
    NodeTraversal.traverse(compiler, parent, pass);
    verify(compiler).reportJSCompWarning(
        eq(DiagnosticType.warning("JSC_USELESS_CODE", "Suspicious code. {0}")),
        eq(parent),
        JSCompCorrespondences.error("This code lacks side-effects. Is there a bug?"));
  }

  @Test
  public void testHotSwapScript_IssueInCode_ReportsError() {
    Node codeNode = new Node(Token.EMPTY);
    Node parent = new Node(Token.BLOCK, codeNode);
    Node nullNode = new Node(Token.NULL);
    codeNode.addChildToFront(nullNode);
    pass.hotSwapScript(parent, parent);
    verify(compiler).reportJSCompWarning(
        eq(DiagnosticType.warning("JSC_USELESS_CODE", "Suspicious code. {0}")),
        eq(parent),
        JSCompCorrespondences.error("This code lacks side-effects. Is there a bug?"));
  }

  @Test
  public void testVisit_IssueInCode_ReportsError() {
    Node codeNode = new Node(Token.EMPTY);
    Node parent = new Node(Token.BLOCK, codeNode);
    Node nullNode = new Node(Token.NULL);
    codeNode.addChildToFront(nullNode);
    Callback traversalCallback = pass.createCallback();
    traversalCallback.visit(nullNode, codeNode, null);
    verify(compiler).reportJSCompWarning(
        eq(DiagnosticType.warning("JSC_USELESS_CODE", "Suspicious code. {0}")),
        eq(nullNode),
        JSCompCorrespondences.error("This code lacks side-effects. Is there a bug?"));
  }

  @Test
  public void testHotSwapScript_NoIssues_ReportsNoErrors() {
    Node codeNode = new Node(Token.EMPTY);
    Node parent = new Node(Token.BLOCK, codeNode);
    Node nullNode = new Node(Token.NULL);
    codeNode.addChildToFront(nullNode);
    pass.hotSwapScript(parent, parent);
    verify(compiler, never()).report(any());
  }

  @Test
  public void testVisit_NoIssues_ReportsNoErrors() {
    Node codeNode = new Node(Token.EMPTY);
    Node parent = new Node(Token.BLOCK, codeNode);
    Node nullNode = new Node(Token.NULL);
    codeNode.addChildToFront(nullNode);
    Callback traversalCallback = pass.createCallback();
    traversalCallback.visit(nullNode, codeNode, null);
    verify(compiler, never()).report(any());
  }

  @Test
  public void testProcess_WithProtectedCode_AddsProtectionExtern() {
    CheckSideEffects pass = new CheckSideEffects(compiler, null, true);
    pass.process(externs, root);
    verify(compiler).reportCodeChange();
    List<Node> externsChildren = Iterables.toList(compiler.getSynthesizedExternsInput().getAstRoot(compiler).children());
    assertEquals(3, externsChildren.size());
    assertEquals("var JSCOMPILER_PRESERVE", externsChildren.get(0).toString());
    assertEquals("@noalias", externsChildren.get(0).getJSDocInfo().toString());
  }

  @Test
  public void testHotSwapScript_WithProtectedCode_AddsProtectionExtern() {
    CheckSideEffects pass = new CheckSideEffects(compiler, null, true);
    pass.hotSwapScript(root, root);
    verify(compiler).reportCodeChange();
    List<Node> externsChildren = Iterables.toList(compiler.getSynthesizedExternsInput().getAstRoot(compiler).children());
    assertEquals(3, externsChildren.size());
    assertEquals("var JSCOMPILER_PRESERVE", externsChildren.get(0).toString());
    assertEquals("@noalias", externsChildren.get(0).getJSDocInfo().toString());
  }

  @Test
  public void testProtectSideEffects_NoIssues_ReportsNoErrors() {
    List<Node> problemNodes = Lists.newArrayList();
    pass.setProblemNodes(problemNodes);
    pass.protectSideEffects();
    verify(compiler, never()).report(any());
    assertEquals(0, compiler.getSynthesizedExternsInput().getAstRoot(compiler).getChildCount());
  }

  @Test
  public void testProtectSideEffects_WithProblemNodes_AddsProtectionExtern() {
    List<Node> problemNodes = Lists.newArrayList(new Node(Token.EMPTY));
    pass.setProblemNodes(problemNodes);
    pass.protectSideEffects();
    List<Node> externsChildren = Iterables.toList(compiler.getSynthesizedExternsInput().getAstRoot(compiler).children());
    assertEquals(1, externsChildren.size());
    assertEquals("var JSCOMPILER_PRESERVE", externsChildren.get(0).toString());
    assertEquals("@noalias", externsChildren.get(0).getJSDocInfo().toString());
  }

  @Test
  public void testStripProtection_NoProtectionExtern_LeavesCodeUnchanged() {
    Node codeNode = new Node(Token.EMPTY);
    Node externs = new Node(Token.EMPTY);
    NodeTraversal.traverse(compiler, codeNode, pass.createStripProtectionCallback());
    assertEquals(codeNode.getChildCount(), 0);
  }

  @Test
  public void testStripProtection_WithProtectionExtern_RemovesProtectorCall() {
    Node codeNode = new Node(Token.EMPTY);
    Node externs = new Node(Token.EMPTY);
    Node protectionExtern = new Node(Token.VAR, new Node(Token.NAME, "JSCOMPILER_PRESERVE"));
    codeNode.addChildToFront(protectionExtern);
    Node protectorCall = new Node(Token.CALL, new Node(Token.NAME, "JSCOMPILER_PRESERVE"));
    codeNode.addChildToBack(protectorCall);
    NodeTraversal.traverse(compiler, codeNode, pass.createStripProtectionCallback());
    assertEquals(codeNode.getChildCount(), 0);
  }

  @Test
  public void testStripProtection_WithProtectionExtern_LeavesOtherNodesUnchanged() {
    Node codeNode = new Node(Token.EMPTY);
    Node externs = new Node(Token.EMPTY);
    Node protectionExtern = new Node(Token.VAR, new Node(Token.NAME, "JSCOMPILER_PRESERVE"));
    codeNode.addChildToFront(protectionExtern);
    Node protectorOtherCode = new Node(Token.EXPR_RESULT);
    codeNode.addChildToBack(protectorOtherCode);
    NodeTraversal.traverse(compiler, codeNode, pass.createStripProtectionCallback());
    assertEquals(codeNode.getChildCount(), 1);
    assertEquals(protectorOtherCode, codeNode.getFirstChild());
  }
}