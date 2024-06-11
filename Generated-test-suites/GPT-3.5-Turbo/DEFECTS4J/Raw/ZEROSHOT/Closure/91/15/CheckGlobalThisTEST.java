import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;

    @Before
    public void setup() {
        compiler = new Compiler();
    }

    @Test
    public void shouldTraverse_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(NodeTraversal.Callback.class);
        Node parent = new Node(NodeTraversal.Callback.class);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, null);
        assertTrue(checkGlobalThis.shouldTraverse(t, node, parent));
    }

    @Test
    public void shouldTraverse_ReturnsFalse_IsConstructor() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        NodeTraversal.Callback jsDoc = new CheckGlobalThis(compiler, null);
        assertFalse(checkGlobalThis.shouldTraverse(t, node, parent));
    }

    @Test
    public void shouldTraverse_ReturnsFalse_HasThisType() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        NodeTraversal.Callback jsDoc = new CheckGlobalThis(compiler, null);
        assertFalse(checkGlobalThis.shouldTraverse(t, node, parent));
    }

    @Test
    public void shouldTraverse_ReturnsFalse_HasOverride() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        NodeTraversal.Callback jsDoc = new CheckGlobalThis(compiler, null);
        assertFalse(checkGlobalThis.shouldTraverse(t, node, parent));
    }

    @Test
    public void shouldTraverse_ReturnsFalse_ParentType() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        NodeTraversal.Callback jsDoc = new CheckGlobalThis(compiler, null);
        assertFalse(checkGlobalThis.shouldTraverse(t, node, parent));
    }

    @Test
    public void visit_ReportError() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.VAR);
        NodeTraversal.Callback jsDoc = new CheckGlobalThis(compiler, null);
        checkGlobalThis.visit(t, node, parent);
        assertEquals(1, compiler.getErrorCount());
    }

    @Test
    public void visit_DoNotReportError() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.BLOCK);
        NodeTraversal.Callback jsDoc = new CheckGlobalThis(compiler, null);
        checkGlobalThis.visit(t, node, parent);
        assertEquals(0, compiler.getErrorCount());
    }

    @Test
    public void visit_SetAssignLhsChildToNull() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.BLOCK);
        NodeTraversal.Callback jsDoc = new CheckGlobalThis(compiler, null);
        checkGlobalThis.visit(t, node, parent);
        assertNull(checkGlobalThis.assignLhsChild);
    }

}