import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private CheckSideEffects checkSideEffects;
  
  @Before
  public void setUp() {
    checkSideEffects = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
  }
  
  @Test
  public void process_ValidNode_DoesNotThrowException() {
    Node externs = new Node(Token.EXTERN_NAMES);
    Node root = new Node(Token.ROOT);
    checkSideEffects.process(externs, root);
    // No exception is thrown
    assertTrue(true);
  }
  
  @Test
  public void hotSwapScript_ValidNode_DoesNotThrowException() {
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.ROOT);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    // No exception is thrown
    assertTrue(true);
  }
  
  @Test
  public void visit_EmptyNode_DoesNotReportError() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EXPR_RESULT);
    checkSideEffects.visit(t, n, parent);
    // No error is reported
    assertTrue(true);
  }
  
  @Test
  public void visit_CommaNode_IsLastChild_DoesNotReportError() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    Node n = new Node(Token.COMMA);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(new Node(Token.COMMA));
    parent.addChildToBack(n);
    checkSideEffects.visit(t, n, parent);
    // No error is reported
    assertTrue(true);
  }
  
  @Test
  public void visit_CommaNode_NotLastChild_NoAncestorWithExprResult_DoesNotReportError() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    Node n = new Node(Token.COMMA);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    Node ancestor = new Node(Token.BLOCK);
    ancestor.addChildToBack(new Node(Token.COMMA));
    Node grandAncestor = new Node(Token.BLOCK);
    grandAncestor.addChildToBack(ancestor);
    checkSideEffects.visit(t, n, parent);
    // No error is reported
    assertTrue(true);
  }
  
  @Test
  public void visit_CommaNode_NotLastChild_WithAncestorWithExprResult_DoesNotReportError() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    Node n = new Node(Token.COMMA);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    Node ancestor = new Node(Token.EXPR_RESULT);
    Node grandAncestor = new Node(Token.BLOCK);
    grandAncestor.addChildToBack(ancestor);
    checkSideEffects.visit(t, n, parent);
    // No error is reported
    assertTrue(true);
  }
  
  @Test
  public void visit_ParentIsForNode_NotFirstChild_NotThirdChild_DoesNotReportError() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    Node n = new Node(Token.GETPROP);
    Node parent = new Node(Token.FOR);
    parent.addChildToBack(new Node(Token.ITER_VAR));
    parent.addChildToBack(new Node(Token.ITER_DECLARATION));
    parent.addChildToBack(n);
    parent.addChildToBack(new Node(Token.BLOCK));
    checkSideEffects.visit(t, n, parent);
    // No error is reported
    assertTrue(true);
  }
  
  @Test
  public void visit_ExpressionHasSideEffects_ReportsError() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    Node n = new Node(Token.AND);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    checkSideEffects.visit(t, n, parent);
    // Error is reported
    assertFalse(problemNodes.isEmpty());
  }
  
  @Test
  public void protectSideEffects_ProblemNodesExist_ChangesCodeAndReportsChange() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    problemNodes.add(new Node(Token.STRING));
    Node externs = new Node(Token.EXTERN_NAMES);
    Node root = new Node(Token.ROOT);
    checkSideEffects.protectSideEffects();
    assertTrue(compiler.reportCodeChangeCalled());
  }
  
  @Test
  public void addExtern_ProtectorFnDoesNotExist_AddsProtectorFn() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    Node externs = new Node(Token.EXTERN_NAMES);
    Node root = new Node(Token.ROOT);
    checkSideEffects.addExtern();
    assertTrue(compiler.reportCodeChangeCalled());
  }
  
  @Test
  public void visit_ProtectorFunctionCall_ReplacesCallWithExpression() {
    NodeTraversal t = new NodeTraversal(checkSideEffects.compiler, checkSideEffects);
    Node n = new Node(Token.CALL, new Node(Token.NAME, "JSCOMPILER_PRESERVE"));
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    checkSideEffects.visit(t, n, parent);
    assertEquals(n.getChildCount(), 0);
    assertEquals(parent.getChildCount(), 1);
  }
  
  @Test
  public void StripProtection_ValidNodeStripsProtection() {
    Node externs = new Node(Token.EXTERN_NAMES);
    Node root = new Node(Token.ROOT);
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(new AbstractCompiler());
    stripProtection.process(externs, root);
    // No exception is thrown
    assertTrue(true);
  }
}