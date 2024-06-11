import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_withString() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", false, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
    }
    
    @Test
    public void testHasOption_withChar() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", false, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        
        assertTrue(cmd.hasOption('a'));
        assertFalse(cmd.hasOption('b'));
        assertFalse(cmd.hasOption('c'));
    }
    
    @Test
    public void testGetOptionValue_withString() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", true, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addArg("value1");
        
        assertEquals("value1", cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("c"));
    }
    
    @Test
    public void testGetOptionValue_withChar() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", true, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addArg("value1");
        
        assertEquals("value1", cmd.getOptionValue('a'));
        assertNull(cmd.getOptionValue('b'));
        assertNull(cmd.getOptionValue('c'));
    }
    
    @Test
    public void testGetOptionValues_withString() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", true, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addArg("value1");
        cmd.addArg("value2");
        
        assertNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));
        assertNull(cmd.getOptionValues("c"));
    }
    
    @Test
    public void testGetOptionValues_withChar() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", true, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        cmd.addArg("value1");
        cmd.addArg("value2");
        
        assertNull(cmd.getOptionValues('a'));
        assertNull(cmd.getOptionValues('b'));
        assertNull(cmd.getOptionValues('c'));
    }
    
    @Test
    public void testGetOptionValue_withDefault_withString() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", true, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        
        assertEquals("defaultValue", cmd.getOptionValue("a", "defaultValue"));
        assertEquals("defaultValue", cmd.getOptionValue("b", "defaultValue"));
        assertEquals("defaultValue", cmd.getOptionValue("c", "defaultValue"));
    }
    
    @Test
    public void testGetOptionValue_withDefault_withChar() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", true, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        
        assertEquals("defaultValue", cmd.getOptionValue('a', "defaultValue"));
        assertEquals("defaultValue", cmd.getOptionValue('b', "defaultValue"));
        assertEquals("defaultValue", cmd.getOptionValue('c', "defaultValue"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        String[] args = cmd.getArgs();
        
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        List<String> argList = cmd.getArgList();
        
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }
    
    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", true, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        
        Iterator<Option> iterator = cmd.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(opt1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(opt2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", true, "option a");
        Option opt2 = new Option("b", true, "option b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        
        Option[] options = cmd.getOptions();
        
        assertEquals(2, options.length);
        assertEquals(opt1, options[0]);
        assertEquals(opt2, options[1]);
    }
}