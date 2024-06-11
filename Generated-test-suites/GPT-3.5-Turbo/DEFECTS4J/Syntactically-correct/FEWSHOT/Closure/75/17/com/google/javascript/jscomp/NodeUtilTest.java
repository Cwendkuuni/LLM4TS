import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        n1.addChildToBack(new Node(Token.EMPTY));
        n1.addChildToBack(new Node(Token.EMPTY));
        n1.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n1));

        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.EMPTY));
        n2.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT);
        Node n2 = new Node(Token.BLOCK);
        Node n3 = new Node(Token.LABEL);
        Node n4 = new Node(Token.WHILE);
        assertFalse(NodeUtil.isStatement(n1));
        assertTrue(NodeUtil.isStatement(n2));
        assertTrue(NodeUtil.isStatement(n3));
        assertTrue(NodeUtil.isStatement(n4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(n1));

        Node n2 = new Node(Token.FUNCTION);
        n2.addChildToBack(new Node(Token.NAME));
        Node n3 = new Node(Token.SCRIPT);
        n3.addChildToBack(n2);
        assertTrue(NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n1));

        Node n2 = new Node(Token.FUNCTION);
        n2.addChildToBack(new Node(Token.NAME));
        Node n3 = new Node(Token.SCRIPT);
        n3.addChildToBack(n2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));

        Node n4 = new Node(Token.FUNCTION);
        n4.addChildToBack(new Node(Token.NAME));
        Node n5 = new Node(Token.SCRIPT);
        n5.addChildToBack(n4);
        Node n6 = new Node(Token.FUNCTION);
        n6.addChildToBack(n5);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n4));
    }
}