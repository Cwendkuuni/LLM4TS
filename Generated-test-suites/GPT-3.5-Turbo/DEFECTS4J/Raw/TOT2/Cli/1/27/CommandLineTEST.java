import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        // Test case for hasOption method
        // Typical use case
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "option"));
        assertTrue(cmd.hasOption("o"));

        // Edge case
        assertFalse(cmd.hasOption("x"));
        
        // Error scenario
        assertFalse(cmd.hasOption(null));
    }

    @Test
    public void testGetOptionValue() {
        // Test case for getOptionValue method
        // Typical use case
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("o", "option"));
        cmd.addOption(new Option("r", "required", true, "This is a required option"));
        assertEquals("option", cmd.getOptionValue("o"));
        
        // Edge case
        assertNull(cmd.getOptionValue("x"));
        
        // Error scenario
        assertNull(cmd.getOptionValue(null));
    }

    @Test
    public void testGetOptionValues() {
        // Test case for getOptionValues method
        // Typical use case
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("m", "multiple", true, "This option can have multiple values"));
        cmd.addOption(new Option("s", "single", true, "This option has a single value"));
        cmd.addOption(new Option("n", "none"));
        String[] multipleValues = {"value1", "value2", "value3"};
        assertEquals(multipleValues, cmd.getOptionValues("m"));

        // Edge case
        assertNull(cmd.getOptionValues("x"));

        // Error scenario
        assertNull(cmd.getOptionValues(null));
    }

    // Add more test methods for other public methods of the CommandLine class

}