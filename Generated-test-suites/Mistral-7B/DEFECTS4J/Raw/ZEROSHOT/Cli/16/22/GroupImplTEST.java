package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestUtils;

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImpl() throws Exception {
        List<Option> options = new ArrayList<>();

        // Option 1
        Option opt1 = new Option("a", "option1", true, "Option 1");
        opt1.setValueSeparator(';');
        opt1.setRequired(false);
        opt1.setArgName("arg1");
        options.add(opt1);

        // Option 2
        Option opt2 = new Option("b", "option2", false, "Option 2");
        opt2.setArgName("arg2");
        options.add(opt2);

        // Option 3
        Option opt3 = new Option("c", "option3", true, "Option 3")
                .setArgName("arg3")
                .setRequired(true);
        options.add(opt3);

        // Option 4
        Option opt4 = new Option("d", "option4", false, "Option 4")
                .setArgName("arg4");
        options.add(opt4);

        // Group
        List<Option> groupOptions = Arrays.asList(opt3, opt4);
        Group group = new GroupImpl(groupOptions, "My Group", "My Group description", 1, 2);

        // Test properties
        assertEquals("My Group", group.getPreferredName());
        assertEquals("My Group description", group.getDescription());
        assertEquals(Collections.unmodifiableList(groupOptions), group.getOptions());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertTrue(group.isRequired());

        // Test canProcess
        WriteableCommandLine cmdLine = TestUtils.createCommandLine();
        cmdLine.addOption("c");
        cmdLine.addOption("d");

        assertTrue(group.canProcess(cmdLine, ""));

        cmdLine.clear();

        cmdLine.addOption("a");

        assertFalse(group.canProcess(cmdLine, ""));

        cmdLine.addOption("c");

        assertTrue(group.canProcess(cmdLine, ""));

        cmdLine.clear();

        cmdLine.addOption("e");

        assertFalse(group.canProcess(cmdLine, ""));

        // Test getPrefixes
        Set<String> prefixes = group.getPrefixes();

        assertEquals(Collections.emptySet(), prefixes);

        // Test getTriggers
        Set<String> triggers = group.getTriggers();

        assertEquals(Collections.unmodifiableSet(Arrays.asList("c", "d")), triggers);

        // Test process
        cmdLine.clear();

        cmdLine.addOption("c");
        cmdLine.addOption("d");

        group.process(cmdLine, cmdLine.listIterator());

        assertEquals(3, cmdLine.getNumArgs());
        assertEquals("c", cmdLine.getOptionName(2));
        assertEquals("d", cmdLine.getOptionName(3));

        cmdLine.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("a");
        cmdLine.addOption("c");
        cmdLine.addOption("d");

        group.process(cmdLine, cmdLine.listIterator());

        assertEquals(5, cmdLine.getNumArgs());
        assertEquals("a", cmdLine.getOptionName(2));
        assertEquals("a", cmdLine.getOptionName(3));

        cmdLine.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("a");
        cmdLine.addOption("c");
        cmdLine.addOption("d");
        cmdLine.addOption("a");

        group.process(cmdLine, cmdLine.listIterator());

        assertEquals(6, cmdLine.getNumArgs());
        assertEquals("a", cmdLine.getOptionName(4));

        // Test validate
        cmdLine.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("c");

        try {
            group.validate(cmdLine);
        } catch (OptionException e) {
            assertEquals("option3 is required", e.getMessage());
            throw e;
        }

        cmdLine.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("c");
        cmdLine.addOption("c");

        try {
            group.validate(cmdLine);
            fail("Expected an exception");
        } catch (OptionException e) {
            assertEquals("too many options", e.getMessage());
            throw e;
        }

        cmdLine.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("c");

        try {
            group.validate(cmdLine);
            fail("Expected an exception");
        } catch (OptionException e) {
            assertEquals("option3 is required", e.getMessage());
            throw e;
        }

        cmdLine.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("c");
        cmdLine.addOption("e");

        try {
            group.validate(cmdLine);
            fail("Expected an exception");
        } catch (OptionException e) {
            assertEquals("unrecognized option: e", e.getMessage());
            throw e;
        }

        cmdLine.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("c");
        cmdLine.addOption("d");
        cmdLine.addOption("d");

        try {
            group.validate(cmdLine);
            fail("Expected an exception");
        } catch (OptionException e) {
            assertEquals("too many options", e.getMessage());
            throw e;
        }

        cmdLine.clear();

        cmdLine.addOption("a");
        cmdLine.addOption("c");

        try {
            group.validate(cmdLine);
        } catch (OptionException e) {
            assertEquals("option3 is required", e.getMessage());
            throw e;
        }

        // Test appendUsage
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, null, null);

        assertEquals("My Group:\n" +
                "My Group description\n" +
                "  option3: Option 3\n" +
                "  option4: Option 4\n", buffer.toString());

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);

        assertEquals("My Group:\n", buffer.toString());

        buffer.setLength(0);

        group.appendUsage(buffer, null, new ReverseStringComparator());

        assertEquals("My Group description\n" +
                "My Group: option4\n" +
                "My Group: option3\n", buffer.toString());

        // Test helpLines
        List<HelpLine> helpLines = group.helpLines(0, null, null);

        assertEquals(2, helpLines.size());

        HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        HelpLine helpLine2 = (HelpLine) helpLines.get(1);

        assertEquals("My Group:\n" +
                "My Group description\n" +
                "  option3: Option 3\n" +
                "  option4: Option 4\n", helpLine1.getHelpText());
        assertEquals("option4: Option 4\n", helpLine2.getHelpText());

        // Test findOption
        Option foundOption = group.findOption("c");

        assertSame(opt3, foundOption);

        foundOption = group.findOption("e");

        assertNull(foundOption);

        // Test getMinimum
        assertEquals(1, group.getMinimum());

        // Test getMaximum
        assertEquals(2, group.getMaximum());

        // Test isRequired
        assertEquals(true, group.isRequired());

        // Test defaults
        cmdLine.clear();

        group.defaults(cmdLine);

        assertFalse(cmdLine.hasOption("a"));
        assertFalse(cmdLine.hasOption("b"));
        assertFalse(cmdLine.hasOption("c"));
        assertFalse(cmdLine.hasOption("d"));

        cmdLine.clear();

        group.defaults(cmdLine);

        cmdLine.addOption("c");

        assertSame(opt3, cmdLine.getOption("c"));

        cmdLine.clear();

        group.defaults(cmdLine);

        cmdLine.addOption("c");
        cmdLine.addOption("d");

        assertSame(opt3, cmdLine.getOption("c"));
        assertSame(opt4, cmdLine.getOption("d"));
    }
}