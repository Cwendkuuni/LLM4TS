import org.junit.Test;

import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();

        assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.reset();

        // Ensure that options is empty after reset
        Set options = patternBuilder.options;
        assertTrue(options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();

        // Test pattern with required option and argument type
        String pattern = "!@:filename";
        patternBuilder.withPattern(pattern);
        Option option = patternBuilder.create();
        assertTrue(option.isRequired());
        assertNotNull(option.getArgument());
        assertEquals("filename", option.getArgument().getValidator().toString());

        // Test pattern with optional option and argument type
        patternBuilder.reset();
        pattern = "@:classname";
        patternBuilder.withPattern(pattern);
        option = patternBuilder.create();
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertEquals("classname", option.getArgument().getValidator().toString());

        // Test pattern with multiple options
        patternBuilder.reset();
        pattern = "a@:arg1b#:arg2";
        patternBuilder.withPattern(pattern);
        option = patternBuilder.create();
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertEquals("arg1", option.getArgument().getValidator().toString());
        assertNotNull(option.getId());
        assertNotNull(option.getLongName());
    }
}