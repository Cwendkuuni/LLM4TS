import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.cli2.TestUtils.createTestCommandLine;
import static org.apache.commons.cli2.TestUtils.createTestOptions;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final String groupName = "group";
        final String groupDescription = "this is a group";

        final List<Option> options = new ArrayList<>();
        final Option opt1 = new Option("o1", "option1", true, "option 1 description");
        options.add(opt1);
        final Option opt2 = new Option("o2", "option2", false, "option 2 description");
        options.add(opt2);
        final Argument arg1 = new Argument("arg1", "argument1", true, "argument description");
        options.add(arg1);

        final Group group = new GroupImpl(options, groupName, groupDescription, 1, 2);

        // Test canProcess
        final CommandLine commandLine = createTestCommandLine("--o1 --o2 foo");
        Assert.assertTrue(group.canProcess(commandLine, "foo"));

        final CommandLine commandLine2 = createTestCommandLine("--o1 --o1 --o2 foo");
        Assert.assertTrue(group.canProcess(commandLine2, "foo"));

        final CommandLine commandLine3 = createTestCommandLine("--o1 --o1 --o1 --o2 foo");
        Assert.assertTrue(group.canProcess(commandLine3, "foo"));

        final CommandLine commandLine4 = createTestCommandLine("--o1 --o2 --o1 foo");
        Assert.assertTrue(group.canProcess(commandLine4, "foo"));

        final CommandLine commandLine5 = createTestCommandLine("--o1 --arg1 foo");
        Assert.assertFalse(group.canProcess(commandLine5, "foo"));

        final CommandLine commandLine6 = createTestCommandLine("--o2 foo");
        Assert.assertFalse(group.canProcess(commandLine6, "foo"));

        final CommandLine commandLine7 = createTestCommandLine("--arg1 foo");
        Assert.assertFalse(group.canProcess(commandLine7, "foo"));

        // Test getPrefixes
        Set prefixes = group.getPrefixes();
        Assert.assertEquals(Collections.emptySet(), prefixes);

        // Test getTriggers
        Set triggers = group.getTriggers();
        List<String> triggersList = new ArrayList<>(triggers);
        triggersList.remove(opt1.getOpt());
        triggersList.remove(opt2.getOpt());
        Assert.assertEquals(Collections.emptySet(), triggers);

        // Test process
        CommandLine commandLine8 = createTestCommandLine();
        group.process(commandLine8, commandLine8.iterator());
        Assert.assertNull(commandLine8.getOptionValues("o1")[0]);
        Assert.assertNull(commandLine8.getOptionValues("o2")[0]);
        Assert.assertNull(commandLine8.getOptionValues("arg1")[0]);

        commandLine8.parse(group.getOptions());
        Assert.assertEquals(Collections.singletonList("foo"), commandLine8.getOptionValues("o1"));
        Assert.assertFalse(commandLine8.hasOption("o2"));
        Assert.assertNull(commandLine8.getOptionValues("arg1")[0]);

        // Test validate
        commandLine8.parse(group.getOptions());
        group.validate(commandLine8);

        // Test getPreferredName
        Assert.assertEquals(groupName, group.getPreferredName());

        // Test getDescription
        Assert.assertEquals(groupDescription, group.getDescription());

        // Test appendUsage
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);
        String expectedUsage = "-" + groupName + " [options]\n";
        Assert.assertEquals(expectedUsage, buffer.toString());

        // Test helpLines
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        Assert.assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        StringBuilder helpLineBuffer = new StringBuilder();
        helpLine.print(helpLineBuffer, 0, null);
        String expectedHelpLine = "  -" + groupName + " [options]\n";
        Assert.assertEquals(expectedHelpLine, helpLineBuffer.toString());

        // Test getOptions
        List optionsList = group.getOptions();
        Assert.assertEquals(2, optionsList.size());
        Assert.assertTrue(optionsList.contains(opt1));
        Assert.assertTrue(optionsList.contains(opt2));

        // Test getAnonymous
        List anonymousList = group.getAnonymous();
        Assert.assertEquals(1, anonymousList.size());
        Assert.assertTrue(anonymousList.contains(arg1));

        // Test findOption
        Option foundOption = group.findOption("o1");
        Assert.assertEquals(opt1, foundOption);

        // Test isRequired
        Assert.assertFalse(group.isRequired());

        // Test getMinimum
        Assert.assertEquals(1, group.getMinimum());

        // Test getMaximum
        Assert.assertEquals(2, group.getMaximum());

        // Test defaults
        commandLine8.parse(group.getOptions());
        group.defaults(commandLine8);
        Assert.assertNull(commandLine8.getOptionValues("o1")[0]);
        Assert.assertFalse(commandLine8.hasOption("o2"));
        Assert.assertNull(commandLine8.getOptionValues("arg1")[0]);

        // Test sorting
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Option) o1).getOpt().compareTo((Option) o2).getOpt();
            }
        };
        List sortedOptions = new ArrayList(group.getOptions());
        Collections.sort(sortedOptions, comparator);
        List expectedSortedOptions = new ArrayList(options);
        Collections.sort(expectedSortedOptions, comparator);
        Assert.assertEquals(expectedSortedOptions, sortedOptions);
    }
}