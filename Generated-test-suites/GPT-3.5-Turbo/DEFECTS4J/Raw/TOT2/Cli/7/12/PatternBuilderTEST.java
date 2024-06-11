import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder.create());
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!!");
        patternBuilder.reset();
        // Check that options are cleared after reset
        assertEquals(0, patternBuilder.create().getOptions().size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-@");
        assertNotNull(patternBuilder.create());
    }

    @Test
    public void testValidator() {
        assertNotNull(PatternBuilder.validator('@'));
        assertNotNull(PatternBuilder.validator('%'));
        assertNotNull(PatternBuilder.validator('#'));
        assertNotNull(PatternBuilder.validator('<'));
        assertNotNull(PatternBuilder.validator('>'));
        assertNotNull(PatternBuilder.validator('*'));
        assertNotNull(PatternBuilder.validator('/'));
        assertNull(PatternBuilder.validator(':'));
    }
    
    @Test
    public void testCompletePattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!#");
        assertNotNull(patternBuilder.create());
    }

}