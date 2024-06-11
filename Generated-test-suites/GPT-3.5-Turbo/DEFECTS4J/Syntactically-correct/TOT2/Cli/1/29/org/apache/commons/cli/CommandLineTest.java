import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption('x'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("file"));
        assertNull(cmd.getOptionObject('f'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("input"));
        assertNull(cmd.getOptionValue('i'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("output"));
        assertNull(cmd.getOptionValues('o'));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        assertArrayEquals(new String[]{}, cmd.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "Description");
        cmd.addOption(option);
        assertTrue(cmd.hasOption("f"));
        assertNotNull(cmd.getOptionObject("f"));
        assertEquals("Description", cmd.getOptionValue("file"));
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("argument1");
        cmd.addArg("argument2");
        assertArrayEquals(new String[]{"argument1", "argument2"}, cmd.getArgs());
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("f", "file", true, "Description");
        Option option2 = new Option("o", "output", true, "Output file");
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        int count = 0;
        for (Object obj : cmd) {
            count++;
        }
        
        assertEquals(2, count);
    }

}