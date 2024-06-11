import static org.junit.Assert.*;

import org.junit.Test;

public class NodeUtilTest {
    // test isForIn
    @Test
    public void testIsForIn() {
        // edge cases
        assertFalse(NodeUtil.isForIn(null));
        assertFalse(NodeUtil.isForIn(new Node()));
        
        // positive test cases
        Node node1 = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME, new Node(Token.FOR));
        Node child2 = new Node(Token.NAME, new Node(Token.FOR));
        node1.addChildToFront(child1);
        node1.addChildToFront(child2);
        Node node2 = new Node(Token.FOR_IN);

        assertFalse(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
    }

    // test isStatement
    @Test
    public void testIsStatement() {
        // edge cases
        assertFalse(NodeUtil.isStatement(null));
        assertFalse(NodeUtil.isStatement(new Node()));
        
        // positive test cases
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(scriptNode));
        
        Node blockNode = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(blockNode));
        
        Node labelNode = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(labelNode));
        
        Node otherNode = new Node(Token.BREAK);
        assertFalse(NodeUtil.isStatement(otherNode));
    }

    // test isFunctionDeclaration
    @Test
    public void testIsFunctionDeclaration() {
        // edge cases
        assertFalse(NodeUtil.isFunctionDeclaration(null));
        assertFalse(NodeUtil.isFunctionDeclaration(new Node()));
        
        // positive test cases
        Node functionNode = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        functionNode.setParent(parent1);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        
        Node parent2 = new Node(Token.BLOCK);
        functionNode.setParent(parent2);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        
        Node parent3 = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        grandParent.setParent(parent3);
        functionNode.setParent(grandParent);
        assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    }

    // test isHoistedFunctionDeclaration
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // edge cases
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(new Node()));
        
        // positive test cases
        Node functionNode = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        functionNode.setParent(parent1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node parent2 = new Node(Token.BLOCK);
        functionNode.setParent(parent2);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        Node parent3 = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        grandParent.setParent(parent3);
        functionNode.setParent(grandParent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}