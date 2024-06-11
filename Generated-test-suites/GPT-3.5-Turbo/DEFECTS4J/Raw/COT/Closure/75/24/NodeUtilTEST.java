
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToFront(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(forInNode));
        
        Node notForInNode = new Node(Token.FOR);
        notForInNode.addChildToFront(new Node(Token.EMPTY));
        notForInNode.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        Node statementNode = new Node(Token.SCRIPT);
        Node childNode = new Node(Token.EMPTY);
        statementNode.addChildToBack(childNode);
        
        assertTrue(NodeUtil.isStatement(childNode));
        
        Node notStatementNode = new Node(Token.IS);
        Node childNode2 = new Node(Token.EMPTY);
        notStatementNode.addChildToBack(childNode2);
        
        assertFalse(NodeUtil.isStatement(childNode2));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
        
        Node notFunctionDeclarationNode = new Node(Token.IS);
        notFunctionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(hoistedFunctionDeclarationNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
        
        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        notHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}
