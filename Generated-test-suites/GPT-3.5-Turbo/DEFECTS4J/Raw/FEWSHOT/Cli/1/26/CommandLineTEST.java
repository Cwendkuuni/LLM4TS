
package org.apache.commons.cli;

import org.junit.*;

import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", false, "file option");
        cmd.addOption(option);

        assertFalse(cmd.hasOption("x"));
        assertTrue(cmd.hasOption("f"));
    }

    @Test
    public void testHasOption_char() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", false, "file option");
        cmd.addOption(option);

        assertFalse(cmd.hasOption('x'));
        assertTrue(cmd.hasOption('f'));
    }

    @Test
    public void testGetOptionObject_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "file option");
        option.setType(Integer.class);
        cmd.addOption(option);
        cmd.addArg("10");

        assertNull(cmd.getOptionObject("x"));
        assertEquals(10, cmd.getOptionObject("f"));
    }

    @Test
    public void testGetOptionObject_char() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "file option");
        option.setType(Integer.class);
        cmd.addOption(option);
        cmd.addArg("10");

        assertNull(cmd.getOptionObject('x'));
        assertEquals(10, cmd.getOptionObject('f'));
    }

    @Test
    public void testGetOptionValue_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "file option");
        cmd.addOption(option);
        cmd.addArg("myfile.txt");

        assertNull(cmd.getOptionValue("x"));
        assertEquals("myfile.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testGetOptionValue_char() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "file option");
        cmd.addOption(option);
        cmd.addArg("myfile.txt");

        assertNull(cmd.getOptionValue('x'));
        assertEquals("myfile.txt", cmd.getOptionValue('f'));
    }

    @Test
    public void testGetOptionValues_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "file option");
        cmd.addOption(option);
        cmd.addArg("file1.txt");
        cmd.addArg("file2.txt");

        assertNull(cmd.getOptionValues("x"));
        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, cmd.getOptionValues("f"));
    }

    @Test
    public void testGetOptionValues_char() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "file option");
        cmd.addOption(option);
        cmd.addArg("file1.txt");
        cmd.addArg("file2.txt");

        assertNull(cmd.getOptionValues('x'));
        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, cmd.getOptionValues('f'));
    }

    @Test
    public void testGetOptionValue_String_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "file option");
        cmd.addOption(option);

        assertNull(cmd.getOptionValue("x", "default"));
        assertEquals("default", cmd.getOptionValue("f", "default"));
    }

    @Test
    public void testGetOptionValue_char_String() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "file option");
        cmd.addOption(option);

        assertNull(cmd.getOptionValue('x', "default"));
        assertEquals("default", cmd.getOptionValue('f', "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();

        assertArrayEquals(new String[]{}, cmd.getArgs());

        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();

        assertEquals(0, cmd.getArgList().size());

        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertEquals(2, cmd.getArgList().size());
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testAddArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option option1 = new Option("f", "file", false, "file option");
        Option option2 = new Option("d", "dir", false, "directory option");

        cmd.addOption(option1);
        cmd.addOption(option2);

        assertEquals(option1, cmd.getOptions()[0]);
        assertEquals(option2, cmd.getOptions()[1]);
    }
}
