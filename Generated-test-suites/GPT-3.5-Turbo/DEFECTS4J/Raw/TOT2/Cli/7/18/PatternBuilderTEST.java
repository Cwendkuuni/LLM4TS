import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateOption() {
        // Test creating an option with non-empty argument
        patternBuilder.createOption('c', true, 'o');
        // Assert that the options size is 1
        assertEquals(1, options.size());
        
        // Test creating an option with empty argument
        patternBuilder.createOption(' ', false, 't');
        // Assert that the options size is 2
        assertEquals(2, options.size());
    }

    @Test
    public void testWithPattern() {
        // Test building options using a pattern string
        patternBuilder.withPattern("!@:o");
        // Assert that the options size is 1
        assertEquals(1, options.size());
        
        patternBuilder.withPattern(":t%");
        // Assert that the options size is 2
        assertEquals(2, options.size());
    }

    @Test
    public void testValidator() {
        // Test getting validator for different types
        assertEquals(ClassValidator.class, patternBuilder.validator('@').getClass());
        assertEquals(NumberValidator.class, patternBuilder.validator('%').getClass());
        assertEquals(DateValidator.class, patternBuilder.validator('#').getClass());
        assertEquals(FileValidator.class, patternBuilder.validator('>').getClass());
        assertEquals(UrlValidator.class, patternBuilder.validator('/').getClass());
        assertNull(patternBuilder.validator(' '));
    }

    // Add more test cases for other public methods if needed

}