package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for GroupImpl class
 */
public class GroupImplTest {

    private static final String GROUP_NAME = "group";
    private static final String GROUP_DESCRIPTION = "A sample group";
    private static final int GROUP_MINIMUM = 1;
    private static final int GROUP_MAXIMUM = 2;

    private GroupImpl group;

    @Before
    public void setUp() {
        // prepare the list of options for the group
        final List<Option> options = new ArrayList<>();

        // Option 1
        final Option option1 = Option.builder(Option.builder()
                .longOpt("option1")
                .hasArg(true)
                .argName("arg1")
                .valueSeparator(';')
                .required(false)
                .desc("Option 1 description")
                .build())
                .build();

        // Option 2
        final Option option2 = Option.builder(Option.builder()
                .longOpt("option2")
                .hasArg(true)
                .argName("arg2")
                .valueSeparator(' ')
                .required(true)
                .desc("Option 2 description")
                .build())
                .build();

        // Argument 1
        final Argument argument1 = Argument.builder()
                .name("arg1")
                .usage("ARG1 [ARG1]")
                .desc("Argument 1 description")
                .build();

        // Argument 2
        final Argument argument2 = Argument.builder()
                .name("arg2")
                .usage("ARG2 [ARG2]")
                .desc("Argument 2 description")
                .build();

        options.add(option1);
        options.add(option2);

        // prepare the anonymous arguments
        final List<Option> anonymous = new ArrayList<>();
        anonymous.add(argument1);
        anonymous.add(argument2);

        // create the group
        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION,
                GROUP_MINIMUM, GROUP_MAXIMUM, false);
    }

    @Test
    public void testCanProcess() {
        final WriteableCommandLine commandLine = new BasicParseResult();

        // Option 1
        assertTrue(group.canProcess(commandLine, "option1"));

        // Option 1 with arguments
        assertTrue(group.canProcess(commandLine, "option1 arg1 arg2"));

        // Option 1 with wrong arguments
        assertFalse(group.canProcess(commandLine, "option1 arg"));

        // Option 1 with wrong trigger
        assertFalse(group.canProcess(commandLine, "option2"));

        // Option 2
        assertTrue(group.canProcess(commandLine, "option2"));

        // Option 2 with arguments
        assertTrue(group.canProcess(commandLine, "option2 arg1 arg2"));

        // Option 2 with wrong arguments
        assertFalse(group.canProcess(commandLine, "option2 arg"));

        // Argument 1
        assertTrue(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "arg1 arg"));

        // Argument 2
        assertTrue(group.canProcess(commandLine, "arg2"));
        assertTrue(group.canProcess(commandLine, "arg2 arg"));

        // wrong trigger
        assertFalse(group.canProcess(commandLine, "wrongTrigger"));
    }

    @Test
    public void testMinimumAndMaximum() {
        final WriteableCommandLine commandLine = new BasicParseResult();

        // test minimum
        for (int i = 0; i < GROUP_MINIMUM; i++) {
            group.process(commandLine, commandLine.getArguments().listIterator());
        }

        // test maximum
        for (int i = 0; i < GROUP_MINIMUM + GROUP_MAXIMUM; i++) {
            group.process(commandLine, commandLine.getArguments().listIterator());
        }

        // test maximum+1
        assertThrows(OptionException.class, () -> {
            group.process(commandLine, commandLine.getArguments().listIterator());
        });
    }

    @Test
    public void testGetOptions() {
        assertEquals(group.getOptions(), group.options);
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(group.getAnonymous(), group.anonymous);
    }

    @Test
    public void testFindOption() {
        assertNull(group.findOption("wrongTrigger"));
        assertEquals(option1, group.findOption("option1"));
        assertEquals(option2, group.findOption("option2"));
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testProcess() {
        final WriteableCommandLine commandLine = new BasicParseResult();

        // Option 1
        group.process(commandLine, commandLine.getArguments().listIterator());

        // Argument 1
        group.process(commandLine, commandLine.getArguments().listIterator());

        // Option 2
        group.process(commandLine, commandLine.getArguments().listIterator());

        // Argument 2
        group.process(commandLine, commandLine.getArguments().listIterator());

        // test that group has processed all options
        assertTrue(commandLine.getOptions().isEmpty());

        // test that group has processed all anonymous arguments
        assertTrue(commandLine.getArguments().isEmpty());
    }

    @Test
    public void testValidate() throws OptionException {
        final WriteableCommandLine commandLine = new BasicParseResult();

        // test that all options are present
        group.validate(commandLine);

        // test that one option is missing
        commandLine.getArguments().clear();
        commandLine.getArguments().add("arg1");
        assertThrows(OptionException.class, () -> {
            group.validate(commandLine);
        });

        // test that one optional option is missing
        commandLine.clear();
        commandLine.getOptions().add(option1);
        assertThrows(OptionException.class, () -> {
            group.validate(commandLine);
        });

        // test that one required option is missing
        commandLine.clear();
        commandLine.getOptions().add(option2);
        assertThrows(OptionException.class, () -> {
            group.validate(commandLine);
        });
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, Collections.emptySet(), null);

        assertTrue(buffer.toString().contains(GROUP_NAME));
        assertTrue(buffer.toString().contains(GROUP_DESCRIPTION));

        // test with expanded options
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null);

        assertTrue(buffer.toString().contains("option1"));
        assertTrue(buffer.toString().contains("arg1"));
        assertTrue(buffer.toString().contains("option1 arg1 arg2"));

        assertTrue(buffer.toString().contains("option2"));
        assertTrue(buffer.toString().contains("arg2"));
        assertTrue(buffer.toString().contains("option2 arg1 arg2"));

        // test with anonymous arguments
        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null);

        assertTrue(buffer.toString().contains("arg1"));
        assertTrue(buffer.toString().contains("ARG1 [ARG1]"));

        assertTrue(buffer.toString().contains("arg2"));
        assertTrue(buffer.toString().contains("ARG2 [ARG2]"));
    }

    @Test
    public void testHelpLines() {
        final StringBuilder buffer = new StringBuilder();

        group.helpLines(0, Collections.emptySet(), null, "|").forEach(hl -> hl.appendUsage(buffer, Collections.emptySet(), null));

        assertTrue(buffer.toString().contains(GROUP_NAME));
        assertTrue(buffer.toString().contains(GROUP_DESCRIPTION));

        // test with expanded options
        group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null, "|").forEach(hl -> hl.appendUsage(buffer, Collections.emptySet(), null));

        assertTrue(buffer.toString().contains("option1"));
        assertTrue(buffer.toString().contains("arg1"));
        assertTrue(buffer.toString().contains("option1 arg1 arg2"));

        assertTrue(buffer.toString().contains("option2"));
        assertTrue(buffer.toString().contains("arg2"));
        assertTrue(buffer.toString().contains("option2 arg1 arg2"));

        // test with anonymous arguments
        group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null, "|").forEach(hl -> hl.appendUsage(buffer, Collections.emptySet(), null));

        assertTrue(buffer.toString().contains("arg1"));
        assertTrue(buffer.toString().contains("ARG1 [ARG1]"));

        assertTrue(buffer.toString().contains("arg2"));
        assertTrue(buffer.toString().contains("ARG2 [ARG2]"));
    }

}

class BasicParseResult extends CommandLine {
    @Override
    public void addArgument(final Argument option, final int index) {
        // do nothing
    }

    @Override
    public void addOption(final Option option, final int index) {
        // do nothing
    }

    @Override
    public List getArguments() {
        return new ArrayList<>();
    }

    @Override
    public List getOptions() {
        return new ArrayList<>();
    }

    @Override
    public Iterator iterator() {
        return new ListIterator<>() {
            private final List<String> elements = new ArrayList<>();

            @Override
            public boolean hasNext() {
                return !elements.isEmpty();
            }

            @Override
            public String next() {
                final String result = elements.remove(0);
                if (elements.isEmpty()) {
                    return null;
                }
                return result;
            }

            @Override
            public ListIterator listIterator() {
                return this;
            }
        };
    }

    @Override
    public void addOption(final Option option) {
        // do nothing
    }

    @Override
    public void addArgument(final Argument argument) {
        // do nothing
    }
}