package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Test constructor with no arguments
        List options = new ArrayList();
        Group group = new GroupImpl(options, null, null, 0, 0);
        assertEquals(0, group.getMinimum());
        assertEquals(0, group.getMaximum());
        assertNull(group.getName());
        assertNull(group.getDescription());
        assertEquals(0, group.getOptions().size());

        // Test constructor with arguments
        List options2 = new ArrayList();
        options2.add(new Option("a", "arg", false, "An argument"));
        options2.add(new Option("b", "anotherArg", false, "Another argument"));
        Group group2 = new GroupImpl(options2, "group", "A group", 1, 2);
        assertEquals("group", group2.getName());
        assertEquals("A group", group2.getDescription());
        assertEquals(1, group2.getMinimum());
        assertEquals(2, group2.getMaximum());
        assertEquals(2, group2.getOptions().size());

        // Test canProcess method
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List arguments = new ArrayList();
        List options3 = new ArrayList();
        options3.add(new Option("a", "arg1", false, "Argument 1"));
        options3.add(new Option("b", "arg2", false, "Argument 2"));
        Group group3 = new GroupImpl(options3, "group", "A group", 1, 2);
        assertTrue(group3.canProcess(commandLine, "a"));
        assertTrue(group3.canProcess(commandLine, "arg1"));
        assertTrue(group3.canProcess(commandLine, "b"));
        assertTrue(group3.canProcess(commandLine, "arg2"));
        assertFalse(group3.canProcess(commandLine, "c"));

        // Test getPrefixes method
        Set prefixes = group3.getPrefixes();
        assertEquals(0, prefixes.size());

        // Test getTriggers method
        Set triggers = group3.getTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("a"));
        assertTrue(triggers.contains("arg1"));
        assertTrue(triggers.contains("b"));
        assertTrue(triggers.contains("arg2"));

        // Test process method
        commandLine.parseArgument(new String[]{"group", "a"});
        group3.process(commandLine, arguments.listIterator());
        assertEquals(1, commandLine.getValues("a").size());
        assertEquals("arg1", commandLine.getValues("a").get(0));

        commandLine.clearParsed();
        commandLine.parseArgument(new String[]{"group", "b"});
        group3.process(commandLine, arguments.listIterator());
        assertEquals(2, commandLine.getValues("a").size());
        assertEquals("arg1", commandLine.getValues("a").get(0));
        assertEquals("arg2", commandLine.getValues("a").get(1));

        // Test validate method
        try {
            group3.validate(commandLine);
        } catch (OptionException e) {
            fail("validate should not throw an exception");
        }

        commandLine.parseArgument(new String[]{"group", "a"});
        commandLine.parseArgument(new String[]{"group", "b"});
        commandLine.parseArgument(new String[]{"group", "c"});
        try {
            group3.validate(commandLine);
            fail("validate should throw an exception");
        } catch (OptionException e) {
            assertEquals("group: The option 'c' is incorrect", e.getMessage());
        }

        // Test appendUsage method
        StringBuffer buffer = new StringBuffer();
        group3.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("group:\n" +
                "A group\n" +
                "\n" +
                "Options:\n" +
                "  -a ARG_OPTIONAL ...\n" +
                "  --arg1=ARG_OPTIONAL ...\n" +
                "  -b ARG_OPTIONAL ...\n" +
                "  --arg2=ARG_OPTIONAL ...\n", buffer.toString());

        // Test helpLines method
        List helpLines = group3.helpLines(0, Collections.emptySet(), null);
        assertEquals(6, helpLines.size());
        assertEquals("group:", ((HelpLine) helpLines.get(0)).getHeader());
        assertEquals("A group", ((HelpLine) helpLines.get(0)).getShortDescription());
        assertEquals("Options:", ((HelpLine) helpLines.get(3)).getHeader());
        assertEquals("", ((HelpLine) helpLines.get(4)).getShortDescription());
        assertEquals("--arg1=ARG_OPTIONAL", ((HelpLine) helpLines.get(5)).getOpt());

        // Test getOptions and getAnonymous methods
        List options4 = new ArrayList();
        options4.add(new Option("a", "arg1", false, "Argument 1"));
        options4.add(new Option("b", "arg2", false, "Argument 2"));
        Group group4 = new GroupImpl(options4, "group", "A group", 1, 2);
        List options5 = group4.getOptions();
        List anonymous = group4.getAnonymous();
        assertEquals(2, options5.size());
        assertEquals(0, anonymous.size());

        // Test findOption method
        Option option = group4.findOption("a");
        assertNotNull(option);
        assertEquals("arg1", option.getLongOpt());
        option = group4.findOption("c");
        assertNull(option);

        // Test getMinimum and isRequired methods
        Group group5 = new GroupImpl(options4, "group", "A group", 1, 1);
        assertTrue(group5.isRequired());
        assertEquals(1, group5.getMinimum());

        // Test defaults method
        commandLine.clearParsed();
        group5.defaults(commandLine);
        assertFalse(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    @Test
    public void testReverseStringComparator() {
        Comparator comparator = ReverseStringComparator.getInstance();
        List list = new ArrayList();
        list.add("z");
        list.add("a");
        list.add("y");
        list.add("b");
        list.add("x");
        List sortedList = new ArrayList(list);
        Collections.sort(sortedList, null);
        List reversedList = new ArrayList(list);
        Collections.sort(reversedList, comparator);
        assertEquals(sortedList, reversedList);
    }
}