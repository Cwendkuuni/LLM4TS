
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.*;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;

import static org.junit.Assert.*;

public class ScopedAliasesTest {

    private ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        scopedAliases = new ScopedAliases(null, null, null);
    }

    @Test
    public void testEnterScope() {
        Node externs = new Node(Token.EXPR_RESULT);
        Node root = new Node(Token.EXPR_RESULT);
        scopedAliases.enterScope(new NodeTraversal(null, externs, root));
    }

    @Test
    public void testExitScope() {
        Node externs = new Node(Token.EXPR_RESULT);
        Node root = new Node(Token.EXPR_RESULT);
        scopedAliases.exitScope(new NodeTraversal(null, externs, root));
    }

    @Test
    public void testShouldTraverse() {
        Node externs = new Node(Token.EXPR_RESULT);
        Node root = new Node(Token.EXPR_RESULT);
        assertTrue(scopedAliases.shouldTraverse(new NodeTraversal(null, externs, root), new Node(Token.FUNCTION), new Node(Token.IF)));
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal(null, new Node(Token.EXPR_RESULT), new Node(Token.FUNCTION));
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        scopedAliases.visit(t, node, parent);
    }

    @Test
    public void testHotSwapScript() {
        Node root = new Node(Token.EXPR_RESULT);
        scopedAliases.hotSwapScript(root, null);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXPR_RESULT);
        Node root = new Node(Token.EXPR_RESULT);
        scopedAliases.process(externs, root);
    }
}
