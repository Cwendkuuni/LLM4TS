import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cli2CommandLineParser().parse("--test");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        Option optionFromCommandLine = options.get(0);
        assertEquals(option, optionFromCommandLine);

        // Test edge cases and exception handling
        try {
            commandLine.addOption(option); // Test adding the same option multiple times
        } catch (IllegalStateException e) {
            // Expected
        }

        Option nullOption = null;
        try {
            commandLine.addOption(nullOption); // Test passing a null Option object
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cli2CommandLineParser().parse("--test value1 value2");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getOptionValues("test");
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        // Test edge cases and exception handling
        try {
            commandLine.addValue(option, null); // Test passing a null value
        } catch (NullPointerException e) {
            // Expected
        }

        Option optionWithValues = Option.builder("test").build();
        commandLine.addOption(optionWithValues);
        commandLine.addValue(optionWithValues, "value1");
        commandLine.addValue(optionWithValues, "value2");
        values = commandLine.getOptionValues("test");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new Cli2CommandLineParser().parse("--test value1 value2");
        Option option = Option.builder("test").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        // Test edge cases and exception handling
        Option optionWithDefaults = Option.builder("test").build();
        commandLine.setDefaultValues(optionWithDefaults, Arrays.asList("default1", "default2"));
        values = commandLine.getUndefaultedValues(optionWithDefaults);
        assertEquals(0, values.size());

        Option nonExistentOption = Option.builder("nonexistent").build();
        values = commandLine.getUndefaultedValues(nonExistentOption);
        assertEquals(0, values.size());
    }
}