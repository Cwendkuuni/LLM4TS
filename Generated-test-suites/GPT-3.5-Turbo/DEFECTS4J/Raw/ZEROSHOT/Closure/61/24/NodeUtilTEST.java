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
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(nodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(nodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);
        
        assertTrue(nodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.NAME);
        
        assertFalse(nodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.addChildToBack(new Node(Token.NAME));
        
        assertTrue(nodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        
        assertFalse(nodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME));
        scriptNode.addChildToBack(functionNode);
        
        assertTrue(nodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
    
    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode);
        
        assertFalse(nodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}