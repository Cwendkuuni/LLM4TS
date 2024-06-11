import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = NodeFactory.createToken(Token.FOR, null, null, null);
    assertTrue(NodeUtil.isForIn(n));

    Node nInvalid = NodeFactory.createToken(Token.IF, null, null, null);
    assertFalse(NodeUtil.isForIn(nInvalid));
  }
}