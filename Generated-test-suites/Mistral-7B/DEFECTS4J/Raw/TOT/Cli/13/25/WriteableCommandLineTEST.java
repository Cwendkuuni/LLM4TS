import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        CommandLine commandLine = new DefaultCommandLine();
        Option option = new Option("o", "output", true, "Output file path");
        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("o", commandLine.getOptions().get(0).getLongOpt());
        Assert.assertEquals("Output file path", commandLine.getOptions().get(0).getLongOptDescription());
        Assert.assertEquals(true, commandLine.getOptions().get(0).isRequired());

        // Test setting value programmatically
        commandLine.setOptionValue("output", "output.txt");
        Assert.assertEquals("output.txt", commandLine.getOptionValue("output"));
    }

    @Test
    public void testAddValue() {
        CommandLine commandLine = new DefaultCommandLine();
        Option option = new Option("i", "input", 1, "Input file path");
        commandLine.addOption(option);

        commandLine.addValue("input", "input1.txt");
        commandLine.addValue("input", "input2.txt");

        Assert.assertEquals(2, commandLine.getValuesForOption("input").size());
        Assert.assertEquals("input1.txt", commandLine.getValuesForOption("input").get(0));
        Assert.assertEquals("input2.txt", commandLine.getValuesForOption("input").get(1));
    }

    @Test
    public void testGetValues() {
        CommandLine commandLine = new DefaultCommandLine();
        Option option = new Option("i", "input");
        commandLine.addOption(option);

        commandLine.addValue("input", "input1.txt");
        commandLine.addValue("input", "input2.txt");

        List<String> values = commandLine.getValuesForOption("input");

        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("input1.txt", values.get(0));
        Assert.assertEquals("input2.txt", values.get(1));

        // Test empty list
        Assert.assertEquals(0, commandLine.getValuesForOption("nonexistent").size());
    }

    @Test
    public void testSetDefaultValues() {
        CommandLine commandLine = new DefaultCommandLine();
        Option option = new Option("o", "output");
        commandLine.addOption(option);

        List<String> defaultValues = Arrays.asList("output1.txt", "output2.txt");
        commandLine.setDefaultValues(option, defaultValues);

        Assert.assertEquals(2, commandLine.getDefaultValues("output").size());
        Assert.assertEquals("output1.txt", commandLine.getDefaultValue("output", 0));
        Assert.assertEquals("output2.txt", commandLine.getDefaultValue("output", 1));
    }

    @Test
    public void testAddSwitch() {
        CommandLine commandLine = new DefaultCommandLine();
        Option option = new Option("d", "debug");
        commandLine.addOption(option);

        commandLine.addSwitch("debug", true);

        Assert.assertTrue(commandLine.hasOption("debug"));
        Assert.assertTrue(commandLine.getOptionValue("debug") != null);

        try {
            commandLine.addSwitch("debug", false);
            Assert.fail("Should throw an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        CommandLine commandLine = new DefaultCommandLine();
        Option option = new Option("d", "debug");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch("debug", false);

        Assert.assertFalse(commandLine.getOptionValue("debug") != null);
        Assert.assertFalse(commandLine.hasOption("debug"));
    }

    @Test
    public void testAddProperty() {
        CommandLine commandLine = new DefaultCommandLine();

        commandLine.addProperty("test.property", "testValue");

        Assert.assertEquals("testValue", commandLine.getOptionValue("test.property"));
    }

    @Test
    public void testLooksLikeOption() {
        CommandLine commandLine = new DefaultCommandLine();

        Assert.assertTrue(commandLine.looksLikeOption("--test"));
        Assert.assertTrue(commandLine.looksLikeOption("-t"));
        Assert.assertFalse(commandLine.looksLikeOption("input.txt"));
    }
}