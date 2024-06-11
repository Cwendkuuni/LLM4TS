import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.DO));
    n.addChildToBack(new Node(Token.BLOCK));
    assertTrue(NodeUtil.isForIn(n));
    
    Node notForIn = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isForIn(notForIn));
  }

  @Test
  public void testIsStatement() {
    Node scriptParent = new Node(Token.SCRIPT);
    Node n = new Node(Token.VAR);
    n.setParent(scriptParent);
    assertTrue(NodeUtil.isStatement(n));
    
    Node nonStatementParent = new Node(Token.FUNCTION);
    n.setParent(nonStatementParent);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node statementParent = new Node(Token.SCRIPT);
    functionNode.setParent(statementParent);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    
    Node expressionParent = new Node(Token.VAR);
    functionNode.setParent(expressionParent);
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptParent = new Node(Token.SCRIPT);
    functionNode.setParent(scriptParent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    
    Node functionParent = new Node(Token.FUNCTION);
    functionNode.setParent(new Node(Token.BLOCK));
    functionParent.addChildToFront(functionNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

}