import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node script = new Node(Token.SCRIPT);
        Node node = new Node(Token.EMPTY);
        script.addChildToBack(node);

        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.EMPTY);

        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);
        Node statement = new Node(Token.EMPTY);
        script.addChildToBack(statement);
        statement.addChildToFront(function);

        assertTrue(NodeUtil.isFunctionDeclaration(function));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);

        assertFalse(NodeUtil.isFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclarationInScript() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);
        Node statement = new Node(Token.EMPTY);
        script.addChildToBack(function);
        function.addChildToBack(statement);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclarationInFunction() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);
        Node function2 = new Node(Token.FUNCTION);
        Node statement = new Node(Token.EMPTY);
        script.addChildToBack(function);
        function.addChildToBack(function2);
        function2.addChildToBack(statement);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function2));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        Node statement = new Node(Token.EMPTY);
        function.addChildToBack(statement);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }
}

