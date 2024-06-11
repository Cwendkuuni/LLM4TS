import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(n));
        
        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node script = new Node(Token.SCRIPT);
        Node block = new Node(Token.BLOCK);
        Node label = new Node(Token.LABEL);
        
        Node n = new Node(Token.ADD);
        n.addChildToBack(script);
        
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.ADD);
        n.addChildToBack(block);
        
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.ADD);
        n.addChildToBack(label);
        
        assertTrue(NodeUtil.isStatement(n));
        
        n = new Node(Token.ADD);
        n.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        
        parent.addChildToBack(function);
        
        assertTrue(NodeUtil.isFunctionDeclaration(function));
        
        function = new Node(Token.FUNCTION);
        parent = new Node(Token.BLOCK);
        
        parent.addChildToBack(function);
        
        assertFalse(NodeUtil.isFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        Node parent2 = new Node(Token.FUNCTION);
        
        parent1.addChildToBack(function);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
        
        function = new Node(Token.FUNCTION);
        parent1 = new Node(Token.SCRIPT);
        parent2.addChildToBack(function);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }
}