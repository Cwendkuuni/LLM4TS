import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testWithPattern_singleOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_singleRequiredOption() {
        patternBuilder.withPattern("a!");
        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPattern_multipleOptions() {
        patternBuilder.withPattern("a:b:c");
        Option option = patternBuilder.create();

        assertEquals("a:b:c", option.getShortName());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_multipleRequiredOptions() {
        patternBuilder.withPattern("a!:b!:c!");
        Option option = patternBuilder.create();

        assertEquals("a:b:c", option.getShortName());
        assertTrue(option.isRequired());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testWithPattern_emptyPattern() {
        patternBuilder.withPattern("");
        Option option = patternBuilder.create();

        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_noOptions() {
        patternBuilder.withPattern("!@:%");
        Option option = patternBuilder.create();

        assertEquals("", option.getShortName());
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPattern_multipleOptionsWithDifferentTypes() {
        patternBuilder.withPattern("a@:b#:c/");
        Option option = patternBuilder.create();

        assertEquals("a:b:c", option.getShortName());
    }

    @Test
    public void testWithPattern_multipleOptionsWithSameType() {
        patternBuilder.withPattern("a+:b+");
        Option option = patternBuilder.create();

        assertEquals("a:b", option.getShortName());
    }

    @Test
    public void testWithPattern_invalidPatternCharacter() {
        patternBuilder.withPattern("a!");
        Option option = patternBuilder.create();

        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("a");
        patternBuilder.reset();
        Option option = patternBuilder.create();

        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
    }

    // 
    // JUnit test cases are defined above this line
    // 
}