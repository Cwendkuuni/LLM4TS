import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParserBuilder;
import org.apache.commons.cli2.exceptions.ParseException;
import org.apache.commons.cli2.test.TestHelper;
import org.junit.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.apache.commons.cli2.TestHelper.group;
import static org.apache.commons.cli2.TestHelper.option;
import static org.apache.commons.cli2.TestHelper.optionBuilder;

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImpl() throws ParseException {
        // Test construction
        List<Option> options = new ArrayList<>();
        options.add(option("a", "a", false, "Test option A"));
        options.add(option("b", "b", false, "Test option B"));
        options.add(option("c", "c", false, "Test option C"));
        Group group = new GroupImpl(options, "Test Group", "Test Group", 1, 3);

        // Test getters
        Assert.assertEquals("Test Group", group.getPreferredName());
        Assert.assertEquals("Test Group", group.getDescription());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(3, group.getMaximum());
        Assert.assertEquals(options, group.getOptions());
        Assert.assertEquals(Collections.emptyList(), group.getAnonymous());

        // Test canProcess
        CommandLine commandLine = new CommandLine();
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("c");
        Assert.assertTrue(group.canProcess(commandLine, null));

        commandLine = new CommandLine();
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("c");
        commandLine.addOption("d");
        Assert.assertFalse(group.canProcess(commandLine, null));

        commandLine = new CommandLine();
        commandLine.addOption("b");
        commandLine.addOption("a");
        commandLine.addOption("c");
        Assert.assertTrue(group.canProcess(commandLine, null));

        // Test getPrefixes
        Set<String> prefixes = group.getPrefixes();
        Assert.assertEquals(Collections.emptySet(), prefixes);

        // Test getTriggers
        Set<String> triggers = group.getTriggers();
        Assert.assertEquals(Collections.unmodifiableSet(Arrays.asList("a", "b", "c")), triggers);

        // Test process
        commandLine = new CommandLine();
        List<String> arguments = new ArrayList<>();
        commandLine.parse(group, arguments);
        Assert.assertEquals(3, arguments.size());
        Assert.assertEquals("a", arguments.get(0));
        Assert.assertEquals("b", arguments.get(1));
        Assert.assertEquals("c", arguments.get(2));

        // Test validate
        commandLine = new CommandLine();
        group.validate(commandLine);

        commandLine = new CommandLine();
        commandLine.addOption("a");
        commandLine.addOption("b");
        group.validate(commandLine);

        commandLine = new CommandLine();
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("c");
        commandLine.addOption("d");
        group.validate(commandLine);

        commandLine = new CommandLine();
        commandLine.addOption("a");
        commandLine.addOption("b");
        group.validate(commandLine);

        // Test appendUsage
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        group.appendUsage(buffer, helpSettings, null);
        String expectedUsage = "Test Group:\n" +
                "  Test Group (optional, allows null)\n" +
                "    a  Test option A (optional)\n" +
                "    b  Test option B (optional)\n" +
                "    c  Test option C (optional)\n";
        Assert.assertEquals(expectedUsage, buffer.toString());

        // Test helpLines
        List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);
        String expectedHelpLines = "Test Group:\n" +
                "  Test Group (optional, allows null)\n" +
                "    a  Test option A (optional)\n" +
                "      Test option A\n" +
                "    b  Test option B (optional)\n" +
                "      Test option B\n" +
                "    c  Test option C (optional)\n" +
                "      Test option C\n";
        Assert.assertEquals(expectedHelpLines, TestHelper.formatHelpLines(helpLines));

        // Test findOption
        Assert.assertNull(group.findOption("x"));
        Assert.assertEquals("a", group.findOption("a").getOpt());
        Assert.assertEquals("b", group.findOption("b").getOpt());
        Assert.assertEquals("c", group.findOption("c").getOpt());

        // Test isRequired
        Assert.assertFalse(group.isRequired());

        // Test defaults
        group.defaults(commandLine);
        Assert.assertFalse(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));
        Assert.assertFalse(commandLine.hasOption("c"));

        // Test findOption with nested group
        Options parentOptions = new Options();
        parentOptions.addOption(group("parent", "Parent Group", 1, "p", new ArrayList<>()));
        Options childOptions = new Options();
        childOptions.addOption(group("child", "Child Group", 1, "c", new ArrayList<>()));
        childOptions.addOption(option("z", "z", false, "Test option Z"));
        parentOptions.addOption(optionBuilder("g").longOpt("group").valueNames("g").build().setGroup(childOptions));

        CommandLine parentCommandLine = new CommandLine();
        parentCommandLine.parse(parentOptions, arguments);

        Assert.assertEquals(1, arguments.size());
        Assert.assertEquals("g", arguments.get(0));

        CommandLine childCommandLine = (CommandLine) parentCommandLine.getOptionValue("g");
        Assert.assertEquals(1, childCommandLine.getArgs().size());
        Assert.assertEquals("z", childCommandLine.getArgs().get(0));
    }

    @org.junit.Before
    public void setUp() {
        TestHelper.loadDefaultLocales();
    }

    @org.junit.After
    public void tearDown() {
        TestHelper.resetDefaultLocales();
    }
}