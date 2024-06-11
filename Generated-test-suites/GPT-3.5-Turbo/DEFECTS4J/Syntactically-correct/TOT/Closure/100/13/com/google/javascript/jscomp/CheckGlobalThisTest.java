import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    checker = new CheckGlobalThis(null, null);
  }

  @Test
  public void testShouldTraverse_FunctionNotConstructor_NoThisAnnotation_ReturnsTrue() {
    // given
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();

    // when
    boolean result = checker.shouldTraverse(t, n, parent);

    // then
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionIsConstructor_ReturnsFalse() {
    // given
    NodeTraversal t = new NodeTraversal();
    Node n = createConstructorFunctionNode();
    Node parent = new Node();

    // when
    boolean result = checker.shouldTraverse(t, n, parent);

    // then
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionHasThisAnnotation_ReturnsFalse() {
    // given
    NodeTraversal t = new NodeTraversal();
    Node n = createFunctionNodeWithThisAnnotation();
    Node parent = new Node();

    // when
    boolean result = checker.shouldTraverse(t, n, parent);

    // then
    assertFalse(result);
  }

  @Test
  public void testVisit_NodeIsThis_ShouldReportError() {
    // given
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    NodeTraversal t = new NodeTraversal();
    boolean shouldReportThis = true;
    boolean shouldReportPropertyValue = false;

    // when
    checker.visit(t, n, parent);

    // then
    assertTrue(checker.compiler.hasReportedErrorWithMessageLevelType(
        CheckGlobalThis.GLOBAL_THIS, CheckLevel.WARNING));
  }

  @Test
  public void testShouldReportThis_AssignLhsChildNotNull_ShouldReturnTrue() {
    // given
    Node n = new Node();
    Node parent = new Node();
    checker.assignLhsChild = new Node();

    // when
    boolean result = checker.shouldReportThis(n, parent);

    // then
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ParentIsNotAssign_ShouldReturnFalse() {
    // given
    Node n = new Node();
    Node parent = new Node();
    checker.assignLhsChild = null;

    // when
    boolean result = checker.shouldReportThis(n, parent);

    // then
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_ParentIsAssign_NodeIsNotLeftOperand_ReturnsFalse() {
    // given
    Node n = new Node();
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node();
    Node rhs = new Node();
    parent.addChildToFront(lhs);
    parent.addChildToFront(rhs);
    checker.assignLhsChild = null;

    // when
    boolean result = checker.shouldReportThis(n, parent);

    // then
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_ParentIsAssign_NodeIsLeftOperand_ShouldReturnTrue() {
    // given
    Node n = new Node();
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node();
    Node rhs = new Node();
    parent.addChildToFront(lhs);
    parent.addChildToFront(rhs);
    checker.assignLhsChild = lhs;

    // when
    boolean result = checker.shouldReportThis(n, parent);

    // then
    assertTrue(result);
  }

  private Node createConstructorFunctionNode() {
    Node constructorFunctionNode = new Node(Token.FUNCTION);

    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    jsDocInfo.addParameter("param1");

    constructorFunctionNode.setJSDocInfo(jsDocInfo);

    return constructorFunctionNode;
  }

  private Node createFunctionNodeWithThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);

    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThis(new Node(Token.NAME));

    functionNode.setJSDocInfo(jsDocInfo);

    return functionNode;
  }
}