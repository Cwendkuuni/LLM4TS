package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private List<Option> options;
    private Option groupOption;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        commandLine = new WriteableCommandLine();

        // create group option
        groupOption = new GroupImpl(options, "group", "Group", 1, 1);
    }

    @Test
    public void testCanProcess() {
        // test with no options
        assertFalse(groupOption.canProcess(commandLine, null));

        // test with valid option
        options.add(new Option("option1", "Option 1", false));
        assertTrue(groupOption.canProcess(commandLine, "option1"));

        // test with invalid option
        assertFalse(groupOption.canProcess(commandLine, "invalid"));
    }

    @Test
    public void testProcess() throws OptionException {
        // test with no options
        assertNull(groupOption.process(commandLine, commandLine.iterator()));

        // test with valid option
        options.add(new Option("option1", "Option 1", false));
        groupOption.process(commandLine, commandLine.iterator());
        assertEquals("option1", commandLine.getOptionValues("option1").get(0));

        // test with invalid option
        assertNull(groupOption.process(commandLine, commandLine.iterator()));

        // test with valid argument
        options.clear();
        options.add(new Argument("arg1", "Argument 1", true));
        groupOption.process(commandLine, commandLine.iterator());
        assertEquals("", commandLine.getOptionValues("arg1").get(0));
    }

    @Test
    public void testValidate() throws OptionException {
        // test with no options
        assertNull(groupOption.validate(commandLine));

        // test with minimum number of options
        options.add(new Option("option1", "Option 1", false));
        assertNull(groupOption.validate(commandLine));

        // test with maximum number of options
        options.add(new Option("option2", "Option 2", false));
        assertNull(groupOption.validate(commandLine));

        // test with one option less than minimum
        options.remove(1);
        assertNull(groupOption.validate(commandLine));

        // test with one option more than maximum
        options.add(new Option("option3", "Option 3", false));
        assertNull(groupOption.validate(commandLine));
        options.remove(2);

        // test with missing option
        options.clear();
        options.add(new Option("option1", "Option 1", true));
        assertNull(groupOption.validate(commandLine));

        // test with extra argument
        options.clear();
        options.add(new Argument("arg1", "Argument 1", true));
        options.add(new Argument("arg2", "Argument 2", true));
        commandLine.addOption("option1");
        assertNull(groupOption.validate(commandLine));
        commandLine.addOption("arg2");
        assertNull(groupOption.validate(commandLine));
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("group", groupOption.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Group", groupOption.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();

        groupOption.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("Group", buffer.toString());

        final Set helpSettings = new HashSet();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_LONG_SYNOPSIS);

        groupOption.appendUsage(buffer, helpSettings, null);
        assertEquals("Group\n" +
                "Group\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        groupOption.appendUsage(buffer, helpSettings, null);
        assertEquals("Group\n" +
                "Group\n" +
                "  Option 1\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        groupOption.appendUsage(buffer, helpSettings, null);
        assertEquals("Group\n" +
                "Group\n" +
                "  Option 1\n" +
                "  Arg1: Argument 1\n", buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME_USE_ALIAS);

        groupOption.appendUsage(buffer, helpSettings, null);
        assertEquals("group\n" +
                "group\n" +
                "  Option 1\n" +
                "  Arg1: Argument 1\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> helpLines = groupOption.helpLines(0, Collections.emptySet(), null);
        assertEquals(1, helpLines.size());
        assertSame(groupOption, helpLines.get(0).getOption());

        final Set helpSettings = new HashSet();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        final List<HelpLine> expandedHelpLines = groupOption.helpLines(1, helpSettings, null);
        assertEquals(2, expandedHelpLines.size());
        assertTrue(expandedHelpLines.get(0).getOption().getLongOpt().startsWith("group."));
        assertTrue(expandedHelpLines.get(1).getOption().getLongOpt().startsWith("group."));

        final Set longOptHelpSettings = new HashSet();
        longOptHelpSettings.add(DisplaySetting.DISPLAY_LONG_SYNOPSIS);

        final List<HelpLine> longOptHelpLines = groupOption.helpLines(2, longOptHelpSettings, null);
        assertEquals(3, longOptHelpLines.size());
        assertTrue(longOptHelpLines.get(0).getOption().getLongOpt().equals("group.option1"));
        assertTrue(longOptHelpLines.get(1).getOption().getLongOpt().equals("group.option1"));
        assertTrue(longOptHelpLines.get(2).getOption().getLongOpt().equals("group.arg1"));
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, groupOption.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(Collections.emptyList(), groupOption.getAnonymous());

        options.add(new Argument("arg1", "Argument 1", true));
        assertEquals(Collections.singletonList(options.get(0)), groupOption.getAnonymous());
    }

    @Test
    public void testFindOption() {
        assertNull(groupOption.findOption("invalid"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, groupOption.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, groupOption.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(groupOption.isRequired());
    }

    @Test
    public void testDefaults() {
        groupOption.defaults(commandLine);
        assertNull(commandLine.getOptionValues("option1"));
        assertNull(commandLine.getOptionValues("arg1"));
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

class ReverseStringComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2) * -1;
    }
}