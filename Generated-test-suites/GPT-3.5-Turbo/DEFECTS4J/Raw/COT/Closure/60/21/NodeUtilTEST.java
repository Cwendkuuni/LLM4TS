import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import static org.junit.Assert.assertTrue;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node[] {new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY)});
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT, new Node[] {new Node(Token.EMPTY)});
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION, new Node[] {});
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT, new Node[] {new Node(Token.FUNCTION, new Node[] {})});
        Node node = new Node(Token.FUNCTION, new Node[] {}, parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

}