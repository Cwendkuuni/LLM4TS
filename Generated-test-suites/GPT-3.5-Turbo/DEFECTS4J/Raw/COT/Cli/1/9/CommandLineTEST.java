import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption("test"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine commandLine = new CommandLine();
        Assert.assertFalse(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionObject("test"));
    }

    @Test
    public void testGetOptionObjectChar() {
        CommandLine commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValueChar() {
        CommandLine commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionValues("test"));
    }

    @Test
    public void testGetOptionValuesChar() {
        CommandLine commandLine = new CommandLine();
        Assert.assertNull(commandLine.getOptionValues('t'));
    }

    @Test
    public void testGetOptionValueWithDefaultValue() {
        CommandLine commandLine = new CommandLine();
        Assert.assertEquals("default", commandLine.getOptionValue("test", "default"));
    }

    @Test
    public void testGetOptionValueCharWithDefaultValue() {
        CommandLine commandLine = new CommandLine();
        Assert.assertEquals("default", commandLine.getOptionValue('t', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        String[] expected = new String[0];
        Assert.assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        List<String> expected = new ArrayList<>();
        Assert.assertEquals(expected, commandLine.getArgList());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg");
        String[] expected = {"arg"};
        Assert.assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", false, "test option");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", false, "test option");
        commandLine.addOption(option);
        Iterator<Option> iterator = commandLine.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(option, iterator.next());
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("t", "test", false, "test option");
        commandLine.addOption(option);
        Option[] expected = {option};
        Assert.assertArrayEquals(expected, commandLine.getOptions());
    }

}