import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.apache.commons.cli2.builder.DefaultParserBuilder.defaultParser;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option optionA = new Option("a", "apple")
                .required(false)
                .argName("APPLE")
                .longOpt("apple")
                .setValueSeparator('=')
                .valuesSeparator(',')
                .desc("an apple");

        Option optionB = new Option("b", "banana")
                .required(false)
                .argName("BANANA")
                .shortOpt("b")
                .longOpt("banana")
                .valuesSeparator(',')
                .desc("a banana");

        Option optionC = new Option("c", "color")
                .required(false)
                .argName("COLOR")
                .longOpt("color")
                .desc("the color of the fruit");

        Option optionD = new Option("d", "duration", true, "120")
                .required(false)
                .longOpt("duration")
                .desc("the duration of the test in seconds (default: 120)")
                .type(Integer.TYPE);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(
                new Option("fruit", true, null, Arrays.asList(optionA, optionB, optionC)),
                Arrays.asList("--apple=red,green", "--banana", "--color=blue", "--duration=150"));

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = defaultParser().parse(commandLine.getSpec(), commandLine.getArguments(), true);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("ParseException thrown: " + e.getMessage());
        }

        // Test getting values from parsed command line
        List appleValues = commandLine.getValues(optionA, null);
        Assert.assertEquals("Size of apple values list should be 2", 2, appleValues.size());
        Assert.assertEquals("Apple values should be 'red' and 'green'", "red", appleValues.get(0));
        Assert.assertEquals("Apple values should be 'red' and 'green'", "green", appleValues.get(1));

        // Test getting switch from parsed command line
        Boolean bananaSwitch = commandLine.getSwitch(optionB, null);
        Assert.assertNull("Banana switch should be null as it's not set", bananaSwitch);

        // Test getting property from parsed command line
        String colorProperty = commandLine.getProperty("color", null);
        Assert.assertNull("Color property should be null as it's not set", colorProperty);

        // Test invalid option
        Assert.assertFalse("Invalid option should not be present", commandLine.hasOption(new Option("e", "egg")));

        // Test option with no value
        Option optionE = new Option("e", "echo")
                .required(false)
                .argName("ECHO")
                .longOpt("echo");

        commandLine = new WriteableCommandLineImpl(
                new Option("output", true, null, Arrays.asList(optionA, optionB, optionC, optionE)),
                Arrays.asList("--output"));

        parsedCommandLine = null;
        try {
            parsedCommandLine = defaultParser().parse(commandLine.getSpec(), commandLine.getArguments(), true);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("ParseException thrown: " + e.getMessage());
        }

        // Test getting value of optional option
        List echoValues = commandLine.getValues(optionE, null);
        Assert.assertNull("Echo value should be null as it's optional", echoValues);

        // Test setting value of optional option
        commandLine = new WriteableCommandLineImpl(
                new Option("output", true, null, Arrays.asList(optionA, optionB, optionC, optionE)),
                Arrays.asList("--output", "--echo=test"));

        parsedCommandLine = null;
        try {
            parsedCommandLine = defaultParser().parse(commandLine.getSpec(), commandLine.getArguments(), true);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("ParseException thrown: " + e.getMessage());
        }

        echoValues = commandLine.getValues(optionE, null);
        Assert.assertEquals("Echo value should be 'test'", "test", echoValues.get(0));

        // Test setting switch
        commandLine = new WriteableCommandLineImpl(
                new Option("output", true, null, Arrays.asList(optionA, optionB, optionC, optionE)),
                Arrays.asList("--output", "--echo"));

        parsedCommandLine = null;
        try {
            parsedCommandLine = defaultParser().parse(commandLine.getSpec(), commandLine.getArguments(), true);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("ParseException thrown: " + e.getMessage());
        }

        bananaSwitch = commandLine.getSwitch(optionB, null);
        Assert.assertTrue("Banana switch should be set to true", bananaSwitch);

        // Test setting default values
        commandLine = new WriteableCommandLineImpl(
                new Option("output", true, null, Arrays.asList(optionA, optionB, optionC, optionE)),
                Arrays.asList("--output"));

        commandLine.setDefaultValues(optionA, new ArrayList(Arrays.asList("apple1", "apple2")));
        commandLine.setDefaultValues(optionC, new ArrayList(Arrays.asList("red", "green")));

        parsedCommandLine = null;
        try {
            parsedCommandLine = defaultParser().parse(commandLine.getSpec(), commandLine.getArguments(), true);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("ParseException thrown: " + e.getMessage());
        }

        appleValues = commandLine.getValues(optionA, Arrays.asList("apple1", "apple2"));
        Assert.assertEquals("Apple values should be 'apple1' and 'apple2'", "apple1", appleValues.get(0));
        Assert.assertEquals("Apple values should be 'apple1' and 'apple2'", "apple2", appleValues.get(1));

        colorProperty = commandLine.getProperty("color", null);
        Assert.assertEquals("Color property should be 'red'", "red", colorProperty);

        // Test setting default switch
        commandLine = new WriteableCommandLineImpl(
                new Option("output", true, null, Arrays.asList(optionA, optionB, optionC, optionE)),
                Arrays.asList());

        commandLine.setDefaultSwitch(optionB, Boolean.TRUE);

        parsedCommandLine = null;
        try {
            parsedCommandLine = defaultParser().parse(commandLine.getSpec(), commandLine.getArguments(), true);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("ParseException thrown: " + e.getMessage());
        }

        bananaSwitch = commandLine.getSwitch(optionB, null);
        Assert.assertTrue("Banana switch should be set to true by default", bananaSwitch);

        // Test getting default values
        appleValues = commandLine.getValues(optionA, null);
        Assert.assertEquals("Apple values should be empty by default", 0, appleValues.size());

        colorProperty = commandLine.getProperty("color", null);
        Assert.assertNull("Color property should be null by default", colorProperty);

        // Test getting properties
        Properties commandLineProperties = commandLine.getProperties();
        Assert.assertNotNull("Command line properties should not be null", commandLineProperties);
        Assert.assertEquals("Number of properties should be 0 by default", 0, commandLineProperties.size());

        commandLine.addProperty("test", "test");
        commandLineProperties = commandLine.getProperties();
        Assert.assertEquals("Command line properties should contain 'test=test'", "test=test", commandLineProperties.getProperty("test"));
    }
}