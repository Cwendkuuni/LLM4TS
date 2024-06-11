import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        // Typical use case
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);

        // Edge case: multiple options
        patternBuilder.withPattern("-a -b -c");
        option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        // Typical use case
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        assertEquals(0, patternBuilder.reset().size());

        // Edge case: reset multiple times
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        assertEquals(0, patternBuilder.reset().size());
        patternBuilder.reset();
        assertEquals(0, patternBuilder.reset().size());
    }

    @Test
    public void testWithPattern() {
        // Typical use case
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        assertNotNull(patternBuilder);

        // Edge case: empty pattern
        patternBuilder.withPattern("");
        assertNotNull(patternBuilder);

        // Edge case: invalid pattern
        patternBuilder.withPattern("@: %");
        assertNotNull(patternBuilder);
    }

    @Test
    public void testValidator() {
        // Typical use case
        Validator validator = PatternBuilder.validator('@');
        assertNotNull(validator);

        // Edge case: invalid validator type
        validator = PatternBuilder.validator('!');
        assertNull(validator);
    }
}