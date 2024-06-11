import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("test"));
        
        cmd.addOption(new Option("test", "Test Option"));
        assertTrue(cmd.hasOption("test"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("test"));
        
        cmd.addOption(new Option("test", "Test Option", true));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("test"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("test"));
        
        cmd.addOption(new Option("test", "Test Option", true));
        cmd.addArg("value1");
        cmd.addArg("value2");
        String[] values = cmd.getOptionValues("test");
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("test", "Test Option");
        cmd.addOption(option);
        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        assertEquals(option, options[0]);
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("test1", "Test Option 1");
        Option option2 = new Option("test2", "Test Option 2");
        cmd.addOption(option1);
        cmd.addOption(option2);
        Iterator<Option> iter = cmd.iterator();
        assertTrue(iter.hasNext());
        assertEquals(option1, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(option2, iter.next());
        assertFalse(iter.hasNext());
    }
}