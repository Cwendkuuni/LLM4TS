import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        checkSideEffects.process(externs, root);
        // TODO: Assert any necessary side effects.
    }

    @Test
    public void testHotSwapScript() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        // TODO: Assert any necessary side effects.
    }

    @Test
    public void testVisit() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal nodeTraversal = new NodeTraversal(null, null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.EXPR_RESULT);
        checkSideEffects.visit(nodeTraversal, n, parent);
        // TODO: Assert any necessary side effects.
    }

    @Test
    public void testProtectSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        checkSideEffects.protectSideEffects();
        // TODO: Assert any necessary side effects.
    }

    @Test
    public void testAddExtern() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        checkSideEffects.addExtern();
        // TODO: Assert any necessary side effects.
    }

    @Test
    public void testStripProtection() {
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        stripProtection.process(externs, root);
        // TODO: Assert any necessary side effects.
    }

}