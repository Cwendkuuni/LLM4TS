import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.ADD));
    node.addChildToBack(new Node(Token.SUB));
    node.addChildToBack(new Node(Token.MUL));
    
    assertFalse("Should return false", NodeUtil.isForIn(node));
    
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToBack(new Node(Token.ADD));
    forInNode.addChildToBack(new Node(Token.SUB));
    forInNode.addChildToBack(new Node(Token.IN));
    
    assertTrue("Should return true", NodeUtil.isForIn(forInNode));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    Node childNode = new Node(Token.BLOCK);
    node.addChildToFront(childNode);

    assertTrue("Should return true", NodeUtil.isStatement(childNode));
    
    Node nonStatementNode = new Node(Token.ADD);
    
    assertFalse("Should return false", NodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    
    assertFalse("Should return false", NodeUtil.isFunctionDeclaration(functionNode));
    
    Node statementNode = new Node(Token.ADD);
    functionNode.addChildToFront(statementNode);
    
    assertFalse("Should return false", NodeUtil.isFunctionDeclaration(functionNode));
    
    Node parentNode = new Node(Token.SCRIPT);
    parentNode.addChildToFront(functionNode);
    
    assertTrue("Should return true", NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);
    parentNode.addChildToFront(functionNode);
    
    assertFalse("Should return false", NodeUtil.isHoistedFunctionDeclaration(functionNode));
    
    Node grandParentNode = new Node(Token.FUNCTION);
    grandParentNode.addChildToFront(parentNode);
    
    assertFalse("Should return false", NodeUtil.isHoistedFunctionDeclaration(functionNode));
    
    Node greatGrandParentNode = new Node(Token.SCRIPT);
    greatGrandParentNode.addChildToFront(grandParentNode);
    
    assertTrue("Should return true", NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}