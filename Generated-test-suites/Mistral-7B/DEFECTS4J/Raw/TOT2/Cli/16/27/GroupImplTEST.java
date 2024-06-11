package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setUp() {
        List options = new ArrayList();
        options.add(new Option("o", "opt", false, "Option test"));
        options.add(new Option("p", "prefix", true, "Prefix test"));
        options.add(new Option("a", "arg", 1, "Argument test"));
        Option arg1 = new Option("a1", "arg1", 1, "Argument test 1");
        arg1.setRequired(true);
        options.add(arg1);
        Option arg2 = new Option("a2", "arg2", 1, "Argument test 2");
        options.add(arg2);

        group = new GroupImpl(options, "Group", "Test group", 1, 3);
    }

    @Test
    public void testCanProcess() {
        // Test with valid options
        WriteableCommandLine cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        cmd.addOption("a", "arg1");
        cmd.addOption("a", "arg2");
        assertTrue(group.canProcess(cmd, "a1"));
        assertTrue(group.canProcess(cmd, "a2"));
        assertTrue(group.canProcess(cmd, "o"));
        assertTrue(group.canProcess(cmd, "p"));

        // Test with invalid options
        cmd = new WriteableCommandLine();
        assertFalse(group.canProcess(cmd, "invalid"));
        assertFalse(group.canProcess(cmd, "opt"));
        assertFalse(group.canProcess(cmd, "prefix"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertTrue(prefixes.contains(""));
        assertTrue(prefixes.contains("o"));
        assertTrue(prefixes.contains("p"));
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertTrue(triggers.contains("a1"));
        assertTrue(triggers.contains("a2"));
        assertTrue(triggers.contains("a"));
        assertTrue(triggers.contains("o"));
        assertTrue(triggers.contains("p"));
    }

    @Test
    public void testProcess() {
        // Test with valid options
        WriteableCommandLine cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        cmd.addOption("a", "arg1", true, "arg1_val");
        cmd.addOption("a", "arg2", true, "arg2_val");

        group.process(cmd, cmd.listIterator());
        assertEquals("arg1_val", arg1.getValue(cmd));
        assertEquals("arg2_val", arg2.getValue(cmd));

        // Test with prefixes
        cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("p");
        cmd.addOption("parg1", "arg1_val");
        cmd.addOption("parg2", "arg2_val");

        group.process(cmd, cmd.listIterator());
        assertEquals("arg1_val", arg1.getValue(cmd));
        assertEquals("arg2_val", arg2.getValue(cmd));

        // Test with minimum and maximum
        cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        cmd.addOption("a", "arg1", true, "arg1_val");
        cmd.addOption("a", "arg2", true, "arg2_val");
        cmd.addOption("a", "arg3", true, "arg3_val");

        group.process(cmd, cmd.listIterator());
        assertEquals("arg1_val", arg1.getValue(cmd));
        assertEquals("arg2_val", arg2.getValue(cmd));
        assertEquals("arg3_val", arg1.getValue(cmd)); // should fail

        // Test with invalid options
        cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        cmd.addOption("invalid");
        assertThrows(OptionException.class, () -> group.process(cmd, cmd.listIterator()));
    }

    @Test
    public void testValidate() {
        // Test with valid options
        WriteableCommandLine cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        cmd.addOption("a", "arg1", true, "arg1_val");
        cmd.addOption("a", "arg2", true, "arg2_val");
        group.validate(cmd);

        // Test with minimum
        cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        group.validate(cmd);

        // Test with maximum
        cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        cmd.addOption("a", "arg1", true, "arg1_val");
        cmd.addOption("a", "arg2", true, "arg2_val");
        cmd.addOption("a", "arg3", true, "arg3_val");
        group.validate(cmd);

        // Test with missing options
        cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        assertThrows(OptionException.class, () -> group.validate(cmd));

        // Test with too many options
        cmd = new WriteableCommandLine();
        cmd.addOption("o");
        cmd.addOption("o");
        cmd.addOption("p");
        cmd.addOption("a", "arg1", true, "arg1_val");
        cmd.addOption("a", "arg2", true, "arg2_val");
        cmd.addOption("a", "arg3", true, "arg3_val");
        cmd.addOption("a", "arg4", true, "arg4_val"); // should fail
        assertThrows(OptionException.class, () -> group.validate(cmd));
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("Group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test group", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        String usage = buffer.toString();
        assertTrue(usage.contains("Test group"));
        assertTrue(usage.contains("[o]"));
        assertTrue(usage.contains("[p]"));
        assertTrue(usage.contains("Option test"));
    }

    @Test
    public void testHelpLines() {
        List helpLines = group.helpLines(0, null, null);
        assertTrue(helpLines.size() > 1);
        String firstHelpLine = (String) helpLines.get(0);
        assertTrue(firstHelpLine.contains("Test group"));
        assertTrue(firstHelpLine.contains("[o]"));
        assertTrue(firstHelpLine.contains("[p]"));
    }

    @Test
    public void testGetOptions() {
        List options = group.getOptions();
        assertTrue(options.contains(arg1));
        assertTrue(options.contains(arg2));
    }

    @Test
    public void testGetAnonymous() {
        List anonymous = group.getAnonymous();
        assertTrue(anonymous.contains(arg1));
        assertTrue(anonymous.contains(arg2));
    }

    @Test
    public void testFindOption() {
        Option option = group.findOption("a1");
        assertSame(arg1, option);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine cmd = new WriteableCommandLine();
        group.defaults(cmd);
        assertNull(arg1.getValue(cmd));
        assertNull(arg2.getValue(cmd));
    }
}