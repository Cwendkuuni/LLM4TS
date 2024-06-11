import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption("t"));
        Assert.assertTrue(commandLine.hasOption('t'));
    }

    @Test
    public void testHasNoOption() {
        CommandLine commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption("n"));
        Assert.assertFalse(commandLine.hasOption('n'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("t").longOpt("test").valueSeparator('=').buildValue("value");
        commandLine.addOption(option);
        commandLine.addArg("--test=testValue");
        Assert.assertEquals("testValue", commandLine.getOptionValue("t"));
        Assert.assertEquals("testValue", commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        Assert.assertNull(commandLine.getOptionValue("t"));
        Assert.assertNull(commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("t").longOpt("test").valueSeparator('=').buildValue(new String[]{"v1", "v2"});
        commandLine.addOption(option);
        commandLine.addArg("--test=v1");
        commandLine.addArg("--test=v2");
        String[] values = commandLine.getOptionValues("t");
        Assert.assertArrayEquals(new String[]{"v1", "v2"}, values);
        String[] values2 = commandLine.getOptionValues('t');
        Assert.assertArrayEquals(new String[]{"v1", "v2"}, values2);
    }

    @Test
    public void testGetOptionValuesWithNoValue() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        String[] values = commandLine.getOptionValues("t");
        Assert.assertArrayEquals(new String[0], values);
        String[] values2 = commandLine.getOptionValues('t');
        Assert.assertArrayEquals(new String[0], values2);
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        List expectedArgList = new ArrayList();
        expectedArgList.add("arg1");
        expectedArgList.add("arg2");
        Assert.assertEquals(expectedArgList, commandLine.getArgList());
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] expectedArgs = new String[]{"arg1", "arg2"};
        Assert.assertArrayEquals(expectedArgs, commandLine.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("--unknownOption");
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        commandLine.getOptionValue("unknownOption");
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        commandLine.addArg("--test=testValue");
        Object optionObject = commandLine.getOptionObject("t");
        Assert.assertEquals("testValue", optionObject);
        Option option2 = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option2);
        commandLine.addArg("--test=testValue2");
        Object optionObject2 = commandLine.getOptionObject("t");
        Assert.assertEquals("testValue2", optionObject2);
    }

    @Test
    public void testGetOptionObjectWithNoValue() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        Object optionObject = commandLine.getOptionObject("t");
        Assert.assertNull(optionObject);
    }

    @Test
    public void testToString() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String expectedString = "[ CommandLine: [ options: {test=null} ] [ args: [arg1, arg2] ] ]";
        Assert.assertEquals(expectedString, commandLine.toString());
    }
}