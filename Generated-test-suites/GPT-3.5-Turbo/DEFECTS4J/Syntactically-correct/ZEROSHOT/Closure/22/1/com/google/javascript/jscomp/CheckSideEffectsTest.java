import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

    @Test
    public void testVisit() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal nodeTraversal = new NodeTraversal(null, checkSideEffects, null);
        Node n = IR.root();
        Node parent = IR.root();
        nodeTraversal.visit(null, n, parent);
        // Assert statements here
    }

    @Test
    public void testHotSwapScript() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        Node scriptRoot = IR.root();
        Node originalRoot = IR.root();
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        // Assert statements here
    }

    @Test
    public void testProcess() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        Node externs = IR.root();
        Node root = IR.root();
        checkSideEffects.process(externs, root);
        // Assert statements here
    }

    @Test
    public void testProtectSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        checkSideEffects.protectSideEffects();
        // Assert statements here
    }
}