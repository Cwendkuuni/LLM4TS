import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n));

        n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));

        parent = new Node(Token.BLOCK);
        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));

        parent = new Node(Token.LABEL);
        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isStatement(n));

        parent = new Node(Token.FUNCTION);
        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertFalse(NodeUtil.isStatement(n));

        parent = new Node(Token.EMPTY);
        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandParent = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(grandParent);
        Node n = new Node(Token.FUNCTION);
        grandParent.addChildToBack(n);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        grandParent = new Node(Token.SCRIPT);
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(grandParent);
        n = new Node(Token.FUNCTION);
        grandParent.addChildToBack(n);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        grandParent = new Node(Token.SCRIPT);
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(new Node(Token.EMPTY));
        parent.addChildToBack(grandParent);
        n = new Node(Token.FUNCTION);
        grandParent.addChildToBack(n);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        n = new Node(Token.EMPTY);
        parent.addChildToBack(n);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}