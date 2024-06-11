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
        Assert.assertTrue(commandLine.hasOption('a'));
    }

    @Test
    public void testHasOptionFalse() {
        CommandLine commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption("nonExistentOption"));
        Assert.assertFalse(commandLine.hasOption('z'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a", "value"}));
        Assert.assertEquals("value", commandLine.getOptionValue("a"));
        Assert.assertEquals("value", commandLine.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValueEmpty() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a"}));
        Assert.assertNull(commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a", "value1", "value2"}));
        String[] expectedValues = new String[]{"value1", "value2"};
        Assert.assertArrayEquals(expectedValues, commandLine.getOptionValues("a"));
        Assert.assertArrayEquals(expectedValues, commandLine.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValuesEmpty() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a"}));
        Assert.assertNull(commandLine.getOptionValues("a"));
        Assert.assertNull(commandLine.getOptionValues('a'));
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse(new String[]{"--invalidOption"}));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse(new String[]{"arg1", "arg2", "--optionA", "-a"}));
        String[] expectedArgs = new String[]{"arg1", "arg2"};
        Assert.assertArrayEquals(expectedArgs, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse(new String[]{"arg1", "arg2", "--optionA", "-a"}));
        List expectedArgs = Arrays.asList(new String[]{"arg1", "arg2"});
        Assert.assertEquals(expectedArgs, commandLine.getArgList());
    }
}