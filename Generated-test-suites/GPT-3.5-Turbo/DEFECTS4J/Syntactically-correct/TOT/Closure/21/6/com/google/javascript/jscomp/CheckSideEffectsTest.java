import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);

        // TODO: Add test setup here

        checkSideEffects.process(externs, root);

        // TODO: Add expected assertions here
    }

    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        // TODO: Add test setup here

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // TODO: Add expected assertions here
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node(Token.SCRIPT);

        // TODO: Add test setup here

        checkSideEffects.visit(t, n, parent);

        // TODO: Add expected assertions here
    }

    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        // TODO: Add test setup here

        checkSideEffects.protectSideEffects();

        // TODO: Add expected assertions here
    }

    @Test
    public void testAddExtern() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

        // TODO: Add test setup here

        checkSideEffects.addExtern();

        // TODO: Add expected assertions here
    }

    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);

        // TODO: Add test setup here

        stripProtection.process(externs, root);

        // TODO: Add expected assertions here
    }
}