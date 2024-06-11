import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Test creating a new WriteableCommandLineImpl
        Option option = new Option("o", "output", true, "Output file");
        List arguments = Arrays.asList("test.txt");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        // Test adding option
        Option option2 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option2);

        // Test adding value
        commandLine.addValue(option, "output.txt");

        // Test adding switch
        commandLine.addSwitch(option, true);

        // Test getting option
        Option getOption = commandLine.getOption("o");
        Assert.assertEquals("output", getOption.getLongOpt());
        Assert.assertEquals("output", getOption.getOptString());
        Assert.assertEquals(true, getOption.isRequired());
        Assert.assertEquals("Output file", getOption.getDescription());
        Assert.assertNull(getOption.getValue());
        Assert.assertNull(getOption.getValues());

        // Test getting values
        commandLine.addValue(option2, "input.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("output.txt", ((String) values.get(0)));

        // Test getting switch
        Assert.assertTrue(commandLine.getSwitch(option, false));
        Assert.assertFalse(commandLine.getSwitch(option2, null));

        // Test setting properties
        commandLine.addProperty("property1", "value1");
        Properties properties = commandLine.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value1", properties.getProperty("property1"));

        // Test looking like option
        Assert.assertTrue(commandLine.looksLikeOption("o"));
        Assert.assertFalse(commandLine.looksLikeOption("t"));

        // Test getting options
        CommandLine parsedCommandLine = new DefaultParser().parse(option.getOptionParser(), "o output.txt i input.txt");
        List options = parsedCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option testOption = (Option) options.get(0);
        Assert.assertEquals("output", testOption.getLongOpt());
        Assert.assertEquals("o", testOption.getOptString());
        Option testOption2 = (Option) options.get(1);
        Assert.assertEquals("input", testOption2.getLongOpt());
        Assert.assertEquals("i", testOption2.getOptString());

        // Test toString
        String toString = commandLine.toString();
        Assert.assertEquals("output.txt i input.txt", toString);

        // Test getting normalised
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("output.txt", normalised.get(0));
        Assert.assertEquals("i", normalised.get(1));

        // Test setting default values
        commandLine = new WriteableCommandLineImpl(option, null);
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));

        // Test setting default switch
        commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));

        // Test error scenarios
        try {
            commandLine = new WriteableCommandLineImpl(null, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, null);
            commandLine.addValue(option, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, null);
            commandLine.addValue(option, "value");
            commandLine.addValue(option, "value2");
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, null);
            commandLine.addValue(option, "value");
            commandLine.addSwitch(option, true);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, Arrays.asList("value", "value2"));
            commandLine.addValue(option, "value3");
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, Arrays.asList("value", "value2"));
            commandLine.addValue(option, "value");
            commandLine.addValue(option, "value2");
            commandLine.addValue(option, "value3");
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, Arrays.asList("value", "value2"));
            commandLine.addSwitch(option, true);
            commandLine.addSwitch(option, true);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, Arrays.asList("value", "value2"));
            commandLine.addSwitch(option, true);
            commandLine.addValue(option, "value3");
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, Arrays.asList("value", "value2"));
            commandLine.addValue(option, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, Arrays.asList("value", "value2"));
            commandLine.addValue(option, Collections.singletonList(null));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            commandLine = new WriteableCommandLineImpl(option, Arrays.asList("value", "value2"));
            commandLine.addValue(option, Arrays.asList(null, null));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}