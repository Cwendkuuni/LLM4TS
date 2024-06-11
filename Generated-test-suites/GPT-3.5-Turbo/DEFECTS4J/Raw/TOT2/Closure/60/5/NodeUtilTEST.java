import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(forInNode));
    
    Node ifNode = new Node(Token.IF, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    assertTrue(NodeUtil.isStatement(scriptNode));

    Node exprNode = new Node(Token.EXPR_RESULT, new Node(Token.EMPTY));
    assertFalse(NodeUtil.isStatement(exprNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node funcDeclNode = new Node(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(funcDeclNode));

    Node exprNode = new Node(Token.EXPR_RESULT, new Node(Token.FUNCTION));
    assertFalse(NodeUtil.isFunctionDeclaration(exprNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFuncDeclNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT, hoistedFuncDeclNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFuncDeclNode));

    Node funcDeclNode = new Node(Token.FUNCTION);
    Node parentFuncNode = new Node(Token.FUNCTION, funcDeclNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(funcDeclNode));
  }
}