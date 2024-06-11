import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
      // Test typical usage scenario
      Node n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
      assertTrue(NodeUtil.isForIn(n));

      // Test edge case: node is not of type FOR
      n = new Node(Token.WHILE, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
      assertFalse(NodeUtil.isForIn(n));

      // Test edge case: node has less than 3 children
      n = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
      assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
      // Test typical usage scenario
      Node n = new Node(Token.BLOCK);
      Node parent = new Node(Token.SCRIPT);
      parent.addChildToFront(n);
      assertTrue(NodeUtil.isStatement(n));

      // Test edge case: parent is not of type SCRIPT, BLOCK, or LABEL
      parent = new Node(Token.IF);
      parent.addChildToFront(n);
      assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
      // Test typical usage scenario
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.SCRIPT);
      parent.addChildToFront(new Node(Token.EMPTY));
      parent.addChildToFront(n);
      assertTrue(NodeUtil.isFunctionDeclaration(n));

      // Test edge case: node is not of type FUNCTION
      n = new Node(Token.ARRAYLIT);
      assertFalse(NodeUtil.isFunctionDeclaration(n));

      // Test edge case: node is not a statement
      n = new Node(Token.FUNCTION);
      parent = new Node(Token.IF);
      parent.addChildToFront(new Node(Token.EMPTY));
      parent.addChildToFront(n);
      assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
      // Test typical usage scenario
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.SCRIPT);
      parent.addChildToFront(new Node(Token.EMPTY));
      parent.addChildToFront(n);
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

      // Test edge case: node is not a hoisted function declaration
      n = new Node(Token.FUNCTION);
      parent = new Node(Token.SCRIPT);
      parent.addChildToBack(n);
      parent.addChildToFront(new Node(Token.EMPTY));
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

      // Test edge case: node is not a function declaration
      n = new Node(Token.WHILE);
      parent = new Node(Token.SCRIPT);
      parent.addChildToBack(n);
      parent.addChildToFront(new Node(Token.EMPTY));
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}