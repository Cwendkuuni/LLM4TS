package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final List<Option> options = new ArrayList<>();
        final Option option1 = new Option("o1", "opt1", false, "desc1");
        options.add(option1);
        final Option option2 = new Option("o2", "opt2", true, "desc2");
        options.add(option2);
        final Option option3 = new Option("o3", "opt3", false, "desc3");
        options.add(option3);

        final GroupImpl group = new GroupImpl(options, "g", "description", 1, 3);

        assertEquals("g", group.getPreferredName());
        assertEquals("description", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertEquals(options, group.getOptions());
        assertSame(group, group.findOption("g"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "o1"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "o2"));
        assertTrue(group.canProcess(new WriteableCommandLine(), "o3"));
        assertFalse(group.canProcess(new WriteableCommandLine(), "invalid"));

        // test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, new HashSet<>(), null);
        assertEquals("description\n" +
                     "g (opt1 | opt2 | opt3)\n", sb.toString());

        // test helpLines
        final List<HelpLine> helpLines = group.helpLines(0, new HashSet<>(), null);
        assertEquals(3, helpLines.size());
        assertEquals("-g, --g\tdescription\n" +
                     "  g (opt1 | opt2 | opt3)\n", helpLines.get(0).getHelp());
        assertEquals("--opt1\tdesc1\n", helpLines.get(1).getHelp());
        assertEquals("--opt2\tdesc2\n" +
                     "  [multiple values]\n", helpLines.get(2).getHelp());
    }

    @Test
    public void testGroupImplMissingOption() throws OptionException {
        final List<Option> options = new ArrayList<>();
        final Option option1 = new Option("o1", "opt1", false, "desc1");
        options.add(option1);
        final GroupImpl group = new GroupImpl(options, "g", "description", 1, 3);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseCommandLine(new String[]{"g"}));

        assertTrue(commandLine.hasOption("o1"));
        assertFalse(commandLine.hasOption("o2"));

        group.validate(commandLine);

        final List<String> messages = commandLine.getParsingErrorMessages();
        assertEquals(1, messages.size());
        assertEquals("Missing option 'o2'.", messages.get(0));
    }

    @Test
    public void testGroupImplExtraOption() throws OptionException {
        final List<Option> options = new ArrayList<>();
        final Option option1 = new Option("o1", "opt1", false, "desc1");
        options.add(option1);
        final Option option2 = new Option("o2", "opt2", true, "desc2");
        options.add(option2);
        final GroupImpl group = new GroupImpl(options, "g", "description", 1, 3);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseCommandLine(new String[]{"g", "o3"}));

        assertTrue(commandLine.hasOption("o1"));
        assertTrue(commandLine.hasOption("o2"));
        assertFalse(commandLine.hasOption("o3"));

        group.validate(commandLine);

        final List<String> messages = commandLine.getParsingErrorMessages();
        assertEquals(1, messages.size());
        assertEquals("Unexpected option 'o3'.", messages.get(0));
    }

    @Test
    public void testGroupImplMultipleValues() throws OptionException {
        final List<Option> options = new ArrayList<>();
        final Option option = new Option("o", "opt", true, "desc");
        final Argument argument = new Argument("arg", "arg", true, "desc");
        option.addArgument(argument);
        options.add(option);
        final GroupImpl group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseCommandLine(new String[]{"g", "opt", "--arg=a", "--arg=b"}));

        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.hasOption("arg"));
        assertEquals(2, commandLine.getArgumentValues("arg").size());
        assertEquals("a", commandLine.getArgumentValues("arg").get(0));
        assertEquals("b", commandLine.getArgumentValues("arg").get(1));
    }

    @Test
    public void testGroupImplMinimum() throws OptionException {
        final List<Option> options = new ArrayList<>();
        final Option option = new Option("o", "opt", true, "desc");
        options.add(option);
        final GroupImpl group = new GroupImpl(options, null, null, 2, Integer.MAX_VALUE);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseCommandLine(new String[]{"g"}));

        assertFalse(commandLine.hasOption("o"));
        assertFalse(commandLine.isSet("o"));

        group.validate(commandLine);

        final List<String> messages = commandLine.getParsingErrorMessages();
        assertEquals(1, messages.size());
        assertEquals("Missing option 'o' (minimum: 2).", messages.get(0));
    }

    @Test
    public void testGroupImplMaximum() throws OptionException {
        final List<Option> options = new ArrayList<>();
        final Option option = new Option("o", "opt", true, "desc");
        options.add(option);
        final GroupImpl group = new GroupImpl(options, null, null, Integer.MIN_VALUE, 2);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parseCommandLine(new String[]{"g", "o", "o"}));

        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.isSet("o"));
        assertEquals(2, commandLine.getOptionValues("o").size());

        group.validate(commandLine);

        final List<String> messages = commandLine.getParsingErrorMessages();
        assertEquals(1, messages.size());
        assertEquals("Too many occurrences of option 'o' (maximum: 1).", messages.get(0));
    }
}