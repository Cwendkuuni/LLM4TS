import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n1 = new Node(Token.FOR);
    n1.addChildToBack(new Node(Token.EMPTY)); // initialization
    n1.addChildToBack(new Node(Token.EMPTY)); // condition
    n1.addChildToBack(new Node(Token.EMPTY)); // increment

    assertFalse(NodeUtil.isForIn(n1)); // empty child nodes

    Node n2 = new Node(Token.FOR);
    n2.addChildToBack(new Node(Token.EMPTY)); // initialization
    n2.addChildToBack(new Node(Token.IN)); // condition
    n2.addChildToBack(new Node(Token.EMPTY)); // increment

    assertTrue(NodeUtil.isForIn(n2)); // has a "IN" token

    Node n3 = new Node(Token.COMMA);
    n3.addChildToBack(new Node(Token.EMPTY)); // initialization
    n3.addChildToBack(new Node(Token.EMPTY)); // condition
    n3.addChildToBack(new Node(Token.EMPTY)); // increment

    assertFalse(NodeUtil.isForIn(n3)); // not a "FOR" token
  }

  @Test
  public void testIsStatement() {
    Node n1 = new Node(Token.SCRIPT);
    n1.addChildToBack(new Node(Token.EMPTY)); // expression

    assertTrue(NodeUtil.isStatement(n1)); // is a "SCRIPT" token

    Node n2 = new Node(Token.BLOCK);
    n2.addChildToBack(new Node(Token.EMPTY)); // expression

    assertTrue(NodeUtil.isStatement(n2)); // is a "BLOCK" token

    Node n3 = new Node(Token.LABEL);
    n3.addChildToBack(new Node(Token.EMPTY)); // expression

    assertTrue(NodeUtil.isStatement(n3)); // is a "LABEL" token

    Node n4 = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isStatement(n4)); // no parent node
  }

  @Test
  public void testIsFunctionDeclaration() {
    // typical use case
    Node n1 = new Node(Token.FUNCTION);
    n1.addChildToFront(new Node(Token.NAME));

    assertTrue(NodeUtil.isFunctionDeclaration(n1));

    // edge case
    Node n2 = new Node(Token.FUNCTION);
    n2.addChildToBack(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isFunctionDeclaration(n2));
    
    // error scenario
    assertFalse(NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // typical use case
    Node n1 = new Node(Token.SCRIPT);
    Node n2 = new Node(Token.FUNCTION);
    n2.addChildToFront(new Node(Token.NAME));
    n1.addChildToEnd(n2);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n2));

    // edge case
    Node n3 = new Node(Token.SCRIPT);
    Node n4 = new Node(Token.FUNCTION);
    n4.addChildToBack(new Node(Token.EMPTY));
    n3.addChildToEnd(n4);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n4));

    // error scenario
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
  }

}