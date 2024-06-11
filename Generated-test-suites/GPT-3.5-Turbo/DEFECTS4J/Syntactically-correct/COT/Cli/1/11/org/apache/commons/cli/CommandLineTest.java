import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        
        assertFalse(commandLine.hasOption("a"));
        
        commandLine.addOption(new Option("a", false, "test"));
        
        assertTrue(commandLine.hasOption("a"));
    }
    
    @Test
    public void testHasOption_char() {
        CommandLine commandLine = new CommandLine();
        
        assertFalse(commandLine.hasOption('a'));
        
        commandLine.addOption(new Option("a", false, "test"));
        
        assertTrue(commandLine.hasOption('a'));
    }
    
    @Test
    public void testGetOptionObject() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionObject("a"));
        
        commandLine.addOption(new Option("a", "arg", false, "test"));
        
        assertNull(commandLine.getOptionObject("a"));
        
        commandLine.addArg("arg");
        
        assertNotNull(commandLine.getOptionObject("a"));
    }
    
    @Test
    public void testGetOptionObject_char() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionObject('a'));
        
        commandLine.addOption(new Option("a", "arg", false, "test"));
        
        assertNull(commandLine.getOptionObject('a'));
        
        commandLine.addArg("arg");
        
        assertNotNull(commandLine.getOptionObject('a'));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionValue("a"));
        
        commandLine.addOption(new Option("a", "arg", false, "test"));
        
        assertNull(commandLine.getOptionValue("a"));
        
        commandLine.addArg("arg");
        
        assertEquals("arg", commandLine.getOptionValue("a"));
    }
    
    @Test
    public void testGetOptionValue_char() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionValue('a'));
        
        commandLine.addOption(new Option("a", "arg", false, "test"));
        
        assertNull(commandLine.getOptionValue('a'));
        
        commandLine.addArg("arg");
        
        assertEquals("arg", commandLine.getOptionValue('a'));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionValues("a"));
        
        commandLine.addOption(new Option("a", "args", true, "test"));
        
        assertNull(commandLine.getOptionValues("a"));
        
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        assertArrayEquals(new String[] {"arg1", "arg2"}, commandLine.getOptionValues("a"));
    }
    
    @Test
    public void testGetOptionValues_char() {
        CommandLine commandLine = new CommandLine();
        
        assertNull(commandLine.getOptionValues('a'));
        
        commandLine.addOption(new Option("a", "args", true, "test"));
        
        assertNull(commandLine.getOptionValues('a'));
        
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        
        assertArrayEquals(new String[] {"arg1", "arg2"}, commandLine.getOptionValues('a'));
    }
    
    @Test
    public void testGetOptionValue_withDefaultValue() {
        CommandLine commandLine = new CommandLine();
        
        assertEquals("default", commandLine.getOptionValue("a", "default"));
        
        commandLine.addOption(new Option("a", "arg", false, "test"));
        
        assertEquals("default", commandLine.getOptionValue("a", "default"));
        
        commandLine.addArg("arg");
        
        assertEquals("arg", commandLine.getOptionValue("a", "default"));
    }
    
    @Test
    public void testGetOptionValue_char_withDefaultValue() {
        CommandLine commandLine = new CommandLine();
        
        assertEquals("default", commandLine.getOptionValue('a', "default"));
        
        commandLine.addOption(new Option("a", "arg", false, "test"));
        
        assertEquals("default", commandLine.getOptionValue('a', "default"));
        
        commandLine.addArg("arg");
        
        assertEquals("arg", commandLine.getOptionValue('a', "default"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        
        assertArrayEquals(new String[] {}, commandLine.getArgs());
        
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
    }
    
    @Test
    public void testIterator() {
        CommandLine commandLine = new CommandLine();
        
        assertFalse(commandLine.iterator().hasNext());
        
        Option option1 = new Option("a", false, "test1");
        Option option2 = new Option("b", "arg", false, "test2");
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        Iterator iterator = commandLine.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetOptions() {
        CommandLine commandLine = new CommandLine();
        
        assertArrayEquals(new Option[] {}, commandLine.getOptions());
        
        Option option1 = new Option("a", false, "test1");
        Option option2 = new Option("b", "arg", false, "test2");
        
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        
        assertArrayEquals(new Option[] {option1, option2}, commandLine.getOptions());
    }
}