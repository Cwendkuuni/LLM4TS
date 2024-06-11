package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Test with empty Group
        List<Option> emptyOptions = new ArrayList<>();
        Group emptyGroup = new GroupImpl(emptyOptions, null, null, 0, 0, false);

        // Test canProcess with empty CommandLine
        WriteableCommandLine emptyCommandLine = new WriteableCommandLine("empty", new ArrayList<>());
        assertFalse(emptyGroup.canProcess(emptyCommandLine, null));

        // Test getPrefixes and getTriggers
        List<Option> options = Arrays.asList(
                new OptionBuilder()
                        .withLongOpt("longOption1")
                        .withOptionName("shortOption1")
                        .withDescription("Option 1")
                        .withPrefix("prefix1")
                        .create(),
                new OptionBuilder()
                        .withLongOpt("longOption2")
                        .withOptionName("shortOption2")
                        .withDescription("Option 2")
                        .withPrefix("prefix2")
                        .create(),
                new OptionBuilder()
                        .withLongOpt("longOption3")
                        .withOptionName("shortOption3")
                        .withDescription("Option 3")
                        .withPrefix("prefix3")
                        .create(),
                new ArgumentBuilder()
                        .withLongOpt("longArgument")
                        .withOptionName("shortArgument")
                        .withDescription("Argument")
                        .isUnicode()
                        .create()
        );
        Group group = new GroupImpl(options, "Group", null, 0, 1, false);

        Set<String> prefixes = group.getPrefixes();
        Set<String> triggers = group.getTriggers();

        assertEquals(3, prefixes.size());
        assertEquals(Arrays.asList("prefix1", "prefix2", "prefix3"), prefixes);
        assertEquals(Arrays.asList("longOption1", "longOption2", "longOption3", "longArgument"), triggers);

        // Test canProcess with valid arguments
        WriteableCommandLine commandLine = new WriteableCommandLine("command", Arrays.asList("--longOption1", "value1", "--shortOption2", "value2"));
        assertTrue(group.canProcess(commandLine, "value1"));
        assertTrue(group.canProcess(commandLine, "value2"));

        // Test process with valid arguments
        ListIterator<String> arguments = commandLine.getArguments().listIterator();
        group.process(commandLine, arguments);

        assertEquals("value1", commandLine.getOptionValue("longOption1"));
        assertEquals("value2", commandLine.getOptionValue("shortOption2"));

        // Test process with invalid arguments
        commandLine = new WriteableCommandLine("command", Arrays.asList("--longOption1", "value1", "--longOption2", "value1"));
        group.process(commandLine, arguments);

        // The method should abort when an option with the same trigger is encountered twice.
        assertEquals(2, commandLine.getOptions().size());
        assertFalse(commandLine.hasOption("longOption2"));

        // Test validate
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Validation should not throw an exception.");
        }

        // Test appendUsage
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);

        String expectedUsage = "Group:\n" +
                "  --longOption1 <value>\n" +
                "  --longOption2 <value>\n" +
                "  --longOption3 <value>\n" +
                "  --longArgument\n";

        assertEquals(expectedUsage, buffer.toString());

        // Test helpLines
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(Arrays.asList(
                new HelpLineImpl("--longOption1", "", "Option 1", null),
                new HelpLineImpl("--longOption2", "", "Option 2", null),
                new HelpLineImpl("--longOption3", "", "Option 3", null),
                new HelpLineImpl("--longArgument", "", "Argument", null)
        ), helpLines);

        // Test getOptions and getAnonymous
        List<Option> groupOptions = group.getOptions();
        List<Option> anonymous = group.getAnonymous();

        assertEquals(3, groupOptions.size());
        assertEquals(1, anonymous.size());

        // Test findOption
        Option foundOption = group.findOption("longOption1");
        assertNotNull(foundOption);
        assertEquals("longOption1", foundOption.getLongOpt());

        // Test isRequired
        assertTrue(group.isRequired());

        // Test defaults
        commandLine = new WriteableCommandLine("command", Arrays.asList("--longOption1"));
        group.defaults(commandLine);

        assertTrue(commandLine.hasOption("longOption1"));
        assertNull(commandLine.getOptionValue("longOption1"));
    }
}