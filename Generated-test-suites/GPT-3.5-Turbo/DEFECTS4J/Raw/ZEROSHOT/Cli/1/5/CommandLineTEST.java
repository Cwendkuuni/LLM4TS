import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption_String() {
        assertFalse(commandLine.hasOption("opt"));
    }

    @Test
    public void testHasOption_Char() {
        assertFalse(commandLine.hasOption('c'));
    }

    @Test
    public void testGetOptionObject_String() {
        assertNull(commandLine.getOptionObject("opt"));
    }

    @Test
    public void testGetOptionObject_Char() {
        assertNull(commandLine.getOptionObject('c'));
    }

    @Test
    public void testGetOptionValue_String() {
        assertNull(commandLine.getOptionValue("opt"));
    }

    @Test
    public void testGetOptionValue_Char() {
        assertNull(commandLine.getOptionValue('c'));
    }

    @Test
    public void testGetOptionValues_String() {
        assertNull(commandLine.getOptionValues("opt"));
    }

    @Test
    public void testGetOptionValues_Char() {
        assertNull(commandLine.getOptionValues('c'));
    }

    @Test
    public void testGetOptionValue_DefaultValue_String() {
        assertEquals("default", commandLine.getOptionValue("opt", "default"));
    }

    @Test
    public void testGetOptionValue_DefaultValue_Char() {
        assertEquals("default", commandLine.getOptionValue('c', "default"));
    }

    @Test
    public void testGetArgs() {
        String[] expected = new String[]{"arg1", "arg2"};
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertEquals(expected, commandLine.getArgList());
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertEquals(2, commandLine.getArgList().size());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptions()[0]);
    }

    @Test
    public void testIterator() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
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
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Option[] expected = new Option[]{option1, option2};
        assertArrayEquals(expected, commandLine.getOptions());
    }
}