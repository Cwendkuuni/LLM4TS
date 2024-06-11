import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("opt"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("opt"));
    }

    @Test
    public void testGetOptionObject_char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("opt"));
    }

    @Test
    public void testGetOptionValue_char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("opt"));
    }

    @Test
    public void testGetOptionValues_char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValue_String_String() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue("opt", "default"));
    }

    @Test
    public void testGetOptionValue_char_String() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue('o', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        String[] expected = new String[]{};
        assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        List<String> expected = new LinkedList<>();
        assertEquals(expected, commandLine.getArgList());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg");
        String[] expected = new String[]{"arg"};
        assertArrayEquals(expected, commandLine.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("opt", "example option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("opt"));
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("opt", "example option");
        commandLine.addOption(option);
        Iterator<Option> iterator = commandLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("opt", "example option");
        commandLine.addOption(option);
        Option[] expected = new Option[]{option};
        assertArrayEquals(expected, commandLine.getOptions());
    }
}