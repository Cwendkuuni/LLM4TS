import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    @Test
    public void testCheckSideEffects() {
        CheckSideEffects cse = new CheckSideEffects();
        assertEquals("This code lacks side-effects. Is there a bug?", cse.checkSideEffects());
    }

    @Test
    public void testSideEffectFreeString() {
        CheckSideEffects cse = new CheckSideEffects();
        assertEquals("Is there a missing '+' on the previous line?", cse.checkSideEffects());
    }
    
    @Test
    public void testUnusedOperatorResult() {
        CheckSideEffects cse = new CheckSideEffects();
        assertEquals("The result of the 'operator' operator is not being used.", cse.checkSideEffects());
    }
    
    @Test
    public void testProtectSideEffects() {
        CheckSideEffects cse = new CheckSideEffects();
        cse.protectSideEffects();
        assertTrue(cse.isProtected());
    }
    
    @Test
    public void testStripProtection() {
        CheckSideEffects cse = new CheckSideEffects();
        cse.stripProtection();
        assertFalse(cse.isProtected());
    }
}