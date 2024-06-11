import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test typical use case
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.FOR));
        node.addChildToBack(new Node(Token.FOR));
        node.addChildToBack(new Node(Token.FOR));
        assertTrue(NodeUtil.isForIn(node));

        // Test edge case with zero child count
        Node emptyNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(emptyNode));

        // Test edge case with three child count
        Node threeChildNode = new Node(Token.FOR);
        threeChildNode.addChildToBack(new Node(Token.FOR));
        threeChildNode.addChildToBack(new Node(Token.FOR));
        threeChildNode.addChildToBack(new Node(Token.FOR));
        assertFalse(NodeUtil.isForIn(threeChildNode));
    }

    @Test
    public void testIsStatement() {
        // Test typical use case
        Node node = new Node(Token.SCRIPT);
        Node child = new Node(Token.FUNCTION);
        node.addChildToBack(child);
        assertTrue(NodeUtil.isStatement(child));

        // Test edge case with null parent
        Node nullParentNode = new Node(Token.STRING);
        assertFalse(NodeUtil.isStatement(nullParentNode));
        
        // Test edge case with different parent types
        Node parentBlockNode = new Node(Token.BLOCK);
        parentBlockNode.addChildToBack(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isStatement(parentBlockNode));
        
        Node parentLabelNode = new Node(Token.LABEL);
        parentLabelNode.addChildToBack(new Node(Token.FUNCTION));
        assertFalse(NodeUtil.isStatement(parentLabelNode));
        
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test typical use case
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.FUNCTION));
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        // Test edge case with different parent type
        Node parentScriptNode = new Node(Token.SCRIPT);
        parentScriptNode.addChildToBack(new Node(Token.FUNCTION));
        assertTrue(NodeUtil.isFunctionDeclaration(parentScriptNode.getChildAtIndex(0)));

        // Test edge case with different node type
        Node nonFunctionNode = new Node(Token.MEMBER_FUNCTION_DEF);
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));

    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test typical use case
        Node node = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        node.addChildToBack(new Node(Token.FUNCTION));
        parentScriptNode.addChildToBack(node);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

        // Test edge case with different parent type
        Node parentFunctionNode = new Node(Token.FUNCTION);
        Node grandParentScriptNode = new Node(Token.SCRIPT);
        parentFunctionNode.addChildToBack(new Node(Token.FUNCTION));
        grandParentScriptNode.addChildToBack(parentFunctionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(parentFunctionNode.getChildAtIndex(0)));

        // Test edge case with non-hoisted function declaration
        Node nonHoistedNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedNode));

    }
}