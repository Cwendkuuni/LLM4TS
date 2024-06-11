import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        Assert.assertFalse(commandLine.hasOption("test"));
        commandLine.addOption(new Option("test", false, ""));
        Assert.assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testHasOption_Char() {
        Assert.assertFalse(commandLine.hasOption('t'));
        commandLine.addOption(new Option("test", 't', false, ""));
        Assert.assertTrue(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        commandLine.addOption(new Option("test", false, ""));
        Object value = commandLine.getOptionObject("test");
        Assert.assertNull(value);
    }

    @Test
    public void testGetOptionObject_Char() {
        commandLine.addOption(new Option("test", 't', false, ""));
        Object value = commandLine.getOptionObject('t');
        Assert.assertNull(value);
    }

    @Test
    public void testGetOptionValue() {
        commandLine.addOption(new Option("test", false, ""));
        String value = commandLine.getOptionValue("test");
        Assert.assertNull(value);
    }

    @Test
    public void testGetOptionValue_Char() {
        commandLine.addOption(new Option("test", 't', false, ""));
        String value = commandLine.getOptionValue('t');
        Assert.assertNull(value);
    }

    @Test
    public void testGetOptionValues() {
        commandLine.addOption(new Option("test", true, ""));
        String[] values = commandLine.getOptionValues("test");
        Assert.assertNull(values);
    }

    @Test
    public void testGetOptionValues_Char() {
        commandLine.addOption(new Option("test", 't', true, ""));
        String[] values = commandLine.getOptionValues('t');
        Assert.assertNull(values);
    }

    @Test
    public void testGetOptionValue_Default() {
        commandLine.addOption(new Option("test", false, ""));
        String value = commandLine.getOptionValue("test", "default");
        Assert.assertEquals("default", value);
    }

    @Test
    public void testGetOptionValue_Char_Default() {
        commandLine.addOption(new Option("test", 't', false, ""));
        String value = commandLine.getOptionValue('t', "default");
        Assert.assertEquals("default", value);
    }

    @Test
    public void testGetArgs() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] args = commandLine.getArgs();
        Assert.assertArrayEquals(new String[] {"arg1", "arg2"}, args);
    }

    @Test
    public void testGetArgList() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        List<String> argList = commandLine.getArgList();
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), argList);
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] args = commandLine.getArgs();
        Assert.assertArrayEquals(new String[] {"arg1", "arg2"}, args);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", false, "");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption("test"));
    }

    @Test
    public void testIterator() {
        Option option1 = new Option("test1", false, "");
        Option option2 = new Option("test2", false, "");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = new ArrayList<>();
        for (Iterator<Option> iterator = commandLine.iterator(); iterator.hasNext(); ) {
            options.add(iterator.next());
        }
        Assert.assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1", false, "");
        Option option2 = new Option("test2", false, "");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Option[] options = commandLine.getOptions();
        Assert.assertArrayEquals(new Option[] {option1, option2}, options);
    }

}