import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.NAME));
    assertTrue("Node does not represent a FOR-IN loop", NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.FUNCTION), new Node(Token.NAME));
    assertTrue("Node is not used as a statement", NodeUtil.isStatement(scriptNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.BLOCK));
    assertTrue("Node is not a function declaration", NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.NAME), new Node(Token.BLOCK)));
    assertTrue("Node is not a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(scriptNode.getFirstChild()));
  }
}