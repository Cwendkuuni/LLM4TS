import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.util.ArgParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.apache.commons.cli2.TestUtils.createTestOption;
import static org.apache.commons.cli2.TestUtils.createTestOptionGroup;

public class GroupImplTest {

    @Test
    public void testGroupImpl() throws Exception {
        final String usage = "GroupImplTest [options]";

        final Options options = new Options();
        final Group optionGroup1 = createTestOptionGroup("group1", options, "-g1,--group1");
        optionGroup1.addOption(createTestOption("opt1", '1', false, "Option 1", null));
        optionGroup1.addOption(createTestOption("opt2", '2', false, "Option 2", null));

        final Group optionGroup2 = createTestOptionGroup("group2", options, "-g2,--group2");
        optionGroup2.addOption(createTestOption("opt3", '3', false, "Option 3", null));
        optionGroup2.addOption(createTestOption("opt4", '4', false, "Option 4", null));

        final Group group = new GroupImpl(List.of(optionGroup1, optionGroup2), null, null, 0, 0);

        final ArgParser parser = new ArgParser();
        parser.setUsage(usage);
        parser.parse(new String[]{"--test"}, group.getParser());
        final CommandLine cmd = parser.getCommandLine();

        Assert.assertEquals(0, cmd.getNumArgs());

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, usage, true);
        formatter.printHelp(System.out, usage, true, "GroupImplTest", group.getHelpLines(null, null, null));
        formatter.printHelp(System.out, usage, true, "GroupImplTest", group.getHelpLines(null, null, null), 5, 5, null);

        // Test getOptionMap
        final Map<String, Option> optionMap = group.getOptionMap();
        Assert.assertEquals(4, optionMap.size());
        Assert.assertEquals(optionGroup1.getOption("opt1"), optionMap.get("opt1"));
        Assert.assertEquals(optionGroup1.getOption("opt2"), optionMap.get("opt2"));
        Assert.assertEquals(optionGroup2.getOption("opt3"), optionMap.get("opt3"));
        Assert.assertEquals(optionGroup2.getOption("opt4"), optionMap.get("opt4"));

        // Test canProcess
        Assert.assertTrue(group.canProcess(cmd, ""));
        Assert.assertTrue(group.canProcess(cmd, "g1"));
        Assert.assertTrue(group.canProcess(cmd, "g1 opt1"));
        Assert.assertTrue(group.canProcess(cmd, "g1 opt1 opt2"));
        Assert.assertTrue(group.canProcess(cmd, "g2"));
        Assert.assertTrue(group.canProcess(cmd, "g2 opt3"));
        Assert.assertTrue(group.canProcess(cmd, "g2 opt3 opt4"));
        Assert.assertFalse(group.canProcess(cmd, "g"));
        Assert.assertFalse(group.canProcess(cmd, "g1 opt1 opt3"));
        Assert.assertFalse(group.canProcess(cmd, "g1 opt2 opt3"));
        Assert.assertFalse(group.canProcess(cmd, "g2 opt1 opt4"));

        // Test getMinimum
        Assert.assertEquals(0, group.getMinimum());

        // Test getMaximum
        Assert.assertEquals(0, group.getMaximum());

        // Test isRequired
        Assert.assertFalse(group.isRequired());

        // Test getName
        Assert.assertNull(group.getName());

        // Test getDescription
        Assert.assertNull(group.getDescription());

        // Test getOptions
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals(optionGroup1, group.getOptions().get(0));
        Assert.assertEquals(optionGroup2, group.getOptions().get(1));

        // Test getAnonymous
        Assert.assertEquals(0, group.getAnonymous().size());

        // Test findOption
        Assert.assertNull(group.findOption("opt5"));
        Assert.assertEquals(optionGroup1.getOption("opt1"), group.findOption("opt1"));
        Assert.assertEquals(optionGroup1.getOption("opt2"), group.findOption("opt2"));
        Assert.assertEquals(optionGroup2.getOption("opt3"), group.findOption("opt3"));
        Assert.assertEquals(optionGroup2.getOption("opt4"), group.findOption("opt4"));

        // Test process
        final CommandLine cmd2 = new CommandLine();
        cmd2.addOption(createTestOption("g1", 0, false, "Group 1", null));
        cmd2.addOption(createTestOption("g2", 0, false, "Group 2", null));
        cmd2.addOption(createTestOption("g1 opt1"));
        cmd2.addOption(createTestOption("g2 opt3"));

        group.process(cmd2, cmd2.listIterator());

        Assert.assertEquals(2, cmd2.getNumArgs());
        Assert.assertEquals("opt1", cmd2.getOptionValues("opt1")[0]);
        Assert.assertEquals("opt3", cmd2.getOptionValues("opt3")[0]);

        // Test validate
        final CommandLine cmd3 = new CommandLine();
        cmd3.addOption(createTestOption("g1", 0, false, "Group 1", null));
        cmd3.addOption(createTestOption("g2", 0, false, "Group 2", null));

        try {
            group.validate(cmd3);
        } catch (final OptionException e) {
            Assert.assertEquals("Option g1 must be present", e.getMessage());
        }

        final CommandLine cmd4 = new CommandLine();
        cmd4.addOption(createTestOption("g1", 0, false, "Group 1", null));
        cmd4.addOption(createTestOption("g2", 0, false, "Group 2", null));
        cmd4.addOption(createTestOption("g1 opt1"));
        cmd4.addOption(createTestOption("g1 opt2"));

        try {
            group.validate(cmd4);
        } catch (final OptionException e) {
            Assert.assertEquals("Option g1opt2 is not allowed", e.getMessage());
        }

        final CommandLine cmd5 = new CommandLine();
        cmd5.addOption(createTestOption("g1", 0, false, "Group 1", null));
        cmd5.addOption(createTestOption("g2", 0, false, "Group 2", null));
        cmd5.addOption(createTestOption("g1 opt1"));
        cmd5.addOption(createTestOption("g1 opt2"));
        cmd5.addOption(createTestOption("g1 opt1"));

        try {
            group.validate(cmd5);
        } catch (final OptionException e) {
            Assert.assertEquals("Option g1opt1 is duplicated", e.getMessage());
        }

        final CommandLine cmd6 = new CommandLine();
        cmd6.addOption(createTestOption("g1", 0, false, "Group 1", null));
        cmd6.addOption(createTestOption("g2", 0, false, "Group 2", null));
        cmd6.addOption(createTestOption("g1 opt1"));
        cmd6.addOption(createTestOption("g1 opt2"));
        cmd6.addOption(createTestOption("g2 opt3"));
        cmd6.addOption(createTestOption("g2 opt3"));

        try {
            group.validate(cmd6);
        } catch (final OptionException e) {
            Assert.assertEquals("Option g2opt3 is duplicated", e.getMessage());
        }

        // Test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, null, null);
        Assert.assertEquals("", sb.toString());

        final StringBuilder sb2 = new StringBuilder();
        group.appendUsage(sb2, null, null, "|");
        Assert.assertEquals("GroupImplTest:\n" +
                "Options:\n" +
                "  group1\n" +
                "    Option 1\n" +
                "    Option 2\n" +
                "  group2\n" +
                "    Option 3\n" +
                "    Option 4\n", sb2.toString());

        final StringBuilder sb3 = new StringBuilder();
        group.appendUsage(sb3, null, null, "|");
        Assert.assertEquals("GroupImplTest:\n" +
                "Options:\n" +
                "  group1\n" +
                "    Option 1\n" +
                "    Option 2\n" +
                "  group2\n" +
                "    Option 3\n" +
                "    Option 4\n", sb3.toString());

        final StringBuilder sb4 = new StringBuilder();
        group.appendUsage(sb4, null, null, "|");
        Assert.assertEquals("GroupImplTest:\n" +
                "Options:\n" +
                "  group1\n" +
                "    Option 1\n" +
                "    Option 2\n" +
                "  group2\n" +
                "    Option 3\n" +
                "    Option 4\n", sb4.toString());

        final StringBuilder sb5 = new StringBuilder();
        group.appendUsage(sb5, null, null, "\n");
        Assert.assertEquals("GroupImplTest:\n" +
                "Options:\n" +
                "  group1\n" +
                "    Option 1\n" +
                "    Option 2\n" +
                "  group2\n" +
                "    Option 3\n" +
                "    Option 4\n", sb5.toString());

        final StringBuilder sb6 = new StringBuilder();
        group.appendUsage(sb6, null, null, "\n");
        Assert.assertEquals("GroupImplTest:\n" +
                "Options:\n" +
                "  group1\n" +
                "    Option 1\n" +
                "    Option 2\n" +
                "  group2\n" +
                "    Option 3\n" +
                "    Option 4\n", sb6.toString());

        final StringBuilder sb7 = new StringBuilder();
        group.appendUsage(sb7, null, null, "\n");
        Assert.assertEquals("GroupImplTest:\n" +
                "Options:\n" +
                "  group1\n" +
                "    Option 1\n" +
                "    Option 2\n" +
                "  group2\n" +
                "    Option 3\n" +
                "    Option 4\n", sb7.toString());

        // Test helpLines
        final List<HelpLine> helpLines = group.helpLines(1, null, null);
        Assert.assertEquals(12, helpLines.size());

        final List<HelpLine> helpLines2 = group.helpLines(1, null, null);
        Assert.assertEquals(12, helpLines2.size());

        final List<HelpLine> helpLines3 = group.helpLines(1, null, null);
        Assert.assertEquals(12, helpLines3.size());

        final List<HelpLine> helpLines4 = group.helpLines(1, null, null);
        Assert.assertEquals(12, helpLines4.size());

        // Test getPrefixes
        Assert.assertEquals(Collections.emptySet(), group.getPrefixes());

        // Test getTriggers
        Assert.assertEquals(Collections.emptySet(), group.getTriggers());

        // Test defaults
        parser.parse(new String[]{"--test"}, group.getParser());
        final CommandLine cmd7 = parser.getCommandLine();
        Assert.assertNull(cmd7.getOptionValue("g1"));
        Assert.assertNull(cmd7.getOptionValue("g2"));

        final CommandLine cmd8 = new CommandLine();
        cmd8.addOption(createTestOption("g1", 0, false, "Group 1", null));
        cmd8.addOption(createTestOption("g2", 0, false, "Group 2", null));

        group.defaults(cmd8);

        Assert.assertNull(cmd8.getOptionValue("g1"));
        Assert.assertNull(cmd8.getOptionValue("g2"));

        final CommandLine cmd9 = new CommandLine();
        cmd9.addOption(createTestOption("g1", 0, false, "Group 1", null));
        cmd9.addOption(createTestOption("g2", 0, false, "Group 2", null));
        cmd9.addOption(createTestOption("g1 opt1", 1, false, "Option 1", null));
        cmd9.addOption(createTestOption("g2 opt3", 1, false, "Option 3", null));

        group.defaults(cmd9);

        Assert.assertEquals("", cmd9.getOptionValue("g1"));
        Assert.assertEquals("", cmd9.getOptionValue("g2"));
        Assert.assertEquals("", cmd9.getOptionValue("g1 opt1"));
        Assert.assertEquals("", cmd9.getOptionValue("g2 opt3"));
    }
}