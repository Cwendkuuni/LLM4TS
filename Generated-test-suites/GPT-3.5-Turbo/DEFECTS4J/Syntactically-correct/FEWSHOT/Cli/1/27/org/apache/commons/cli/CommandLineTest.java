import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption_String() {
        assertFalse(commandLine.hasOption("f"));
        assertFalse(commandLine.hasOption("foo"));
        assertFalse(commandLine.hasOption("bar"));
    }

    @Test
    public void testHasOption_Char() {
        assertFalse(commandLine.hasOption('f'));
        assertFalse(commandLine.hasOption('o'));
        assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject_String() {
        assertNull(commandLine.getOptionObject("f"));
        assertNull(commandLine.getOptionObject("foo"));
        assertNull(commandLine.getOptionObject("bar"));
    }

    @Test
    public void testGetOptionObject_Char() {
        assertNull(commandLine.getOptionObject('f'));
        assertNull(commandLine.getOptionObject('o'));
        assertNull(commandLine.getOptionObject('b'));
    }

    @Test
    public void testGetOptionValue_String() {
        assertNull(commandLine.getOptionValue("f"));
        assertNull(commandLine.getOptionValue("foo"));
        assertNull(commandLine.getOptionValue("bar"));
    }

    @Test
    public void testGetOptionValue_Char() {
        assertNull(commandLine.getOptionValue('f'));
        assertNull(commandLine.getOptionValue('o'));
        assertNull(commandLine.getOptionValue('b'));
    }

    @Test
    public void testGetOptionValues_String() {
        assertNull(commandLine.getOptionValues("f"));
        assertNull(commandLine.getOptionValues("foo"));
        assertNull(commandLine.getOptionValues("bar"));
    }

    @Test
    public void testGetOptionValues_Char() {
        assertNull(commandLine.getOptionValues('f'));
        assertNull(commandLine.getOptionValues('o'));
        assertNull(commandLine.getOptionValues('b'));
    }

    @Test
    public void testGetOptionValue_String_String() {
        assertEquals("default", commandLine.getOptionValue("f", "default"));
        assertEquals("default", commandLine.getOptionValue("foo", "default"));
        assertEquals("default", commandLine.getOptionValue("bar", "default"));
    }

    @Test
    public void testGetOptionValue_Char_String() {
        assertEquals("default", commandLine.getOptionValue('f', "default"));
        assertEquals("default", commandLine.getOptionValue('o', "default"));
        assertEquals("default", commandLine.getOptionValue('b', "default"));
    }

    @Test
    public void testGetArgs() {
        assertArrayEquals(new String[]{}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("arg1");
        assertEquals(1, commandLine.getArgList().size());
        assertEquals("arg1", commandLine.getArgList().get(0));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "foo", false, "description");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("foo"));
        assertFalse(commandLine.hasOption("bar"));
        assertEquals(option, commandLine.getOptionObject("f"));
        assertEquals(option, commandLine.getOptionObject("foo"));
        assertNull(commandLine.getOptionObject("bar"));
        assertEquals(option, commandLine.getOptionObject('f'));
        assertEquals(option, commandLine.getOptionObject('o'));
        assertNull(commandLine.getOptionObject('b'));
        assertEquals("description", commandLine.getOptionObject("f").getDescription());
    }

    @Test
    public void testIterator() {
        assertFalse(commandLine.iterator().hasNext());
        Option option = new Option("f", "foo", false, "description");
        commandLine.addOption(option);
        assertTrue(commandLine.iterator().hasNext());
        assertEquals(option, commandLine.iterator().next());
    }

    @Test
    public void testGetOptions() {
        assertArrayEquals(new Option[]{}, commandLine.getOptions());
        Option option = new Option("f", "foo", false, "description");
        commandLine.addOption(option);
        assertArrayEquals(new Option[]{option}, commandLine.getOptions());
    }
}