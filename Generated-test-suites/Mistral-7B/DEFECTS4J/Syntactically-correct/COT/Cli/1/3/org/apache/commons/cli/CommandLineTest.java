import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("o").build();
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption("o"));
        Assert.assertFalse(commandLine.hasOption("n"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("o").longOpt("output").hasArg(true).buildValueParser(String.class);
        commandLine.addOption(option);

        String value = "test";
        commandLine.parse("--output=test");

        Assert.assertEquals(value, commandLine.getOptionValue("o"));
        Assert.assertEquals(value, commandLine.getOptionValue("output"));
        Assert.assertNull(commandLine.getOptionValue("n"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("i").longOpt("input").isRequired(false).hasArg(true).buildValueParser(String[].class);
        commandLine.addOption(option);

        String[] values = new String[]{"test1", "test2"};
        commandLine.parse(new String[]{"--input", "test1", "--input", "test2"});

        String[] expectedValues = {"test1", "test2"};
        Assert.assertArrayEquals(expectedValues, commandLine.getOptionValues("i"));
        Assert.assertArrayEquals(expectedValues, commandLine.getOptionValues("input"));
        Assert.assertNull(commandLine.getOptionValues("n"));
    }

    @Test(expected = AssertionError.class)
    public void testGetOptionValueWithInvalidOption() {
        CommandLine commandLine = new CommandLine();

        Assert.assertNull(commandLine.getOptionValue("n"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2", "arg3"});

        String[] expectedArgs = {"arg1", "arg2", "arg3"};
        Assert.assertArrayEquals(expectedArgs, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2", "arg3"});

        List<String> expectedArgs = new LinkedList<>();
        expectedArgs.add("arg1");
        expectedArgs.add("arg2");
        expectedArgs.add("arg3");

        Assert.assertEquals(expectedArgs, commandLine.getArgList());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2", "arg3"});

        String arg = "arg4";
        commandLine.addArg(arg);

        List<String> expectedArgs = new LinkedList<>();
        expectedArgs.add("arg1");
        expectedArgs.add("arg2");
        expectedArgs.add("arg3");
        expectedArgs.add(arg);

        Assert.assertEquals(expectedArgs, commandLine.getArgList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithDuplicateOption() {
        CommandLine commandLine = new CommandLine();
        Option option = Option.builder("o").build();
        commandLine.addOption(option);

        commandLine.addOption(option);
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        Option option1 = Option.builder("o1").longOpt("o1").hasArg(true).buildValueParser(String.class);
        Option option2 = Option.builder("o2").longOpt("o2").hasArg(true).buildValueParser(Integer.class);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option[] expectedOptions = new Option[]{option1, option2};
        Assert.assertArrayEquals(expectedOptions, commandLine.getOptions());
    }
}