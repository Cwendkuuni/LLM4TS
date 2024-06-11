import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class CheckSideEffectsTest {

    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new Compiler();
    }

    @Test
    public void testProcess() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        Node externs = new Node(Token.EXTERN, new Node(Token.EMPTY));
        Node root = new Node(Token.SCRIPT, new Node(Token.EMPTY));

        checkSideEffects.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        Node scriptRoot = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        Node originalRoot = new Node(Token.SCRIPT, new Node(Token.EMPTY));

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    }

    @Test
    public void testProtectSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        checkSideEffects.protectSideEffects();
    }

    @Test
    public void testAddExtern() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        checkSideEffects.addExtern();
    }

    @Test
    public void testVisit() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        NodeTraversal traversal = new NodeTraversal(compiler, null);

        Node n = new Node(Token.UNDEFINED);
        Node parent = new Node(Token.SCRIPT, new Node(Token.EMPTY));

        checkSideEffects.visit(traversal, n, parent);
    }

    @Test
    public void testStripProtection() {
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node externs = new Node(Token.EXTERN, new Node(Token.EMPTY));
        Node root = new Node(Token.SCRIPT, new Node(Token.EMPTY));

        stripProtection.process(externs, root);
    }
}