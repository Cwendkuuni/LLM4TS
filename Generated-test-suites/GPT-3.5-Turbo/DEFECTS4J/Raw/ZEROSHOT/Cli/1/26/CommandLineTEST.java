import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOptionWithString() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertFalse(cmdLine.hasOption("testOption"));
        
        // Test when option is set
        cmdLine.addOption(new Option("testOption"));
        assertTrue(cmdLine.hasOption("testOption"));
    }

    @Test
    public void testHasOptionWithChar() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertFalse(cmdLine.hasOption('t'));
        
        // Test when option is set
        cmdLine.addOption(new Option('t'));
        assertTrue(cmdLine.hasOption('t'));
    }

    @Test
    public void testGetOptionObjectWithString() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertNull(cmdLine.getOptionObject("testOption"));
        
        // Test when option is set
        Option option = new Option("testOption");
        option.setType(Integer.class);
        option.setValue("123");
        cmdLine.addOption(option);
        
        assertEquals(Integer.valueOf(123), cmdLine.getOptionObject("testOption"));
    }

    @Test
    public void testGetOptionObjectWithChar() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertNull(cmdLine.getOptionObject('t'));
        
        // Test when option is set
        Option option = new Option('t');
        option.setType(String.class);
        option.setValue("test");
        cmdLine.addOption(option);
        
        assertEquals("test", cmdLine.getOptionObject('t'));
    }

    @Test
    public void testGetOptionValueWithString() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertNull(cmdLine.getOptionValue("testOption"));
        
        // Test when option is set
        Option option = new Option("testOption");
        option.addValue("value");
        cmdLine.addOption(option);
        
        assertEquals("value", cmdLine.getOptionValue("testOption"));
    }

    @Test
    public void testGetOptionValueWithChar() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertNull(cmdLine.getOptionValue('t'));
        
        // Test when option is set
        Option option = new Option('t');
        option.addValue("value");
        cmdLine.addOption(option);
        
        assertEquals("value", cmdLine.getOptionValue('t'));
    }

    @Test
    public void testGetOptionValuesWithString() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertNull(cmdLine.getOptionValues("testOption"));
        
        // Test when option is set
        Option option = new Option("testOption");
        option.addValue("value1");
        option.addValue("value2");
        cmdLine.addOption(option);
        
        assertArrayEquals(new String[]{"value1", "value2"}, cmdLine.getOptionValues("testOption"));
    }

    @Test
    public void testGetOptionValuesWithChar() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertNull(cmdLine.getOptionValues('t'));
        
        // Test when option is set
        Option option = new Option('t');
        option.addValue("value1");
        option.addValue("value2");
        cmdLine.addOption(option);
        
        assertArrayEquals(new String[]{"value1", "value2"}, cmdLine.getOptionValues('t'));
    }

    @Test
    public void testGetOptionValueWithDefaultWithString() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertEquals("default", cmdLine.getOptionValue("testOption", "default"));
        
        // Test when option is set
        Option option = new Option("testOption");
        option.addValue("value");
        cmdLine.addOption(option);
        
        assertEquals("value", cmdLine.getOptionValue("testOption", "default"));
    }

    @Test
    public void testGetOptionValueWithDefaultWithChar() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when option is not set
        assertEquals("default", cmdLine.getOptionValue('t', "default"));
        
        // Test when option is set
        Option option = new Option('t');
        option.addValue("value");
        cmdLine.addOption(option);
        
        assertEquals("value", cmdLine.getOptionValue('t', "default"));
    }

    @Test
    public void testGetArgs() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when there are no args
        assertArrayEquals(new String[]{}, cmdLine.getArgs());
        
        // Test when there are args
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmdLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when there are no args
        assertEquals(0, cmdLine.getArgList().size());
        
        // Test when there are args
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        
        assertEquals(2, cmdLine.getArgList().size());
        assertEquals("arg1", cmdLine.getArgList().get(0));
        assertEquals("arg2", cmdLine.getArgList().get(1));
    }

    @Test
    public void testAddArg() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test adding an arg
        cmdLine.addArg("arg1");
        
        assertEquals(1, cmdLine.getArgList().size());
        assertEquals("arg1", cmdLine.getArgList().get(0));
    }

    @Test
    public void testAddOption() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test adding an option
        Option option = new Option("testOption");
        cmdLine.addOption(option);
        
        assertTrue(cmdLine.hasOption("testOption"));
    }

    @Test
    public void testIterator() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when there are no options
        assertFalse(cmdLine.iterator().hasNext());
        
        // Test when there are options
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        
        Iterator<Option> iterator = cmdLine.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        // Create a command line object
        CommandLine cmdLine = new CommandLine();
        
        // Test when there are no options
        assertArrayEquals(new Option[]{}, cmdLine.getOptions());
        
        // Test when there are options
        Option option1 = new Option("testOption1");
        Option option2 = new Option("testOption2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        
        assertArrayEquals(new Option[]{option1, option2}, cmdLine.getOptions());
    }
}