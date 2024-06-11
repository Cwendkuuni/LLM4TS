import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ScopedAliasesTest {

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private NodeTraversal traversal;

    private ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scopedAliases = new ScopedAliases(compiler, null, null);
    }

    @Test
    public void testEnterScope() {
        Node n = new Node(Token.FUNCTION);
        scopedAliases.enterScope(traversal);
        verify(traversal, never()).makeError(n, ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY);
    }

    @Test
    public void testExitScope() {
        scopedAliases.exitScope(traversal);
        verify(traversal, never()).getCurrentNode();
    }

    @Test
    public void testShouldTraverse() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.VAR);
        assertFalse(scopedAliases.shouldTraverse(traversal, n, parent));
        n.setType(Token.SCRIPT);
        assertTrue(scopedAliases.shouldTraverse(traversal, n, parent));

        n.setType(Token.FUNCTION);
        assertTrue(scopedAliases.shouldTraverse(traversal, n, null));
        parent = new Node(Token.CALL);
        parent.getFirstChild().setQualifiedName("goog.scope");
        assertTrue(scopedAliases.shouldTraverse(traversal, n, parent));
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal(compiler, null, null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.VAR);
        scopedAliases.visit(t, n, parent);
        verify(compiler, never()).report(t.makeError(n, ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY));

        n = new Node(Token.CALL);
        n.setFirstChild(new Node(Token.NAME, "goog.scope"));
        parent = new Node(Token.EXPR_RESULT);
        scopedAliases.visit(t, n, parent);
        verify(compiler, never()).report(t.makeError(n, ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS));

        parent.setFirstChild(new Node(Token.TRUE));
        scopedAliases.visit(t, n, parent);
        verify(compiler).report(t.makeError(n, ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS));

        n.setSecondChild(new Node(Token.FUNCTION));
        scopedAliases.visit(t, n, parent);
        verify(traversal).getCurrentNode();
    }

    @Test
    public void testFixTypeNode() {
        Node typeNode = new Node(Token.STRING);
        Node childNode = new Node(Token.NAME);
        childNode.setString("dom");
        typeNode.addChildToBack(childNode);

        NodeTraversal t = new NodeTraversal(compiler, null, null);
        Node n = new Node(Token.VAR);
        n.setString("var dom = goog.dom");
        t.getScope().declare("dom", n.getFirstChild(), null);

        scopedAliases.fixTypeNode(typeNode);
        assertEquals("goog.dom", typeNode.getString());
    }
}