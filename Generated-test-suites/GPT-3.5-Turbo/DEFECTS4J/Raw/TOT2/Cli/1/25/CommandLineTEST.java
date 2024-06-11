import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        
        assertFalse(cmd.hasOption("-abc"));
        
        cmd.addOption(new Option("a", "apple"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        
        assertEquals(null, cmd.getOptionValue("xyz"));
        
        cmd.addOption(new Option("b", "banana", true, "Banana option"));
        cmd.addArg("value");
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        
        assertNull(cmd.getOptionValues("xyz"));
        
        cmd.addOption(Option.builder("c", "carrot").hasArgs().build());
        cmd.addArg("one");
        cmd.addArg("two");
        assertArrayEquals(new String[]{"one", "two"}, cmd.getOptionValues("c"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        
        assertEquals(0, cmd.getArgs().length);
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("d", "dog", false, "Dog option");
        
        cmd.addOption(opt);
        assertTrue(cmd.hasOption("d"));
        assertEquals("Dog option", opt.getDescription());
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option opt1 = Option.builder("e", "elephant").hasArg().desc("Elephant option").build();
        Option opt2 = Option.builder("f", "frog").hasArgs().desc("Frog option").build();
        
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        
        for (Option option : cmd.getOptions()) {
            assertTrue(cmd.hasOption(option.getKey()));
        }
    }

}