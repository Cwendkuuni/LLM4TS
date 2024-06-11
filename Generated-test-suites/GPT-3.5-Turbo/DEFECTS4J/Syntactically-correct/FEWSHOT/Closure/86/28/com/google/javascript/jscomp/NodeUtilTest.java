import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
  
    @Test
    public void testIsForIn() {
        Node n = createNode(Token.FOR, createNode(Token.NAME), createNode(Token.NAME), createNode(Token.NAME));
        assertFalse(NodeUtil.isForIn(n));

        n = createNode(Token.FOR, createNode(Token.NAME), createNode(Token.NAME));
        assertFalse(NodeUtil.isForIn(n));

        n = createNode(Token.FOR, createNode(Token.NAME), createNode(Token.NAME), createNode(Token.NAME), createNode(Token.NAME));
        assertFalse(NodeUtil.isForIn(n));

        n = createNode(Token.FOR, createNode(Token.NAME), createNode(Token.NAME), createNode(Token.NAME));
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = createNode(Token.BLOCK, createNode(Token.NAME));
        assertTrue(NodeUtil.isStatement(n));

        n = createNode(Token.SCRIPT, createNode(Token.NAME));
        assertTrue(NodeUtil.isStatement(n));

        n = createNode(Token.LABEL, createNode(Token.NAME));
        assertTrue(NodeUtil.isStatement(n));

        n = createNode(Token.IF, createNode(Token.NAME));
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = createNode(Token.FUNCTION, createNode(Token.NAME));
        assertFalse(NodeUtil.isFunctionDeclaration(n));

        n = createNode(Token.FUNCTION, createNode(Token.NAME));
        n.setParent(createNode(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(n));

        n = createNode(Token.FUNCTION, createNode(Token.NAME));
        n.setParent(createNode(Token.IF));
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = createNode(Token.FUNCTION, createNode(Token.NAME));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));

        Node parent = createNode(Token.SCRIPT);
        n = createNode(Token.FUNCTION, createNode(Token.NAME));
        n.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        parent = createNode(Token.FUNCTION);
        parent.setParent(createNode(Token.SCRIPT));
        n = createNode(Token.FUNCTION, createNode(Token.NAME));
        n.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

        parent = createNode(Token.IF);
        n = createNode(Token.FUNCTION, createNode(Token.NAME));
        n.setParent(parent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    private Node createNode(int type, Node... children) {
        Node node = new Node(type);
        for (Node child : children) {
            node.addChildToBack(child);
        }
        return node;
    }
}