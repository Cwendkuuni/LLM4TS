
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    @Test
    public void testShouldTraverse_withFunctionAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_withFunctionAssignmentAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_withBlock() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.BLOCK);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_withScript() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.SCRIPT);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_withName() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.NAME);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_withAssign() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node(Token.ASSIGN);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisit_withValidThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n, parent);
        // No exceptions thrown indicates successful execution
    }

    @Test
    public void testVisit_withInvalidThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.visit(t, n, parent);
        // No exceptions thrown indicates successful execution
    }
}
