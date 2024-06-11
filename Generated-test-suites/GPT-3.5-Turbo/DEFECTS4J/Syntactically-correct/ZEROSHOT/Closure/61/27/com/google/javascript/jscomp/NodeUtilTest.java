import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node("test1");
        Node child2 = new Node("test2");
        Node child3 = new Node("test3");
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);

        boolean result = NodeUtil.isForIn(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement() {
        Node n = new Node("test");
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node statementParent = new Node(Token.SCRIPT);
        statementParent.addChildToBack(n);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node scriptParent = new Node(Token.SCRIPT);
        scriptParent.addChildToBack(n);
        Node functionParent = new Node(Token.FUNCTION);
        functionParent.addChildToBack(scriptParent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertTrue(result);
    }
}