package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("opt1", "opt1", true, "o1"));
        options.add(TestUtils.createOption("opt2", "opt2", false, "o2"));
        options.add(TestUtils.createOption("opt3", "opt3", false, "o3"));
        options.add(TestUtils.createOption("opt4", "opt4", false, "o4"));
        options.add(TestUtils.createOption("opt5", "opt5", false, "o5"));
        options.add(TestUtils.createArgument("arg1", "arg1"));
        options.add(TestUtils.createArgument("arg2", "arg2"));

        final Group group = new GroupImpl(options, "group", "Group", 1, -1, false);

        // When

        // Can process
        final WriteableCommandLine commandLine = TestUtils.createCommandLine();
        Assert.assertTrue(group.canProcess(commandLine, "opt1"));
        Assert.assertTrue(group.canProcess(commandLine, "opt2"));
        Assert.assertTrue(group.canProcess(commandLine, "opt3"));
        Assert.assertTrue(group.canProcess(commandLine, "opt4"));
        Assert.assertTrue(group.canProcess(commandLine, "opt5"));
        Assert.assertFalse(group.canProcess(commandLine, "arg1"));
        Assert.assertFalse(group.canProcess(commandLine, "arg2"));

        // GetPrefixes
        Assert.assertEquals(group.getPrefixes(), TestUtils.createSet("opt1", "opt2", "opt3", "opt4", "opt5"));

        // GetTriggers
        Assert.assertEquals(group.getTriggers(), TestUtils.createSet("opt1", "opt2", "opt3", "opt4", "opt5"));

        // Process
        commandLine.addOption("opt1");
        commandLine.addOption("opt2");
        commandLine.addOption("opt3");
        commandLine.addOption("opt4");

        try {
            group.process(commandLine, TestUtils.createListIterator());
            Assert.assertTrue(commandLine.hasOption("opt1"));
            Assert.assertTrue(commandLine.hasOption("opt2"));
            Assert.assertTrue(commandLine.hasOption("opt3"));
            Assert.assertTrue(commandLine.hasOption("opt4"));
            Assert.assertFalse(commandLine.hasOption("opt5"));
            Assert.assertFalse(commandLine.hasOption("arg1"));
            Assert.assertFalse(commandLine.hasOption("arg2"));
        } catch (OptionException e) {
            Assert.fail(e.getMessage());
        }

        // Validate
        group.validate(commandLine);

        // GetPreferredName
        Assert.assertEquals(group.getPreferredName(), "group");

        // GetDescription
        Assert.assertNull(group.getDescription());

        // AppendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        Assert.assertEquals(sb.toString(), ResourceConstants.USAGE_GROUP + " group\n" +
                "  " + ResourceConstants.USAGE_OPTIONS + "\n");

        // HelpLines
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        Assert.assertEquals(helpLines.size(), 2);
        Assert.assertEquals(helpLines.get(0).getLine(), ResourceConstants.USAGE_GROUP + " group\n");
        Assert.assertEquals(helpLines.get(1).getLine(), ResourceConstants.USAGE_OPTIONS);

        // GetOptions
        Assert.assertEquals(group.getOptions(), options);

        // GetAnonymous
        Assert.assertEquals(group.getAnonymous(), TestUtils.createList(TestUtils.createArgument("arg1"), TestUtils.createArgument("arg2")));

        // FindOption
        Assert.assertNull(group.findOption("opt6"));

        // GetMinimum
        Assert.assertEquals(group.getMinimum(), 1);

        // GetMaximum
        Assert.assertEquals(group.getMaximum(), -1);

        // IsRequired
        Assert.assertFalse(group.isRequired());
    }
}