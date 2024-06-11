import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.reset();
        Set options = patternBuilder.options;
        assertTrue(options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("!@:123");
        Option option = patternBuilder.options.iterator().next();
        assertEquals("!", option.getShortName());
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        Validator validator = argument.getValidator();
        assertTrue(validator instanceof ClassValidator);
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testValidator() {
        Validator validator = PatternBuilder.validator('@');
        assertTrue(validator instanceof ClassValidator);

        validator = PatternBuilder.validator(':');
        assertNull(validator);

        validator = PatternBuilder.validator('%');
        assertTrue(validator instanceof NumberValidator);

        validator = PatternBuilder.validator('#');
        assertTrue(validator instanceof DateValidator);

        validator = PatternBuilder.validator('<');
        assertTrue(validator instanceof FileValidator);
        assertTrue(((FileValidator)validator).isExisting());
        assertTrue(((FileValidator)validator).isFile());

        validator = PatternBuilder.validator('>');
        assertTrue(validator instanceof FileValidator);

        validator = PatternBuilder.validator('*');
        assertTrue(validator instanceof FileValidator);

        validator = PatternBuilder.validator('/');
        assertTrue(validator instanceof UrlValidator);

        validator = PatternBuilder.validator(' ');
        assertNull(validator);
    }

}