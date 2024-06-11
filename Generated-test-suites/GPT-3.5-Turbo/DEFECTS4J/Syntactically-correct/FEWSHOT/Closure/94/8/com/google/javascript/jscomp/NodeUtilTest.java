import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.NAME));
        forInNode.addChildToBack(new Node(Token.IN));
        forInNode.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(forInNode));

        Node notForInNode = new Node(Token.FOR);
        notForInNode.addChildToBack(new Node(Token.NAME));
        notForInNode.addChildToBack(new Node(Token.ADD));
        notForInNode.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node functionNode = new Node(Token.FUNCTION);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        Node notFunctionDeclarationNode = new Node(Token.ADD);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode1 = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode1.addChildToBack(new Node(Token.NAME));
        Node hoistedFunctionDeclarationNode2 = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode2.addChildToBack(new Node(Token.NAME));
        Node parentScriptNode = new Node(Token.SCRIPT);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentScriptNode.addChildToBack(hoistedFunctionDeclarationNode1);
        parentFunctionNode.addChildToBack(hoistedFunctionDeclarationNode2);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentScriptNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentFunctionNode));
    }
}