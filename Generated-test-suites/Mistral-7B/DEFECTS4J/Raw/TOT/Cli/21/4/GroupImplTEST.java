package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Test cases for {@link GroupImpl}.
 */
public class GroupImplTest {

    private Group group;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "descA"));
        options.add(new Option("b", "optionB", false, "descB"));
        options.add(new Option("c", "optionC", true, "descC"));
        options.add(new Option("d", "optionD", false, "descD"));
        options.add(new Option("e", "optionE", true, "descE"));
        options.add(new Option("f", "optionF", true, "descF"));
        options.add(new Argument("arg1", "arg1"));
        options.add(new Argument("arg2", "arg2"));

        group = new GroupImpl(options, "group", "description", 1, 3, true);
        formatter = new HelpFormatter();
    }

    @Test
    public void testCanProcess() {
        CommandLine cmd = new CommandLine("-a -b -c --arg1 value1 --arg2 value2");
        assertTrue(group.canProcess(cmd, "a"));
        assertTrue(group.canProcess(cmd, "b"));
        assertTrue(group.canProcess(cmd, "c"));
        assertTrue(group.canProcess(cmd, "d"));
        assertTrue(group.canProcess(cmd, "e"));
        assertTrue(group.canProcess(cmd, "f"));
        assertTrue(group.canProcess(cmd, "arg1"));
        assertTrue(group.canProcess(cmd, "arg2"));

        // bursting
        assertTrue(group.canProcess(cmd, "a"));
        assertTrue(group.canProcess(cmd, "b"));
        assertTrue(group.canProcess(cmd, "c"));
        assertFalse(group.canProcess(cmd, "a")); // should rollback

        // invalid option
        assertFalse(group.canProcess(cmd, "x"));

        // invalid argument
        assertFalse(group.canProcess(cmd, "arg3"));
    }

    @Test
    public void testProcess() throws OptionException {
        CommandLine cmd = new CommandLine("-a -b -c --arg1 value1 --arg2 value2");

        List<Object> arguments = new ArrayList<>();
        group.process(cmd, arguments.listIterator());

        assertEquals("optionA", arguments.get(0));
        assertEquals("optionB", arguments.get(1));
        assertEquals("optionC", arguments.get(2));
        assertEquals("value1", arguments.get(3));
        assertEquals("value2", arguments.get(4));
        assertEquals("arg1", arguments.get(5));
        assertEquals("arg2", arguments.get(6));
    }

    @Test
    public void testValidate() throws OptionException {
        CommandLine cmd = new CommandLine();

        // valid command line
        group.validate(cmd);

        // missing option
        cmd.addOption("a");
        try {
            group.validate(cmd);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option 'optionB'", e.getMessage());
        }

        // too many options
        cmd.addOption("d");
        cmd.addOption("e");
        cmd.addOption("f");
        try {
            group.validate(cmd);
            fail();
        } catch (OptionException e) {
            assertEquals("Unexpected option found: optionF", e.getMessage());
        }

        // missing argument
        arguments = new ArrayList<>();
        arguments.add(new CommandLineParser.ParseResult("", new String[0], null));
        try {
            group.validate(arguments);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing argument for option 'group': arg1", e.getMessage());
        }

        // too few arguments
        arguments = new ArrayList<>();
        arguments.add(new CommandLineParser.ParseResult("", new String[]{"arg1"}, null));
        try {
            group.validate(arguments);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing argument for option 'group': arg2", e.getMessage());
        }
    }

    @Test
    public void testHelp() {
        StringWriter sw = new StringWriter();
        HelpLine helpLine = group.getHelpLine("", new HashSet<DisplaySetting>(), null);
        formatter.printHelp(sw, "", helpLine);

        String helpText = sw.toString();

        assertTrue(helpText.contains("group"));
        assertTrue(helpText.contains("description"));
        assertTrue(helpText.contains("optionA"));
        assertTrue(helpText.contains("is required."));
        assertTrue(helpText.contains("descA"));
        assertTrue(helpText.contains("optionB"));
        assertTrue(helpText.contains("descB"));
        assertTrue(helpText.contains("optionC"));
        assertTrue(helpText.contains("is required."));
        assertTrue(helpText.contains("descC"));
        assertTrue(helpText.contains("optionD"));
        assertTrue(helpText.contains("descD"));
        assertTrue(helpText.contains("optionE"));
        assertTrue(helpText.contains("is required."));
        assertTrue(helpText.contains("descE"));
        assertTrue(helpText.contains("optionF"));
        assertTrue(helpText.contains("is required."));
        assertTrue(helpText.contains("descF"));
        assertTrue(helpText.contains("arg1"));
        assertTrue(helpText.contains("arg2"));
    }
}