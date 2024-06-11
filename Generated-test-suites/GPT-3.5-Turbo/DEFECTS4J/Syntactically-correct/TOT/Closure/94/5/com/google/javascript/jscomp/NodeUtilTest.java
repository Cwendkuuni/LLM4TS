import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  private NodeUtil nodeUtil;
  
  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }
  
  @Test
  public void testIsForIn_ValidForInLoop_ReturnsTrue() {
    Node forInNode = new Node(Token.FOR);
    Node childNode1 = new Node(Token.SOMETHING);
    Node childNode2 = new Node(Token.SOMETHING);
    Node childNode3 = new Node(Token.SOMETHING);
    
    forInNode.addChildToBack(childNode1);
    forInNode.addChildToBack(childNode2);
    forInNode.addChildToBack(childNode3);
    
    boolean result = nodeUtil.isForIn(forInNode);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_NotForInLoop_ReturnsFalse() {
    Node notForInNode = new Node(Token.FOR);
    Node childNode1 = new Node(Token.SOMETHING);
    Node childNode2 = new Node(Token.SOMETHING);
    
    notForInNode.addChildToBack(childNode1);
    notForInNode.addChildToBack(childNode2);
    
    boolean result = nodeUtil.isForIn(notForInNode);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement_StatementNode_ReturnsTrue() {
    Node statementNode = new Node(Token.ANY_STATEMENT_NODE_TYPE);
    
    boolean result = nodeUtil.isStatement(statementNode);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_NonStatementNode_ReturnsFalse() {
    Node nonStatementNode = new Node(Token.SOMETHING_ELSE);
    
    boolean result = nodeUtil.isStatement(nonStatementNode);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_FunctionDeclaration_ReturnsTrue() {
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);
    functionDeclarationNode.setParent(parentNode);
    
    boolean result = nodeUtil.isFunctionDeclaration(functionDeclarationNode);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_NonFunctionDeclaration_ReturnsFalse() {
    Node nonFunctionDeclarationNode = new Node(Token.SOMETHING_ELSE);
    
    boolean result = nodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_HoistedFunctionDeclaration_ReturnsTrue() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node parentNode1 = new Node(Token.SCRIPT);
    hoistedFunctionDeclarationNode.setParent(parentNode1);
    
    boolean result = nodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_NonHoistedFunctionDeclaration_ReturnsFalse() {
    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    Node parentNode2 = new Node(Token.FUNCTION);
    Node grandparentNode1 = new Node(Token.SCRIPT);
    parentNode2.setParent(grandparentNode1);
    nonHoistedFunctionDeclarationNode.setParent(parentNode2);
    
    boolean result = nodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode);
    
    assertFalse(result);
  }
  
}