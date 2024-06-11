import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", false, "Option A"));

        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", true, "Option A"));

        commandLine.addOptionValue("a", "value1");
        commandLine.addOptionValue("a", "value2");

        Assert.assertEquals("value1", commandLine.getOptionObject("a"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", true, "Option A"));

        commandLine.addOptionValue("a", "value1");
        commandLine.addOptionValue("a", "value2");

        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA", true, "Option A"));

        commandLine.addOptionValue("a", "value1");
        commandLine.addOptionValue("a", "value2");

        Assert.assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("a"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
    }

}
