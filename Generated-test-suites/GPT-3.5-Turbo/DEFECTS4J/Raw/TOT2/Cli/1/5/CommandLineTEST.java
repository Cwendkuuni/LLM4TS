import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "test option"));

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "test option", true, "value"));

        assertEquals("value", cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "test option", true, new String[]{"value1", "value2"}));

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("a", "test option");

        cmd.addOption(opt);

        Option[] options = cmd.getOptions();

        assertEquals(1, options.length);
        assertEquals(opt, options[0]);
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("a", "test option");

        cmd.addOption(opt);

        int count = 0;
        for (Iterator iter = cmd.iterator(); iter.hasNext(); ) {
            count++;
            assertEquals(opt, iter.next());
        }

        assertEquals(1, count);
    }
}