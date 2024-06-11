import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.Option;

public class PatternBuilderTest {
    private PatternBuilder pb;

    @Before
    public void setUp() {
        pb = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = pb.create();
        // Verify that the option is not null
        assertNotNull(option);
        // Verify that the option has no arguments
        assertNull(option.getArgument());
        // Verify that the option is not required
        assertFalse(option.isRequired());
    }

    @Test
    public void testReset() {
        pb.reset();
        // Verify that options set is empty after reset
        assertEquals(0, pb.options.size());
    }

    @Test
    public void testWithPattern() {
        pb.withPattern("!a");
        Option option = pb.create();
        // Verify that the option has a short name of 'a'
        assertEquals("a", option.getShortName());
        // Verify that the option is required
        assertTrue(option.isRequired());

        pb.reset();

        pb.withPattern("@b:");
        option = pb.create();
        // Verify that the option has a short name of 'b'
        assertEquals("b", option.getShortName());
        // Verify that the option has an argument
        assertNotNull(option.getArgument());
        // Verify that the option's argument has a validator of type ClassValidator
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        // Verify that the option is not required
        assertFalse(option.isRequired());

        pb.reset();

        pb.withPattern("%c*");
        option = pb.create();
        // Verify that the option has a short name of 'c'
        assertEquals("c", option.getShortName());
        // Verify that the option has an argument
        assertNotNull(option.getArgument());
        // Verify that the option's argument has a validator of type NumberValidator
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
        // Verify that the option can have multiple arguments
        assertTrue(option.getArgument().getMaximum() > 1);

        pb.reset();

        pb.withPattern("<d%,#>");
        option = pb.create();
        // Verify that the option has a short name of 'd'
        assertEquals("d", option.getShortName());
        // Verify that the option has an argument
        assertNotNull(option.getArgument());
        // Verify that the option's argument has a validator of type NumberValidator
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
        // Verify that the option's argument has a maximum of 1
        assertEquals(1, option.getArgument().getMaximum());
        // Verify that the option's argument has a minimum of 1
        assertEquals(1, option.getArgument().getMinimum());

        pb.reset();

        pb.withPattern("/e");
        option = pb.create();
        // Verify that the option has a short name of 'e'
        assertEquals("e", option.getShortName());
        // Verify that the option has an argument
        assertNotNull(option.getArgument());
        // Verify that the option's argument has a validator of type UrlValidator
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }

    @Test
    public void testValidator() {
        // Verify that the validator method returns a ClassValidator when the input is '@'
        assertTrue(pb.validator('@') instanceof ClassValidator);
        // Verify that the validator method returns a ClassValidator when the input is '+'
        assertTrue(pb.validator('+') instanceof ClassValidator);
        // Verify that the validator method returns a DateValidator when the input is '#'
        assertTrue(pb.validator('#') instanceof DateValidator);
        // Verify that the validator method returns a FileValidator when the input is '<'
        assertTrue(pb.validator('<') instanceof FileValidator);
        // Verify that the validator method returns a FileValidator when the input is '>'
        assertTrue(pb.validator('>') instanceof FileValidator);
        // Verify that the validator method returns a FileValidator when the input is '*'
        assertTrue(pb.validator('*') instanceof FileValidator);
        // Verify that the validator method returns a UrlValidator when the input is '/'
        assertTrue(pb.validator('/') instanceof UrlValidator);
        // Verify that the validator method returns null when the input is any other character
        assertNull(pb.validator(':'));
    }

}