import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));
        
        assertTrue(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_edge() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));
        
        assertFalse(nodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_typical() {
        Node node = new Node(Token.NAME);
        nodeUtil.isStatement(node);
        
        // Assertion for typical use case goes here
    }

    @Test
    public void testIsStatement_edge() {
        Node node = new Node(Token.LABEL);
        nodeUtil.isStatement(node);
        
        // Assertion for edge case goes here
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.NAME));
        nodeUtil.isFunctionDeclaration(node);
        
        // Assertion for typical use case goes here
    }

    @Test
    public void testIsFunctionDeclaration_edge() {
        Node node = new Node(Token.NAME);
        nodeUtil.isFunctionDeclaration(node);
        
        // Assertion for edge case goes here
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node parentNode = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        parentNode.addChildToFront(node);
        nodeUtil.isHoistedFunctionDeclaration(node);
        
        // Assertion for typical use case goes here
    }

    @Test
    public void testIsHoistedFunctionDeclaration_edge() {
        Node parentNode = new Node(Token.SCRIPT);
        Node grandparentNode = new Node(Token.FUNCTION);
        parentNode.addChildToFront(grandparentNode);
        Node node = new Node(Token.FUNCTION);
        grandparentNode.addChildToFront(node);
        nodeUtil.isHoistedFunctionDeclaration(node);
        
        // Assertion for edge case goes here
    }
}