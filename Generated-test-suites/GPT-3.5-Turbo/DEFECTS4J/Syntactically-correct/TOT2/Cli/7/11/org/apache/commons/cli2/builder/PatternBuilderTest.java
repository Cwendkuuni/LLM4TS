import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        // Test creating a new Option instance
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        // Test resetting the builder
        patternBuilder.reset();
        // Assert that the options set is cleared
        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        // Test building an Option using a pattern string
        patternBuilder.withPattern("-!");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testValidator() {
        // Test the validator method for different types
        Validator classValidator = PatternBuilder.validator('@');
        assertTrue(classValidator instanceof ClassValidator);

        Validator numberValidator = PatternBuilder.validator('%');
        assertTrue(numberValidator instanceof NumberValidator);

        Validator dateValidator = PatternBuilder.validator('#');
        assertTrue(dateValidator instanceof DateValidator);

        Validator fileValidator = PatternBuilder.validator('>');
        assertTrue(fileValidator instanceof FileValidator);

        Validator urlValidator = PatternBuilder.validator('/');
        assertTrue(urlValidator instanceof UrlValidator);
    }
}