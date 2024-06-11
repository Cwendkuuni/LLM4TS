import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node child1 = new Node(Token.NAME);
    Node child2 = new Node(Token.EMPTY);
    Node child3 = new Node(Token.EMPTY);
    node.addChildToBack(child1);
    node.addChildToBack(child2);
    node.addChildToBack(child3);

    boolean result = NodeUtil.isForIn(node);

    assertTrue(result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.BLOCK);
    Node child = new Node(Token.ROOT);
    node.addChildToBack(child);

    boolean result = NodeUtil.isStatement(node);

    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);

    boolean result = NodeUtil.isFunctionDeclaration(node);

    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    parent.addChildToBack(node);
    grandParent.addChildToBack(parent);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

    assertTrue(result);
  }

}