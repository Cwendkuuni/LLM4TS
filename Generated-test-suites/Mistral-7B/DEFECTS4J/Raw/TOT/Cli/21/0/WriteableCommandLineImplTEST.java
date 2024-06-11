package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    /**
     * Tests the default behavior of WriteableCommandLineImpl.
     */
    @Test
    public void testDefaultBehavior() {
        Option option = new Option("o", "output", true, "Output file path.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addValue(option, "test.txt");

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("output", commandLine.getOptions().get(0).getLongOpt());
        Assert.assertEquals("o", commandLine.getOptions().get(0).getOpt());
        Assert.assertEquals("Output file path.", commandLine.getOptions().get(0).getDescription());
        Assert.assertEquals(true, commandLine.getOption(option).isSet());
        Assert.assertEquals("test.txt", commandLine.getValues(option).get(0));
    }

    /**
     * Tests the behavior of WriteableCommandLineImpl with an option that has a value list.
     */
    @Test
    public void testOptionWithValueList() {
        Option option = new Option("l", "list", false, "Show list of files.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addValue(option, "file1.txt");
        commandLine.addValue(option, "file2.txt");

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("list", commandLine.getOptions().get(0).getOpt());
        Assert.assertEquals(false, commandLine.getOption(option).isSet());
        Assert.assertEquals(2, commandLine.getValues(option).size());
        Assert.assertEquals("file1.txt", commandLine.getValues(option).get(0));
        Assert.assertEquals("file2.txt", commandLine.getValues(option).get(1));
    }

    /**
     * Tests the behavior of WriteableCommandLineImpl with an option that has a default value.
     */
    @Test
    public void testOptionWithDefaultValue() {
        Option option = new Option("d", "debug", false, "Run in debug mode.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("debug", commandLine.getOptions().get(0).getOpt());
        Assert.assertEquals(false, commandLine.getOption(option).isSet());
        Assert.assertNull(commandLine.getValues(option));

        commandLine.setDefaultValues(option, Collections.singletonList("true"));

        commandLine.addSwitch(option, true);

        Assert.assertEquals("true", commandLine.getValues(option).get(0));
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    /**
     * Tests the behavior of WriteableCommandLineImpl with an option that has a default switch.
     */
    @Test
    public void testOptionWithDefaultSwitch() {
        Option option = new Option("s", "silent", false, "Suppress output.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("silent", commandLine.getOptions().get(0).getOpt());
        Assert.assertEquals(false, commandLine.getOption(option).isSet());
        Assert.assertNull(commandLine.getValues(option));

        commandLine.setDefaultSwitch(option, true);

        commandLine.addSwitch(option, false);

        Assert.assertFalse(commandLine.getSwitch(option, false));
        Assert.assertTrue(commandLine.getSwitch(option, true));
    }

    /**
     * Tests the behavior of WriteableCommandLineImpl with empty and non-empty argument lists.
     */
    @Test
    public void testArguments() {
        Option option = new Option("a", "arg", true, "Argument.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option,
                Collections.singletonList("arg1"));

        commandLine.addOption(option);

        commandLine.addValue(option, "arg2");
        commandLine.addValue(option, "arg3");

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("arg", commandLine.getOptions().get(0).getOpt());
        Assert.assertEquals("Argument.", commandLine.getOptions().get(0).getDescription());
        Assert.assertEquals(true, commandLine.getOption(option).isSet());
        Assert.assertEquals(3, commandLine.getValues(option).size());
        Assert.assertEquals("arg1", commandLine.getValues(option).get(0));
        Assert.assertEquals("arg2", commandLine.getValues(option).get(1));
        Assert.assertEquals("arg3", commandLine.getValues(option).get(2));

        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addValue(option, "arg4");

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("arg", commandLine.getOptions().get(0).getOpt());
        Assert.assertEquals("Argument.", commandLine.getOptions().get(0).getDescription());
        Assert.assertEquals(true, commandLine.getOption(option).isSet());
        Assert.assertEquals(1, commandLine.getValues(option).size());
        Assert.assertEquals("arg4", commandLine.getValues(option).get(0));
    }

    /**
     * Tests the behavior of WriteableCommandLineImpl with an option that has a property.
     */
    @Test
    public void testOptionWithProperty() {
        Option option = new Option("p", "port", true, "Port number.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        commandLine.addProperty(option, "port", "8080");

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals("port", commandLine.getOptions().get(0).getLongOpt());
        Assert.assertEquals("p", commandLine.getOptions().get(0).getOpt());
        Assert.assertEquals("Port number.", commandLine.getOptions().get(0).getDescription());
        Assert.assertEquals("8080", commandLine.getProperty(option, "8080"));
        Assert.assertNull(commandLine.getProperty(option, "1234"));
        Assert.assertEquals(1, commandLine.getProperties(option).size());
        Assert.assertEquals("port", commandLine.getProperties(option).iterator().next());
    }
}