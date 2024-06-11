import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!@#:*");

        // Typical use case
        Option option = patternBuilder.create();
        assertNotNull(option);

        // Edge cases
        // Test with a pattern that has no options
        patternBuilder.reset();
        Option optionNoOptions = patternBuilder.create();
        assertNull(optionNoOptions);

        // Error scenarios
        // Test with an invalid pattern
        patternBuilder.withPattern("!@#:*%"); // Invalid pattern with two type characters
        Option optionInvalidPattern = patternBuilder.create();
        assertNull(optionInvalidPattern);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();

        // Typical use case
        patternBuilder.withPattern("!@#:*");
        patternBuilder.reset();
        Set<Option> options = patternBuilder.reset();
        assertEquals(0, options.size());

        // Edge cases
        // Test reset when there are no options
        Set<Option> emptyOptions = patternBuilder.reset();
        assertEquals(0, emptyOptions.size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();

        // Typical use case
        patternBuilder.withPattern("!@#:*");
        Set<Option> options = patternBuilder.getOptions();
        assertEquals(1, options.size());

        // Edge cases
        // Test with empty pattern
        patternBuilder.withPattern("");
        Set<Option> emptyOptions = patternBuilder.getOptions();
        assertEquals(0, emptyOptions.size());

        // Error scenarios
        // Test with invalid pattern
        patternBuilder.withPattern("!@#:*%"); // Invalid pattern with two type characters
        Set<Option> invalidOptions = patternBuilder.getOptions();
        assertEquals(0, invalidOptions.size());
    }

    @Test
    public void testValidator() {
        // Typical use case
        Validator classValidator = PatternBuilder.validator('@');
        assertTrue(classValidator instanceof ClassValidator);

        // Edge cases
        // Test with an unsupported type
        Validator nullValidator = PatternBuilder.validator('!');
        assertNull(nullValidator);
    }

}