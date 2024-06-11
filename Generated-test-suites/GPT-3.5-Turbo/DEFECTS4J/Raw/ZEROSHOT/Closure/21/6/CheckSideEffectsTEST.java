import org.junit.*;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testVisit_NoOpStatement() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestNode();
    Node parent = createTestNode();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_QualifiedNameWithJSDocInfo() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestQualifiedNameNode();
    Node parent = createTestNode();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_EmptyNode() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestEmptyNode();
    Node parent = createTestNode();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_CommaNode_ResultUsed() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestCommaNode();
    Node parent = createTestCommaNodeParent();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_CommaNode_ResultUnused_NotLastChild() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestCommaNode();
    Node parent = createTestCommaNodeParentNotLastChild();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_CommaNode_ResultUnused_LastChild_NotExpressionResult() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestCommaNode();
    Node parent = createTestCommaNodeParentLastChildNotExpressionResult();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_CommaNode_ResultUnused_LastChild_ExpressionResult() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestCommaNode();
    Node parent = createTestCommaNodeParentLastChildExpressionResult();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_NotExprResultOrBlock_NotForLoop() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestNode();
    Node parent = createTestCommaNodeParent();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_NotExprResultOrBlock_ForLoop_NumChildNotFour() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestNode();
    Node parent = createTestForLoopNodeParentNumChildNotFour();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_NotExprResultOrBlock_ForLoop_NumChildFour_NotFirstOrThirdChild() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestNode();
    Node parent = createTestForLoopNodeParentNumChildFourNotFirstOrThirdChild();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testVisit_ExprResultNoSideEffects() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestNoSideEffectsExprResult();
    Node parent = createTestNode();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    assertEquals(1, compiler.getWarningCount());
    assertEquals("This code lacks side-effects. Is there a bug?", compiler.getWarnings().get(0).getDescription());

  }

  @Test
  public void testVisit_ExprResultWithSideEffects() {
    // Setup
    NodeTraversal t = createTestNodeTraversal();
    Node n = createTestSideEffectsExprResult();
    Node parent = createTestNode();

    // Execute
    CheckSideEffects.checkVisit(t, n, parent);

    // Verify
    // No assertion, verify that the code does not throw an exception

  }

  @Test
  public void testProtectSideEffects() {
    // Setup
    CheckSideEffects checkSideEffects = createTestCheckSideEffects();
    List<Node> problemNodes = createTestProblemNodes();
    
    // Execute
    checkSideEffects.protectSideEffects();

    // Verify
    assertEquals(1, input.getAstRoot(compiler).getChildCount());

  }

  @Test
  public void testAddExtern() {
    // Setup
    CheckSideEffects checkSideEffects = createTestCheckSideEffects();

    // Execute
    checkSideEffects.addExtern();

    // Verify
    assertEquals(1, input.getAstRoot(compiler).getChildCount());

  }

  private NodeTraversal createTestNodeTraversal() {
    return new NodeTraversal();
  }

  private Node createTestNode() {
    return new Node();
  }

  private Node createTestQualifiedNameNode() {
    return new Node(Token.NAME);
  }

  private Node createTestEmptyNode() {
    return new Node(Token.EMPTY);
  }

  private Node createTestCommaNode() {
    return new Node(Token.COMMA);
  }

  private Node createTestCommaNodeParent() {
    return new Node(Token.COMMA);
  }

  private Node createTestCommaNodeParentNotLastChild() {
    return new Node(Token.COMMA,
      new Node(Token.STRING),
      new Node(Token.FUNCTION));
  }

  private Node createTestCommaNodeParentLastChildNotExpressionResult() {
    return new Node(Token.COMMA,
      new Node(Token.STRING),
      new Node(Token.EXPR_RESULT));
  }

  private Node createTestCommaNodeParentLastChildExpressionResult() {
    return new Node(Token.COMMA,
      new Node(Token.STRING),
      new Node(Token.EXPR_RESULT,
        new Node(Token.NAME)));
  }

  private Node createTestForLoopNodeParentNumChildNotFour() {
    return new Node(Token.FOR,
      new Node(Token.NAME),
      new Node(Token.FALSE),
      new Node(Token.NAME));
  }

  private Node createTestForLoopNodeParentNumChildFourNotFirstOrThirdChild() {
    return new Node(Token.FOR,
      new Node(Token.NAME),
      new Node(Token.FALSE),
      new Node(Token.NAME),
      new Node(Token.TRUE));
  }

  private Node createTestNoSideEffectsExprResult() {
    return new Node(Token.EXPR_RESULT,
      new Node(Token.EMPTY));
  }

  private Node createTestSideEffectsExprResult() {
    return new Node(Token.EXPR_RESULT,
      new Node(Token.NAME));
  }

  private CheckSideEffects createTestCheckSideEffects() {
    return new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }

  private List<Node> createTestProblemNodes() {
    return new ArrayList<>();
  }

}