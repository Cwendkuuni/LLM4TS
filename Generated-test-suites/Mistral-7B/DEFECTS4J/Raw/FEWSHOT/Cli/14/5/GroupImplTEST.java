package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.GroupImpl;
import org.apache.commons.cli2.util.HelpLineImpl;
import org.apache.commons.cli2.util.OptionImpl;
import org.apache.commons.cli2.util.ReverseStringComparator;

public class GroupImplTest {
    @Test
    public void testGroupImpl() {
        final List<Option> options = new ArrayList<>();

        options.add(createOption("--foo", false, null, null, 0, 1, Collections.emptyList(), null, null));
        options.add(createOption("--bar", false, null, "BAR", 0, 1, Collections.emptyList(), null, null));
        options.add(createOption("--baz", false, null, "BAZ", 0, 1, Collections.emptyList(), null, null));
        options.add(createOption("--qux", false, null, "QUX", 0, 1, Collections.emptyList(), null, null));

        final List<Option> anonymous = new ArrayList<>();
        anonymous.add(createArgument("--quux", null, null, null, null, 1, Collections.emptyList(), null, null));

        final SortedMap<String, Option> optionMap = new TreeMap<>(new ReverseStringComparator());

        final String name = "MyGroup";
        final String description = "A group of options";

        final GroupImpl group = new GroupImpl(options, name, description, 1, 1);

        assertEquals(name, group.getPreferredName());
        assertEquals(description, group.getDescription());
        assertEquals(options, group.getOptions());
        assertEquals(anonymous, group.getAnonymous());
        assertEquals(Collections.unmodifiableSet(Collections.singleton("--")), group.getPrefixes());

        // test canProcess
        WriteableCommandLine commandLine = new WriteableCommandLine();
        final String[] arguments = {"--foo", "--bar", "--baz", "--qux", "--quux", "--"};
        for (final String arg : arguments) {
            commandLine.parseArgument(arg);
        }
        assertTrue(group.canProcess(commandLine, "--foo"));
        assertTrue(group.canProcess(commandLine, "--bar"));
        assertTrue(group.canProcess(commandLine, "--baz"));
        assertTrue(group.canProcess(commandLine, "--qux"));
        assertFalse(group.canProcess(commandLine, "--quux"));
        assertFalse(group.canProcess(commandLine, "--quux2"));

        // test getTriggers
        final Set<String> triggers = group.getTriggers();
        assertEquals(Arrays.asList("--foo", "--bar", "--baz", "--qux"), triggers);

        // test process
        final List<String> args = new ArrayList<>();
        args.add("--");
        args.add("--foo");
        args.add("value1");
        args.add("--bar");
        args.add("value2");
        args.add("--baz");
        args.add("value3");
        args.add("--qux");
        args.add("value4");
        args.add("--quux");
        args.add("value5");

        group.process(commandLine, args.listIterator());

        assertEquals("--foo", commandLine.getOptionValue("--foo"));
        assertEquals("value1", commandLine.getOptionValue("--foo"));
        assertEquals("--bar", commandLine.getOptionValue("--bar"));
        assertEquals("value2", commandLine.getOptionValue("--bar"));
        assertEquals("--baz", commandLine.getOptionValue("--baz"));
        assertEquals("value3", commandLine.getOptionValue("--baz"));
        assertEquals("--qux", commandLine.getOptionValue("--qux"));
        assertEquals("value4", commandLine.getOptionValue("--qux"));
        assertNull(commandLine.getOptionValue("--quux"));

        // test validate
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("validate should not throw an exception");
        }

        // test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        assertEquals("MyGroup:\n" +
                     "A group of options\n" +
                     "  --foo\n" +
                     "  --bar\n" +
                     "  --baz\n" +
                     "  --qux\n" +
                     "  --quux: <value>\n" +
                     " ", sb.toString());

        // test helpLines
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(6, helpLines.size());

        // test findOption
        assertNull(group.findOption("--quux2"));
        assertSame(group.findOption("--foo"), createOption("--foo", false, null, null, 0, 1, Collections.emptyList(), null, null));
    }

    private Option createOption(final String longOpt,
                               final boolean hasArg,
                               final String optArgName,
                               final String optArgDescription,
                               final int min,
                               final int max,
                               final List<String> optArgValues,
                               final String optArgMetavar,
                               final String optArgDefaultValue) {
        final Option option = new OptionImpl(longOpt, hasArg, optArgDescription, min, max, optArgValues, optArgMetavar, optArgDefaultValue);

        option.setOptionalStackName("MyGroup_" + longOpt);

        return option;
    }

    private Argument createArgument(final String longOpt,
                                    final String argName,
                                    final int argMin,
                                    final int argMax,
                                    final List<String> argValues,
                                    final String argMetavar,
                                    final String argDescription,
                                    final String argDefaultValue) {
        final Argument argument = new ArgumentImpl(longOpt, argMin, argMax, argMetavar, argDescription, argDefaultValue);

        argument.setOptionalStackName("MyGroup_" + longOpt);

        return argument;
    }
}