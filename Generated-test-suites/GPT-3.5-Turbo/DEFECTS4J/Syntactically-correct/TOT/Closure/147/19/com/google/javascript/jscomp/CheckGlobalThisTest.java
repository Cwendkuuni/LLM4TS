
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void shouldFlagDangerousUseOfGlobalThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node node = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = new Node(Token.ASSIGN);
        Node grandParent = new Node(Token.GETPROP);
        parent.addChildToFront(node);
        grandParent.addChildToFront(parent);
        t.visit(null, node, grandParent);

        Assert.assertEquals(1, compiler.getErrors().size());
        Assert.assertEquals(1, compiler.getWarnings().size());
    }

    @Test
    public void shouldNotFlagSafeUseOfGlobalThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node node = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = new Node(Token.ASSIGN);
        Node grandParent = new Node(Token.GETPROP);
        parent.addChildAfter(node, new Node(Token.NAME, "property"));
        grandParent.addChildToFront(parent);
        t.visit(null, node, grandParent);

        Assert.assertEquals(0, compiler.getErrors().size());
        Assert.assertEquals(0, compiler.getWarnings().size());
    }

    @Test
    public void shouldNotFlagNonAssignmentUseOfGlobalThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        Node node = new Node(Token.THIS);
        NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
        Node parent = new Node(Token.ADD);
        parent.addChildToFront(node);
        t.visit(null, node, parent);

        Assert.assertEquals(0, compiler.getErrors().size());
        Assert.assertEquals(0, compiler.getWarnings().size());
    }
}
