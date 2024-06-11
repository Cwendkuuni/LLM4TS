import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.GroupBuilder;
import org.apache.commons.cli2.DefaultOptionBuilder;
import org.apache.commons.cli2.ArgumentBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.validation.*;

public class PatternBuilderTest {
    
    @Test
    public void testCreate() {
        PatternBuilder pb = new PatternBuilder();
        
        Option option = pb.create();
        
        assertNull(option);
        assertEquals(0, pb.options.size());
    }

    @Test
    public void testReset() {
        PatternBuilder pb = new PatternBuilder();
        
        pb.options.add(new Option());
        pb.options.add(new Option());
        pb.options.add(new Option());
        
        pb.reset();
        
        assertEquals(0, pb.options.size());
    }
    
    @Test
    public void testWithPattern() {
        PatternBuilder pb = new PatternBuilder();
        
        pb.withPattern("!@:123");
        
        assertEquals(1, pb.options.size());
        
        Option option = (Option) pb.options.iterator().next();
        
        assertTrue(option.isRequired());
        
        Argument argument = option.getArgument();
        
        assertNotNull(argument);
        
        ClassValidator classValidator = (ClassValidator) argument.getValidator();
        
        assertNotNull(classValidator);
        assertTrue(classValidator.isInstance());
        
        assertEquals(2, argument.getMinimum());
        assertEquals(3, argument.getMaximum());
    }
    
    @Test
    public void testValidator() {
        PatternBuilder pb = new PatternBuilder();
        
        Validator validator = pb.validator('@');
        
        assertTrue(validator instanceof ClassValidator);
        assertTrue(((ClassValidator) validator).isInstance());
        
        validator = pb.validator(':');
        
        assertNull(validator);
        
        validator = pb.validator('%');
        
        assertTrue(validator instanceof NumberValidator);
        
        validator = pb.validator('#');
        
        assertTrue(validator instanceof DateValidator);
        
        validator = pb.validator('<');
        
        assertTrue(validator instanceof FileValidator);
        assertTrue(((FileValidator) validator).isExisting());
        assertTrue(((FileValidator) validator).isFile());
        
        validator = pb.validator('>');
        
        assertTrue(validator instanceof FileValidator);
        assertFalse(((FileValidator) validator).isExisting());
        
        validator = pb.validator('*');
        
        assertTrue(validator instanceof FileValidator);
        assertFalse(((FileValidator) validator).isExisting());
        
        validator = pb.validator('/');
        
        assertTrue(validator instanceof UrlValidator);
    }
}