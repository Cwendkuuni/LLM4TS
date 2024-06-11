import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate_SingleOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a");

        Option option = builder.create();

        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
    }

    @Test
    public void testCreate_SingleOption_WithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");

        Option option = builder.create();

        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof StringValidator);
    }

    @Test
    public void testCreate_SingleOption_WithTwoArguments() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a::");

        Option option = builder.create();

        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(option.getArgument());
        assertEquals(2, option.getArgument().getMinimum());
        assertEquals(2, option.getArgument().getMaximum());
        assertTrue(option.getArgument().getValidator() instanceof StringValidator);
    }

    // Add more basic test cases for other public methods

    @Test
    public void testWithPattern_NoOption() {
        PatternBuilder builder = new PatternBuilder();

        builder.withPattern("");

        Option option = builder.create();

        assertNull(option);
    }

    @Test
    public void testWithPattern_MultipleOptions() {
        PatternBuilder builder = new PatternBuilder();

        builder.withPattern("a!b");
        Option option1 = builder.create();

        builder.reset();

        builder.withPattern("c!d");
        Option option2 = builder.create();

        assertNotEquals(option1, option2);
    }

    // Add test cases for potential edge cases and exceptions

}

// 