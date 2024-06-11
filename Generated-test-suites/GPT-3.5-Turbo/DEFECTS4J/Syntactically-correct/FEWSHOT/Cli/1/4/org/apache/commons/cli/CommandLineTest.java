import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CommandLineTest {
  
    @Test
    public void testHasOptionString() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", false, "the file option"));
        cmd.addOption(new Option("v", "verbose", false, "the verbose option"));
        
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("file"));
        assertFalse(cmd.hasOption("v"));
        assertFalse(cmd.hasOption("verbose"));
    }
    
    @Test
    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", false, "the file option"));
        cmd.addOption(new Option("v", "verbose", false, "the verbose option"));
        
        assertTrue(cmd.hasOption('f'));
        assertFalse(cmd.hasOption('v'));
    }
    
    @Test
    public void testGetOptionObjectString() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", true, "the file option"));
        cmd.addOption(new Option("i", "input", false, "the input option"));
        
        cmd.addOptionValue("f", "test.txt");
        cmd.addOptionValue("i", "input.txt");
        
        assertEquals("test.txt", cmd.getOptionObject("f"));
        assertNull(cmd.getOptionObject("i"));
    }
    
    @Test
    public void testGetOptionObjectChar() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", true, "the file option"));
        cmd.addOption(new Option("i", "input", false, "the input option"));
        
        cmd.addOptionValue("f", "test.txt");
        cmd.addOptionValue("i", "input.txt");
        
        assertEquals("test.txt", cmd.getOptionObject('f'));
        assertNull(cmd.getOptionObject('i'));
    }
    
    @Test
    public void testGetOptionValueString() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", true, "the file option"));
        cmd.addOption(new Option("i", "input", false, "the input option"));
        
        cmd.addOptionValue("f", "test.txt");
        cmd.addOptionValue("i", "input.txt");
        
        assertEquals("test.txt", cmd.getOptionValue("f"));
        assertNull(cmd.getOptionValue("i"));
    }
    
    @Test
    public void testGetOptionValueChar() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", true, "the file option"));
        cmd.addOption(new Option("i", "input", false, "the input option"));
        
        cmd.addOptionValue("f", "test.txt");
        cmd.addOptionValue("i", "input.txt");
        
        assertEquals("test.txt", cmd.getOptionValue('f'));
        assertNull(cmd.getOptionValue('i'));
    }
    
    @Test
    public void testGetOptionValuesString() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", true, "the file option"));
        cmd.addOption(new Option("m", "mode", true, "the mode option"));
        
        cmd.addOptionValue("f", "test1.txt");
        cmd.addOptionValue("f", "test2.txt");
        cmd.addOptionValue("m", "mode1");
        
        assertArrayEquals(new String[] {"test1.txt", "test2.txt"}, cmd.getOptionValues("f"));
        assertArrayEquals(new String[] {"mode1"}, cmd.getOptionValues("m"));
        assertNull(cmd.getOptionValues("invalid"));
    }
    
    @Test
    public void testGetOptionValuesChar() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", true, "the file option"));
        cmd.addOption(new Option("m", "mode", true, "the mode option"));
        
        cmd.addOptionValue("f", "test1.txt");
        cmd.addOptionValue("f", "test2.txt");
        cmd.addOptionValue("m", "mode1");
        
        assertArrayEquals(new String[] {"test1.txt", "test2.txt"}, cmd.getOptionValues('f'));
        assertArrayEquals(new String[] {"mode1"}, cmd.getOptionValues('m'));
        assertNull(cmd.getOptionValues('i'));
    }
    
    @Test
    public void testGetOptionValueDefaultString() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", true, "the file option"));
        cmd.addOption(new Option("i", "input", false, "the input option"));
        
        cmd.addOptionValue("f", "test.txt");
        cmd.addOptionValue("i", "input.txt");
        
        assertEquals("test.txt", cmd.getOptionValue("f", "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue("i", "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue("invalid", "default.txt"));
    }
    
    @Test
    public void testGetOptionValueDefaultChar() {
        CommandLine cmd = new CommandLine();
        
        cmd.addOption(new Option("f", "file", true, "the file option"));
        cmd.addOption(new Option("i", "input", false, "the input option"));
        
        cmd.addOptionValue("f", "test.txt");
        cmd.addOptionValue("i", "input.txt");
        
        assertEquals("test.txt", cmd.getOptionValue('f', "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue('i', "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue('a', "default.txt"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        assertArrayEquals(new String[] {"arg1", "arg2"}, cmd.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        assertEquals(Arrays.asList("arg1", "arg2"), cmd.getArgList());
    }
    
    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        
        Option option1 = new Option("f", "file", true, "the file option");
        Option option2 = new Option("m", "mode", true, "the mode option");
        
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        Iterator<Option> it = cmd.iterator();
        assertEquals(option1, it.next());
        assertEquals(option2, it.next());
        assertFalse(it.hasNext());
    }
    
    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        
        Option option1 = new Option("f", "file", true, "the file option");
        Option option2 = new Option("m", "mode", true, "the mode option");
        
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        assertArrayEquals(new Option[] {option1, option2}, cmd.getOptions());
    }
}