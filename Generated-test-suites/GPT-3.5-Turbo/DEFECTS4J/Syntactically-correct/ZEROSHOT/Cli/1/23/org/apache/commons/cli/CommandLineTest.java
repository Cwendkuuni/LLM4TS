import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @After
    public void tearDown() {
        commandLine = null;
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption("opt"));
    }

    @Test
    public void testHasOption_char() {
        assertFalse(commandLine.hasOption('a'));
    }

    @Test
    public void testGetOptionObject() {
        assertNull(commandLine.getOptionObject("opt"));
    }

    @Test
    public void testGetOptionObject_char() {
        assertNull(commandLine.getOptionObject('a'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(commandLine.getOptionValue("opt"));
    }

    @Test
    public void testGetOptionValue_char() {
        assertNull(commandLine.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(commandLine.getOptionValues("opt"));
    }

    @Test
    public void testGetOptionValues_char() {
        assertNull(commandLine.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValue_defaultValue() {
        assertEquals("default", commandLine.getOptionValue("opt", "default"));
    }

    @Test
    public void testGetOptionValue_char_defaultValue() {
        assertEquals("default", commandLine.getOptionValue('a', "default"));
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
        commandLine.addArg("arg");
        String[] args = commandLine.getArgs();
        assertNotNull(args);
        assertEquals(1, args.length);
        assertEquals("arg", args[0]);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option", false, "Option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("opt"));
        assertEquals("option", commandLine.getOptionValue("opt"));
    }

    @Test
    public void testIterator() {
        Option option1 = new Option("opt1", "option1", false, "Option1");
        Option option2 = new Option("opt2", "option2", true, "Option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        int count = 0;
        for (Object obj : commandLine) {
            assertTrue(obj instanceof Option);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("opt1", "option1", false, "Option1");
        Option option2 = new Option("opt2", "option2", true, "Option2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option[] options = commandLine.getOptions();
        assertNotNull(options);
        assertEquals(2, options.length);
        assertArrayEquals(new Option[]{option1, option2}, options);
    }
}

