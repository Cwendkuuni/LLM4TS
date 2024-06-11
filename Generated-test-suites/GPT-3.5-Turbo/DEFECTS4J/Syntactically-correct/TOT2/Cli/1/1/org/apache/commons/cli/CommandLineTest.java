import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        // Test case for hasOption method when option is set
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("test"));
        assertTrue(cmd.hasOption("test"));

        // Test case for hasOption method when option is not set
        assertFalse(cmd.hasOption("invalid"));
    }

    @Test
    public void testGetOptionValue() {
        // Test case for getOptionValue method when option is set
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("test", "value"));
        assertEquals("value", cmd.getOptionValue("test"));

        // Test case for getOptionValue method when option is not set
        assertNull(cmd.getOptionValue("invalid"));
    }

    @Test
    public void testGetOptionValues() {
        // Test case for getOptionValues method when multiple values are set
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("test", new String[]{"value1", "value2"}));
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("test"));

        // Test case for getOptionValues method when option is not set
        assertNull(cmd.getOptionValues("invalid"));
    }

// Add more test cases for other methods as per the requirements

}