import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        // Test case 1: Create an Option with one argument
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!a");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals(1, option.getArguments().size());
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());

        // Test case 2: Create an Option with multiple arguments
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!a:b:c");
        option = patternBuilder.create();

        assertNotNull(option);
        assertEquals(3, option.getArguments().size());
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());

        // Test case 3: Create an Option with different types of arguments
        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!a:b<existing>:c<>/d#");
        option = patternBuilder.create();

        assertNotNull(option);
        assertEquals(4, option.getArguments().size());
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!a");
        Option option = patternBuilder.create();

        assertNotNull(option);

        patternBuilder.reset();
        option = patternBuilder.create();

        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        // Test case 1: Add multiple options with different types of arguments
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.withPattern("!a:b:c");
        Option option = patternBuilder.create();

        assertNotNull(option);
        assertEquals(3, option.getArguments().size());
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());

        patternBuilder.withPattern("d<existing>:");
        option = patternBuilder.create();

        assertNotNull(option);
        assertEquals(1, option.getArguments().size());
        assertEquals("d", option.getShortName());
        assertFalse(option.isRequired());
    }

}