package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Test for the GroupImpl class
 */
public class GroupImplTest {

    /**
     * Test for the constructor with no name and description
     */
    @Test
    public void testGroupWithNoNameAndDescription() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create());

        final GroupImpl group = new GroupImpl(options, null, null, 1, 1);

        Assert.assertEquals("option1", group.getPreferredName());
        Assert.assertEquals("Option 1", group.getDescription());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(1, group.getMaximum());

        // test canProcess
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parse());
        Assert.assertTrue(group.canProcess(cmd, "option1"));
        Assert.assertTrue(group.canProcess(cmd, "option2"));

        // test getPrefixes
        Assert.assertEquals(Collections.emptySet(), group.getPrefixes());

        // test getTriggers
        Assert.assertEquals(Collections.unmodifiableSet(Collections.singleton("option1")), group.getTriggers());
        Assert.assertEquals(Collections.unmodifiableSet(Collections.singleton("option2")), group.getTriggers());

        // test process
        final String[] args = {"--option1", "--option2"};
        cmd.parse(group.parse(), args);
        Assert.assertEquals(2, cmd.getNumArgs());

        // test validate
        group.validate(cmd);

        // test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        Assert.assertEquals("options:\n" +
                "  option1\n" +
                "  option2\n", sb.toString());

        // test helpLines
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        Assert.assertEquals(2, helpLines.size());

        // test getOptions
        Assert.assertEquals(Collections.unmodifiableList(options), group.getOptions());

        // test findOption
        Assert.assertEquals("option1", group.findOption("option1").getOpt());
        Assert.assertNull(group.findOption("nonExistentOption"));

        // test getMinimum and getMaximum
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(1, group.getMaximum());

        // test isRequired
        Assert.assertTrue(group.isRequired());

        // test defaults
        cmd.clear();
        group.defaults(cmd);
        Assert.assertFalse(cmd.hasOptions());
    }

    /**
     * Test for the constructor with name and description
     */
    @Test
    public void testGroupWithNameAndDescription() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create());

        final GroupImpl group = new GroupImpl(options, "My Group", "This is my group");

        Assert.assertEquals("My Group", group.getPreferredName());
        Assert.assertEquals("This is my group", group.getDescription());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(1, group.getMaximum());

        // test canProcess
        final CommandLine cmd = new CommandLine();
        cmd.parse(group.parse());
        Assert.assertTrue(group.canProcess(cmd, "option1"));
        Assert.assertTrue(group.canProcess(cmd, "option2"));

        // test getPrefixes
        Assert.assertEquals(Collections.emptySet(), group.getPrefixes());

        // test getTriggers
        Assert.assertEquals(Collections.unmodifiableSet(Collections.singleton("option1")), group.getTriggers());
        Assert.assertEquals(Collections.unmodifiableSet(Collections.singleton("option2")), group.getTriggers());

        // test process
        final String[] args = {"--my-group", "--option1", "--option2"};
        cmd.parse(group.parse(), args);
        Assert.assertEquals(2, cmd.getNumArgs());

        // test validate
        group.validate(cmd);

        // test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        Assert.assertEquals("My Group:\n" +
                "options:\n" +
                "  option1\n" +
                "  option2\n", sb.toString());

        // test helpLines
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        Assert.assertEquals(2, helpLines.size());

        // test getOptions
        Assert.assertEquals(Collections.unmodifiableList(options), group.getOptions());

        // test findOption
        Assert.assertEquals("option1", group.findOption("option1").getOpt());
        Assert.assertNull(group.findOption("nonExistentOption"));

        // test getMinimum and getMaximum
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(1, group.getMaximum());

        // test isRequired
        Assert.assertTrue(group.isRequired());

        // test defaults
        cmd.clear();
        group.defaults(cmd);
        Assert.assertFalse(cmd.hasOptions());
    }
}