package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {
    @Test
    public void testGroupImpl() {
        Option option1 = new OptionBuilder("option1", "o1")
                .withDescription("Option 1")
                .withLongOpt("long-option1")
                .create();

        Option option2 = new OptionBuilder("option2", "o2")
                .withDescription("Option 2")
                .withLongOpt("long-option2")
                .create();

        Option option3 = new OptionBuilder("option3", "o3")
                .withDescription("Option 3")
                .withLongOpt("long-option3")
                .create();

        Argument arg1 = new ArgumentBuilder()
                .withName("arg1")
                .withDescription("Argument 1")
                .create();

        Argument arg2 = new ArgumentBuilder()
                .withName("arg2")
                .withDescription("Argument 2")
                .create();

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        List<Argument> anonymous = new ArrayList<>();
        anonymous.add(arg1);
        anonymous.add(arg2);

        Group group = new GroupImpl(options, "group", "Group", 1, 1);

        assertEquals("group", group.getName());
        assertEquals("Group", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.unmodifiableList(anonymous), group.getAnonymous());

        assertTrue(group.canProcess(TestHelper.getDummyCommandLine(), "o1"));
        assertTrue(group.canProcess(TestHelper.getDummyCommandLine(), "o2"));
        assertTrue(group.canProcess(TestHelper.getDummyCommandLine(), "o3"));
        assertFalse(group.canProcess(TestHelper.getDummyCommandLine(), "arg1"));
        assertFalse(group.canProcess(TestHelper.getDummyCommandLine(), "arg2"));

        group.process(TestHelper.getDummyCommandLine(new String[]{"o1", "arg1"}), null);
        group.validate(TestHelper.getDummyCommandLine(new String[]{"o1", "arg1"}));

        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(null), null);
        assertEquals(3, helpLines.size());
        HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("group", helpLine1.getHeadText());
        assertEquals("Group", helpLine1.getShortDescription());
        assertEquals(ResourceConstants.OPTIONS, helpLine1.getOptionListHeadText());
        assertEquals("Option 1\n" +
                "  long-option1\n" +
                "  long-option1\n" +
                "  Optional. Use --long-option1 <value> to set\n" +
                "  or --o1 <value> when this command is run as a script\n",
                helpLine1.getOptionSpec());
        HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("Option 2\n" +
                "  long-option2\n" +
                "  long-option2\n" +
                "  Optional. Use --long-option2 <value> to set\n" +
                "  or --o2 <value> when this command is run as a script\n",
                helpLine2.getOptionSpec());
        HelpLine helpLine3 = (HelpLine) helpLines.get(2);
        assertEquals("Option 3\n" +
                "  long-option3\n" +
                "  long-option3\n" +
                "  Optional. Use --long-option3 <value> to set\n" +
                "  or --o3 <value> when this command is run as a script\n",
                helpLine3.getOptionSpec());

        HelpLine argHelpLine1 = null;
        for (HelpLine helpLine : helpLines) {
            if (helpLine.getOptionSpec().contains("Argument 1")) {
                argHelpLine1 = helpLine;
                break;
            }
        }
        assertNotNull(argHelpLine1);
        assertEquals("arg1\n" +
                "Optional. Use --arg1 <value> to set\n" +
                "or -- when this command is run as a script\n",
                argHelpLine1.getOptionSpec());

        HelpLine argHelpLine2 = null;
        for (HelpLine helpLine : helpLines) {
            if (helpLine.getOptionSpec().contains("Argument 2")) {
                argHelpLine2 = helpLine;
                break;
            }
        }
        assertNotNull(argHelpLine2);
        assertEquals("arg2\n" +
                "Optional. Use --arg2 <value> to set\n" +
                "or -- when this command is run as a script\n",
                argHelpLine2.getOptionSpec());
    }

    @Test
    public void testGroupImplWithGroup() {
        Option option1 = new OptionBuilder("group1", "g1")
                .withDescription("Group 1")
                .create();

        Option option2 = new OptionBuilder("option2", "o2")
                .withDescription("Option 2")
                .withLongOpt("long-option2")
                .create();

        Option option3 = new OptionBuilder("option3", "o3")
                .withDescription("Option 3")
                .withLongOpt("long-option3")
                .create();

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(option2);
        groupOptions.add(option3);

        Option group = new OptionBuilder("group", "g")
                .withDescription("Group")
                .withLongOpt("long-group")
                .withArgs(groupOptions)
                .create();

        List<Option> groupOptionsInGroup = new ArrayList<>();
        groupOptionsInGroup.add(option2);
        groupOptionsInGroup.add(option3);

        GroupImpl groupImpl = new GroupImpl(options, null, null, 0, 0, group);

        assertEquals("group", groupImpl.getName());
        assertEquals("Group", groupImpl.getDescription());
        assertEquals(0, groupImpl.getMinimum());
        assertEquals(0, groupImpl.getMaximum());
        assertEquals(Collections.unmodifiableList(options), groupImpl.getOptions());
        assertEquals(Collections.emptyList(), groupImpl.getAnonymous());

        assertTrue(groupImpl.canProcess(TestHelper.getDummyCommandLine(), "g"));
        assertFalse(groupImpl.canProcess(TestHelper.getDummyCommandLine(), "o1"));
        assertFalse(groupImpl.canProcess(TestHelper.getDummyCommandLine(), "o2"));
        assertFalse(groupImpl.canProcess(TestHelper.getDummyCommandLine(), "o3"));

        groupImpl.process(TestHelper.getDummyCommandLine(new String[]{"g"}), null);

        List<HelpLine> helpLines = groupImpl.helpLines(0, Collections.emptySet(null), null);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals("Group\n" +
                "  long-group\n" +
                "  Group\n" +
                "  This is a group\n" +
                ResourceConstants.OPTIONS,
                helpLine.getHeadText());

        List<HelpLine> subHelpLines = helpLine.getHelpSections();
        assertEquals(3, subHelpLines.size());

        HelpLine subHelpLine1 = subHelpLines.get(0);
        assertEquals("Option 2\n" +
                "  long-option2\n" +
                "  Option 2\n" +
                "  Optional. Use --long-option2 <value> to set\n" +
                "  or --o2 <value> when this command is run as a script\n",
                subHelpLine1.getOptionSpec());

        HelpLine subHelpLine2 = subHelpLines.get(1);
        assertEquals("Option 3\n" +
                "  long-option3\n" +
                "  Option 3\n" +
                "  Optional. Use --long-option3 <value> to set\n" +
                "  or --o3 <value> when this command is run as a script\n",
                subHelpLine2.getOptionSpec());
    }

    @Test(expected = OptionException.class)
    public void testGroupImplNoMinimum() {
        Option option1 = new OptionBuilder("option1", "o1")
                .withDescription("Option 1")
                .create();

        Option option2 = new OptionBuilder("option2", "o2")
                .withDescription("Option 2")
                .withLongOpt("long-option2")
                .create();

        Option option3 = new OptionBuilder("option3", "o3")
                .withDescription("Option 3")
                .withLongOpt("long-option3")
                .create();

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        Group group = new GroupImpl(options, "group", "Group", 0, 0);

        group.process(TestHelper.getDummyCommandLine(new String[]{"o1"}), null);

        group.validate(TestHelper.getDummyCommandLine(new String[]{"o1"}));
    }

    @Test(expected = OptionException.class)
    public void testGroupImplNoMaximum() {
        Option option1 = new OptionBuilder("option1", "o1")
                .withDescription("Option 1")
                .create();

        Option option2 = new OptionBuilder("option2", "o2")
                .withDescription("Option 2")
                .withLongOpt("long-option2")
                .create();

        Option option3 = new OptionBuilder("option3", "o3")
                .withDescription("Option 3")
                .withLongOpt("long-option3")
                .create();

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        Group group = new GroupImpl(options, "group", "Group", 1, 0);

        group.process(TestHelper.getDummyCommandLine(new String[]{"o1", "o1"}
                ), null);

        group.validate(TestHelper.getDummyCommandLine(new String[]{"o1", "o1", "o1"}));
    }
}