package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;

    @Before
    public void setup() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new Option("g", "group", true, "group name").setRequired(true));
        groupOptions.add(new Option("a", "anonymous", true, "anonymous argument").setRequired(false));

        options = new Options();
        options.addGroup(new GroupImpl(groupOptions, "group", "A group with an option and an anonymous argument", 1, 1));
        options.addOption(new Option("h", "help", false, "Print help message"));
    }

    @Test
    public void testGroupRequiredOption() {
        CommandLine cmd = new CommandLine();
        try {
            options.parse(cmd, new String[]{"-g"});
            fail();
        } catch (OptionException e) {
            assertEquals("group is required.", e.getMessage());
        }

        cmd = new CommandLine();
        options.parse(cmd, new String[]{"-g", "group"});
        assertTrue(cmd.hasOption("group"));
        assertEquals("group", cmd.getOptionValue("group"));
    }

    @Test
    public void testGroupOptionalOption() {
        CommandLine cmd = new CommandLine();
        options.parse(cmd, new String[]{"--help"});
        assertFalse(cmd.hasOption("group"));

        cmd = new CommandLine();
        options.parse(cmd, new String[]{"-g", "group"});
        assertTrue(cmd.hasOption("group"));
        assertEquals("group", cmd.getOptionValue("group"));
    }

    @Test
    public void testGroupAnonymousArgument() {
        CommandLine cmd = new CommandLine();
        try {
            options.parse(cmd, new String[]{"-g", "group", "arg"});
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("No option 'arg' for group 'group'.", e.getMessage());
        }

        cmd = new CommandLine();
        options.parse(cmd, new String[]{"-g", "group", "--anonymous=arg"});
        assertTrue(cmd.hasOption("group"));
        assertFalse(cmd.hasOption("anonymous"));
        assertEquals("arg", cmd.getArgumentValue(0));
        assertEquals("arg", cmd.getArgumentValue(1));
    }

    @Test
    public void testGroupAnonymousArguments() {
        CommandLine cmd = new CommandLine();
        try {
            options.parse(cmd, new String[]{"-g", "group", "arg1", "arg2"});
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("No option 'arg1' for group 'group'.", e.getMessage());
        }

        cmd = new CommandLine();
        options.parse(cmd, new String[]{"-g", "group", "--anonymous=arg1", "--anonymous=arg2"});
        assertTrue(cmd.hasOption("group"));
        assertFalse(cmd.hasOption("anonymous"));
        assertEquals("arg1", cmd.getArgumentValue(0));
        assertEquals("arg2", cmd.getArgumentValue(1));
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        System.out.println(formatter.printHelp("Test", options));
    }
}