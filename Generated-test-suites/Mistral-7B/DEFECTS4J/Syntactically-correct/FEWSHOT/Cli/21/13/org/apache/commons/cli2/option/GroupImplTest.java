package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private static final String GROUP_NAME = "group";
    private static final String GROUP_DESCRIPTION = "Description of group";
    private static final String OPTION1_NAME = "option1";
    private static final String OPTION1_LONG_NAME = "--option1";
    private static final String OPTION1_SHORT_NAME = "-o1";
    private static final String OPTION1_DESCRIPTION = "Description of option1";

    private static final String OPTION2_NAME = "option2";
    private static final String OPTION2_LONG_NAME = "--option2";
    private static final String OPTION2_SHORT_NAME = "-o2";
    private static final String OPTION2_DESCRIPTION = "Description of option2";

    private static final String ARGUMENT1_NAME = "argument1";
    private static final String ARGUMENT1_LONG_NAME = "--argument1";
    private static final String ARGUMENT1_DESCRIPTION = "Description of argument1";

    private static final int GROUP_MINIMUM = 0;
    private static final int GROUP_MAXIMUM = -1;

    private CommandLine commandLine;
    private WriteableCommandLine writeableCommandLine;
    private GroupImpl group;
    private Option option1;
    private Option option2;
    private Argument argument1;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        writeableCommandLine = new CommandLine();

        option1 = new OptionBuilder()
                .setName(OPTION1_NAME)
                .setLongOpt(OPTION1_LONG_NAME)
                .setShortOpt(OPTION1_SHORT_NAME)
                .setDescription(OPTION1_DESCRIPTION)
                .createOption();

        option2 = new OptionBuilder()
                .setName(OPTION2_NAME)
                .setLongOpt(OPTION2_LONG_NAME)
                .setShortOpt(OPTION2_SHORT_NAME)
                .setDescription(OPTION2_DESCRIPTION)
                .createOption();

        argument1 = new ArgumentBuilder()
                .setName(ARGUMENT1_NAME)
                .setLongOpt(ARGUMENT1_LONG_NAME)
                .setDescription(ARGUMENT1_DESCRIPTION)
                .createArgument();

        List<Option> options = Arrays.asList(option1, option2);
        List<Option> anonymousOptions = Collections.emptyList();
        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION, GROUP_MINIMUM, GROUP_MAXIMUM, false);
    }

    @Test
    public void testCanProcess() {
        // Test with null argument
        assertFalse(group.canProcess(writeableCommandLine, null));

        // Test option1
        writeableCommandLine.parseArgument(OPTION1_LONG_NAME);
        assertTrue(group.canProcess(writeableCommandLine, OPTION1_LONG_NAME));

        // Test option2
        writeableCommandLine.parseArgument(OPTION2_LONG_NAME);
        assertTrue(group.canProcess(writeableCommandLine, OPTION2_LONG_NAME));

        // Test an option that does not belong to the group
        writeableCommandLine.parseArgument("nonOption");
        assertFalse(group.canProcess(writeableCommandLine, "nonOption"));

        // Test an argument
        writeableCommandLine.parseArgument(ARGUMENT1_LONG_NAME, "value");
        assertTrue(group.canProcess(writeableCommandLine, ARGUMENT1_LONG_NAME));

        // Test an invalid argument
        writeableCommandLine.parseArgument("invalidArgument");
        assertFalse(group.canProcess(writeableCommandLine, "invalidArgument"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        List<String> expectedPrefixes = new ArrayList<>();
        expectedPrefixes.add("");
        assertEquals(expectedPrefixes, prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new LinkedHashSet<>();
        triggers.add(OPTION1_LONG_NAME);
        triggers.add(OPTION1_SHORT_NAME);
        triggers.add(OPTION2_LONG_NAME);
        triggers.add(OPTION2_SHORT_NAME);

        Set<String> groupTriggers = group.getTriggers();
        assertEquals(triggers, groupTriggers);
    }

    @Test
    public void testProcess() throws OptionException {
        // Test without arguments
        group.process(writeableCommandLine, writeableCommandLine.listIterator());
        assertFalse(commandLine.hasOption(OPTION1_NAME));
        assertFalse(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));

        // Test with option1
        writeableCommandLine.parseArgument(OPTION1_LONG_NAME);
        group.process(writeableCommandLine, writeableCommandLine.listIterator());
        assertTrue(commandLine.hasOption(OPTION1_NAME));
        assertFalse(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));

        // Test with option2
        writeableCommandLine.parseArgument(OPTION2_LONG_NAME);
        group.process(writeableCommandLine, writeableCommandLine.listIterator());
        assertTrue(commandLine.hasOption(OPTION1_NAME));
        assertTrue(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));

        // Test with argument1
        writeableCommandLine.parseArgument(ARGUMENT1_LONG_NAME, "value");
        group.process(writeableCommandLine, writeableCommandLine.listIterator());
        assertTrue(commandLine.hasOption(OPTION1_NAME));
        assertTrue(commandLine.hasOption(OPTION2_NAME));
        assertTrue(commandLine.hasOption(ARGUMENT1_NAME));

        // Test with an invalid argument
        writeableCommandLine.parseArgument("invalidArgument");
        group.process(writeableCommandLine, writeableCommandLine.listIterator());
        assertTrue(commandLine.hasOption(OPTION1_NAME));
        assertTrue(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));
    }

    @Test
    public void testValidate() throws OptionException {
        // Test with no options
        group.validate(writeableCommandLine);
        assertFalse(commandLine.hasOption(OPTION1_NAME));
        assertFalse(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));

        // Test with option1 and option2
        writeableCommandLine.parseArgument(OPTION1_LONG_NAME);
        writeableCommandLine.parseArgument(OPTION2_LONG_NAME);
        group.validate(writeableCommandLine);
        assertTrue(commandLine.hasOption(OPTION1_NAME));
        assertTrue(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));

        // Test with option1, option2 and argument1
        writeableCommandLine.parseArgument(ARGUMENT1_LONG_NAME, "value");
        group.validate(writeableCommandLine);
        assertTrue(commandLine.hasOption(OPTION1_NAME));
        assertTrue(commandLine.hasOption(OPTION2_NAME));
        assertTrue(commandLine.hasOption(ARGUMENT1_NAME));

        // Test with less options than minimum
        writeableCommandLine.parseArgument(OPTION1_LONG_NAME);
        writeableCommandLine.parseArgument(OPTION2_LONG_NAME);
        group.validate(writeableCommandLine);
        assertFalse(commandLine.hasOption(OPTION1_NAME));
        assertFalse(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));

        // Test with more options than maximum
        writeableCommandLine.parseArgument(OPTION1_LONG_NAME);
        writeableCommandLine.parseArgument(OPTION2_LONG_NAME);
        writeableCommandLine.parseArgument(OPTION1_LONG_NAME);
        group.validate(writeableCommandLine);
        assertFalse(commandLine.hasOption(OPTION1_NAME));
        assertFalse(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(GROUP_NAME, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(GROUP_DESCRIPTION, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        final String expectedUsage =
                "group:" + System.lineSeparator() + "Description of group" + System.lineSeparator() + System.lineSeparator();
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> expectedHelpLines = Arrays.asList(
                new HelpLineImpl(group, 0, "group", null, null),
                new HelpLineImpl(option1, 1, OPTION1_NAME, OPTION1_DESCRIPTION, null),
                new HelpLineImpl(option2, 1, OPTION2_NAME, OPTION2_DESCRIPTION, null)
        );

        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        assertEquals(expectedHelpLines, helpLines);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertSame(option1, options.get(0));
        assertSame(option2, options.get(1));
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertTrue(anonymous.isEmpty());
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption(OPTION1_LONG_NAME);
        assertSame(option1, foundOption);

        foundOption = group.findOption(OPTION2_LONG_NAME);
        assertSame(option2, foundOption);

        foundOption = group.findOption("nonOption");
        assertNull(foundOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(GROUP_MINIMUM, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(GROUP_MAXIMUM, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(writeableCommandLine);
        assertFalse(commandLine.hasOption(OPTION1_NAME));
        assertFalse(commandLine.hasOption(OPTION2_NAME));
        assertFalse(commandLine.hasOption(ARGUMENT1_NAME));
    }
}

class HelpLineImpl implements HelpLine {
    private final Group parent;
    private final int depth;
    private final String name;
    private final String description;
    private final String option;

    public HelpLineImpl(Group parent, int depth, String name, String description, String option) {
        this.parent = parent;
        this.depth = depth;
        this.name = name;
        this.description = description;
        this.option = option;
    }

    public int getDepth() {
        return depth;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOptionName() {
        return option;
    }

    public String getFullName() {
        if (option != null) {
            return parent.getName() + ':' + option;
        } else {
            return parent.getName();
        }
    }

    public String getShortName() {
        if (option != null) {
            Option option = parent.findOption(option);
            return option != null ? option.getShortOpt() : null;
        } else {
            return null;
        }
    }
}

class ReverseStringComparator implements Comparator<String> {
    public int compare(String o1, String o2) {
        return o1.compareTo(o2) * -1;
    }
}