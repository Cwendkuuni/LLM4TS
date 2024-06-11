import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        checkSideEffects.process(null, null);
        // write your test cases here
    }

    @Test
    public void testHotSwapScript() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        checkSideEffects.hotSwapScript(null, null);
        // write your test cases here
    }

    @Test
    public void testVisit() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        checkSideEffects.visit(null, null, null);
        // write your test cases here
    }

    @Test
    public void testProtectSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        checkSideEffects.protectSideEffects();
        // write your test cases here
    }

    @Test
    public void testAddExtern() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        checkSideEffects.addExtern();
        // write your test cases here
    }

    @Test
    public void testStripProtection() {
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);
        stripProtection.process(null, null);
        // write your test cases here
    }
}