import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.NAME));
        forInNode.addChildToBack(new Node(Token.BLOCK));
        forInNode.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(forInNode));

        Node notForInNode = new Node(Token.FOR);
        notForInNode.addChildToBack(new Node(Token.NAME));
        notForInNode.addChildToBack(new Node(Token.BLOCK));
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.BLOCK);
        statementNode.addChildToBack(childNode);
        assertTrue(NodeUtil.isStatement(childNode));

        Node notStatementNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        Node notFunctionDeclarationNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(hoistedFunctionDeclarationNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        notHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));
        Node parent2 = new Node(Token.FUNCTION);
        parent2.addChildToBack(notHoistedFunctionDeclarationNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}