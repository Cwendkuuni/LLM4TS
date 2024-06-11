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
        patternBuilder.withPattern("!@a");
        patternBuilder.reset();
        assertTrue(patternBuilder.create() == null);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@a");
        assertNotNull(patternBuilder.create());
    }

    @Test
    public void testValidator() {
        assertNotNull(PatternBuilder.validator('@'));
        assertNotNull(PatternBuilder.validator(':'));
        assertNotNull(PatternBuilder.validator('%'));
        assertNotNull(PatternBuilder.validator('#'));
        assertNotNull(PatternBuilder.validator('<'));
        assertNotNull(PatternBuilder.validator('>'));
        assertNotNull(PatternBuilder.validator('*'));
        assertNotNull(PatternBuilder.validator('/'));
        assertNull(PatternBuilder.validator('x'));
    }
}