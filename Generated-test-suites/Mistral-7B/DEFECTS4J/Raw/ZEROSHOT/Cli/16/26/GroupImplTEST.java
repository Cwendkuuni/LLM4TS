package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setUp() {
        options = new ArrayList<>();

        Option option1 = new Option("o1", "option 1", true, "desc1");
        Option option2 = new Option("o2", "option 2", false, "desc2");
        Argument argument1 = new Argument("arg1", "argument 1", true);
        Argument argument2 = new Argument("arg2", "argument 2", true);

        options.add(option1);
        options.add(option2);
        options.add(argument1);
        options.add(argument2);

        group = new GroupImpl(options, "group", "description", 1, 2);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.parseArgumentList("-o1 --arg1 value1 --o1 --o2 value2 --arg2 value2");

        assertTrue(group.canProcess(commandLine, "o1"));
        assertTrue(group.canProcess(commandLine, "o2"));
        assertFalse(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "arg2"));
        assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test(expected = OptionException.class)
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.parseArgumentList("-o1 --arg1 value1");

        group.validate(commandLine);

        fail("OptionException expected");
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.parseArgumentList("-o1 --arg1 value1 --o1 --o2 value2 --arg2 value2");

        group.process(commandLine, commandLine.iterator());

        assertEquals("value1", commandLine.getOptionValue("o1"));
        assertEquals("value2", commandLine.getOptionValue("o2"));
        assertNull(commandLine.getOptionValue("unknown"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();

        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();

        assertEquals(options.size(), triggers.size());
        assertTrue(triggers.containsAll(options));
    }

    @Test
    public void testAppendUsage() {
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);

        String expected = "description:\n" +
                "group (required):\n" +
                "  option 1 is required.\n" +
                "  option 1:description1\n" +
                "  option 2:\n" +
                "  option 2:description2\n" +
                "[arg1]:\n" +
                "  argument 1:description argument 1\n" +
                "[arg2]:\n" +
                "  argument 2:description argument 2\n";

        assertEquals(expected, sb.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(3, helpLines.size());

        HelpLine helpLine1 = helpLines.get(0);
        assertEquals("group", helpLine1.getHeader());
        assertEquals("description", helpLine1.getShortDescription());
        assertEquals(null, helpLine1.getLongDescription());
        assertEquals(0, helpLine1.getOptionCount());

        HelpLine helpLine2 = helpLines.get(1);
        assertEquals("option 1", helpLine2.getHeader());
        assertEquals("description1", helpLine2.getShortDescription());
        assertEquals("description1", helpLine2.getLongDescription());
        assertEquals(0, helpLine2.getOptionCount());

        HelpLine helpLine3 = helpLines.get(2);
        assertEquals("option 2", helpLine3.getHeader());
        assertEquals("description2", helpLine3.getShortDescription());
        assertEquals(null, helpLine3.getLongDescription());
        assertEquals(0, helpLine3.getOptionCount());
    }

    @Test
    public void testGetOptions() {
        List options = group.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.containsAll(this.options.subList(0, 2)));
    }

    @Test
    public void testGetAnonymous() {
        List anonymous = group.getAnonymous();

        assertEquals(2, anonymous.size());
        assertTrue(anonymous.containsAll(this.options.subList(2, 4)));
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption("o1");
        Option option2 = group.findOption("o2");

        assertNotNull(option1);
        assertNotNull(option2);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }
}