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
    }

    @Test
    public void testHasOptionChar() {
        assertFalse(commandLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObject() {
        assertNull(commandLine.getOptionObject("test"));
    }

    @Test
    public void testGetOptionObjectChar() {
        assertNull(commandLine.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(commandLine.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValueChar() {
        assertNull(commandLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(commandLine.getOptionValues("test"));
    }

    @Test
    public void testGetOptionValuesChar() {
        assertNull(commandLine.getOptionValues('t'));
    }

    @Test
    public void testGetOptionValueDefault() {
        assertEquals("default", commandLine.getOptionValue("test", "default"));
    }

    @Test
    public void testGetOptionValueCharDefault() {
        assertEquals("default", commandLine.getOptionValue('t', "default"));
    }

    @Test
    public void testGetArgs() {
        String[] args = commandLine.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    @Test
    public void testGetArgList() {
        assertNotNull(commandLine.getArgList());
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("test");
        String[] args = commandLine.getArgs();
        assertNotNull(args);
        assertEquals(1, args.length);
        assertEquals("test", args[0]);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption('t'));
    }

    @Test
    public void testIterator() {
        assertFalse(commandLine.iterator().hasNext());
    }

    @Test
    public void testGetOptions() {
        assertNotNull(commandLine.getOptions());
        assertEquals(0, commandLine.getOptions().length);
    }

}