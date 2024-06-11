package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestUtils;
import org.apache.commons.cli2.test.TestUtils.TestCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    private Group group;
    private WriteableCommandLine cmd;

    @Before
    public void setUp() {
        final List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("o1", "opt1", null, "opt1", false));
        options.add(TestUtils.createOption("o2", "opt2", null, "opt2", true));
        options.add(TestUtils.createOption("o3", "opt3", null, "opt3", false));
        options.add(TestUtils.createOption("o4", "opt4", null, "opt4", false));
        options.add(TestUtils.createOption("o5", "opt5", null, "opt5", false));

        group = new GroupImpl(options, "g", "group", 1, Integer.MAX_VALUE, false);
        cmd = TestCommandLine.getTestCommandLine();
    }

    @Test
    public void testCanProcess() {
        assertEquals(true, group.canProcess(cmd, "o1"));
        assertEquals(true, group.canProcess(cmd, "o2"));
        assertEquals(true, group.canProcess(cmd, "o3"));
        assertEquals(true, group.canProcess(cmd, "o4"));
        assertEquals(true, group.canProcess(cmd, "o5"));
        assertEquals(true, group.canProcess(cmd, "o1")); // can process the same option multiple times

        assertEquals(false, group.canProcess(cmd, "arg1"));
        assertEquals(false, group.canProcess(cmd, "arg2"));
    }

    @Test
    public void testProcess() {
        group.process(cmd, cmd.listIterator());

        assertEquals(1, cmd.getOptionCount());
        assertEquals("o1", cmd.getOptionString(0));

        group.process(cmd, cmd.listIterator());
        assertEquals(2, cmd.getOptionCount());
        assertEquals("o1", cmd.getOptionString(0));
        assertEquals("o2", cmd.getOptionString(1));

        // test that an option can't be parsed twice
        cmd.clearOptions();
        group.process(cmd, cmd.listIterator());
        assertEquals(1, cmd.getOptionCount());
        assertEquals("o1", cmd.getOptionString(0));

        // test that an anonymous argument can be parsed
        cmd.clearOptions();
        group.getAnonymous().get(0).setArgumentsAllowed(true);
        group.getAnonymous().get(0).process(cmd, cmd.listIterator());
        assertEquals(1, cmd.getArgCount());
        assertEquals("arg", cmd.getArg(0));
    }

    @Test(expected = OptionException.class)
    public void testValidate() throws OptionException {
        group.validate(cmd);

        // add an option more than the maximum allowed
        cmd.parse("--o1 --o1 --o2");
        group.validate(cmd);

        // add less options than the minimum required
        cmd.clearOptions();
        group.validate(cmd);
    }

    @Test
    public void testGetName() {
        assertEquals("g", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("group", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);

        // name and description should be printed if not expanded
        assertEquals("g [group]\n", sb.toString());

        final Set<DisplaySetting> displaySettings = new HashSet<>();
        displaySettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        group.appendUsage(sb, displaySettings, null);

        // name and description should be printed, options should not
        assertEquals("g [group]\n", sb.toString());

        displaySettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        group.appendUsage(sb, displaySettings, null);

        // name and description should be printed, options should be printed in order
        assertEquals("g (group)\n" +
                "  opt1: opt1\n" +
                "  opt2: opt2\n" +
                "  opt3: opt3\n" +
                "  opt4: opt4\n" +
                "  opt5: opt5\n", sb.toString());

        displaySettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        group.appendUsage(sb, displaySettings, null);

        // name and description should be printed, options should be printed in order,
        // and anonymous arguments should be printed
        assertEquals("g (group)\n" +
                "  opt1: opt1\n" +
                "  opt2: opt2\n" +
                "  opt3: opt3\n" +
                "  opt4: opt4\n" +
                "  opt5: opt5\n" +
                "  arg: arg\n", sb.toString());

        // name and description should be printed, options should be printed in reverse order
        displaySettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        displaySettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);
        group.appendUsage(sb, displaySettings, ReverseStringComparator.getInstance());

        // name and description should be printed, options should be printed in reverse order,
        // and anonymous arguments should be printed
        assertEquals("g (group)\n" +
                "  opt5: opt5\n" +
                "  opt4: opt4\n" +
                "  opt3: opt3\n" +
                "  opt2: opt2\n" +
                "  opt1: opt1\n" +
                "  arg: arg\n", sb.toString());
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(1, helpLines.size());
        assertEquals("g [group]", helpLines.get(0).getHeader());
        assertEquals("group\n", helpLines.get(0).getShortDescription());
    }

    @Test
    public void testFindOption() {
        final Option o1 = group.findOption("o1");
        assertNotNull(o1);
        assertEquals("o1", o1.getLongOpt());

        final Option o2 = group.findOption("o2");
        assertNotNull(o2);
        assertEquals("o2", o2.getLongOpt());

        final Option o5 = group.findOption("o5");
        assertNull(o5);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }
}