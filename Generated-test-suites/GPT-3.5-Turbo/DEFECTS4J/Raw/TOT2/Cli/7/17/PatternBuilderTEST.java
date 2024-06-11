import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        // Test creating an Option with single option
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
    }

    @Test
    public void testReset() {
        // Test resetting the builder
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        Option option1 = patternBuilder.create();
        
        patternBuilder.reset();
        
        Option option2 = patternBuilder.create();
        
        assertNull(option2);
    }

    @Test
    public void testWithPattern() {
        // Test building an Option with various patterns
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a -b -c");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(3, option.getOptions().size());
    }

    @Test
    public void testValidator() {
        // Test different types of validators
        ClassValidator classValidator = (ClassValidator)PatternBuilder.validator('@');
        assertTrue(classValidator.isInstance());

        NumberValidator numberValidator = (NumberValidator)PatternBuilder.validator('%');
        assertNotNull(numberValidator.getNumberInstance());

        DateValidator dateValidator = (DateValidator)PatternBuilder.validator('#');
        assertNotNull(dateValidator.getDateInstance());

        FileValidator fileValidator = (FileValidator)PatternBuilder.validator('>');
        assertNotNull(fileValidator);

        UrlValidator urlValidator = (UrlValidator)PatternBuilder.validator('/');
        assertNotNull(urlValidator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        // Test invalid pattern
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a -b -c");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());
    }
}