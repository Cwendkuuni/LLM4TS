import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));

        node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(new Node(scriptNode)));

        Node blockNode = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(new Node(blockNode)));

        Node labelNode = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(new Node(labelNode)));

        Node otherNode = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(new Node(otherNode)));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(new Node(functionNode)));

        Node otherNode = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(new Node(otherNode)));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(new Node(functionNode));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(new Node(functionNode)));

        Node blockNode = new Node(Token.BLOCK);
        blockNode.addChildToBack(new Node(functionNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(functionNode)));

        Node otherNode = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node(otherNode)));
    }
}