import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    // Test for hasOption method
    @Test
    public void testHasOption() {
        commandLine.addOption(new Option("a", "optionA", false, "This is option A"));
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));
    }

    // Test for getOptionValue method with no argument
    @Test
    public void testGetOptionValueNoArg() {
        commandLine.addOption(new Option("a", "optionA", false, "This is option A"));
        commandLine.parse("--optionA");
        Assert.assertEquals("true", commandLine.getOptionValue("a"));
    }

    // Test for getOptionValue method with argument
    @Test
    public void testGetOptionValueWithArg() {
        commandLine.addOption(new Option("b", "optionB", true, "This is option B with an argument"));
        commandLine.parse("--optionB myArg");
        Assert.assertEquals("myArg", commandLine.getOptionValue("b"));
    }

    // Test for getOptionValues method with no argument
    @Test
    public void testGetOptionValuesNoArg() {
        commandLine.addOption(new Option("c", "optionC", false, "This is option C"));
        commandLine.parse("--optionC");
        String[] optionsValues = commandLine.getOptionValues("c");
        Assert.assertArrayEquals(new String[]{"true"}, optionsValues);
    }

    // Test for getOptionValues method with multiple arguments
    @Test
    public void testGetOptionValuesWithArg() {
        commandLine.addOption(new Option("d", "optionD", true, "This is option D with multiple arguments"));
        commandLine.parse("--optionD arg1 arg2 arg3");
        String[] optionsValues = commandLine.getOptionValues("d");
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, optionsValues);
    }

    // Test for getArgs method
    @Test
    public void testGetArgs() {
        commandLine.parse("arg1 arg2 optionA optionB arg3");
        String[] args = commandLine.getArgs();
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, args);
    }

    // Test for getArgList method
    @Test
    public void testGetArgList() {
        commandLine.parse("arg1 arg2 optionA optionB arg3");
        List argList = commandLine.getArgList();
        Assert.assertEquals(new LinkedList<String>(){{ add("arg1"); add("arg2"); add("arg3"); }}, argList);
    }

    // Test for parse method with invalid option
    @Test(expected = ParseException.class)
    public void testParseInvalidOption() {
        commandLine.parse("--invalidOption");
    }

    // Test for parse method with missing value for option requiring an argument
    @Test(expected = ParseException.class)
    public void testParseMissingValueForOptionRequiringArg() {
        commandLine.addOption(new Option("e", "optionE", true, "This is option E requiring an argument"));
        commandLine.parse("--optionE");
    }

    // Test for parse method with value for option requiring no argument
    @Test(expected = ParseException.class)
    public void testParseValueForOptionRequiringNoArg() {
        commandLine.addOption(new Option("f", "optionF", false, "This is option F"));
        commandLine.parse("--optionF value");
    }
}