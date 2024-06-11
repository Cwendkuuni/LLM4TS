import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate_SingleOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("a", option.getShortName());
    }

    @Test
    public void testCreate_MultipleOptions() {
        patternBuilder.withPattern("a b c");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals("Group", option.getClass().getSimpleName());
    }

    @Test
    public void testCreate_EmptyPattern() {
        Option option = patternBuilder.create();

        assertNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("a");
        patternBuilder.reset();
        assertNull(patternBuilder.create());
    }

    @Test
    public void testWithPattern_WithRequiredFlag() {
        patternBuilder.withPattern("a!");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithPattern_WithArgumentType() {
        patternBuilder.withPattern("a@");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertEquals("ClassValidator", option.getArgument().getValidator().getClass().getSimpleName());
        assertEquals(true, option.getArgument().getValidator().isInstance());
    }

    @Test
    public void testWithPattern_WithArgumentTypeAndMinMax() {
        patternBuilder.withPattern("a*");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertEquals("FileValidator", option.getArgument().getValidator().getClass().getSimpleName());
        assertEquals(0, option.getArgument().getMinimum());
        assertEquals(Integer.MAX_VALUE, option.getArgument().getMaximum());
    }

    @Test
    public void testWithPattern_WithUnsupportedArgumentType() {
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertEquals(null, option.getArgument().getValidator());
    }

    @Test
    public void testWithPattern_WithArgumentTypeAndMin() {
        patternBuilder.withPattern("a+");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertEquals("ClassValidator", option.getArgument().getValidator().getClass().getSimpleName());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
    }

    @Test
    public void testWithPattern_WithArgumentTypeAndMax() {
        patternBuilder.withPattern("a#");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertEquals("DateValidator", option.getArgument().getValidator().getClass().getSimpleName());
        assertEquals(0, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
    }

    @Test
    public void testWithPattern_WithArgumentTypeAndExistingFile() {
        patternBuilder.withPattern("a<");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertEquals("FileValidator", option.getArgument().getValidator().getClass().getSimpleName());
        assertEquals(true, option.getArgument().getValidator().isExisting());
        assertEquals(true, option.getArgument().getValidator().isFile());
    }

    @Test
    public void testWithPattern_WithArgumentTypeAndFile() {
        patternBuilder.withPattern("a>");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertEquals("FileValidator", option.getArgument().getValidator().getClass().getSimpleName());
        assertEquals(false, option.getArgument().getValidator().isExisting());
        assertEquals(true, option.getArgument().getValidator().isFile());
    }

    @Test
    public void testWithPattern_WithArgumentTypeAndUrl() {
        patternBuilder.withPattern("a/");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertNotNull(option.getArgument());
        assertEquals("UrlValidator", option.getArgument().getValidator().getClass().getSimpleName());
    }

    @Test
    public void testValidator_TypeAt() {
        Validator validator = patternBuilder.validator('@');

        assertEquals("ClassValidator", validator.getClass().getSimpleName());
        assertEquals(true, ((ClassValidator) validator).isInstance());
    }

    @Test
    public void testValidator_TypePlus() {
        Validator validator = patternBuilder.validator('+');

        assertEquals("ClassValidator", validator.getClass().getSimpleName());
        assertEquals(false, ((ClassValidator) validator).isInstance());
    }

    @Test
    public void testValidator_TypeDefault() {
        Validator validator = patternBuilder.validator(':');

        assertEquals(null, validator);
    }

    @Test
    public void testValidator_TypePercent() {
        Validator validator = patternBuilder.validator('%');

        assertNotNull(validator);
        assertEquals("NumberValidator", validator.getClass().getSimpleName());
    }

    @Test
    public void testValidator_TypeHash() {
        Validator validator = patternBuilder.validator('#');

        assertNotNull(validator);
        assertEquals("DateValidator", validator.getClass().getSimpleName());
    }

    @Test
    public void testValidator_TypeLessThan() {
        Validator validator = patternBuilder.validator('<');

        assertNotNull(validator);
        assertEquals("FileValidator", validator.getClass().getSimpleName());
        assertEquals(true, ((FileValidator) validator).isExisting());
        assertEquals(true, ((FileValidator) validator).isFile());
    }

    @Test
    public void testValidator_TypeGreaterThan() {
        Validator validator = patternBuilder.validator('>');

        assertNotNull(validator);
        assertEquals("FileValidator", validator.getClass().getSimpleName());
        assertEquals(false, ((FileValidator) validator).isExisting());
        assertEquals(true, ((FileValidator) validator).isFile());
    }

    @Test
    public void testValidator_TypeAsterisk() {
        Validator validator = patternBuilder.validator('*');

        assertNotNull(validator);
        assertEquals("FileValidator", validator.getClass().getSimpleName());
    }

    @Test
    public void testValidator_TypeSlash() {
        Validator validator = patternBuilder.validator('/');

        assertNotNull(validator);
        assertEquals("UrlValidator", validator.getClass().getSimpleName());
    }
}