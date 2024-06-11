import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("foo"));
        
        Option option = new Option("f", "foo", false, "foo option");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption("foo"));
    }
    
    @Test
    public void testHasOption_char() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption('f'));
        
        Option option = new Option("f", "foo", false, "foo option");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption('f'));
    }
    
    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject("foo"));
        
        Option option = new Option("f", "foo", true, "foo option");
        option.setType(Integer.class);
        option.addValue("10");
        commandLine.addOption(option);
        
        assertEquals(10, commandLine.getOptionObject("foo"));
    }
    
    @Test
    public void testGetOptionObject_char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionObject('f'));
        
        Option option = new Option("f", "foo", true, "foo option");
        option.setType(Integer.class);
        option.addValue("10");
        commandLine.addOption(option);
        
        assertEquals(10, commandLine.getOptionObject('f'));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("foo"));
        
        Option option = new Option("f", "foo", true, "foo option");
        option.addValue("value");
        commandLine.addOption(option);
        
        assertEquals("value", commandLine.getOptionValue("foo"));
    }
    
    @Test
    public void testGetOptionValue_char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue('f'));
        
        Option option = new Option("f", "foo", true, "foo option");
        option.addValue("value");
        commandLine.addOption(option);
        
        assertEquals("value", commandLine.getOptionValue('f'));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues("foo"));
        
        Option option = new Option("f", "foo", true, "foo option");
        option.addValue("value1");
        option.addValue("value2");
        commandLine.addOption(option);
        
        assertArrayEquals(new String[] {"value1", "value2"}, commandLine.getOptionValues("foo"));
    }
    
    @Test
    public void testGetOptionValues_char() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValues('f'));
        
        Option option = new Option("f", "foo", true, "foo option");
        option.addValue("value1");
        option.addValue("value2");
        commandLine.addOption(option);
        
        assertArrayEquals(new String[] {"value1", "value2"}, commandLine.getOptionValues('f'));
    }
    
    @Test
    public void testGetOptionValue_defaultValue() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue("foo", "default"));
        
        Option option = new Option("f", "foo", true, "foo option");
        option.addValue("value");
        commandLine.addOption(option);
        
        assertEquals("value", commandLine.getOptionValue("foo", "default"));
    }
    
    @Test
    public void testGetOptionValue_char_defaultValue() {
        CommandLine commandLine = new CommandLine();
        assertEquals("default", commandLine.getOptionValue('f', "default"));
        
        Option option = new Option("f", "foo", true, "foo option");
        option.addValue("value");
        commandLine.addOption(option);
        
        assertEquals("value", commandLine.getOptionValue('f', "default"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        assertArrayEquals(new String[0], commandLine.getArgs());
        
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        assertArrayEquals(new String[] {"arg1", "arg2"}, commandLine.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        CommandLine commandLine = new CommandLine();
        assertEquals(0, commandLine.getArgList().size());
        
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        assertEquals(2, commandLine.getArgList().size());
        assertEquals("arg1", commandLine.getArgList().get(0));
        assertEquals("arg2", commandLine.getArgList().get(1));
    }
    
    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        
        assertFalse(commandLine.iterator().hasNext());
        
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", true, "bar option");
        
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
        CommandLine commandLine = new CommandLine();
        
        assertArrayEquals(new Option[0], commandLine.getOptions());
        
        Option option1 = new Option("f", "foo", false, "foo option");
        Option option2 = new Option("b", "bar", true, "bar option");
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        Option[] options = commandLine.getOptions();
        
        assertEquals(2, options.length);
        assertEquals(option1, options[0]);
        assertEquals(option2, options[1]);
    }
}