import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    private static NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn_WithEmptyNode_ReturnsFalse() {
        Node emptyNode = new Node(Token.EMPTY);
        assertFalse(nodeUtil.isForIn(emptyNode));
    }

    @Test
    public void testIsForIn_WithNonForNode_ReturnsFalse() {
        Node nonForNode = new Node(Token.GETPROP);
        assertFalse(nodeUtil.isForIn(nonForNode));
    }

    @Test
    public void testIsForIn_WithForNodeWithoutThreeChildren_ReturnsFalse() {
        Node forNode = new Node(Token.FOR, new Node(Token.EXPR_RESULT), new Node(Token.BLOCK));
        assertFalse(nodeUtil.isForIn(forNode));
    }

    @Test
    public void testIsForIn_WithValidForNode_ReturnsTrue() {
        Node forNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(nodeUtil.isForIn(forNode));
    }

    @Test
    public void testIsStatement_WithRootScriptNode_ReturnsTrue() {
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(nodeUtil.isStatement(scriptNode));
    }

    @Test
    public void testIsStatement_WithRootBlockNode_ReturnsTrue() {
        Node blockNode = new Node(Token.BLOCK);
        assertTrue(nodeUtil.isStatement(blockNode));
    }

    @Test
    public void testIsStatement_WithRootLabelNode_ReturnsTrue() {
        Node labelNode = new Node(Token.LABEL);
        assertTrue(nodeUtil.isStatement(labelNode));
    }

    @Test
    public void testIsStatement_WithNonStatementParentNode_ReturnsFalse() {
        Node nonStatementParentNode = new Node(Token.GETPROP);
        Node childNode = new Node(Token.FOR);
        nonStatementParentNode.addChildToFront(childNode);
        assertFalse(nodeUtil.isStatement(childNode));
    }

    @Test
    public void testIsFunctionDeclaration_WithNonFunctionNode_ReturnsFalse() {
        Node nonFunctionNode = new Node(Token.EXPR_RESULT);
        assertFalse(nodeUtil.isFunctionDeclaration(nonFunctionNode));
    }

    @Test
    public void testIsFunctionDeclaration_WithFunctionNodeAndNonStatementParent_ReturnsFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        assertFalse(nodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration_WithValidFunctionDeclaration_ReturnsTrue() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentBlockNode = new Node(Token.BLOCK);
        Node scriptNode = new Node(Token.SCRIPT);
        parentBlockNode.addChildToFront(functionNode);
        scriptNode.addChildToFront(parentBlockNode);
        assertTrue(nodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithNonFunctionDeclaration_ReturnsFalse() {
        Node nonFunctionDeclarationNode = new Node(Token.VAR);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(nonFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithFunctionDeclarationOutsideScript_ReturnsFalse() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parentBlockNode = new Node(Token.BLOCK);
        Node grandParentFunctionNode = new Node(Token.FUNCTION);
        parentBlockNode.addChildToFront(functionDeclarationNode);
        grandParentFunctionNode.addChildToFront(parentBlockNode);
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_WithFunctionDeclarationInsideScript_ReturnsTrue() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parentBlockNode = new Node(Token.BLOCK);
        Node scriptNode = new Node(Token.SCRIPT);
        parentBlockNode.addChildToFront(functionDeclarationNode);
        scriptNode.addChildToFront(parentBlockNode);
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    }
}