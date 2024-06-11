import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "apple", false, "apple option"));
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("b", "banana", true, "banana option"));
        assertEquals("banana", cmd.getOptionObject("banana"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("c", "carrot", true, "carrot option"));
        assertEquals("orange", cmd.getOptionValue("carrot", "orange"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        String[] values = {"red", "blue", "green"};
        cmd.addOption(new Option("d", "dog", true, "dog option", values));
        assertArrayEquals(values, cmd.getOptionValues("dog"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertNotNull(cmd.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("e", "elephant", false, "elephant option");
        cmd.addOption(opt);
        assertTrue(cmd.iterator().hasNext());
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("f", "fish", false, "fish option");
        cmd.addOption(opt);
        assertEquals(1, cmd.getOptions().length);
    }
}