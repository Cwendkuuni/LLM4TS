package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Tests for {@link WriteableCommandLineImpl}.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        Option option = new Option("o", "output", true, "Output file name")
                .setRequired(false);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");

        Assert.assertEquals("test.txt", commandLine.getValues(option, null).get(0));
        Assert.assertFalse(commandLine.hasOption(new Option("i", "input")));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("o", commandLine.getOption(option.getLongOpt()).getLongOpt());
        Assert.assertEquals("output", commandLine.getOption(option.getLongOpt()).getShortOpt());
        Assert.assertEquals(true, option.isSettable());
        Assert.assertEquals("Output file name", option.getDescription());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals(null, commandLine.getSwitch(new Option("v", "verbose"), false));
        Assert.assertEquals(false, commandLine.getSwitch(new Option("q", "quiet"), true));
        Assert.assertEquals(Collections.emptySet(), commandLine.getProperties());
        Assert.assertEquals("o output Output file name\n", commandLine.toString());
    }

    @Test
    public void testRequiredOption() {
        Option option = new Option("i", "input", true, "Input file name")
                .setRequired(true);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        try {
            commandLine.addOption(option);
            commandLine.addValue(option, "test.txt");
            commandLine.addValue(option, "test2.txt");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("input.txt"));
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");

        Assert.assertEquals("input.txt", commandLine.getValues(option, null).get(0));
        Assert.assertEquals("output.txt", commandLine.getValues(option, null).get(1));
        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals("i input Input file name", commandLine.getOption(option.getLongOpt()).getLongOpt());
        Assert.assertEquals("input", commandLine.getOption(option.getLongOpt()).getShortOpt());
        Assert.assertEquals(true, option.isSettable());
        Assert.assertEquals("Input file name", option.getDescription());
        Assert.assertEquals(true, option.isRequired());
        Assert.assertEquals(null, commandLine.getSwitch(new Option("v", "verbose"), false));
        Assert.assertEquals(false, commandLine.getSwitch(new Option("q", "quiet"), true));
        Assert.assertEquals(Collections.emptySet(), commandLine.getProperties());
        Assert.assertEquals("i input Input file name\n", commandLine.toString());
    }

    @Test
    public void testSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Assert.assertEquals(true, commandLine.getSwitch(option, null));
        Assert.assertEquals(false, commandLine.getSwitch(new Option("q", "quiet"), true));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("v verbose Verbose output", commandLine.getOption(option.getLongOpt()).getLongOpt());
        Assert.assertEquals("verbose", commandLine.getOption(option.getLongOpt()).getShortOpt());
        Assert.assertEquals(false, option.isSettable());
        Assert.assertEquals(null, option.getValueDescription());
        Assert.assertEquals(null, commandLine.getValues(option, null));
        Assert.assertEquals(null, commandLine.getProperties());
        Assert.assertEquals("v", commandLine.toString());
    }

    @Test
    public void testOptionWithNoValues() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        Assert.assertEquals("", commandLine.getValues(option, null).get(0));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("v verbose Verbose output", commandLine.getOption(option.getLongOpt()).getLongOpt());
        Assert.assertEquals("verbose", commandLine.getOption(option.getLongOpt()).getShortOpt());
        Assert.assertEquals(false, option.isSettable());
        Assert.assertEquals(null, option.getValueDescription());
        Assert.assertEquals(Collections.emptySet(), commandLine.getProperties());
        Assert.assertEquals("v", commandLine.toString());
    }

    @Test
    public void testMultipleSwitchesAndOptions() {
        Option verboseOption = new Option("v", "verbose", false, "Verbose output");
        Option inputOption = new Option("i", "input", true, "Input file name")
                .setRequired(true);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(inputOption, Collections.emptyList());

        commandLine.addOption(verboseOption);
        commandLine.addSwitch(verboseOption, true);
        commandLine.addSwitch(verboseOption, false);
        commandLine.addOption(inputOption);
        commandLine.addValue(inputOption, "test.txt");

        Assert.assertEquals(true, commandLine.getSwitch(verboseOption, true));
        Assert.assertEquals(false, commandLine.getSwitch(verboseOption, false));
        Assert.assertEquals(false, commandLine.getSwitch(new Option("q", "quiet"), true));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("i input Input file name", commandLine.getOption(inputOption.getLongOpt()).getLongOpt());
        Assert.assertEquals("input", commandLine.getOption(inputOption.getLongOpt()).getShortOpt());
        Assert.assertEquals(true, inputOption.isSettable());
        Assert.assertEquals("Input file name", inputOption.getDescription());
        Assert.assertEquals(true, inputOption.isRequired());
        Assert.assertEquals(null, commandLine.getSwitch(new Option("v", "verbose"), false));
        Assert.assertEquals("v", commandLine.toString());

        // test with prefixes
        commandLine = new WriteableCommandLineImpl(inputOption, new ArrayList<>(List.of("my")));

        commandLine.addOption(verboseOption);
        commandLine.addSwitch(verboseOption, true);
        commandLine.addSwitch(verboseOption, false);
        commandLine.addOption(inputOption);
        commandLine.addValue(inputOption, "test.txt");

        Assert.assertEquals(true, commandLine.getSwitch(new Option("myv", "verbose"), true));
        Assert.assertEquals(false, commandLine.getSwitch(new Option("myv", "verbose"), false));
        Assert.assertEquals(false, commandLine.getSwitch(new Option("q", "quiet"), true));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("i input Input file name", commandLine.getOption(inputOption.getLongOpt()).getLongOpt());
        Assert.assertEquals("input", commandLine.getOption(inputOption.getLongOpt()).getShortOpt());
        Assert.assertEquals(true, inputOption.isSettable());
        Assert.assertEquals("Input file name", inputOption.getDescription());
        Assert.assertEquals(true, inputOption.isRequired());
        Assert.assertEquals(null, commandLine.getSwitch(new Option("v", "verbose"), false));
        Assert.assertEquals("myv", commandLine.toString());
    }

    @Test
    public void testPropertyOption() {
        Option propertyOption = new Option("p", "property", true, "Property name")
                .setValues(new String[0])
                .setAllowMultipleValues(true)
                .setValueDescription("Property value")
                .setArgName("propertyValue")
                .setRequired(false);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());

        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "property1", "value1");
        commandLine.addProperty(propertyOption, "property2", "value2");

        List values = commandLine.getValues(propertyOption, null);
        Map properties = commandLine.getProperties();

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("property1", values.get(0));
        Assert.assertEquals("property2", values.get(1));
        Assert.assertEquals(Collections.singletonMap("property1", "value1"), properties);
        Assert.assertEquals(Collections.singletonMap("property2", "value2"), properties);
        Assert.assertEquals(4, commandLine.getOptions().size());
        Assert.assertEquals("p property Property name", commandLine.getOption(propertyOption.getLongOpt()).getLongOpt());
        Assert.assertEquals("property", commandLine.getOption(propertyOption.getLongOpt()).getShortOpt());
        Assert.assertEquals(true, propertyOption.isSettable());
        Assert.assertEquals("Property name", propertyOption.getDescription());
        Assert.assertEquals(false, propertyOption.isRequired());
        Assert.assertEquals(null, commandLine.getSwitch(new Option("v", "verbose"), false));
        Assert.assertEquals("", commandLine.toString());
    }

    @Test
    public void testPropertyOptionWithDefaultValue() {
        Option propertyOption = new Option("p", "property", true, "Property name")
                .setValues(new String[0])
                .setAllowMultipleValues(true)
                .setValueDescription("Property value")
                .setArgName("propertyValue")
                .setDefaultValue("defaultValue");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());

        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "property1", "value1");

        List values = commandLine.getValues(propertyOption, new ArrayList<>(List.of("defaultValue")));
        Map properties = commandLine.getProperties();

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals(Collections.singletonMap("property1", "value1"), properties);
        Assert.assertEquals(Collections.singletonMap("property", "value1"), commandLine.getProperties());
        Assert.assertEquals(4, commandLine.getOptions().size());
        Assert.assertEquals("p property Property name", commandLine.getOption(propertyOption.getLongOpt()).getLongOpt());
        Assert.assertEquals("property", commandLine.getOption(propertyOption.getLongOpt()).getShortOpt());
        Assert.assertEquals(true, propertyOption.isSettable());
        Assert.assertEquals("Property name", propertyOption.getDescription());
        Assert.assertEquals(false, propertyOption.isRequired());
        Assert.assertEquals(null, commandLine.getSwitch(new Option("v", "verbose"), false));
        Assert.assertEquals("", commandLine.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateOptionTrigger() {
        Option option = new Option("o", "output", true, "Output file name")
                .setRequired(false);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addOption(option);

        Assert.fail();
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateOptionName() {
        Option option1 = new Option("o", "output", true, "Output file name")
                .setRequired(false);
        Option option2 = new Option("o", "other", true, "Other option")
                .setRequired(false);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.fail();
    }
}