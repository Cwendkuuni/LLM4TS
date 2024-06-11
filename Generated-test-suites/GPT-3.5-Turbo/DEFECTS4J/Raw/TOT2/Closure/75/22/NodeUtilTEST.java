import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NUMBER));
        n.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.NAME);
        n.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(n));
        
        Node parent2 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.NAME);
        n2.setParent(parent2);
        
        assertFalse(NodeUtil.isStatement(n2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.setParent(new Node(Token.SCRIPT));
        
        assertTrue(NodeUtil.isFunctionDeclaration(n));
        
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        n.setParent(parent);
        parent.setParent(grandParent);
        
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
        
        Node grandParent = new Node(Token.FUNCTION);
        parent.setParent(grandParent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}