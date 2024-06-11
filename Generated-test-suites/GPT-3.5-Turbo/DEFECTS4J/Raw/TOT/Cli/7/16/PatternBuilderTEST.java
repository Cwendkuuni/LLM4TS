import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        GroupBuilder gbuilder = new GroupBuilder();
        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        ArgumentBuilder abuilder = new ArgumentBuilder();
        PatternBuilder builder = new PatternBuilder(gbuilder, obuilder, abuilder);

        Option option = builder.create();

        assertNotNull(option);
    }

    @Test
    public void testReset() {
        GroupBuilder gbuilder = new GroupBuilder();
        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        ArgumentBuilder abuilder = new ArgumentBuilder();
        PatternBuilder builder = new PatternBuilder(gbuilder, obuilder, abuilder);

        builder.reset();

        // Since reset method doesn't return anything, we can only check that it doesn't throw any exception
    }

    @Test
    public void testWithPattern() {
        GroupBuilder gbuilder = new GroupBuilder();
        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        ArgumentBuilder abuilder = new ArgumentBuilder();
        PatternBuilder builder = new PatternBuilder(gbuilder, obuilder, abuilder);

        builder.withPattern("!@:*#<>" + System.lineSeparator() + "+/%");

        // Validate that the options are created correctly
        Option option = builder.create();
        assertNotNull(option);
        Group group = (Group) option;
        assertEquals(4, group.getOptions().size());

        // Validate the properties of the created options
        for (Option opt : group.getOptions()) {
            assertNotNull(opt.getShortName());
            assertNotNull(opt.getArgument());
            assertTrue(opt.isRequired());
        }
    }

    @Test
    public void testValidator() {
        Validator validator;

        // Test the validator for '@'
        validator = PatternBuilder.validator('@');
        assertTrue(validator instanceof ClassValidator);
        ClassValidator classValidator = (ClassValidator) validator;
        assertTrue(classValidator.isInstance());

        // Test the validator for '+'
        validator = PatternBuilder.validator('+');
        assertTrue(validator instanceof ClassValidator);
        classValidator = (ClassValidator) validator;
        assertFalse(classValidator.isInstance());

        // Test the validator for ':'
        validator = PatternBuilder.validator(':');
        assertNull(validator);

        // Test the validator for '%'
        validator = PatternBuilder.validator('%');
        assertNotNull(validator);
        assertTrue(validator instanceof NumberValidator);

        // Test the validator for '#'
        validator = PatternBuilder.validator('#');
        assertNotNull(validator);
        assertTrue(validator instanceof DateValidator);

        // Test the validator for '<'
        validator = PatternBuilder.validator('<');
        assertTrue(validator instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) validator;
        assertTrue(fileValidator.isExisting());
        assertTrue(fileValidator.isFile());

        // Test the validator for '>'
        validator = PatternBuilder.validator('>');
        assertNotNull(validator);
        assertTrue(validator instanceof FileValidator);
        fileValidator = (FileValidator) validator;
        assertFalse(fileValidator.isExisting());
        assertTrue(fileValidator.isFile());

        // Test the validator for '*'
        validator = PatternBuilder.validator('*');
        assertNotNull(validator);
        assertTrue(validator instanceof FileValidator);

        // Test the validator for '/'
        validator = PatternBuilder.validator('/');
        assertNotNull(validator);
        assertTrue(validator instanceof UrlValidator);
    }

}