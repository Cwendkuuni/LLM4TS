package org.apache.commons.cli2.option;

import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for GroupImpl
 */
public class GroupImplTest {

    private Group group;
    private WriteableCommandLine cmd;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("o", "option", true, "an option"));
        options.add(new Option("n", null, false, "no option"));
        options.add(new Option("a", "anonymous", 1, "anonymous option"));

        group = new GroupImpl(options, "group", "description", 1, 2, false);
        cmd = new WriteableCommandLine("command");
    }

    @Test
    public void testConstructor() {
        assertNotNull(group);
        assertEquals("group", group.getName());
        assertEquals("description", group.getDescription());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertFalse(group.isRequired());
    }

    @Test
    public void testCanProcess() throws OptionException {
        // test with no arguments
        assertFalse(group.canProcess(cmd, null));

        // test with option
        cmd.parseArgument("o");
        assertTrue(group.canProcess(cmd, "o"));

        // test with option trigger
        cmd.parseArgument("o1");
        Option option1 = group.findOption("o1");
        assertTrue(option1.canProcess(cmd, "o1"));
        assertTrue(group.canProcess(cmd, "o1"));

        // test with multiple options
        cmd.parseArgument("o1");
        cmd.parseArgument("o2");
        assertTrue(group.canProcess(cmd, "o1"));
        assertTrue(group.canProcess(cmd, "o2"));

        // test with bursting
        cmd.parseArgument("o1");
        cmd.parseArgument("o2");
        cmd.parseArgument("o3");
        Option option3 = group.findOption("o3");
        assertTrue(option3.canProcess(cmd, "o3"));
        assertTrue(group.canProcess(cmd, "o3"));

        // test with anonymous argument
        cmd.parseArgument("anonymous");
        Option anonymous = group.findOption("anonymous");
        assertTrue(anonymous.canProcess(cmd, "anonymous"));
        assertTrue(group.canProcess(cmd, "anonymous"));

        // test with invalid argument
        cmd.parseArgument("invalid");
        assertFalse(group.canProcess(cmd, "invalid"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("o"));
        assertTrue(triggers.contains("n"));
        assertFalse(triggers.contains("anonymous"));
    }

    @Test
    public void testProcess() throws OptionException {
        // test with no arguments
        assertFalse(group.canProcess(cmd, null));

        // test with option
        cmd.parseArgument("o");
        group.process(cmd, cmd.listIterator());
        assertEquals(1, cmd.getArguments().size());
        assertEquals("option", cmd.getArguments().get(0));

        // test with invalid argument
        cmd.parseArgument("invalid");
        assertFalse(group.canProcess(cmd, "invalid"));

        // test with multiple options
        cmd.parseArgument("o1");
        cmd.parseArgument("o2");
        group.process(cmd, cmd.listIterator());
        assertEquals(2, cmd.getArguments().size());
        assertEquals("option", cmd.getArguments().get(0));
        assertEquals("option", cmd.getArguments().get(1));

        // test with bursting
        cmd.parseArgument("o1");
        cmd.parseArgument("o2");
        cmd.parseArgument("o3");
        group.process(cmd, cmd.listIterator());
        assertEquals(3, cmd.getArguments().size());
        assertEquals("option", cmd.getArguments().get(0));
        assertEquals("option", cmd.getArguments().get(1));
        assertEquals("option", cmd.getArguments().get(2));

        // test with anonymous argument
        cmd.parseArgument("anonymous");
        Option anonymous = group.findOption("anonymous");
        anonymous.process(cmd, cmd.listIterator());
        assertEquals(1, cmd.getArguments().size());
        assertEquals("anonymous", cmd.getArguments().get(0));

        // test with multiple anonymous arguments
        cmd.parseArgument("anonymous1");
        cmd.parseArgument("anonymous2");
        group.process(cmd, cmd.listIterator());
        assertEquals(2, cmd.getArguments().size());
        assertEquals("anonymous1", cmd.getArguments().get(0));
        assertEquals("anonymous2", cmd.getArguments().get(1));
    }

    @Test
    public void testValidate() throws OptionException {
        // test with valid command line
        cmd.parseArgument("o");
        group.validate(cmd);

        // test with command line with less options than minimum
        cmd.parseArguments(Collections.emptyList());
        try {
            group.validate(cmd);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option: group", e.getMessage());
        }

        // test with command line with more options than maximum
        cmd.parseArguments(List.of("o", "o"));
        try {
            group.validate(cmd);
            fail();
        } catch (OptionException e) {
            assertEquals("Too many options: group", e.getMessage());
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);
        String usage = buffer.toString();
        String expected = "group (description)\n" +
                "  option  : an option\n" +
                "  no option: no option\n" +
                "  anonymous: an anonymous option\n";
        assertTrue(expected.equals(usage));
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, null, null);
        assertEquals(4, helpLines.size());
        HelpLine helpLine1 = helpLines.get(0);
        assertEquals("group", helpLine1.getOptionSpec().getLongOpt());
        assertEquals("group", helpLine1.getOptionSpec().getOpt());
        assertEquals("description", helpLine1.getDescription());
        HelpLine helpLine2 = helpLines.get(1);
        assertEquals("o", helpLine2.getOptionSpec().getLongOpt());
        assertEquals("option", helpLine2.getOptionSpec().getOpt());
        assertEquals("an option", helpLine2.getDescription());
        HelpLine helpLine3 = helpLines.get(2);
        assertEquals("n", helpLine3.getOptionSpec().getLongOpt());
        assertEquals("no option", helpLine3.getOptionSpec().getOpt());
        assertEquals("no option", helpLine3.getDescription());
        HelpLine helpLine4 = helpLines.get(3);
        assertEquals("a", helpLine4.getOptionSpec().getLongOpt());
        assertEquals("anonymous", helpLine4.getOptionSpec().getOpt());
        assertEquals("anonymous option", helpLine4.getDescription());
    }
}