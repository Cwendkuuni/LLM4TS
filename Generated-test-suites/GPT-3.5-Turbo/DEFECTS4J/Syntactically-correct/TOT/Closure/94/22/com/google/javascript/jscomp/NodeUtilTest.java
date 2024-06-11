import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {
    
    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn_typical() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.FOR_IN));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.EMPTY));
        
        boolean result = nodeUtil.isForIn(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsForIn_noForIn() {
        Node n = new Node(Token.WHILE);
        
        boolean result = nodeUtil.isForIn(n);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsForIn_incompleteForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.FOR_IN));
        
        boolean result = nodeUtil.isForIn(n);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsStatement_typical() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        n.setParent(parent);
        
        boolean result = nodeUtil.isStatement(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsStatement_notStatement() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToFront(n);
        n.setParent(parent);
        
        boolean result = nodeUtil.isStatement(n);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_typical() {
        Node n = new Node(Token.FUNCTION);
        n.setParent(new Node(Token.SCRIPT));
        
        boolean result = nodeUtil.isFunctionDeclaration(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsFunctionDeclaration_notDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.setParent(new Node(Token.STATIC_MEMBER_NAME));
        
        boolean result = nodeUtil.isFunctionDeclaration(n);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(n);
        n.setParent(parent);
        
        boolean result = nodeUtil.isHoistedFunctionDeclaration(n);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.UNKNOWN);
        parent.addChildToFront(n);
        n.setParent(parent);
        
        boolean result = nodeUtil.isHoistedFunctionDeclaration(n);
        
        assertFalse(result);
    }
}