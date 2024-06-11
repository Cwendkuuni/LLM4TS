import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {
    
    @Test
    public void testProcess() {
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        Node externs = new Node();
        Node root = new Node();
        checkSideEffects.process(externs, root);
    }
    
    @Test
    public void testHotSwapScript() {
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        Node scriptRoot = new Node();
        Node originalRoot = new Node();
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    }
    
    @Test
    public void testVisit() {
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        checkSideEffects.visit(t, n, parent);
    }
    
    @Test
    public void testProtectSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        checkSideEffects.protectSideEffects();
    }
    
    @Test
    public void testAddExtern() {
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        checkSideEffects.addExtern();
    }
    
    @Test
    public void testStripProtection() {
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection();
        Node externs = new Node();
        Node root = new Node();
        stripProtection.process(externs, root);
    }
}