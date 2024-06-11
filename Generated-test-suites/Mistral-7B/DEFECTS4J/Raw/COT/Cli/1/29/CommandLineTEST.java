import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a"}));
        Assert.assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOptionFalse() {
        CommandLine commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a", "value"}));
        Assert.assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionValueNullOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2"});
        commandLine.getOptionValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionValueUnknownOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2"});
        commandLine.getOptionValue("unknownOption");
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a", "value1", "-a", "value2"}));
        String[] expected = new String[]{"value1", "value2"};
        Assert.assertArrayEquals(expected, commandLine.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValuesEmpty() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"arg1"}));
        Assert.assertNull(commandLine.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValuesUnknownOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2"});
        Assert.assertNull(commandLine.getOptionValues("unknownOption"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2", "arg3"});
        String[] expected = new String[]{"arg1", "arg2", "arg3"};
        Assert.assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[]{"arg1", "arg2", "arg3"});
        List expected = Arrays.asList("arg1", "arg2", "arg3");
        Assert.assertEquals(expected, commandLine.getArgList());
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse(new String[]{"invalidOption"}));
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWithMessage() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"arg1", "arg2", "-a", "invalidValue"}));
    }

    @Test
    public void testToString() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a", "value"}));
        String expected = "CommandLine: [ options: {Option: name='a', longOpt='', value=[value], args=[], optional=true, typeClass=null}, ] [ args: [ ] ] ]";
        Assert.assertEquals(expected, commandLine.toString());
    }
}