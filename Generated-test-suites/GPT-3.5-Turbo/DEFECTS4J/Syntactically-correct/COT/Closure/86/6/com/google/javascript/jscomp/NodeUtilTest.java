
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  private NodeUtil nodeUtil;
  
  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn() {
    // Test case for FOR-IN loop
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToBack(new Node(Token.NAME));
    forInNode.addChildToBack(new Node(Token.NAME));
    forInNode.addChildToBack(new Node(Token.NAME));
    assertTrue(nodeUtil.isForIn(forInNode));

    // Test case for non FOR-IN loop
    Node notForInNode = new Node(Token.FUNCTION);
    assertFalse(nodeUtil.isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    // Test case for statement node
    Node statementNode = new Node(Token.SCRIPT);
    assertTrue(nodeUtil.isStatement(statementNode));

    // Test case for non-statement node
    Node notStatementNode = new Node(Token.NAME);
    assertFalse(nodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test case for function declaration
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionDeclarationNode);
    assertTrue(nodeUtil.isFunctionDeclaration(functionDeclarationNode));

    // Test case for non-function declaration
    Node notFunctionDeclarationNode = new Node(Token.NAME);
    assertFalse(nodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test case for hoisted function declaration
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(hoistedFunctionDeclarationNode);
    assertTrue(nodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    // Test case for non-hoisted function declaration
    Node notHoistedFunctionDeclarationNode = new Node(Token.NAME);
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}

