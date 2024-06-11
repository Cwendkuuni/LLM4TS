package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        Option option = new Option("o", "output", true, "Specify output file")
                .required(false)
                .defaultValue("output.txt");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addValue(option, "test.txt");

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option.parser().parse(commandLine.getOptions(), "o test.txt".split(" "));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(option.getValue(parsedCommandLine), "test.txt");
        Assert.assertEquals(commandLine.getValues(option, null), Collections.singletonList("test.txt"));
        Assert.assertFalse(commandLine.hasOption(option));
        Assert.assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testOptionalUsage() {
        Option option = new Option("o", "output", true, "Specify output file")
                .required(false)
                .defaultValue("output.txt");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option.parser().parse(commandLine.getOptions(), new String[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(option.getValue(parsedCommandLine), "output.txt");
        Assert.assertNull(commandLine.getValues(option, null));
        Assert.assertFalse(commandLine.hasOption(option));
        Assert.assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testSwitchUsage() {
        Option option = new Option("v", "verbose", false, "Print verbose output")
                .required(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option.parser().parse(commandLine.getOptions(), new String[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(commandLine.getSwitch(option, false));
        Assert.assertTrue(parsedCommandLine.hasOption(option));
        Assert.assertFalse(parsedCommandLine.getValue(option) != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithNoValueSet() {
        Option option = new Option("o", "output", false, "Specify output file");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addValue(option, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidValue() {
        Option option = new Option("o", "output", true, "Specify output file")
                .required(false)
                .defaultValue("output.txt");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addValue(option, new Object() {});
    }

    @Test
    public void testMultipleOptions() {
        Option option1 = new Option("o", "output1", true, "Specify output file 1")
                .required(false)
                .defaultValue("output1.txt");

        Option option2 = new Option("o", "output2", true, "Specify output file 2")
                .required(false)
                .defaultValue("output2.txt");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, option2, Collections.emptyList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.addValue(option1, "test1.txt");
        commandLine.addValue(option2, "test2.txt");

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option1.parser().parse(commandLine.getOptions(), new String[]{"o test1.txt", "o test2.txt"});
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(option1.getValue(parsedCommandLine), "test1.txt");
        Assert.assertEquals(option2.getValue(parsedCommandLine), "test2.txt");
        Assert.assertFalse(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(option2));
        Assert.assertFalse(commandLine.getSwitch(option1, false));
        Assert.assertFalse(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testOptionWithDefaultValue() {
        Option option = new Option("o", "output", true, "Specify output file")
                .required(false)
                .defaultValue("output.txt");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option.parser().parse(commandLine.getOptions(), new String[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(option.getValue(parsedCommandLine), "output.txt");
        Assert.assertNull(commandLine.getValues(option, null));
        Assert.assertFalse(commandLine.hasOption(option));
        Assert.assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testPropertyOption() {
        Option option = new Option("p", "property", true, "Set a property");
        option.setType(PropertyOption.TYPE);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addProperty(option, "key", "value");

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option.parser().parse(commandLine.getOptions(), new String[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(commandLine.getProperty("key"), "value");
        Assert.assertEquals(parsedCommandLine.getProperties().size(), 1);
        Assert.assertEquals(parsedCommandLine.getProperties().keySet().iterator().next(), "key");
    }

    @Test
    public void testParseEmptyCommandLine() {
        Option option = new Option("o", "output", true, "Specify output file")
                .required(false)
                .defaultValue("output.txt");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option.parser().parse(commandLine.getOptions(), new String[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertNull(parsedCommandLine);
        Assert.assertFalse(commandLine.hasOption(option));
        Assert.assertFalse(commandLine.getSwitch(option, false));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidCommandLine() {
        Option option = new Option("o", "output", true, "Specify output file")
                .required(false)
                .defaultValue("output.txt");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option.parser().parse(commandLine.getOptions(), new String[]{"o"});
        } catch (ParseException e) {
            Assert.assertEquals("The option 'o' does not accept an argument.", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidCommandLineWithInvalidTrigger() {
        Option option = new Option("x", "output", true, "Specify output file")
                .required(false)
                .defaultValue("output.txt");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = option.parser().parse(commandLine.getOptions(), new String[]{"x"});
        } catch (ParseException e) {
            Assert.assertEquals("The option 'x' is unknown.", e.getMessage());
        }
    }
}