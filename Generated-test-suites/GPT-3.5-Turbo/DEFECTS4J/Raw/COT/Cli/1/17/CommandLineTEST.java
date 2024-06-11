import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Test;


public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionObject("a"));
        assertNull(cmd.getOptionObject("b"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        assertNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        String[] args = cmd.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        assertNotNull(cmd.getArgList());
        assertEquals(0, cmd.getArgList().size());
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        String[] args = cmd.getArgs();
        assertNotNull(args);
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("a", "option a");
        cmd.addOption(opt);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertNull(cmd.getOptionObject("a"));
        assertNull(cmd.getOptionObject("b"));
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
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
        Option opt1 = new Option("a");
        Option opt2 = new Option("b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);
        Option[] options = cmd.getOptions();
        assertNotNull(options);
        assertEquals(2, options.length);
        assertEquals(opt1, options[0]);
        assertEquals(opt2, options[1]);
    }
}