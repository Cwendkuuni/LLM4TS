import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testReset() {
        patternBuilder.options.add(new Option("test"));
        patternBuilder.reset();
        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("-a:b");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-a:b", option.toString());
    }

    @Test
    public void testValidator() {
        Validator validator = patternBuilder.validator('@');
        assertTrue(validator instanceof ClassValidator);

        validator = patternBuilder.validator('+');
        assertTrue(validator instanceof ClassValidator);

        validator = patternBuilder.validator('%');
        assertTrue(validator instanceof NumberValidator);

        validator = patternBuilder.validator('#');
        assertTrue(validator instanceof DateValidator);

        validator = patternBuilder.validator('<');
        assertTrue(validator instanceof FileValidator);

        validator = patternBuilder.validator('>');
        assertTrue(validator instanceof FileValidator);

        validator = patternBuilder.validator('*');
        assertTrue(validator instanceof FileValidator);

        validator = patternBuilder.validator('/');
        assertTrue(validator instanceof UrlValidator);

        validator = patternBuilder.validator(':');
        assertNull(validator);
    }
}