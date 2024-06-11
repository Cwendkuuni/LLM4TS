package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", false, "Specify a file"));

        assertTrue(cmd.hasOption("f"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", false, "Specify a file"));

        assertTrue(cmd.hasOption('f'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("n", "name", true, "Specify a name"));
        cmd.addOption(new Option("a", "age", true, "Specify an age"));

        cmd.addArg("John Doe");

        assertEquals("John Doe", cmd.getOptionObject("n"));
    }

    @Test
    public void testGetOptionObject_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("n", "name", true, "Specify a name"));
        cmd.addOption(new Option("a", "age", true, "Specify an age"));

        cmd.addArg("John Doe");

        assertEquals("John Doe", cmd.getOptionObject('n'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "Specify a file"));

        cmd.addArg("example.txt");

        assertEquals("example.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testGetOptionValue_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "Specify a file"));

        cmd.addArg("example.txt");

        assertEquals("example.txt", cmd.getOptionValue('f'));
    }

    // Testing edge cases and exception handling scenarios

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "Specify a file"));

        cmd.addArg("example1.txt");
        cmd.addArg("example2.txt");

        assertArrayEquals(new String[]{"example1.txt", "example2.txt"}, cmd.getOptionValues("f"));
    }

    @Test
    public void testGetOptionValues_char() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "Specify a file"));

        cmd.addArg("example1.txt");
        cmd.addArg("example2.txt");

        assertArrayEquals(new String[]{"example1.txt", "example2.txt"}, cmd.getOptionValues('f'));
    }

    @Test
    public void testGetOptionValue_default() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "Specify a file"));

        assertEquals("default.txt", cmd.getOptionValue("f", "default.txt"));
    }

    @Test
    public void testGetOptionValue_char_default() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("f", "file", true, "Specify a file"));

        assertEquals("default.txt", cmd.getOptionValue('f', "default.txt"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("example1.txt");
        cmd.addArg("example2.txt");

        assertArrayEquals(new String[]{"example1.txt", "example2.txt"}, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("example1.txt");
        cmd.addArg("example2.txt");

        assertEquals(2, cmd.getArgList().size());
        assertTrue(cmd.getArgList().contains("example1.txt"));
        assertTrue(cmd.getArgList().contains("example2.txt"));
    }

    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("n", "name", true, "Specify a name"));
        cmd.addOption(new Option("a", "age", true, "Specify an age"));

        assertTrue(cmd.iterator().hasNext());
        assertEquals(2, cmd.getOptions().length);
    }

    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("n", "name", true, "Specify a name"));
        cmd.addOption(new Option("a", "age", true, "Specify an age"));

        assertEquals(2, cmd.getOptions().length);
    }

}