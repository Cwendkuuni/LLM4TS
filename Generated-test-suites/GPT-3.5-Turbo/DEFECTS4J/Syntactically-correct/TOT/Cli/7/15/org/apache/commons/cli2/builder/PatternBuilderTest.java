import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder builder = new PatternBuilder();
        
        builder.withPattern("");
        Option option = builder.create();
        assertNull(option);
        
        builder.reset();
        builder.withPattern("a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        
        builder.reset();
        builder.withPattern("!");
        option = builder.create();
        assertNull(option.getArgument());
        assertTrue(option.isRequired());
        
        builder.reset();
        builder.withPattern("a!");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertNull(option.getArgument());
        assertTrue(option.isRequired());
        
        builder.reset();
        // test edge case with multiple options
        builder.withPattern("a!ab!");
        option = builder.create();
        Group group = (Group) option;
        assertEquals(2, group.getOptions().size());
        assertTrue(group.isRequired());
        
    }

    @Test
    public void testReset() {
        PatternBuilder builder = new PatternBuilder();
        
        builder.withPattern("a");
        builder.reset();
        
        Option option = builder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder builder = new PatternBuilder();
        
        builder.withPattern("");
        Option option = builder.create();
        assertNull(option);
        
        builder.reset();
        builder.withPattern("a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        
        builder.reset();
        builder.withPattern("!");
        option = builder.create();
        assertNull(option.getArgument());
        assertTrue(option.isRequired());
        
        builder.reset();
        builder.withPattern("a!");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertNull(option.getArgument());
        assertTrue(option.isRequired());
    }

    @Test
    public void testValidator() {
        char c = '@';
        Validator validator = validator(c);
        assertTrue(validator instanceof ClassValidator);
        
        c = '+';
        validator = validator(c);
        assertTrue(validator instanceof ClassValidator);
        
        c = '%';
        validator = validator(c);
        assertTrue(validator instanceof NumberValidator);
        
        c = '#';
        validator = validator(c);
        assertTrue(validator instanceof DateValidator);
        
        c = '<';
        validator = validator(c);
        FileValidator fileValidator = (FileValidator) validator;
        assertTrue(fileValidator.isExisting());
        assertTrue(fileValidator.isFile());
        
        c = '>';
        validator = validator(c);
        assertTrue(validator instanceof FileValidator);
        
        c = '/';
        validator = validator(c);
        assertTrue(validator instanceof UrlValidator);
        
        c = ':';
        validator = validator(c);
        assertNull(validator);
    }

}