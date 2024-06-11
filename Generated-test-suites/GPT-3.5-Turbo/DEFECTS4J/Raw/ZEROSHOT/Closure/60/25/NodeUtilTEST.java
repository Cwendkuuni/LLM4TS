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
    // Test for FOR-IN loop node
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToBack(new Node(Token.EMPTY));
    forInNode.addChildToBack(new Node(Token.EMPTY));
    forInNode.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(forInNode));

    // Test for non-FOR-IN loop node
    Node otherNode = new Node(Token.WHILE);
    otherNode.addChildToBack(new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(otherNode));
  }

  @Test
  public void testIsStatement() {
    // Test for statement node
    Node statementNode = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(statementNode));

    // Test for non-statement node
    Node nonStatementNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test for function declaration node
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    functionDeclarationNode.putProp(NodeUtil.JSDOC_INFO_PROP, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    // Test for non-function declaration node
    Node nonFunctionDeclarationNode = new Node(Token.NAME);
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test for hoisted function declaration node
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT));
    hoistedFunctionDeclarationNode.putProp(NodeUtil.JSDOC_INFO_PROP, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    // Test for non-hoisted function declaration node
    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }
}