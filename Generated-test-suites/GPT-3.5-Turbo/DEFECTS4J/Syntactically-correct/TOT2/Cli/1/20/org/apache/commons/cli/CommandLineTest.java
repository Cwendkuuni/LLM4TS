import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        // Test if hasOption returns true for a set option
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "test option"));
        assertTrue(cmd.hasOption("a"));

        // Test if hasOption returns false for an unset option
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testGetOptionObject() {
        // Test if getOptionObject returns the correct type of the option
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "test option").setType(Integer.class));
        cmd.addArg("5");
        assertEquals(5, cmd.getOptionObject("a"));

        // Test if getOptionObject returns null for an unset option
        assertNull(cmd.getOptionObject("b"));
    }

    @Test
    public void testGetOptionValue() {
        // Test if getOptionValue returns the value of the option
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "test option"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("a"));

        // Test if getOptionValue returns null for an unset option
        assertNull(cmd.getOptionValue("b"));
    }

    // Add more test cases for other public methods in CommandLine class

}