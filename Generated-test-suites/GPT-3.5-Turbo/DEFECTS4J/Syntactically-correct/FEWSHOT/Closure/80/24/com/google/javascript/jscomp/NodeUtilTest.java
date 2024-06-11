import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Token;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FOR);
        node.addChildToBack(new Node());
        node.addChildToBack(new Node());
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.LABEL);
        parent.addChildToBack(node);
        assertTrue(NodeUtil.isStatement(node));

        parent = new Node(Token.ADD);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        node = new Node(Token.ADD);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.ARROW);
        grandParent.addChildToBack(node);
        parent.addChildToBack(grandParent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}