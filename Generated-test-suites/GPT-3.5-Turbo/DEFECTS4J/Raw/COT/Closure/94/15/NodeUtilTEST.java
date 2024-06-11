
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test for FOR-IN loop
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToFront(new Node(Token.EMPTY));
      
        assertTrue(NodeUtil.isForIn(node));

        // Test for non FOR-IN loop
        Node node2 = new Node(Token.FUNCTION);
        node2.addChildToFront(new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(node2));
    }
    
    @Test
    public void testIsStatement() {
        // Test for statement
        Node node = new Node(Token.SCRIPT);
        node.addChildToFront(new Node(Token.EMPTY));
      
        assertTrue(NodeUtil.isStatement(node));

        // Test for non-statement
        Node node2 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(node2));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        // Test for function declaration
        Node node = new Node(Token.FUNCTION);
        node.addChildToFront(new Node(Token.EMPTY));
      
        assertTrue(NodeUtil.isFunctionDeclaration(node));

        // Test for non-function declaration
        Node node2 = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test for hoisted function declaration
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(new Node(Token.EMPTY));
        scriptNode.addChildToFront(functionNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        // Test for non-hoisted function declaration
        Node functionNode2 = new Node(Token.FUNCTION);
        functionNode2.addChildToFront(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
    }

}
