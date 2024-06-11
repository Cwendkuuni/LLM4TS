import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    //Typical use case
    Node forInNode = new Node(Token.FOR);
    forInNode.addChildToBack(new Node(Token.NAME));
    forInNode.addChildToBack(new Node(Token.EXPR_RESULT));
    forInNode.addChildToBack(new Node(Token.BLOCK));
    assertTrue("Should return true for typical FOR-IN loop", NodeUtil.isForIn(forInNode));
    
    //Edge case: empty FOR statement
    Node emptyForNode = new Node(Token.FOR);
    assertFalse("Should return false for empty FOR statement", NodeUtil.isForIn(emptyForNode));
    
    //Error scenario: invalid node type
    Node invalidNode = new Node(Token.IF);
    assertFalse("Should return false for invalid node type", NodeUtil.isForIn(invalidNode));
  }
  
  @Test
  public void testIsStatement() {
    //Typical use case
    Node statementNode = new Node(Token.SCRIPT);
    Node childNode = new Node(Token.EXPR_RESULT);
    statementNode.addChildToBack(childNode);
    assertTrue("Should return true for node used as a statement", NodeUtil.isStatement(childNode));
    
    //Edge case: node is a block
    Node blockNode = new Node(Token.BLOCK);
    assertFalse("Should return false for node that is a block", NodeUtil.isStatement(blockNode));
    
    //Error scenario: missing parent node
    Node missingParentNode = new Node(Token.IF);
    assertFalse("Should return false for node with missing parent", NodeUtil.isStatement(missingParentNode));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    //Typical use case
    Node functionDeclNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);
    parentNode.addChildToBack(functionDeclNode);
    assertTrue("Should return true for function declaration", NodeUtil.isFunctionDeclaration(functionDeclNode));
    
    //Edge case: function declaration with expression
    Node expressionNode = new Node(Token.EXPR_RESULT);
    expressionNode.addChildToBack(functionDeclNode);
    assertFalse("Should return false for function declaration with expression", NodeUtil.isFunctionDeclaration(functionDeclNode));
    
    //Error scenario: node is not a statement
    Node nonStatementNode = new Node(Token.IF);
    assertFalse("Should return false for non-statement node", NodeUtil.isFunctionDeclaration(nonStatementNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    //Typical use case
    Node functionDeclNode = new Node(Token.FUNCTION);
    Node parentNode1 = new Node(Token.SCRIPT);
    parentNode1.addChildToBack(functionDeclNode);
    assertTrue("Should return true for hoisted function declaration in script", NodeUtil.isHoistedFunctionDeclaration(functionDeclNode));
    
    Node parentNode2 = new Node(Token.FUNCTION);
    parentNode2.addChildToBack(functionDeclNode);
    assertTrue("Should return true for hoisted function declaration in function", NodeUtil.isHoistedFunctionDeclaration(functionDeclNode));
    
    //Edge case: function declaration not in script or function
    Node nonScriptOrFunctionNode = new Node(Token.BLOCK);
    nonScriptOrFunctionNode.addChildToBack(functionDeclNode);
    assertFalse("Should return false for function declaration not in script or function", NodeUtil.isHoistedFunctionDeclaration(functionDeclNode));
    
    //Error scenario: function declaration with expression
    Node expressionNode = new Node(Token.EXPR_RESULT);
    expressionNode.addChildToBack(functionDeclNode);
    assertFalse("Should return false for function declaration with expression", NodeUtil.isHoistedFunctionDeclaration(functionDeclNode));
  }
}