import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.NAME), new Node(Token.NEW), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT, new Node(Token.BLOCK));
        assertTrue(NodeUtil.isStatement(scriptNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK));
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT, new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK)));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getFirstChild()));
    }
}