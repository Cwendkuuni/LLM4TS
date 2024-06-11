import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
        Option opt3 = new Option("c");
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
    }
    
    @Test
    public void testHasOption_char() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option('a');
        Option opt2 = new Option('b');
        Option opt3 = new Option('c');
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        assertTrue(cmd.hasOption('a'));
        assertTrue(cmd.hasOption('b'));
        assertTrue(cmd.hasOption('c'));
        assertFalse(cmd.hasOption('d'));
    }
    
    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
        Option opt3 = new Option("c");
        
        opt1.setType("int");
        opt2.setType("double");
        opt3.setType("boolean");
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        cmd.addArg("10");
        cmd.addArg("3.14");
        cmd.addArg("true");
        
        assertEquals(Integer.valueOf(10), cmd.getOptionObject("a"));
        assertEquals(Double.valueOf(3.14), cmd.getOptionObject("b"));
        assertEquals(Boolean.valueOf(true), cmd.getOptionObject("c"));
    }
    
    @Test
    public void testGetOptionObject_char() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option('a');
        Option opt2 = new Option('b');
        Option opt3 = new Option('c');
        
        opt1.setType("int");
        opt2.setType("double");
        opt3.setType("boolean");
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        cmd.addArg("10");
        cmd.addArg("3.14");
        cmd.addArg("true");
        
        assertEquals(Integer.valueOf(10), cmd.getOptionObject('a'));
        assertEquals(Double.valueOf(3.14), cmd.getOptionObject('b'));
        assertEquals(Boolean.valueOf(true), cmd.getOptionObject('c'));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
        Option opt3 = new Option("c");
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        cmd.addArg("value1");
        cmd.addArg("value2");
        cmd.addArg("value3");
        
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals("value2", cmd.getOptionValue("b"));
        assertEquals("value3", cmd.getOptionValue("c"));
        assertNull(cmd.getOptionValue("d"));
    }
    
    @Test
    public void testGetOptionValue_char() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option('a');
        Option opt2 = new Option('b');
        Option opt3 = new Option('c');
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        cmd.addArg("value1");
        cmd.addArg("value2");
        cmd.addArg("value3");
        
        assertEquals("value1", cmd.getOptionValue('a'));
        assertEquals("value2", cmd.getOptionValue('b'));
        assertEquals("value3", cmd.getOptionValue('c'));
        assertNull(cmd.getOptionValue('d'));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
        Option opt3 = new Option("c");
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        String[] values1 = {"value1", "value2"};
        String[] values2 = {"value3", "value4"};
        String[] values3 = {"value5", "value6"};
        
        opt1.setValues(values1);
        opt2.setValues(values2);
        opt3.setValues(values3);
        
        assertArrayEquals(values1, cmd.getOptionValues("a"));
        assertArrayEquals(values2, cmd.getOptionValues("b"));
        assertArrayEquals(values3, cmd.getOptionValues("c"));
        assertNull(cmd.getOptionValues("d"));
    }
    
    @Test
    public void testGetOptionValues_char() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option('a');
        Option opt2 = new Option('b');
        Option opt3 = new Option('c');
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        String[] values1 = {"value1", "value2"};
        String[] values2 = {"value3", "value4"};
        String[] values3 = {"value5", "value6"};
        
        opt1.setValues(values1);
        opt2.setValues(values2);
        opt3.setValues(values3);
        
        assertArrayEquals(values1, cmd.getOptionValues('a'));
        assertArrayEquals(values2, cmd.getOptionValues('b'));
        assertArrayEquals(values3, cmd.getOptionValues('c'));
        assertNull(cmd.getOptionValues('d'));
    }
    
    @Test
    public void testGetOptionValue_default() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        
        cmd.addOption(opt1);
        
        assertNull(cmd.getOptionValue("a", "default"));
        assertEquals("default", cmd.getOptionValue("b", "default"));
    }
    
    @Test
    public void testGetOptionValue_char_default() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option('a');
        
        cmd.addOption(opt1);
        
        assertNull(cmd.getOptionValue('a', "default"));
        assertEquals("default", cmd.getOptionValue('b', "default"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");
        
        String[] args = {"arg1", "arg2", "arg3"};
        
        assertArrayEquals(args, cmd.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");
        
        List<String> argList = new LinkedList<>();
        argList.add("arg1");
        argList.add("arg2");
        argList.add("arg3");
        
        assertEquals(argList, cmd.getArgList());
    }
    
    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");
        
        String[] args = {"arg1", "arg2", "arg3"};
        
        assertArrayEquals(args, cmd.getArgs());
    }
    
    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
        Option opt3 = new Option("c");
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertFalse(cmd.hasOption("d"));
        
        assertEquals(opt1, cmd.getOptionObject("a"));
        assertEquals(opt2, cmd.getOptionObject("b"));
        assertEquals(opt3, cmd.getOptionObject("c"));
    }
    
    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
        Option opt3 = new Option("c");
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        Iterator<Option> iter = cmd.iterator();
        
        assertTrue(iter.hasNext());
        assertEquals(opt1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(opt2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(opt3, iter.next());
        assertFalse(iter.hasNext());
    }
    
    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
        Option opt3 = new Option("c");
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addOption(opt3);
        
        Option[] options = {opt1, opt2, opt3};
        
        assertArrayEquals(options, cmd.getOptions());
    }
}