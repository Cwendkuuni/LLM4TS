import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("a", "Description");
        assertEquals("a", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertFalse(option.hasLongOpt());
    }

    @Test
    public void testOptionConstructorWithHasArg() {
        Option option = new Option("a", true, "Description");
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testOptionConstructorWithLongOpt() {
        Option option = new Option("a", "long", true, "Description");
        assertEquals("long", option.getLongOpt());
    }

    // Add more individual test cases for other public methods

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("a", "Description");
        assertThrows(RuntimeException.class, () -> {
            option.addValueForProcessing("value");
        });
    }

    // Add test cases for edge cases and exception scenarios

}