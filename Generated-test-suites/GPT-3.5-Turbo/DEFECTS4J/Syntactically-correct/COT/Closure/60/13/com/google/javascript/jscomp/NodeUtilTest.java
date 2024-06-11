import org.junit.Test;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToFront(new Node(Token.EMPTY));
        forInNode.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsNotForIn() {
        Node notForInNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.EMPTY);
        statementNode.addChildToBack(childNode);
        assertTrue(NodeUtil.isStatement(childNode));
    }

    @Test
    public void testIsNotStatement() {
        Node nonStatementNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node nonFunctionDeclarationNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));
        hoistedFunctionDeclarationNode.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
    }

    // Add additional test cases for edge cases and exception handling scenarios here

}


// Generated test cases for the NodeUtil class
@Test
public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToFront(new Node(Token.EMPTY));
        forInNode.addChildToFront(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(forInNode));
    }
    
    @Test
    public void testIsNotForIn() {
        Node notForInNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(notForInNode));
    }
    
    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.EMPTY);
        statementNode.addChildToBack(childNode);
        assertTrue(NodeUtil.isStatement(childNode));
    }
    
    @Test
    public void testIsNotStatement() {
        Node nonStatementNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }
    
    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    }
    
    @Test
    public void testIsNotFunctionDeclaration() {
        Node nonFunctionDeclarationNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));
        hoistedFunctionDeclarationNode.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    }
    
    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
    }
}
