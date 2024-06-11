import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        // Create WriteableCommandLine instance
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        // Create Option and add it to the WriteableCommandLine instance
        Option option = new Option("f", "file", true, "input file");
        cmd.addOption(option);

        // Verify that the option is added correctly
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("file"));
    }

    @Test
    public void testAddValue() {
        // Create WriteableCommandLine instance
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        // Create Option and add it to the WriteableCommandLine instance
        Option option = new Option("f", "file", true, "input file");
        cmd.addOption(option);

        // Add value to the option
        cmd.addValue(option, "test.txt");

        // Verify that the value is added correctly
        assertEquals("test.txt", cmd.getValue(option));
    }

    @Test
    public void testGetValues() {
        // Create WriteableCommandLine instance
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        // Create Option and add it to the WriteableCommandLine instance
        Option option = new Option("f", "file", true, "input file");
        cmd.addOption(option);

        // Add values to the option
        cmd.addValue(option, "test1.txt");
        cmd.addValue(option, "test2.txt");

        // Get the values of the option
        List<String> values = cmd.getValues(option);

        // Verify that the values are retrieved correctly
        assertEquals(2, values.size());
        assertTrue(values.contains("test1.txt"));
        assertTrue(values.contains("test2.txt"));
    }

    @Test
    public void testSetDefaultValues() {
        // Create WriteableCommandLine instance
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        // Create Option and add it to the WriteableCommandLine instance
        Option option = new Option("f", "file", true, "input file");
        cmd.addOption(option);

        // Set default values for the option
        List<String> defaultValues = Arrays.asList("default1.txt", "default2.txt");
        cmd.setDefaultValues(option, defaultValues);

        // Verify that the default values are set correctly
        List<String> values = cmd.getValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("default1.txt"));
        assertTrue(values.contains("default2.txt"));
    }

    @Test
    public void testAddSwitch() {
        // Create WriteableCommandLine instance
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        // Create Option and add it to the WriteableCommandLine instance
        Option option = new Option("v", "verbose", false, "enable verbose mode");
        cmd.addOption(option);

        // Add switch value to the option
        cmd.addSwitch(option, true);

        // Verify that the switch value is added correctly
        assertTrue(cmd.getSwitch(option));

        // Add switch value again (throws IllegalStateException)
        try {
            cmd.addSwitch(option, false);
        } catch (IllegalStateException e) {
            // Verify that an exception is thrown
            assertEquals("Switch already added", e.getMessage());
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        // Create WriteableCommandLine instance
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        // Create Option and add it to the WriteableCommandLine instance
        Option option = new Option("v", "verbose", false, "enable verbose mode");
        cmd.addOption(option);

        // Set default switch value for the option
        cmd.setDefaultSwitch(option, true);

        // Verify that the default switch value is set correctly
        assertTrue(cmd.getSwitch(option));

        // Set default switch value again
        cmd.setDefaultSwitch(option, false);

        // Verify that the default switch value is overridden
        assertFalse(cmd.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        // Create WriteableCommandLine instance
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        // Add property to the CommandLine
        cmd.addProperty("key1", "value1");

        // Verify that the property is added correctly
        assertEquals("value1", cmd.getProperty("key1"));
    }

    @Test
    public void testLooksLikeOption() {
        // Create WriteableCommandLine instance
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        // Test argument that looks like an Option trigger
        assertTrue(cmd.looksLikeOption("-f"));
        assertTrue(cmd.looksLikeOption("--file"));

        // Test argument that does not look like an Option trigger
        assertFalse(cmd.looksLikeOption("test.txt"));
    }
}