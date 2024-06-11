import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.NAME));
    
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node script = new Node(Token.SCRIPT);
    Node block = new Node(Token.BLOCK);
    Node label = new Node(Token.LABEL);

    assertTrue(NodeUtil.isStatement(script));
    assertTrue(NodeUtil.isStatement(block));
    assertTrue(NodeUtil.isStatement(label));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDecl = new Node(Token.FUNCTION);
    
    assertTrue(NodeUtil.isFunctionDeclaration(functionDecl));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDecl = new Node(Token.FUNCTION);
    Node script = new Node(Token.SCRIPT);
    script.addChildToFront(functionDecl);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDecl));
  }
}