import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test when node is a FOR loop with 3 children
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertEquals(true, NodeUtil.isForIn(node));

        // Test when node is a FOR loop with less than 3 children
        node.removeChild(node.getLastChild());

        assertEquals(false, NodeUtil.isForIn(node));

        // Test when node is not a FOR loop
        node = new Node(Token.EMPTY);

        assertEquals(false, NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        // Test when node's parent is a SCRIPT node
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(true, NodeUtil.isStatement(node));

        // Test when node's parent is a BLOCK node
        parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(true, NodeUtil.isStatement(node));

        // Test when node's parent is a LABEL node
        parent = new Node(Token.LABEL);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(true, NodeUtil.isStatement(node));

        // Test when node's parent is not a SCRIPT, BLOCK, or LABEL node
        parent = new Node(Token.EMPTY);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(false, NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test when node is a FUNCTION and is a statement
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(true, NodeUtil.isFunctionDeclaration(node));

        // Test when node is a FUNCTION but not a statement
        parent = new Node(Token.EMPTY);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(false, NodeUtil.isFunctionDeclaration(node));

        // Test when node is not a FUNCTION
        node = new Node(Token.EMPTY);
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(false, NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when node is a hoisted FUNCTION declaration
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));

        // Test when node is a FUNCTION declaration but not hoisted
        parent = new Node(Token.FUNCTION);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));

        // Test when node is not a FUNCTION declaration
        node = new Node(Token.EMPTY);
        parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(node));
    }
}