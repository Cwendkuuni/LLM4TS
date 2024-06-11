import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckSideEffectsTest {
    @Test
    public void testProcess() {
        CheckSideEffects check = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        check.process(externs, scriptRoot);
        // verify the result of the process
        // ...
    }
    
    @Test
    public void testHotSwapScript() {
        CheckSideEffects check = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        check.hotSwapScript(scriptRoot, originalRoot);
        // verify the result of the hotSwapScript
        // ...
    }
    
    @Test
    public void testVisit() {
        CheckSideEffects check = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        NodeTraversal traversal = new NodeTraversal(compiler, check);
        NodeTraversal.traverse(traversal, externs);
        // verify the result of the visit
        // ...
    }
    
    @Test
    public void testProtectSideEffects() {
        CheckSideEffects check = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        check.protectSideEffects();
        // verify the result of the protectSideEffects
        // ...
    }
    
    @Test
    public void testAddExtern() {
        CheckSideEffects check = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        check.addExtern();
        // verify the result of the addExtern
        // ...
    }
    
    @Test
    public void testStripProtection() {
        CheckSideEffects.StripProtection strip = new CheckSideEffects.StripProtection(compiler);
        strip.process(externs, root);
        // verify the result of the stripProtection
        // ...
    }
}