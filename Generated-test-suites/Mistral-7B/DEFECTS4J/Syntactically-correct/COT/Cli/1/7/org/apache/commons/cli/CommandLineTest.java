import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        Option option = new Option("t", "test", false, "Run tests.");
        options.addOption(option);
        commandLine.parse(options.parse("--test"));

        Assert.assertTrue(commandLine.hasOption("t"));
        Assert.assertTrue(commandLine.hasOption('t'));
        Assert.assertFalse(commandLine.hasOption("nonexistent"));
        Assert.assertFalse(commandLine.hasOption('z'));

        commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption("t"));
        Assert.assertFalse(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        Option option = new Option("t", "test", true, "Test file.");
        option.setRequired(false);
        options.addOption(option);
        commandLine.parse(options.parse("--test file.txt"));

        Assert.assertNull(commandLine.getOptionValue("t"));
        Assert.assertNull(commandLine.getOptionValue('t'));

        Assert.assertEquals("file.txt", commandLine.getOptionValue("test"));
        Assert.assertEquals("file.txt", commandLine.getOptionValue('t'));

        Assert.assertNull(commandLine.getOptionValue("nonexistent"));
        Assert.assertNull(commandLine.getOptionValue('x'));

        commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionValue("t"));
        Assert.assertNull(commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        Option option = new Option("t", "test", true, "Test files.");
        option.setRequired(false);
        options.addOption(option);
        commandLine.parse(options.parse("--test file1.txt file2.txt"));

        String[] expectedValues = {"file1.txt", "file2.txt"};
        assertArrayEquals(expectedValues, commandLine.getOptionValues("test"));
        assertArrayEquals(expectedValues, commandLine.getOptionValues('t'));

        commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("t"));
        assertNull(commandLine.getOptionValues('t'));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptionValueWithMissingOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[0]);
        commandLine.getOptionValue("missing");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptionValueWithMissingOptionChar() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(new String[0]);
        commandLine.getOptionValue('missing');
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        commandLine.parse(options.parse("--test file.txt arg1 arg2"));

        String[] expectedArgs = {"file.txt", "arg1", "arg2"};
        assertArrayEquals(expectedArgs, commandLine.getArgs());
        assertArrayEquals(expectedArgs, commandLine.getArgList().toArray(new String[0]));

        commandLine = new CommandLine();
        assertNotNull(commandLine.getArgs());
        assertNotNull(commandLine.getArgList());
    }

    @Test
    public void testToString() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        Option option = new Option("t", "test", false, "Run tests.");
        options.addOption(option);
        commandLine.parse(options.parse("--test"));

        String expectedString = "[ CommandLine: [ options: {test=false} ] [ args: [] ] ]";
        Assert.assertEquals(expectedString, commandLine.toString());

        commandLine = new CommandLine();
        options.addOption(option);
        commandLine.parse(options.parse("--test arg1 arg2"));

        expectedString = "[ CommandLine: [ options: {test=true, args=[arg1, arg2]} ] [ args: [] ] ]";
        Assert.assertEquals(expectedString, commandLine.toString());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        String[] expectedArgs = {"arg1", "arg2"};
        assertArrayEquals(expectedArgs, commandLine.getArgs());
        assertArrayEquals(expectedArgs, commandLine.getArgList().toArray(new String[0]));

        commandLine = new CommandLine();
        commandLine.addArg("arg");
        Assert.assertEquals(1, commandLine.getArgs().length);
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        Option option = new Option("t", "test", false, "Run tests.");
        commandLine.parse(options.parse("--test"));
        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().length);
        Option addedOption = commandLine.getOptions()[0];
        Assert.assertEquals("test", addedOption.getLongOpt());
        Assert.assertEquals('t', addedOption.getKey());
        Assert.assertEquals(false, addedOption.hasArg());
        Assert.assertEquals(Option.NO_ARGUMENT, addedOption.getArgPosition());
        Assert.assertEquals(Option.TYPE_BOOLEAN, addedOption.getType());

        commandLine = new CommandLine();
        commandLine.parse(new String[0]);
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().length);
        Assert.assertEquals("test", commandLine.getOptions()[0].getLongOpt());
        Assert.assertEquals('t', commandLine.getOptions()[0].getKey());
        Assert.assertEquals(false, commandLine.getOptions()[0].hasArg());
        Assert.assertEquals(Option.NO_ARGUMENT, commandLine.getOptions()[0].getArgPosition());
        Assert.assertEquals(Option.TYPE_BOOLEAN, commandLine.getOptions()[0].getType());

        option.setArgName("argName");
        option.setRequired(true);
        option.setArgPosition(Option.ARG_POSITION_AFTER);
        commandLine = new CommandLine();
        commandLine.parse(new String[0]);
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().length);
        Assert.assertEquals("argName", commandLine.getOptions()[0].getArgName());
        Assert.assertEquals(true, commandLine.getOptions()[0].isRequired());
        Assert.assertEquals(Option.ARG_POSITION_AFTER, commandLine.getOptions()[0].getArgPosition());
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("Expected: " + Arrays.toString(expected) +
                    "\n     But was: " + Arrays.toString(actual));
        }
    }

    private void assertNull(Object value) {
        if (value != null) {
            throw new AssertionError("Expected null but was: " + value);
        }
    }
}