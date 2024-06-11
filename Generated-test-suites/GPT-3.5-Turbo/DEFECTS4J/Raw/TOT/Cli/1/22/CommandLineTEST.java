import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption("test"));
        commandLine.addOption(new Option("a", "alpha", false, "Option Alpha"));
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOption_Char() {
        assertFalse(commandLine.hasOption('t'));
        commandLine.addOption(new Option("t", "test", false, "Test Option"));
        assertTrue(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(commandLine.getOptionValue("name"));
        commandLine.addOption(new Option("n", "name", true, "Name Option"));
        commandLine.addOptionValue("n", "John");
        assertEquals("John", commandLine.getOptionValue("n"));
    }

    @Test
    public void testGetOptionValue_Char() {
        assertNull(commandLine.getOptionValue('n'));
        commandLine.addOption(new Option("n", "name", true, "Name Option"));
        commandLine.addOptionValue('n', "John");
        assertEquals("John", commandLine.getOptionValue('n'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(commandLine.getOptionValues("names"));
        commandLine.addOption(new Option("names", true, "Names Option"));
        commandLine.addOptionValue("names", "John");
        commandLine.addOptionValue("names", "Mary");
        String[] values = commandLine.getOptionValues("names");
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("John", values[0]);
        assertEquals("Mary", values[1]);
    }

    @Test
    public void testGetOptionValues_Char() {
        assertNull(commandLine.getOptionValues('n'));
        commandLine.addOption(new Option("n", true, "Names Option"));
        commandLine.addOptionValue('n', "John");
        commandLine.addOptionValue('n', "Mary");
        String[] values = commandLine.getOptionValues('n');
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("John", values[0]);
        assertEquals("Mary", values[1]);
    }

    @Test
    public void testGetOptionValue_DefaultValue() {
        assertNull(commandLine.getOptionValue("name", "default"));
        assertEquals("default", commandLine.getOptionValue("name", "default"));
        commandLine.addOption(new Option("n", "name", true, "Name Option"));
        commandLine.addOptionValue("n", "John");
        assertEquals("John", commandLine.getOptionValue("n", "default"));
    }

    @Test
    public void testGetOptionValue_Char_DefaultValue() {
        assertNull(commandLine.getOptionValue('n', "default"));
        assertEquals("default", commandLine.getOptionValue('n', "default"));
        commandLine.addOption(new Option("n", "name", true, "Name Option"));
        commandLine.addOptionValue('n', "John");
        assertEquals("John", commandLine.getOptionValue('n', "default"));
    }

    @Test
    public void testGetArgs() {
        assertEquals(0, commandLine.getArgs().length);
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] args = commandLine.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testGetArgList() {
        assertEquals(0, commandLine.getArgList().size());
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        List<String> argList = commandLine.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    @Test
    public void testIterator() {
        assertFalse(commandLine.iterator().hasNext());
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", true, "Option Beta");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Iterator<Option> iterator = commandLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        assertEquals(0, commandLine.getOptions().length);
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", true, "Option Beta");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Option[] options = commandLine.getOptions();
        assertEquals(2, options.length);
        assertEquals(option1, options[0]);
        assertEquals(option2, options[1]);
    }

    @Test
    public void testAddArg() {
        assertEquals(0, commandLine.getArgList().size());
        commandLine.addArg("arg1");
        assertEquals(1, commandLine.getArgList().size());
        assertEquals("arg1", commandLine.getArgList().get(0));
    }

    @Test
    public void testAddOption() {
        assertEquals(0, commandLine.getOptions().length);
        Option option = new Option("t", "test", false, "Test Option");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().length);
        assertEquals(option, commandLine.getOptions()[0]);
    }
}