import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node forInNode = new Node(Token.FOR);
        Node firstChild = new Node(Token.NAME);
        Node secondChild = new Node(Token.NAME);
        Node thirdChild = new Node(Token.NAME);
        forInNode.addChildToBack(firstChild);
        forInNode.addChildToBack(secondChild);
        forInNode.addChildToBack(thirdChild);
        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsForIn_empty() {
        Node forInNode = new Node(Token.FOR);
        assertFalse(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsForIn_differentType() {
        Node node = new Node(Token.NAME);
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_typical() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
    }

    @Test
    public void testIsStatement_notStatement() {
        Node node = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_typical() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        functionNode.setParent(parentScriptNode);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        functionNode.setParent(parentFunctionNode);
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration_notDeclaration() {
        Node node = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typical() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        functionNode.setParent(parentScriptNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        functionNode.setParent(parentFunctionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentBlockNode = new Node(Token.BLOCK);
        functionNode.setParent(parentBlockNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}