import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CommandLineTest {
    
    @Test
    public void testHasOption_String() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", false, "File option"));
        cmdLine.addOption(new Option("r", "rebuild", false, "Rebuild option"));

        assertTrue(cmdLine.hasOption("f"));
        assertTrue(cmdLine.hasOption("file"));
        assertFalse(cmdLine.hasOption("f1"));
        assertFalse(cmdLine.hasOption("something"));
        
    }
    
    @Test
    public void testHasOption_Character() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", false, "File option"));
        cmdLine.addOption(new Option("r", "rebuild", false, "Rebuild option"));

        assertTrue(cmdLine.hasOption('f'));
        assertTrue(cmdLine.hasOption('r'));
        assertFalse(cmdLine.hasOption('s'));
        assertFalse(cmdLine.hasOption('1'));
        
    }
    
    @Test
    public void testGetOptionObject_String() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", true, "File option"));

        cmdLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", cmdLine.getOptionObject("f"));

        cmdLine.addOptionValue("file", "test2.txt");
        assertEquals("test2.txt", cmdLine.getOptionObject("file"));
        
        assertNull(cmdLine.getOptionObject("r"));
        assertNull(cmdLine.getOptionObject("random"));
    }
    
    @Test
    public void testGetOptionObject_Character() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", true, "File option"));

        cmdLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", cmdLine.getOptionObject('f'));

        cmdLine.addOptionValue("file", "test2.txt");
        assertEquals("test2.txt", cmdLine.getOptionObject('f'));
        
        assertNull(cmdLine.getOptionObject('r'));
        assertNull(cmdLine.getOptionObject('s'));
    }
    
    @Test
    public void testGetOptionValue_String() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", true, "File option"));

        cmdLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", cmdLine.getOptionValue("f"));

        cmdLine.addOptionValue("file", "test2.txt");
        assertEquals("test2.txt", cmdLine.getOptionValue("file"));
        
        assertNull(cmdLine.getOptionValue("r"));
        assertNull(cmdLine.getOptionValue("random"));
    }
    
    @Test
    public void testGetOptionValue_Character() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", true, "File option"));

        cmdLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", cmdLine.getOptionValue('f'));

        cmdLine.addOptionValue("file", "test2.txt");
        assertEquals("test2.txt", cmdLine.getOptionValue('f'));
        
        assertNull(cmdLine.getOptionValue('r'));
        assertNull(cmdLine.getOptionValue('s'));
    }
    
    @Test
    public void testGetOptionValues_String() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", true, "File option"));
        cmdLine.addOption(new Option("n", "name", true, "Name option"));

        cmdLine.addOptionValue("f", "test.txt");
        cmdLine.addOptionValue("f", "test2.txt");
        String[] expected1 = {"test.txt", "test2.txt"};
        assertArrayEquals(expected1, cmdLine.getOptionValues("f"));

        cmdLine.addOptionValue("name", "John");
        String[] expected2 = {"John"};
        assertArrayEquals(expected2, cmdLine.getOptionValues("n"));
        
        assertNull(cmdLine.getOptionValues("r"));
        assertNull(cmdLine.getOptionValues("random"));
    }
    
    @Test
    public void testGetOptionValues_Character() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", true, "File option"));
        cmdLine.addOption(new Option("n", "name", true, "Name option"));

        cmdLine.addOptionValue("f", "test.txt");
        cmdLine.addOptionValue("f", "test2.txt");
        String[] expected1 = {"test.txt", "test2.txt"};
        assertArrayEquals(expected1, cmdLine.getOptionValues('f'));

        cmdLine.addOptionValue("name", "John");
        String[] expected2 = {"John"};
        assertArrayEquals(expected2, cmdLine.getOptionValues('n'));
        
        assertNull(cmdLine.getOptionValues('r'));
        assertNull(cmdLine.getOptionValues('s'));
    }
    
    @Test
    public void testGetOptionValue_DefaultValue() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", true, "File option"));

        cmdLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", cmdLine.getOptionValue("f", "default.txt"));

        assertNull(cmdLine.getOptionValue("r", "default.txt"));
    }
    
    @Test
    public void testGetOptionValue_DefaultValue_Character() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addOption(new Option("f", "file", true, "File option"));

        cmdLine.addOptionValue("f", "test.txt");
        assertEquals("test.txt", cmdLine.getOptionValue('f', "default.txt"));

        assertNull(cmdLine.getOptionValue('r', "default.txt"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        String[] expected = {"arg1", "arg2"};
        assertArrayEquals(expected, cmdLine.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmdLine = new CommandLine();
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");
        assertEquals(expected, cmdLine.getArgList());
    }
    
    @Test
    public void testIterator() {
        CommandLine cmdLine = new CommandLine();
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("r", "rebuild", false, "Rebuild option");
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
        CommandLine cmdLine = new CommandLine();
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("r", "rebuild", false, "Rebuild option");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        
        Option[] expected = {option1, option2};
        assertArrayEquals(expected, cmdLine.getOptions());
    }
    
}