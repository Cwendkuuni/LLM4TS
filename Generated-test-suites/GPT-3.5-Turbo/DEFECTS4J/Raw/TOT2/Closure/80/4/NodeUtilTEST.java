import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    private Node rootNode;
    
    @Before
    public void setUp() {
        rootNode = new Node(Token.ROOT);
    }

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.FOR_IN, new Node(Token.COMMA), new Node(Token.EXPR_RESULT)));
        assertTrue(NodeUtil.isForIn(forInNode));
        
        Node notForInNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        statementNode.addChildToBack(new Node(Token.BLOCK));
        assertTrue(NodeUtil.isStatement(statementNode));
        
        Node nonStatementNode = new Node(Token.IF);
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclNode = new Node(Token.FUNCTION);
        functionDeclNode.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "testFunctionName")));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclNode));
        
        Node notFunctionDeclNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclNode.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "hoistedFunction")));
        Node scriptNode = new Node(Token.SCRIPT, hoistedFunctionDeclNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclNode));
        
        Node notHoistedFunctionDeclNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclNode));
    }
}