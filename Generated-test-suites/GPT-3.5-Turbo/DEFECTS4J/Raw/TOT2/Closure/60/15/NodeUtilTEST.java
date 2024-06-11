import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(forInNode));
    
    Node notForInNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node nonStatementNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node nonFunctionDeclarationNode = new Node(Token.SCRIPT);
    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }
  
  // Additional test cases for edge cases and exception handling scenarios could be added here

}