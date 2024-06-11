import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;

import static org.junit.Assert.*;

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

        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
    }

    @Test
    public void testCreate_SingleRequiredOption() {
        patternBuilder.withPattern("!a");

        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
        assertNull(option.getArgument());
    }

    @Test
    public void testCreate_SingleOptionWithArgument() {
        patternBuilder.withPattern("a:");

        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertFalse(option.getArgument().isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
    }

    @Test
    public void testCreate_SingleRequiredOptionWithArgument() {
        patternBuilder.withPattern("!a:");

        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNull(option.getArgument().getValidator());
    }

    @Test
    public void testCreate_MultipleOptions() {
        patternBuilder.withPattern("a:x");

        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertFalse(option.getArgument().isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertEquals("x", ((ClassValidator)option.getArgument().getValidator()).getClassname());
    }

    // Additional test cases for edge/typical/error scenarios can be added here

}