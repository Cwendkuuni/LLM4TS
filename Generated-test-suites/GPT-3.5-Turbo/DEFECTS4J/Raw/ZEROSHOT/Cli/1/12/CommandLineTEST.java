import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("opt"));
    }

    @Test
    public void testHasOption_Char() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("opt"));
    }

    @Test
    public void testGetOptionObject_Char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("opt"));
    }

    @Test
    public void testGetOptionValue_Char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("opt"));
    }

    @Test
    public void testGetOptionValues_Char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValue_String_String() {
        CommandLine commandLine = new CommandLine();
        assertEquals("defaultValue", commandLine.getOptionValue("opt", "defaultValue"));
    }

    @Test
    public void testGetOptionValue_Char_String() {
        CommandLine commandLine = new CommandLine();
        assertEquals("defaultValue", commandLine.getOptionValue('o', "defaultValue"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[] {}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        assertEquals(new LinkedList<String>(), commandLine.getArgList());
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("arg");
        assertEquals(1, commandLine.getArgs().length);
        assertEquals("arg", commandLine.getArgs()[0]);
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("opt", "option", true, "Option");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().length);
        assertEquals(option, commandLine.getOptions()[0]);
    }
  
    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        Iterator iterator = commandLine.iterator();
        assertFalse(iterator.hasNext());
    }
}