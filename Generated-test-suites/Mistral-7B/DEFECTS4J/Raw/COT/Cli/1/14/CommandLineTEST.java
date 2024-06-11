import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLineTest {

    @Test
    public void testConstructor() {
        new CommandLine();
    }

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", false, "A test option"));
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", false, "A test option"));
        Assert.assertTrue(commandLine.hasOption('a'));
        Assert.assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "optionA", Integer.TYPE, "A test option");
        commandLine.addOption(option);
        commandLine.parse(new String[]{"--optionA", "1"});
        Assert.assertEquals(Integer.valueOf(1), commandLine.getOptionObject("a"));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "optionA", Integer.TYPE, "A test option");
        commandLine.addOption(option);
        commandLine.parse(new String[]{"-a", "1"});
        Assert.assertEquals(Integer.valueOf(1), commandLine.getOptionObject('a'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "optionA", true, "A test option");
        commandLine.addOption(option);
        commandLine.parse(new String[]{"--optionA"});
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.isSet("a"));
        Assert.assertNull(commandLine.getOptionValue("a"));

        commandLine.parse(new String[]{"--optionA", "value"});
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.isSet("a"));
        Assert.assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "optionA", true, "A test option");
        commandLine.addOption(option);
        commandLine.parse(new String[]{"-a"});
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.isSet("a"));
        Assert.assertNull(commandLine.getOptionValue("a"));

        commandLine.parse(new String[]{"-a", "value"});
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.isSet("a"));
        Assert.assertEquals("value", commandLine.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "optionA", true, "A test option");
        commandLine.addOption(option.setValueSeparator(';'));
        commandLine.parse(new String[]{"--optionA", "value1;value2"});
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "optionA", true, "A test option");
        commandLine.addOption(option.setValueSeparator(';'));
        commandLine.parse(new String[]{"-a", "value1;value2"});
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues('a'));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2", "--optionA", "arg3"});
        String[] args = commandLine.getArgs();
        List expectedArgs = new ArrayList();
        expectedArgs.add("arg1");
        expectedArgs.add("arg2");
        expectedArgs.add("arg3");
        Assert.assertEquals(expectedArgs, Arrays.asList(args));
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2", "--optionA", "arg3"});
        List args = commandLine.getArgList();
        List expectedArgs = new ArrayList();
        expectedArgs.add("arg1");
        expectedArgs.add("arg2");
        expectedArgs.add("arg3");
        Assert.assertEquals(expectedArgs, args);
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"--wrongOption"});
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        List args = commandLine.getArgList();
        List expectedArgs = new ArrayList();
        expectedArgs.add("arg1");
        Assert.assertEquals(expectedArgs, args);
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "optionA", false, "A test option");
        commandLine.addOption(option);
        Assert.assertFalse(commandLine.hasOption("a"));

        commandLine.parse(new String[]{"--optionA"});
        Assert.assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        Option option1 = new Option("a", "optionA", false, "A test option 1");
        Option option2 = new Option("b", "optionB", false, "A test option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Iterator iterator = commandLine.iterator();
        List options = new ArrayList();
        while (iterator.hasNext()) {
            options.add(iterator.next());
        }
        List expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        Assert.assertEquals(expectedOptions, options);
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        Option option1 = new Option("a", "optionA", false, "A test option 1");
        Option option2 = new Option("b", "optionB", false, "A test option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option[] options = commandLine.getOptions();
        Option expectedOption1 = new Option("a", "optionA", false, "A test option 1");
        Option expectedOption2 = new Option("b", "optionB", false, "A test option 2");
        Assert.assertArrayEquals(new Option[]{expectedOption1, expectedOption2}, options);
    }
}