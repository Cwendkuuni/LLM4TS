import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR).addChildToBack(new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT).addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(scriptNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionDeclNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionDeclNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclNode));
    }
}