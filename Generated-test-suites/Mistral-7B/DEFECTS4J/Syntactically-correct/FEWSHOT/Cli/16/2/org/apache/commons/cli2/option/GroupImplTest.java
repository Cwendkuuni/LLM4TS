package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder("o1")
                .withDescription("Option 1")
                .build();
        options.add(option1);

        Option option2 = new OptionBuilder("o2")
                .withDescription("Option 2")
                .build();
        options.add(option2);

        Option optionAnonymous = new OptionBuilder("a")
                .withDescription("Anonymous")
                .isAnnotationPresent(Argument.class)
                .build();
        options.add(optionAnonymous);

        GroupImpl group = new GroupImpl(options, "Group", "Group description", 1, 2);

        assertNotNull(group.getName());
        assertNotNull(group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertNotNull(group.getOptions());
        assertEquals(2, group.getOptions().size());
        assertTrue(group.canProcess(new WriteableCommandLine(""), "o1"));
        assertTrue(group.canProcess(new WriteableCommandLine(""), "o2"));
        assertFalse(group.canProcess(new WriteableCommandLine(""), "a"));
        assertFalse(group.canProcess(new WriteableCommandLine(""), "non-existent"));

        List<Option> groupOptions = group.getOptions();
        assertEquals(2, groupOptions.size());
        Option opt1 = groupOptions.get(0);
        Option opt2 = groupOptions.get(1);
        assertEquals("o1", opt1.getLongOpt());
        assertEquals("Option 1", opt1.getDescription());
        assertEquals(null, opt1.getValue());
        assertEquals("o2", opt2.getLongOpt());
        assertEquals("Option 2", opt2.getDescription());
        assertEquals(null, opt2.getValue());

        Set<String> triggers = option1.getTriggers();
        assertTrue(triggers.isEmpty());
        Set<String> triggers2 = option2.getTriggers();
        assertTrue(triggers2.isEmpty());

        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        // test appendUsage
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<String> comp = null;
        group.appendUsage(buffer, helpSettings, comp);
        assertTrue(buffer.toString().contains("Group description"));
        assertTrue(buffer.toString().contains("|"));
        assertTrue(buffer.toString().contains("Option 1"));
        assertTrue(buffer.toString().contains("Option 2"));

        // test helpLines
        List<HelpLine> helpLines = group.helpLines(1, helpSettings, comp);
        assertEquals(2, helpLines.size());
        HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertTrue(helpLine1.getOption().getLongOpt().equals("o1"));
        assertTrue(helpLine2.getOption().getLongOpt().equals("o2"));

        // test findOption
        Option foundOption = group.findOption("o1");
        assertNotNull(foundOption);
        Option notFoundOption = group.findOption("non-existent");
        assertNull(notFoundOption);

        // test process
        WriteableCommandLine commandLine = new WriteableCommandLine("arg1 arg2");
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(2, commandLine.getArguments().size());
        assertEquals("arg1", commandLine.getArguments().get(0).getName());
        assertEquals("arg2", commandLine.getArguments().get(1).getName());

        // test validate
        commandLine = new WriteableCommandLine("arg1 arg2 arg3");
        try {
            group.validate(commandLine);
            fail("Exception expected");
        } catch (OptionException e) {
            assertEquals("arg3", e.getOption().getLongOpt());
        }

        // test validate with anonymous argument
        commandLine = new WriteableCommandLine("arg1 arg2 arg");
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Exception not expected");
        }

        // test process with anonymous argument
        commandLine = new WriteableCommandLine("arg1 arg2 arg");
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(3, commandLine.getArguments().size());
        assertEquals("arg1", commandLine.getArguments().get(0).getName());
        assertEquals("arg2", commandLine.getArguments().get(1).getName());
        assertEquals("arg", commandLine.getArguments().get(2).getName());

        // test canProcess with anonymous argument
        assertTrue(group.canProcess(commandLine, "arg"));

        // test process with minimum value
        commandLine = new WriteableCommandLine("arg1 arg2");
        try {
            group.process(commandLine, commandLine.getArguments().listIterator());
            fail("OptionException expected");
        } catch (OptionException e) {
            assertEquals("arg3", e.getOption().getLongOpt());
        }

        // test process with maximum value
        commandLine = new WriteableCommandLine("arg1 arg2 arg3 arg4");
        try {
            group.process(commandLine, commandLine.getArguments().listIterator());
            fail("OptionException expected");
        } catch (OptionException e) {
            assertEquals("arg5", e.getOption().getLongOpt());
        }
    }

    @Test
    public void testGroupImplWithSortedOptions() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder("o1")
                .withDescription("Option 1")
                .build();
        options.add(option1);

        Option option2 = new OptionBuilder("o2")
                .withDescription("Option 2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "Group", "Group description", 1, 2);

        List<Option> groupOptions = group.getOptions();
        Comparator<Option> comp = new Comparator<Option>() {
            @Override
            public int compare(Option o1, Option o2) {
                return o1.getLongOpt().compareTo(o2.getLongOpt());
            }
        };
        Collections.sort(groupOptions, comp);

        Option opt1 = groupOptions.get(0);
        Option opt2 = groupOptions.get(1);
        assertEquals("o1", opt1.getLongOpt());
        assertEquals("Option 1", opt1.getDescription());
        assertEquals(null, opt1.getValue());
        assertEquals("o2", opt2.getLongOpt());
        assertEquals("Option 2", opt2.getDescription());
        assertEquals(null, opt2.getValue());

        // test appendUsage
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        group.appendUsage(buffer, helpSettings, comp);
        assertTrue(buffer.toString().contains("Option 1"));
        assertTrue(buffer.toString().contains("Option 2"));

        // test helpLines
        List<HelpLine> helpLines = group.helpLines(1, helpSettings, comp);
        assertEquals(2, helpLines.size());
        HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("o1", helpLine1.getOption().getLongOpt());
        assertEquals("Option 1", helpLine1.getOption().getDescription());
        assertEquals(null, helpLine1.getOption().getValue());
        assertEquals("o2", helpLine2.getOption().getLongOpt());
        assertEquals("Option 2", helpLine2.getOption().getDescription());
        assertEquals(null, helpLine2.getOption().getValue());
    }

    @Test
    public void testGroupImplWithTriggers() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder("o1")
                .withLongOpt("long1")
                .withDescription("Option 1")
                .isRequired(false)
                .withTriggers("t1", "t2")
                .build();
        options.add(option1);

        Option option2 = new OptionBuilder("o2")
                .withLongOpt("long2")
                .withDescription("Option 2")
                .isRequired(false)
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "Group", "Group description", 1, 2);

        Set<String> triggers1 = option1.getTriggers();
        Set<String> triggers2 = option2.getTriggers();
        assertTrue(triggers1.containsAll(Collections.singletonList("t1")));
        assertTrue(triggers1.containsAll(Collections.singletonList("t2")));
        assertTrue(triggers2.isEmpty());

        // test canProcess with trigger
        WriteableCommandLine commandLine = new WriteableCommandLine("--t1");
        assertTrue(group.canProcess(commandLine, "--t1"));

        // test process with trigger
        commandLine = new WriteableCommandLine("--t1");
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(1, commandLine.getArguments().size());
        assertEquals("--t1", commandLine.getArguments().get(0).getName());

        // test canProcess without trigger
        assertFalse(group.canProcess(commandLine, "non-trigger"));

        // test process without trigger
        commandLine = new WriteableCommandLine("non-trigger");
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(0, commandLine.getArguments().size());
    }
}

class HelpLineImpl implements HelpLine {
    private final Option option;
    private final int depth;

    public HelpLineImpl(Option option, int depth) {
        this.option = option;
        this.depth = depth;
    }

    @Override
    public Option getOption() {
        return option;
    }

    @Override
    public int getDepth() {
        return depth;
    }
}

class ReverseStringComparatorTest {
    @Test
    public void testComparator() {
        Comparator<String> comparator = new ReverseStringComparator();
        String str1 = "str1";
        String str2 = "str2";

        int result = comparator.compare(str1, str2);
        int expected = -1;
        assertEquals(expected, result);

        result = comparator.compare(str2, str1);
        expected = 1;
        assertEquals(expected, result);

        result = comparator.compare(str1, str1);
        expected = 0;
        assertEquals(expected, result);
    }
}