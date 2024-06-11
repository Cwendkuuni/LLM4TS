import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node rootNode = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertTrue(NodeUtil.isForIn(rootNode));
  }

  @Test
  public void testIsStatement() {
    Node rootNode = new Node(Token.SCRIPT, new Node(Token.VAR));
    assertTrue(NodeUtil.isStatement(rootNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node rootNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    assertTrue(NodeUtil.isFunctionDeclaration(rootNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME));
    scriptNode.addChildToFront(functionNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}