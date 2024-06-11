import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.BLOCK);
    n.addChildToBack(new Node(Token.EMPTY));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);

    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    parent.addChildToBack(n);

    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToBack(functionNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}