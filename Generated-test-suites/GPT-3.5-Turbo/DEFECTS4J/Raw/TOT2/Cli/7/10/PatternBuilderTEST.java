import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        // Test creating a new Option instance
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        // Test resetting the builder
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();
        assertTrue(patternBuilder.options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        // Test building an Option using a pattern string
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-@!#:*>");
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testValidator() {
        // Test validator method for different types
        assertEquals(ClassValidator.class, PatternBuilder.validator('@').getClass());
        assertEquals(ClassValidator.class, PatternBuilder.validator('+').getClass());
        assertEquals(NumberValidator.class, PatternBuilder.validator('%').getClass());
        assertEquals(DateValidator.class, PatternBuilder.validator('#').getClass());
        assertEquals(FileValidator.class, PatternBuilder.validator('<').getClass());
        assertEquals(FileValidator.class, PatternBuilder.validator('>').getClass());
        assertEquals(UrlValidator.class, PatternBuilder.validator('/').getClass());
        assertNull(PatternBuilder.validator('a'));
    }

}