import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandLineTest {

    @Test
    public void testHasOption() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a"}));
        Assert.assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOptionFalse() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{}));
        Assert.assertFalse(commandLine.hasOption("a"));
    }

    @Test
    public void testGetOptionValue() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a", "true"}));
        Assert.assertEquals("true", commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("b"));
    }

    @Test
    public void testGetOptionValues() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"-a", "val1", "val2"}));
        String[] expected = new String[]{"val1", "val2"};
        Assert.assertArrayEquals(expected, commandLine.getOptionValues("a"));
        Assert.assertNull(commandLine.getOptionValues("b"));
    }

    @Test
    public void testGetArgs() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{"arg1", "arg2", "-a"}));
        String[] expected = new String[]{"arg1", "arg2"};
        Assert.assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse(new String[]{"-unknown"}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOption() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse(new String[]{null}));
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        List<String> args = (List<String>) commandLine.getArgList();
        Assert.assertEquals(1, args.size());
        Assert.assertEquals("arg1", args.get(0));
    }

    @Test
    public void testAddOption() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse(new String[]{}));
        commandLine.addOption(new Option("b", true, "Option B"));
        List<Option> optionsList = new LinkedList<>((Collection<? extends Option>) commandLine.getOptions());
        Assert.assertEquals(2, optionsList.size());
        Option optionA = optionsList.get(0);
        Option optionB = optionsList.get(1);
        Assert.assertEquals("b", optionB.getLongOpt());
        Assert.assertEquals("Option B", optionB.getDescription());
    }
}