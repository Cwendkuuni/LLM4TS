import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn_True() {
        Node n = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.FOR);
        Mockito.when(n.getChildCount()).thenReturn(3);

        boolean result = NodeUtil.isForIn(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_False_InvalidType() {
        Node n = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.IF);
        Mockito.when(n.getChildCount()).thenReturn(3);

        boolean result = NodeUtil.isForIn(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsForIn_False_InvalidChildCount() {
        Node n = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.FOR);
        Mockito.when(n.getChildCount()).thenReturn(2);

        boolean result = NodeUtil.isForIn(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement_True() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(n.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_False_InvalidParentType() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(n.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.EXPR_RESULT);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_True() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(NodeUtil.isStatement(n)).thenReturn(true);
        Mockito.when(n.getParent()).thenReturn(parent);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_False_InvalidType() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.IF);
        Mockito.when(NodeUtil.isStatement(n)).thenReturn(true);
        Mockito.when(n.getParent()).thenReturn(parent);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_False_InvalidStatement() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(NodeUtil.isStatement(n)).thenReturn(false);
        Mockito.when(n.getParent()).thenReturn(parent);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_True() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Node grandParent = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(NodeUtil.isStatement(n)).thenReturn(true);
        Mockito.when(n.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);
        Mockito.when(parent.getParent()).thenReturn(grandParent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_False_InvalidType() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Node grandParent = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.IF);
        Mockito.when(NodeUtil.isStatement(n)).thenReturn(true);
        Mockito.when(n.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);
        Mockito.when(parent.getParent()).thenReturn(grandParent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_False_InvalidStatement() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Node grandParent = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(NodeUtil.isStatement(n)).thenReturn(false);
        Mockito.when(n.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.SCRIPT);
        Mockito.when(parent.getParent()).thenReturn(grandParent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_False_InvalidParentType() {
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        Node grandParent = Mockito.mock(Node.class);
        Mockito.when(n.getType()).thenReturn(Token.FUNCTION);
        Mockito.when(NodeUtil.isStatement(n)).thenReturn(true);
        Mockito.when(n.getParent()).thenReturn(parent);
        Mockito.when(parent.getType()).thenReturn(Token.IF);
        Mockito.when(parent.getParent()).thenReturn(grandParent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertFalse(result);
    }
}