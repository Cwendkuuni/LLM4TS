import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption("opt"));
        assertFalse(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        assertNull(commandLine.getOptionObject("opt"));
        assertNull(commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(commandLine.getOptionValue("opt"));
        assertNull(commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(commandLine.getOptionValues("opt"));
        assertNull(commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueWithDefaultValue() {
        assertEquals("defaultValue", commandLine.getOptionValue("opt", "defaultValue"));
        assertEquals("defaultValue", commandLine.getOptionValue('o', "defaultValue"));
    }

    @Test
    public void testGetArgs() {
        String[] args = commandLine.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    @Test
    public void testGetArgList() {
        List argList = commandLine.getArgList();
        assertNotNull(argList);
        assertTrue(argList.isEmpty());
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("arg");
        String[] args = commandLine.getArgs();
        assertNotNull(args);
        assertEquals(1, args.length);
        assertEquals("arg", args[0]);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", true, "Option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("opt"));
        assertTrue(commandLine.hasOption('o'));
        assertEquals(option, commandLine.getOptionObject("opt"));
        assertEquals(option, commandLine.getOptionObject('o'));
        assertEquals("defaultValue", commandLine.getOptionValue("opt", "defaultValue"));
        assertEquals("defaultValue", commandLine.getOptionValue('o', "defaultValue"));
        assertEquals("defaultValue", commandLine.getOptionValues("opt", "defaultValue")[0]);
        assertEquals("defaultValue", commandLine.getOptionValues('o', "defaultValue")[0]);
        assertEquals(option, commandLine.getOptions()[0]);
        assertEquals(option, commandLine.iterator().next());
    }
}