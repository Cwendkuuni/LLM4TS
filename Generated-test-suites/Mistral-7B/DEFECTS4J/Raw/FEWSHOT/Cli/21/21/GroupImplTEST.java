package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {
    @Test
    public void testGroupImpl() {
        final List options = new ArrayList();
        final Option option1 = new Option("a", "option1", false, "Description of option1");
        options.add(option1);

        final Option option2 = new Option("b", "option2", true, "Description of option2");
        options.add(option2);

        final Option option3 = new Argument("arg", "Argument1", true, "Description of Argument1");
        options.add(option3);

        final GroupImpl group = new GroupImpl(options, "MyGroup", null, 1, 1, false);

        // test canProcess
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("b");
        commandLine.addOption("a");

        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertFalse(group.canProcess(commandLine, "c"));

        // test getPrefixes
        assertNotNull(group.getPrefixes());
        assertEquals(0, group.getPrefixes().size());

        // test getTriggers
        assertNotNull(group.getTriggers());
        assertEquals(3, group.getTriggers().size());

        // test process
        final ListIterator arguments = new ArrayList().listIterator();
        arguments.add("a");
        arguments.add("b");
        arguments.add("b");
        arguments.add("a");

        try {
            group.process(commandLine, arguments);
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }

        // test validate
        commandLine.clear();
        commandLine.addOption("a");
        commandLine.addOption("b");

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected OptionException: " + e.getMessage());
        }

        commandLine.clear();
        commandLine.addOption("b");

        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        commandLine.clear();

        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        // test getPreferredName
        assertEquals("MyGroup", group.getPreferredName());

        // test getDescription
        assertNull(group.getDescription());

        // test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, null, null);
        assertEquals("MyGroup (option1, option2)\n" +
                " option1  Description of option1\n" +
                " option2  Description of option2\n" +
                " arg       Description of Argument1", sb.toString());

        // test helpLines
        final List helpLines = group.helpLines(1, null, null);
        assertEquals(4, helpLines.size());

        final HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("MyGroup", helpLine1.getHeadText());
        assertEquals("", helpLine1.getBodyText());

        final HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("option1", helpLine2.getHeadText());
        assertEquals("Description of option1", helpLine2.getBodyText());

        final HelpLine helpLine3 = (HelpLine) helpLines.get(2);
        assertEquals("option2", helpLine3.getHeadText());
        assertEquals("Description of option2", helpLine3.getBodyText());

        final HelpLine helpLine4 = (HelpLine) helpLines.get(3);
        assertEquals("arg", helpLine4.getHeadText());
        assertEquals("Description of Argument1", helpLine4.getBodyText());

        // test getOptions
        assertEquals(2, group.getOptions().size());

        // test getAnonymous
        assertEquals(1, group.getAnonymous().size());
    }
}