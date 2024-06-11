
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
    }
    
    @Test
    public void testHasOption_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        
        assertTrue(cmd.hasOption('a'));
        assertTrue(cmd.hasOption('b'));
        assertFalse(cmd.hasOption('c'));
    }
    
    @Test
    public void testGetOptionObject_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a").setType(String.class));
        cmd.addOption(new Option("b").setType(Integer.class));
        
        assertNull(cmd.getOptionObject("a"));
        assertNull(cmd.getOptionObject("b"));
        
        cmd.addArg("5");
        assertEquals("5", cmd.getOptionObject("a"));
        
        cmd.addArg("10");
        assertEquals(10, cmd.getOptionObject("b"));
    }
    
    @Test
    public void testGetOptionObject_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a").setType(String.class));
        cmd.addOption(new Option("b").setType(Integer.class));
        
        assertNull(cmd.getOptionObject('a'));
        assertNull(cmd.getOptionObject('b'));
        
        cmd.addArg("5");
        assertEquals("5", cmd.getOptionObject('a'));
        
        cmd.addArg("10");
        assertEquals(10, cmd.getOptionObject('b'));
    }
    
    @Test
    public void testGetOptionValue_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
        
        cmd.addArg("5");
        assertEquals("5", cmd.getOptionValue("a"));
        
        cmd.addArg("10");
        assertEquals("10", cmd.getOptionValue("b"));
    }
    
    @Test
    public void testGetOptionValue_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        
        assertNull(cmd.getOptionValue('a'));
        assertNull(cmd.getOptionValue('b'));
        
        cmd.addArg("5");
        assertEquals("5", cmd.getOptionValue('a'));
        
        cmd.addArg("10");
        assertEquals("10", cmd.getOptionValue('b'));
    }
    
    @Test
    public void testGetOptionValues_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        
        assertNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));
        
        cmd.addArg("5");
        assertArrayEquals(new String[] {"5"}, cmd.getOptionValues("a"));
        
        cmd.addArg("10");
        assertArrayEquals(new String[] {"5", "10"}, cmd.getOptionValues("b"));
    }
    
    @Test
    public void testGetOptionValues_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        
        assertNull(cmd.getOptionValues('a'));
        assertNull(cmd.getOptionValues('b'));
        
        cmd.addArg("5");
        assertArrayEquals(new String[] {"5"}, cmd.getOptionValues('a'));
        
        cmd.addArg("10");
        assertArrayEquals(new String[] {"5", "10"}, cmd.getOptionValues('b'));
    }
    
    @Test
    public void testGetOptionValue_withDefaultValue_String() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        
        assertEquals("default", cmd.getOptionValue("a", "default"));
        assertEquals("default", cmd.getOptionValue("b", "default"));
        
        cmd.addArg("5");
        assertEquals("5", cmd.getOptionValue("a", "default"));
        
        cmd.addArg("10");
        assertEquals("10", cmd.getOptionValue("b", "default"));
    }
    
    @Test
    public void testGetOptionValue_withDefaultValue_Char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        
        assertEquals("default", cmd.getOptionValue('a', "default"));
        assertEquals("default", cmd.getOptionValue('b', "default"));
        
        cmd.addArg("5");
        assertEquals("5", cmd.getOptionValue('a', "default"));
        
        cmd.addArg("10");
        assertEquals("10", cmd.getOptionValue('b', "default"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("a");
        cmd.addArg("b");
        cmd.addArg("c");
        
        assertArrayEquals(new String[] {"a", "b", "c"}, cmd.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("a");
        cmd.addArg("b");
        cmd.addArg("c");
        
        assertEquals(LinkedList.class, cmd.getArgList().getClass());
        assertEquals(3, cmd.getArgList().size());
        assertTrue(cmd.getArgList().contains("a"));
        assertTrue(cmd.getArgList().contains("b"));
        assertTrue(cmd.getArgList().contains("c"));
    }
    
    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        cmd.addOption(new Option("c"));
        
        Iterator<Option> iterator = cmd.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next().getOpt());
        
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next().getOpt());
        
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next().getOpt());
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a"));
        cmd.addOption(new Option("b"));
        cmd.addOption(new Option("c"));
        
        Option[] options = cmd.getOptions();
        
        assertEquals(3, options.length);
        assertEquals("a", options[0].getOpt());
        assertEquals("b", options[1].getOpt());
        assertEquals("c", options[2].getOpt());
    }
}
