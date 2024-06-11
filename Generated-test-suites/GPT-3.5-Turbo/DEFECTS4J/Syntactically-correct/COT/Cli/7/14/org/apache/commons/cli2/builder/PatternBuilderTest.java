import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

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
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("s");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testWithPatternRequired() {
        patternBuilder.withPattern("!s");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithPatternMultipleArguments() {
        patternBuilder.withPattern("s@");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertEquals(true, argument.isMultiple());
        assertEquals(true, argument.isRequired());
    }

    @Test
    public void testWithPatternNoArgument() {
        patternBuilder.withPattern("s ");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNull(argument);
    }

    @Test
    public void testWithPatternClassArgument() {
        patternBuilder.withPattern("s@");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getValidator() instanceof ClassValidator);
        ClassValidator classValidator = (ClassValidator) argument.getValidator();
        assertTrue(classValidator.isInstance());
        assertFalse(classValidator.isArray());
    }

    @Test
    public void testWithPatternArrayArgument() {
        patternBuilder.withPattern("s*");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getValidator() instanceof ClassValidator);
        ClassValidator classValidator = (ClassValidator) argument.getValidator();
        assertFalse(classValidator.isInstance());
        assertTrue(classValidator.isArray());
    }

    @Test
    public void testWithPatternStringArgument() {
        patternBuilder.withPattern("s:");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertNull(argument.getValidator());
    }

    @Test
    public void testWithPatternNumberArgument() {
        patternBuilder.withPattern("s%");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getValidator() instanceof NumberValidator);
    }

    @Test
    public void testWithPatternDateArgument() {
        patternBuilder.withPattern("s#");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getValidator() instanceof DateValidator);
    }

    @Test
    public void testWithPatternExistingFileArgument() {
        patternBuilder.withPattern("s<");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getValidator() instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) argument.getValidator();
        assertTrue(fileValidator.isExisting());
        assertTrue(fileValidator.isFile());
    }

    @Test
    public void testWithPatternFileArgument() {
        patternBuilder.withPattern("s>");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getValidator() instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) argument.getValidator();
        assertFalse(fileValidator.isExisting());
        assertNull(fileValidator.isFile());
    }

    @Test
    public void testWithPatternUrlArgument() {
        patternBuilder.withPattern("s/");
        Option option = patternBuilder.create();
        assertEquals("s", option.getShortName());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getValidator() instanceof UrlValidator);
    }
}