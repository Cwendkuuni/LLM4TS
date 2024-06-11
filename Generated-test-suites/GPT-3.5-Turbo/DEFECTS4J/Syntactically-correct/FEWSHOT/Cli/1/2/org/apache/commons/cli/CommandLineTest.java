import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;

public class CommandLineTest {
    @Test
    public void testHasOptionString() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("a"));
        cmd.addOption(new Option("a", false, ""));
        assertTrue(cmd.hasOption("a"));
    }
    
    @Test
    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption('a'));
        cmd.addOption(new Option("a", false, ""));
        assertTrue(cmd.hasOption('a'));
    }
    
    @Test
    public void testGetOptionObjectString() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("a"));
        cmd.addOption(new Option("a", false, "", "value"));
        assertEquals("value", cmd.getOptionObject("a"));
    }
    
    @Test
    public void testGetOptionObjectChar() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject('a'));
        cmd.addOption(new Option("a", false, "", "value"));
        assertEquals("value", cmd.getOptionObject('a'));
    }
    
    @Test
    public void testGetOptionValueString() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("a"));
        cmd.addOption(new Option("a", true, "value"));
        assertEquals("value", cmd.getOptionValue("a"));
    }
    
    @Test
    public void testGetOptionValueChar() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue('a'));
        cmd.addOption(new Option("a", true, "value"));
        assertEquals("value", cmd.getOptionValue('a'));
    }
    
    @Test
    public void testGetOptionValuesString() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("a"));
        String[] values = {"value1", "value2"};
        cmd.addOption(new Option("a", true, values));
        assertArrayEquals(values, cmd.getOptionValues("a"));
    }
    
    @Test
    public void testGetOptionValuesChar() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues('a'));
        String[] values = {"value1", "value2"};
        cmd.addOption(new Option("a", true, values));
        assertArrayEquals(values, cmd.getOptionValues('a'));
    }
    
    @Test
    public void testGetOptionValueStringString() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("a", "default"));
        cmd.addOption(new Option("a", true, "value"));
        assertEquals("value", cmd.getOptionValue("a", "default"));
    }
    
    @Test
    public void testGetOptionValueCharString() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue('a', "default"));
        cmd.addOption(new Option("a", true, "value"));
        assertEquals("value", cmd.getOptionValue('a', "default"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        assertArrayEquals(new String[] {}, cmd.getArgs());
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertArrayEquals(new String[] {"arg1", "arg2"}, cmd.getArgs());
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        assertEquals(0, cmd.getArgList().size());
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertEquals(2, cmd.getArgList().size());
    }
    
    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.iterator().hasNext());
        Option option = new Option("a", false, "");
        cmd.addOption(option);
        assertTrue(cmd.iterator().hasNext());
        assertEquals(option, cmd.iterator().next());
    }
    
    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        assertArrayEquals(new Option[] {}, cmd.getOptions());
        Option option1 = new Option("a", false, "");
        Option option2 = new Option("b", false, "");
        cmd.addOption(option1);
        cmd.addOption(option2);
        assertArrayEquals(new Option[] {option1, option2}, cmd.getOptions());
    }
}