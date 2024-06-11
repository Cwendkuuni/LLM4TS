import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        Compiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
        checkSideEffects.process(externs, root);
        // Assert statements
    }

    @Test
    public void testHotSwapScript() {
        Compiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        Node scriptRoot = new Node(Token.SCRIPT, new Node(Token.BLOCK));
        Node originalRoot = new Node(Token.SCRIPT, new Node(Token.BLOCK));
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        // Assert statements
    }

    @Test
    public void testVisit() {
        Compiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.BLOCK);
        Node parent = new Node(Token.ROOT);
        traversal.visit(null, node, parent);
        // Assert statements
    }

    @Test
    public void testProtectSideEffects() {
        Compiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checkSideEffects.protectSideEffects();
        // Assert statements
    }

    @Test
    public void testAddExtern() {
        Compiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checkSideEffects.addExtern();
        // Assert statements
    }

    @Test
    public void testStripProtectionProcess() {
        Compiler compiler = new Compiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.ROOT, new Node(Token.BLOCK));
        stripProtection.process(externs, root);
        // Assert statements
    }

    @Test
    public void testStripProtectionVisit() {
        Compiler compiler = new Compiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        NodeTraversal traversal = new NodeTraversal(compiler, stripProtection);
        Node node = new Node(Token.BLOCK);
        Node parent = new Node(Token.ROOT);
        traversal.visit(null, node, parent);
        // Assert statements
    }

}