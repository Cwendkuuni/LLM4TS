package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.HelpLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionImpl;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

public class GroupImplTest {

    @Test
    public void testGroupImpl_1() {
        final List<Option> options = Arrays.asList(
            new Option("o1", "option1", true, "o1-arg"),
            new Option("o2", "option2", false, "o2-arg"),
            new Argument("arg1", "arg1-desc", true),
            new Argument("arg2", "arg2-desc", true)
        );

        final Group group = new GroupImpl(options, "group", "group description", 1, 1);

        assertTrue(group.getName().equals("group"));
        assertTrue(group.getDescription().equals("group description"));
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertSame(Collections.unmodifiableList(options), group.getOptions());
        assertSame(Collections.unmodifiableList(Collections.emptyList()), group.getAnonymous());
        assertSame(Collections.unmodifiableMap(Collections.emptyMap()), group.getOptionMap());
        assertSame(Collections.unmodifiableSet(Collections.emptySet()), group.getPrefixes());
    }

    @Test
    public void testCanProcess_1() {
        final List<Option> options = Arrays.asList(
            new Option("o1", "option1", true, "o1-arg"),
            new Option("o2", "option2", false, "o2-arg")
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        // negative test
        assertFalse(group.canProcess(null, null));

        // test option
        final CommandLine commandLine = new CommandLine();
        commandLine.parse("--o1 value1 --o2 value2");
        assertTrue(group.canProcess(commandLine, "o1"));
        assertTrue(group.canProcess(commandLine, "o2"));

        // test anonymous argument
        final CommandLine commandLineAnonymous = new CommandLine();
        commandLineAnonymous.parse("--arg1 value1 --arg2 value2");
        assertTrue(group.canProcess(commandLineAnonymous, "arg1"));
        assertTrue(group.canProcess(commandLineAnonymous, "arg2"));

        // test non matching option
        assertFalse(group.canProcess(commandLine, "nonMatchingOption"));

        // test non matching anonymous argument
        assertFalse(group.canProcess(commandLineAnonymous, "nonMatchingArg"));
    }

    @Test
    public void testGetTriggers_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg"),
                new Option("o3", "option3", false, "o3-arg")
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        final Set<String> triggers = group.getTriggers();
        assertTrue(triggers.isEmpty());

        for (final Option option : options) {
            triggers.addAll(option.getTriggers());
        }

        assertTrue(triggers.containsAll(Arrays.asList("o1", "o2", "o3")));
    }

    @Test
    public void testProcess_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg"),
                new Argument("arg1", "arg1-desc", true)
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        final CommandLine commandLine = new CommandLine();

        commandLine.parse("--o1 value1 --arg1 value1");

        group.process(commandLine, commandLine.getArguments().listIterator());

        final Option option1 = commandLine.findOption("o1");
        final Argument arg1 = commandLine.findArgument("arg1");

        assertTrue(option1.getValue().equals("value1"));
        assertTrue(arg1.getValue().equals("value1"));

        // test missing option
        commandLine.clear();
        commandLine.parse("--arg1 value1");

        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.getArguments().listIterator()));

        // test too many options
        commandLine.clear();
        commandLine.parse("--o1 value1 --o1 value2");

        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.getArguments().listIterator()));
    }

    @Test
    public void testValidate_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg"),
                new Argument("arg1", "arg1-desc", true)
        );

        final Group group = new GroupImpl(options, null, null, 0, Integer.MAX_VALUE);

        final CommandLine commandLine = new CommandLine();

        commandLine.parse("--o1 value1 --arg1 value1");

        group.validate(commandLine);

        final Option option1 = commandLine.findOption("o1");
        final Argument arg1 = commandLine.findArgument("arg1");

        assertTrue(option1.getValue().equals("value1"));
        assertTrue(arg1.getValue().equals("value1"));

        // test missing option
        commandLine.clear();
        commandLine.parse("--arg1 value1");

        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // test too few options
        commandLine.clear();
        commandLine.parse("--o1 value1");

        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // test too many options
        commandLine.clear();
        commandLine.parse("--o1 value1 --o1 value2 --o1 value3");

        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testAppendUsage_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg"),
                new Argument("arg1", "arg1-desc", true)
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, null, null);

        final String usage = buffer.toString();

        assertTrue(usage.startsWith("group description"));
        assertTrue(usage.endsWith(" arg1-desc"));
        assertTrue(usage.contains("o1-arg"));
        assertTrue(usage.contains("o2"));

        // test with prefixes
        final Set<String> prefixes = Collections.singletonList("p");
        final StringBuilder bufferWithPrefixes = new StringBuilder();

        group.appendUsage(bufferWithPrefixes, prefixes, null);

        final String usageWithPrefixes = bufferWithPrefixes.toString();

        assertTrue(usageWithPrefixes.startsWith("pgroup description"));
        assertTrue(usageWithPrefixes.endsWith(" parg1-desc"));
    }

    @Test
    public void testHelpLines_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg"),
                new Argument("arg1", "arg1-desc", true)
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        final List<HelpLine> helpLines = group.helpLines(0, null, null);

        final HelpLine helpLine = helpLines.get(0);

        assertTrue(helpLine instanceof HelpLineImpl);
        assertTrue(helpLine.getOptionSpec().getName().equals("group"));
        assertTrue(helpLine.getOptionSpec().getLongOpt().equals("group"));
        assertTrue(helpLine.getOptionSpec().getDescription().equals("group description"));

        final List<HelpLine> subHelpLines = ((HelpLineImpl) helpLine).getSubHelpLines();

        assertTrue(subHelpLines.size() == options.size());

        for (int i = 0; i < options.size(); i++) {
            final Option option = options.get(i);
            final HelpLine subHelpLine = subHelpLines.get(i);

            assertTrue(subHelpLine.getOptionSpec().getName().equals(option.getLongOpt()));
            assertTrue(subHelpLine.getOptionSpec().getShortOpt().equals(option.getOpt()));
            assertTrue(subHelpLine.getOptionSpec().getDescription().equals(option.getDescription()));
        }

        // test with prefixes
        final Set<String> prefixes = Collections.singletonList("p");

        final List<HelpLine> helpLinesWithPrefixes = group.helpLines(0, prefixes, null);

        final HelpLine helpLineWithPrefixes = helpLinesWithPrefixes.get(0);

        assertTrue(helpLineWithPrefixes.getOptionSpec().getName().equals("pgroup"));
        assertTrue(helpLineWithPrefixes.getOptionSpec().getLongOpt().equals("pgroup"));
        assertTrue(helpLineWithPrefixes.getOptionSpec().getDescription().equals("pgroup description"));

        final List<HelpLine> subHelpLinesWithPrefixes = ((HelpLineImpl) helpLineWithPrefixes).getSubHelpLines();

        assertTrue(subHelpLinesWithPrefixes.size() == options.size());

        for (int i = 0; i < options.size(); i++) {
            final Option option = options.get(i);
            final HelpLine subHelpLine = subHelpLinesWithPrefixes.get(i);

            assertTrue(subHelpLine.getOptionSpec().getName().equals("po" + option.getLongOpt()));
            assertTrue(subHelpLine.getOptionSpec().getLongOpt().equals("p" + option.getLongOpt()));
            assertTrue(subHelpLine.getOptionSpec().getDescription().equals(option.getDescription()));
        }
    }

    @Test
    public void testGetOptions_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg"),
                new Argument("arg1", "arg1-desc", true)
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg"),
                new Argument("arg1", "arg1-desc", true)
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(Collections.emptyList(), group.getAnonymous());
    }

    @Test
    public void testFindOption_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg")
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        assertNull(group.findOption("nonExistentOption"));
    }

    @Test
    public void testGetMinimum_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg")
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg")
        );

        final Group group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test
    public void testIsRequired_1() {
        final List<Option> options = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg")
        );

        final Group group = new GroupImpl(options, null, null, 1, 1);

        assertFalse(group.isRequired());

        final List<Option> optionsRequired = Arrays.asList(
                new Option("o1", "option1", true, "o1-arg"),
                new Option("o2", "option2", false, "o2-arg"),
                new Option("o3", "option3", true, "o3-arg")
        );

        final Group groupRequired = new GroupImpl(optionsRequired, null, null, 1, 1);

        assertTrue(groupRequired.isRequired());
    }
}