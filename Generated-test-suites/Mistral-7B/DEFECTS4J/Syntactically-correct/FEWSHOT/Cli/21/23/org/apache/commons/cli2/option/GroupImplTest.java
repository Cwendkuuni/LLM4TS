package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        List<Option> options = new ArrayList<>();
        options.add(new OptionBuilder("option1").build());
        options.add(new OptionBuilder("option2").build());
        options.add(new OptionBuilder("option3").build());

        Group group = new GroupImpl(options, null, null, 1, 3, false);

        assertEquals(options, group.getOptions());
        assertEquals(null, group.getName());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertEquals(false, group.isRequired());
        assertEmpty(group.getAnonymous());

        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        assertTrue(group.canProcess(new WriteableCommandLine(), "option1"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "option2"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "option3"));
        assertFalse(group.canProcess(new WriteableCommandLine(), "nonOption"));

        // Test getTriggers()
        Set<String> triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.containsAll(Arrays.asList("option1", "option2", "option3")));

        // Test process()
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = new ArrayList<>();
        commandLine.parseArgumentList(arguments.toArray(new String[0]));

        group.process(commandLine, arguments.listIterator());
        assertEquals(0, commandLine.getArgs().size());
        assertEquals(3, commandLine.getOptions().size());

        // Test validate()
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Validation passed unexpectedly");
        }

        // Test appendUsage()
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, null, null);
        String usage = sb.toString();
        assertTrue(usage.contains("option1"));
        assertTrue(usage.contains("option2"));
        assertTrue(usage.contains("option3"));

        // Test helpLines()
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(3, helpLines.size());
        for (HelpLine helpLine : helpLines) {
            assertTrue(helpLine.getOption().isEnabled());
        }

        // Test findOption()
        Option foundOption = group.findOption("option1");
        assertEquals("option1", foundOption.getLongOpt());

        // Test getMinimum() and getMaximum()
        group = new GroupImpl(options, null, null, 5, 5, false);
        assertEquals(5, group.getMinimum());
        assertEquals(5, group.getMaximum());

        group = new GroupImpl(options, null, null, 1, 2, false);
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());

        group = new GroupImpl(options, null, null, 0, 1, false);
        assertEquals(0, group.getMinimum());
        assertEquals(1, group.getMaximum());

        group = new GroupImpl(options, null, null, -1, 1, false);
        assertEquals(-1, group.getMinimum());
        assertEquals(1, group.getMaximum());

        // Test isRequired()
        group = new GroupImpl(options, null, null, 1, 1, true);
        assertTrue(group.isRequired());

        group = new GroupImpl(options, null, null, 1, 1, false);
        assertFalse(group.isRequired());

        // Test defaults()
        commandLine = new WriteableCommandLine();
        group.defaults(commandLine);
        assertEquals(3, commandLine.getOptions().size());
    }

    @Test
    public void testGroupImplWithAnonymousArguments() {
        List<Option> options = new ArrayList<>();
        options.add(new OptionBuilder("option1").build());
        options.add(new OptionBuilder("option2").build());
        options.add(new OptionBuilder("option3").build());
        options.add(new OptionBuilder("arg1").build().isRequired(false).argName("ARG").numberOfValues(1));
        options.add(new OptionBuilder("arg2").build().isRequired(false).argName("ARG").numberOfValues(1));

        Group group = new GroupImpl(options, null, null, 1, 3, false);

        assertEquals(options, group.getOptions());
        assertEquals(2, group.getAnonymous().size());

        // Test canProcess() with anonymous arguments
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = new ArrayList<>();
        commandLine.parseArgumentList(arguments.toArray(new String[0]));

        commandLine.addOption(new Option("", "arg1", true, "anonymous argument 1"));
        commandLine.addOption(new Option("", "arg2", true, "anonymous argument 2"));

        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option3"));
        assertTrue(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "arg2"));

        // Test process() with anonymous arguments
        group.process(commandLine, arguments.listIterator());
        assertEquals(0, commandLine.getArgs().size());
        assertEquals(5, commandLine.getOptions().size());
        assertEquals(2, commandLine.getOptions(Option.class).stream().filter(option -> option.isArgument()).count());

        // Test validate() with anonymous arguments
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Validation passed unexpectedly");
        }

        // Test appendUsage() with anonymous arguments
        StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, null, null);
        String usage = sb.toString();
        assertTrue(usage.contains("option1"));
        assertTrue(usage.contains("option2"));
        assertTrue(usage.contains("option3"));
        assertTrue(usage.contains("[ARG]"));
        assertTrue(usage.contains("[ARG]"));

        // Test helpLines() with anonymous arguments
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(5, helpLines.size());
        for (HelpLine helpLine : helpLines) {
            if (helpLine.getOption().isArgument()) {
                assertTrue(helpLine.getOption().isEnabled());
                assertTrue(helpLine.getOption().getArgName().equals("ARG"));
            } else {
                assertTrue(helpLine.getOption().isEnabled());
            }
        }

        // Test findOption() with anonymous arguments
        Option foundOption = group.findOption("arg1");
        assertEquals("arg1", foundOption.getLongOpt());

        foundOption = group.findOption("arg2");
        assertEquals("arg2", foundOption.getLongOpt());
    }
}