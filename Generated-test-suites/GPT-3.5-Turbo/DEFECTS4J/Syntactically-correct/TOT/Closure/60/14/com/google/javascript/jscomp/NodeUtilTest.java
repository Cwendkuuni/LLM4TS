import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {
    
    private Node n;
    
    @Before
    public void setUp() {
        n = new Node(Token.FOR);
    }

    @Test
    public void testIsForIn_typicalCase() {
        n.addChildToFront(new Node(Token.FOR));
        n.addChildToFront(new Node(Token.FOR));
        n.addChildToFront(new Node(Token.FOR));
        
        boolean result = NodeUtil.isForIn(n);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsForIn_edgeCase() {
        n.addChildToFront(new Node(Token.FOR));
        n.addChildToFront(new Node(Token.STRING));
        n.addChildToFront(new Node(Token.OBJECTLIT));
        
        boolean result = NodeUtil.isForIn(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsForIn_errorScenario() {
        boolean result = NodeUtil.isForIn(null);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsStatement_typicalCase() {
        n.addChildToFront(new Node(Token.SCRIPT));
        
        boolean result = NodeUtil.isStatement(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_edgeCase() {
        n.addChildToFront(new Node(Token.BLOCK));
        
        boolean result = NodeUtil.isStatement(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_errorScenario() {
        boolean result = NodeUtil.isStatement(null);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_typicalCase() {
        n.addChildToFront(new Node(Token.FUNCTION));
        n.addChildToFront(new Node(Token.NAME));
        
        boolean result = NodeUtil.isFunctionDeclaration(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_edgeCase() {
        n.addChildToFront(new Node(Token.EXPR_RESULT));
        n.addChildToFront(new Node(Token.FUNCTION));
        n.addChildToFront(new Node(Token.NAME));
        
        boolean result = NodeUtil.isFunctionDeclaration(n);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_errorScenario() {
        boolean result = NodeUtil.isFunctionDeclaration(null);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_typicalCase() {
        n.addChildToFront(new Node(Token.FUNCTION));
        n.getParent().addChildToFront(new Node(Token.SCRIPT));
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_edgeCase() {
        n.addChildToFront(new Node(Token.FUNCTION));
        n.getParent().addChildToFront(new Node(Token.SCRIPT));
        n.getParent().getParent().addChildToFront(new Node(Token.FUNCTION));
        
        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_errorScenario() {
        boolean result = NodeUtil.isHoistedFunctionDeclaration(null);
        
        assertFalse(result);
    }
}