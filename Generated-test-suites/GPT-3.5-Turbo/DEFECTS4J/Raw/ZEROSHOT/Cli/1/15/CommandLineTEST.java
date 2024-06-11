import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "test"));
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "test"));
        assertTrue(commandLine.hasOption('a'));
        assertFalse(commandLine.hasOption('b'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "test"));
        commandLine.addArg("testArg1");
        assertEquals("testArg1", commandLine.getOptionObject("a"));
        assertNull(commandLine.getOptionObject("b"));
    }

    @Test
    public void testGetOptionObject_char() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "test"));
        commandLine.addArg("testArg1");
        assertEquals("testArg1", commandLine.getOptionObject('a'));
        assertNull(commandLine.getOptionObject('b'));
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "test"));
        commandLine.addArg("testArg1");
        assertEquals("testArg1", commandLine.getOptionValue("a"));
        assertNull(commandLine.getOptionValue("b"));
    }

    @Test
    public void testGetOptionValue_char() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "test"));
        commandLine.addArg("testArg1");
        assertEquals("testArg1", commandLine.getOptionValue('a'));
        assertNull(commandLine.getOptionValue('b'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "test");
        option.addOptionValue("value1");
        option.addOptionValue("value2");
        commandLine.addOption(option);
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("a"));
        assertNull(commandLine.getOptionValues("b"));
    }

    @Test
    public void testGetOptionValues_char() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "test");
        option.addOptionValue("value1");
        option.addOptionValue("value2");
        commandLine.addOption(option);
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues('a'));
        assertNull(commandLine.getOptionValues('b'));
    }

    @Test
    public void testGetOptionValue_String_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "test"));
        commandLine.addArg("testArg1");
        assertEquals("testArg1", commandLine.getOptionValue("a", "defaultValue"));
        assertEquals("defaultValue", commandLine.getOptionValue("b", "defaultValue"));
    }

    @Test
    public void testGetOptionValue_char_String() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "test"));
        commandLine.addArg("testArg1");
        assertEquals("testArg1", commandLine.getOptionValue('a', "defaultValue"));
        assertEquals("defaultValue", commandLine.getOptionValue('b', "defaultValue"));
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("testArg1");
        commandLine.addArg("testArg2");
        assertArrayEquals(new String[]{"testArg1", "testArg2"}, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("testArg1");
        commandLine.addArg("testArg2");
        assertEquals(2, commandLine.getArgList().size());
        assertTrue(commandLine.getArgList().contains("testArg1"));
        assertTrue(commandLine.getArgList().contains("testArg2"));
    }

    @Test
    public void testAddArg() {
        CommandLine commandLine = new CommandLine();
        commandLine.addArg("testArg1");
        commandLine.addArg("testArg2");
        assertEquals(2, commandLine.getArgs().length);
        assertEquals("testArg1", commandLine.getArgList().get(0));
        assertEquals("testArg2", commandLine.getArgList().get(1));
    }

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "test");
        option.addOptionValue("value1");
        option.addOptionValue("value2");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("a"));
        assertArrayEquals(new String[]{"value1", "value2"}, commandLine.getOptionValues("a"));
    }

    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        Option option = new Option("a", "test");
        commandLine.addOption(option);
        assertTrue(commandLine.iterator().hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        Option option1 = new Option("a", "test1");
        Option option2 = new Option("b", "test2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().length);
        assertEquals(option1, commandLine.getOptions()[0]);
        assertEquals(option2, commandLine.getOptions()[1]);
    }
}