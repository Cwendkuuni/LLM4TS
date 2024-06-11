import static org.junit.Assert.*;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("help"));
        assertTrue(cmd.hasOption("help"));
        assertFalse(cmd.hasOption("version"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("version"));
        assertTrue(cmd.hasOption('v'));
        assertFalse(cmd.hasOption('h'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("output", "output file", true));
        cmd.addArg("output.txt");
        assertEquals("output.txt", cmd.getOptionObject("output"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("input", "input file", true));
        cmd.addArg("input.txt");
        assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("files", "input files", true));
        cmd.addArg("file1.txt");
        cmd.addArg("file2.txt");
        assertEquals("file1.txt", cmd.getOptionValues("files")[0]);
        assertEquals("file2.txt", cmd.getOptionValues("files")[1]);
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine cmd = new CommandLine();
        assertEquals("default.txt", cmd.getOptionValue("output", "default.txt"));
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

}