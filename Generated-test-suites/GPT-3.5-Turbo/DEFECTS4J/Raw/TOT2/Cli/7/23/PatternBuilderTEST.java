import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        // Assert that the options are cleared after reset
        assertTrue(patternBuilder.options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-t!@:p");
        // Assert that the option is created with the correct pattern
        assertEquals(1, patternBuilder.options.size());
    }

    @Test
    public void testValidator() {
        Validator validator = PatternBuilder.validator('@');
        assertTrue(validator instanceof ClassValidator);
    }

}