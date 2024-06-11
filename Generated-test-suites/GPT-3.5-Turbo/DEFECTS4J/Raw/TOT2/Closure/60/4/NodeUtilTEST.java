import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(forInNode));
    
    Node nonForInNode = new Node(Token.IF, new Node(Token.NAME));
    assertFalse(NodeUtil.isForIn(nonForInNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.BLOCK));
    assertTrue(NodeUtil.isStatement(scriptNode));
    
    Node expressionNode = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclNode));
    
    Node nonFunctionDeclNode = new Node(Token.EXPR_RESULT, new Node(Token.NAME));
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    hoistedFunctionDeclNode.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclNode));
    
    Node nonHoistedFunctionDeclNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclNode));
  }
}