import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;

    @Before
    public void setUp() {
        checkSideEffects = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        checkSideEffects.process(externs, root);
        // TODO: Add assertions
    }

    @Test
    public void testHotSwapScript() {
        Node scriptRoot = new Node();
        Node originalRoot = new Node();
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        // TODO: Add assertions
    }

    @Test
    public void testVisit() {
        NodeTraversal traversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        checkSideEffects.visit(traversal, node, parent);
        // TODO: Add assertions
    }

    @Test
    public void testProtectSideEffects() {
        // TODO: Write test
    }

    @Test
    public void testAddExtern() {
        // TODO: Write test
    }

    @Test
    public void testStripProtection() {
        // TODO: Write test
    }
}