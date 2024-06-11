import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate_SingleOption() {
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNull(argument);
    }

    @Test
    public void testCreate_SingleOptionRequired() {
        patternBuilder.withPattern("!-a");
        Option option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNull(argument);
    }

    @Test
    public void testCreate_SingleOptionWithArgumentClassValidator() {
        patternBuilder.withPattern("@-a");
        Option option = patternBuilder.create();
        assertEquals("-a", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        ClassValidator validator = (ClassValidator) argument.getValidator();
        assertTrue(validator.isInstance());
    }

    @Test
    public void testCreate_SingleOptionWithArgumentClassValidatorNotInstance() {
        patternBuilder.withPattern("+a");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        ClassValidator validator = (ClassValidator) argument.getValidator();
        assertFalse(validator.isInstance());
    }

    @Test
    public void testCreate_SingleOptionWithArgumentNumberValidator() {
        patternBuilder.withPattern("%a");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        NumberValidator validator = (NumberValidator) argument.getValidator();
        assertEquals(NumberValidator.getNumberInstance(), validator);
    }

    @Test
    public void testCreate_SingleOptionWithArgumentDateValidator() {
        patternBuilder.withPattern("#a");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        DateValidator validator = (DateValidator) argument.getValidator();
        assertEquals(DateValidator.getDateInstance(), validator);
    }

    @Test
    public void testCreate_SingleOptionWithArgumentFileValidatorExisting() {
        patternBuilder.withPattern("<a");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        FileValidator validator = (FileValidator) argument.getValidator();
        assertTrue(validator.isExisting());
        assertTrue(validator.isFile());
    }

    @Test
    public void testCreate_SingleOptionWithArgumentFileValidatorAny() {
        patternBuilder.withPattern("*a");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        FileValidator validator = (FileValidator) argument.getValidator();
        assertFalse(validator.isExisting());
        assertFalse(validator.isFile());
    }

    @Test
    public void testCreate_SingleOptionWithArgumentUrlValidator() {
        patternBuilder.withPattern("/a");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        UrlValidator validator = (UrlValidator) argument.getValidator();
        assertEquals(new UrlValidator(), validator);
    }

    @Test
    public void testCreate_MultipleOptions() {
        patternBuilder.withPattern("-a-b-c");
        Option option = patternBuilder.create();
        assertEquals("-c", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNull(argument);
    }

    @Test
    public void testCreate_MultipleOptionsWithDifferentArguments() {
        patternBuilder.withPattern("-a:b-c%:d+e");
        Option option = patternBuilder.create();
        assertEquals("-e", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        ClassValidator classValidator = (ClassValidator) argument.getValidator();
        assertFalse(classValidator.isInstance());
    }

    @Test
    public void testCreate_MultipleOptionsWithSameArguments() {
        patternBuilder.withPattern("-a:b-c:d+e");
        Option option = patternBuilder.create();
        assertEquals("-e", option.getShortName());
        assertFalse(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        ClassValidator classValidator = (ClassValidator) argument.getValidator();
        assertTrue(classValidator.isInstance());
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }
}