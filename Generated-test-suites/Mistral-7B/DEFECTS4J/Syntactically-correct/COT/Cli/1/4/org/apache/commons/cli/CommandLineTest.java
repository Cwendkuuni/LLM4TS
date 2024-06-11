import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandLineTest {

    @Test
    public void testHasOption() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse("a"));

        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testGetOptionValue() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse("a"));

        Assert.assertNull(commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("b"));

        options.addOption("b", true, "Option B");
        commandLine.parse(options.parse("a", "1", "b", "2"));

        Assert.assertEquals("1", commandLine.getOptionValue("a"));
        Assert.assertEquals("2", commandLine.getOptionValue("b"));
        Assert.assertNull(commandLine.getOptionValue("c"));
    }

    @Test
    public void testGetOptionValues() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse("a", "1", "a", "2"));

        String[] values = commandLine.getOptionValues("a");
        Assert.assertEquals(values.length, 2);
        Assert.assertEquals(values[0], "1");
        Assert.assertEquals(values[1], "2");

        Assert.assertNull(commandLine.getOptionValues("b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionValueNullOption() throws ParseException {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new Options().parse(new String[0]));
        commandLine.getOptionValue(null);
    }

    @Test
    public void testGetArgList() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse("a", "1", "b", "2"));

        List args = commandLine.getArgList();
        Assert.assertEquals(args.size(), 0);

        options.addRequiredOption("arg", true, "Argument");
        commandLine.parse(options.parse("a", "1", "b", "2", "arg1"));

        args = commandLine.getArgList();
        Assert.assertEquals(args.size(), 1);
        Assert.assertEquals(args.get(0), "arg1");
    }

    @Test
    public void testToString() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse("a", "1", "b", "2"));

        Assert.assertNull(commandLine.toString()); // TODO: Implement toString()
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddArgNullArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg(null);
    }

    @Test
    public void testAddOptionNullOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(null);
    }

    @Test
    public void testAddOptionParseWithValue() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse("a"));

        Option option = new Option("b", "b", false, "Option B");
        commandLine.addOption(option);

        Assert.assertEquals(commandLine.getOptions().length, 2);
        Assert.assertEquals(commandLine.getOptions()[0].getKey(), "a");
        Assert.assertEquals(commandLine.getOptions()[0].getLongOpt(), null);
        Assert.assertTrue(commandLine.getOptions()[0].isRequired());
        Assert.assertFalse(commandLine.getOptions()[0].hasArg());
        Assert.assertNull(commandLine.getOptions()[0].getValue());

        Assert.assertEquals(commandLine.getOptions()[1].getKey(), "b");
        Assert.assertEquals(commandLine.getOptions()[1].getLongOpt(), "b");
        Assert.assertFalse(commandLine.getOptions()[1].isRequired());
        Assert.assertFalse(commandLine.getOptions()[1].hasArg());
        Assert.assertNull(commandLine.getOptions()[1].getValue());
    }

    @Test
    public void testAddOptionParseWithoutValue() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse("a"));

        Option option = new Option("b", null, false, "Option B");
        commandLine.addOption(option);

        Assert.assertEquals(commandLine.getOptions().length, 2);
        Assert.assertEquals(commandLine.getOptions()[0].getKey(), "a");
        Assert.assertEquals(commandLine.getOptions()[0].getLongOpt(), null);
        Assert.assertTrue(commandLine.getOptions()[0].isRequired());
        Assert.assertFalse(commandLine.getOptions()[0].hasArg());
        Assert.assertNull(commandLine.getOptions()[0].getValue());

        Assert.assertEquals(commandLine.getOptions()[1].getKey(), "b");
        Assert.assertEquals(commandLine.getOptions()[1].getLongOpt(), "b");
        Assert.assertFalse(commandLine.getOptions()[1].isRequired());
        Assert.assertFalse(commandLine.getOptions()[1].hasArg());
        Assert.assertNull(commandLine.getOptions()[1].getValue());
    }

    @Test
    public void testAddOptionParseWithValueAndArg() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse("a"));

        Option option = new Option("b", "c", true, "Option B with Argument");
        commandLine.addOption(option);

        Assert.assertEquals(commandLine.getOptions().length, 2);
        Assert.assertEquals(commandLine.getOptions()[0].getKey(), "a");
        Assert.assertEquals(commandLine.getOptions()[0].getLongOpt(), null);
        Assert.assertTrue(commandLine.getOptions()[0].isRequired());
        Assert.assertFalse(commandLine.getOptions()[0].hasArg());
        Assert.assertNull(commandLine.getOptions()[0].getValue());

        Assert.assertEquals(commandLine.getOptions()[1].getKey(), "b");
        Assert.assertEquals(commandLine.getOptions()[1].getLongOpt(), "c");
        Assert.assertFalse(commandLine.getOptions()[1].isRequired());
        Assert.assertTrue(commandLine.getOptions()[1].hasArg());
        Assert.assertEquals(commandLine.getOptions()[1].getValue(), "");
    }

    @Test
    public void testAddOptionParseWithValues() throws ParseException {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        commandLine.parse(options.parse("a"));

        Option option = new Option("b", true, false, "Option B");
        option.setValues(new String[]{"value1", "value2"});
        commandLine.addOption(option);

        Assert.assertEquals(commandLine.getOptions().length, 2);
        Assert.assertEquals(commandLine.getOptions()[0].getKey(), "a");
        Assert.assertEquals(commandLine.getOptions()[0].getLongOpt(), null);
        Assert.assertTrue(commandLine.getOptions()[0].isRequired());
        Assert.assertFalse(commandLine.getOptions()[0].hasArg());
        Assert.assertNull(commandLine.getOptions()[0].getValue());

        Assert.assertEquals(commandLine.getOptions()[1].getKey(), "b");
        Assert.assertEquals(commandLine.getOptions()[1].getLongOpt(), null);
        Assert.assertFalse(commandLine.getOptions()[1].isRequired());
        Assert.assertTrue(commandLine.getOptions()[1].hasArg());
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptions()[1].getValues());
        Assert.assertEquals(commandLine.getOptions()[1].getValue(), null);
    }
}