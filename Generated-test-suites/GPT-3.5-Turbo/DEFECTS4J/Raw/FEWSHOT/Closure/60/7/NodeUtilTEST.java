import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        node2.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isForIn(node1));
        assertTrue(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.BLOCK);
        Node node3 = new Node(Token.LABEL);
        Node node4 = new Node(Token.NAME);
        
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.BLOCK);
        Node parent3 = new Node(Token.LABEL);
        
        parent1.addChildToBack(node1);
        parent2.addChildToBack(node2);
        parent3.addChildToBack(node3);
        
        node1.setParent(parent1);
        node2.setParent(parent2);
        node3.setParent(parent3);
        
        assertTrue(NodeUtil.isStatement(node1));
        assertTrue(NodeUtil.isStatement(node2));
        assertTrue(NodeUtil.isStatement(node3));
        assertFalse(NodeUtil.isStatement(node4));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node function1 = new Node(Token.FUNCTION);
        Node function2 = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isFunctionDeclaration(function1));
        
        function1.setParent(new Node(Token.SCRIPT));
        function2.setParent(new Node(Token.BLOCK));
        
        assertTrue(NodeUtil.isFunctionDeclaration(function1));
        assertFalse(NodeUtil.isFunctionDeclaration(function2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node function1 = new Node(Token.FUNCTION);
        Node function2 = new Node(Token.FUNCTION);
        Node scriptParent = new Node(Token.SCRIPT);
        Node functionParent = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function1));
        
        function1.setParent(scriptParent);
        function2.setParent(functionParent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function2));
    }
}