import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {
    
    @Test
    public void testCreate() {
        // Test create method
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertNotNull(option);
    }
    
    @Test
    public void testReset() {
        // Test reset method
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("!@:A@:B");
        builder.reset();
        Option option = builder.create();
        assertNull(option);
    }
    
    @Test
    public void testWithPattern() {
        // Test withPattern method
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("!@:A@:B");
        Option option = builder.create();
        assertNotNull(option);
        // Assert option properties
        assertEquals(2, option.getOptions().size());
    }
    
    @Test
    public void testValidator() {
        // Test validator method
        PatternBuilder builder = new PatternBuilder();
        // Test string validator
        Validator stringValidator = builder.validator(':');
        assertNull(stringValidator);
        // Test class validator
        Validator classValidator = builder.validator('@');
        assertTrue(classValidator instanceof ClassValidator);
        // Test number validator
        Validator numberValidator = builder.validator('%');
        assertTrue(numberValidator instanceof NumberValidator);
        // Test date validator
        Validator dateValidator = builder.validator('#');
        assertTrue(dateValidator instanceof DateValidator);
        // Test file validator
        Validator fileValidator = builder.validator('*');
        assertTrue(fileValidator instanceof FileValidator);
        // Test url validator
        Validator urlValidator = builder.validator('/');
        assertTrue(urlValidator instanceof UrlValidator);
    }
}