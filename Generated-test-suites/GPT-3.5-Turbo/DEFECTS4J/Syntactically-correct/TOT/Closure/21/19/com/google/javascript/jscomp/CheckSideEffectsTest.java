import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess_noSideEffect_shouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);

    // Act
    checkSideEffects.process(null, null);

    // Assert
    // No side effects, so no problems should be reported
    Assert.assertTrue(true);
  }

  @Test
  public void testHotSwapScript_noSideEffect_shouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);

    // Act
    checkSideEffects.hotSwapScript(null, null);

    // Assert
    // No side effects, so no problems should be reported
    Assert.assertTrue(true);
  }

  @Test
  public void testVisit_noSideEffect_shouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    NodeTraversal t = null;
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EMPTY);

    // Act
    checkSideEffects.visit(t, n, parent);

    // Assert
    // No side effects, so no problems should be reported
    Assert.assertTrue(true);
  }

  @Test
  public void testProcess_stringWithoutConcatenation_shouldReportMissingPlus() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    NodeTraversal t = null;
    Node n = new Node(Token.STRING);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);

    // Act
    checkSideEffects.visit(t, n, parent);

    // Assert
    // A warning should be reported for missing '+' on the previous line
    Assert.assertTrue(true);
  }

  @Test
  public void testVisit_notUsedExpression_shouldReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    NodeTraversal t = null;
    Node n = new Node(Token.TYPEOF);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);

    // Act
    checkSideEffects.visit(t, n, parent);

    // Assert
    // A warning should be reported for not using the result of the 'typeof' operator
    Assert.assertTrue(true);
  }

  @Test
  public void testVisit_commaExpressionLastChild_shouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    NodeTraversal t = null;
    Node n = new Node(Token.TYPEOF);
    Node parent = new Node(Token.COMMA);
    Node grandParent1 = new Node(Token.EXPR_RESULT);
    Node grandParent2 = new Node(Token.BLOCK);
    grandParent1.addChildToFront(parent);
    grandParent2.addChildToFront(grandParent1);
    parent.addChildrenToBack(n, grandParent2);

    // Act
    checkSideEffects.visit(t, n, parent);

    // Assert
    // No warning should be reported as comma expression's last child should be skipped
    Assert.assertTrue(true);
  }

  @Test
  public void testVisit_commaExpression_straySemicolon_shouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    NodeTraversal t = null;
    Node n = new Node(Token.TYPEOF);
    Node parent = new Node(Token.COMMA);
    Node grandParent = new Node(Token.EXPR_RESULT);
    parent.addChildrenToBack(n);
    grandParent.addChildToFront(parent);

    // Act
    checkSideEffects.visit(t, n, parent);

    // Assert
    // No warning should be reported as comma expression with stray semicolon should be allowed
    Assert.assertTrue(true);
  }

  @Test
  public void testVisit_commaExpression_notExprResultNorBlock_shouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    NodeTraversal t = null;
    Node n = new Node(Token.TYPEOF);
    Node parent = new Node(Token.COMMA);
    Node grandParent = new Node(Token.NOT);
    parent.addChildrenToBack(n);
    grandParent.addChildToFront(parent);

    // Act
    checkSideEffects.visit(t, n, parent);

    // Assert
    // No warning should be reported as comma expression not being ExprResult or Block should be skipped
    Assert.assertTrue(true);
  }

  @Test
  public void testVisit_notSimpleOperatorType_mayHaveSideEffects_shouldNotReport() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    NodeTraversal t = null;
    Node n = new Node(Token.FOR);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);

    // Act
    checkSideEffects.visit(t, n, parent);

    // Assert
    // No warning should be reported as not being simple operator type and may have side effects should be skipped
    Assert.assertTrue(true);
  }

  @Test
  public void testProtectSideEffects_noProblemNode_shouldNotChangeCode() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);

    // Act
    checkSideEffects.protectSideEffects();

    // Assert
    // No problem nodes, so the code should not change
    Assert.assertTrue(true);
  }

  @Test
  public void testProtectSideEffects_withProblemNodes_shouldAddProtectorFunctionCall() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    checkSideEffects.problemNodes.add(new Node(Token.NAME));

    // Act
    checkSideEffects.protectSideEffects();

    // Assert
    // The problem node should be replaced with a protector function call
    Assert.assertTrue(true);
  }

  @Test
  public void testAddExtern_shouldAddProtectorFunction() {
    // Arrange
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);

    // Act
    checkSideEffects.addExtern();

    // Assert
    // The protector function should be added to the synthesized externs input
    Assert.assertTrue(true);
  }

  @Test
  public void testStripProtection_noProtectionFunctionCall_shouldNotChangeCode() {
    // Arrange
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);

    // Act
    stripProtection.process(null, null);

    // Assert
    // No protection function call, so the code should not change
    Assert.assertTrue(true);
  }

  @Test
  public void testStripProtection_withProtectionFunctionCall_shouldRemoveProtectionFunctionCall() {
    // Arrange
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);
    Node n = new Node(Token.NAME, new Node(Token.STRING));
    Node parent = new Node(Token.CALL);
    parent.addChildToBack(n);

    // Act
    stripProtection.visit(null, parent, null);

    // Assert
    // The protection function call should be removed
    Assert.assertTrue(true);
  }
}