import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        Node n2 = new Node(Token.ADD);
        Node n3 = new Node(Token.VAR);
        
        // Testing for a FOR-IN loop
        n1.addChildToBack(new Node(Token.VAR));
        n1.addChildToBack(new Node(Token.ASSIGN));
        n1.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(n1));
        
        // Testing for a non-FOR-IN loop
        n2.addChildToBack(new Node(Token.VAR));
        n2.addChildToBack(new Node(Token.ASSIGN));
        n2.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n2));

        // Testing for a non-FOR-IN loop
        n3.addChildToBack(new Node(Token.VAR));
        n3.addChildToBack(new Node(Token.ASSIGN));
        n3.addChildToBack(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(n3));
    }
    
    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT);
        Node n2 = new Node(Token.BLOCK);
        Node n3 = new Node(Token.LABEL);
        Node n4 = new Node(Token.IF);
        
        // Testing for a statement node
        n1.addChildToBack(new Node(Token.VAR));
        assertTrue(NodeUtil.isStatement(n1));
        
        // Testing for a statement node
        n2.addChildToBack(new Node(Token.FUNCTION));
        assertTrue(NodeUtil.isStatement(n2));
        
        // Testing for a statement node
        n3.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isStatement(n3));
        
        // Testing for a non-statement node
        n4.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isStatement(n4));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.NAME);
        
        // Testing for function declaration
        n1.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(n1));
        
        // Testing for non-function declaration
        assertFalse(NodeUtil.isFunctionDeclaration(n2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.NAME);
        Node n3 = new Node(Token.SCRIPT);
        Node n4 = new Node(Token.FUNCTION);
        
        // Testing for hoisted function declaration
        n1.addChildToBack(new Node(Token.NAME));
        n3.addChildToBack(n1);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n1));
        
        // Testing for non-hoisted function declaration
        n2.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
        
        // Testing for non-hoisted function declaration
        n4.addChildToBack(new Node(Token.NAME));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n4));
    }
}