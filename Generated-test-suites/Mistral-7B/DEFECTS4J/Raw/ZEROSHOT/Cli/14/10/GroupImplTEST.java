import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.apache.commons.cli2.test.TestUtils.createGroup;

public class GroupImplTest {

    private Options options;
    private GroupImpl group;

    @BeforeEach
    public void setup() {
        options = new Options();

        // Define a group with some options and arguments
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(Option.builder("g1")
                .longOpt("group1")
                .requiredOption()
                .build());
        groupOptions.add(Option.builder("g2")
                .longOpt("group2")
                .hasArg()
                .argName("VALUE")
                .build());
        groupOptions.add(Option.builder("a1")
                .longOpt("arg1")
                .argName("VALUE")
                .isRequired(false)
                .build());
        group = createGroup("MyGroup", "My Group Description", groupOptions, 1, 1);

        options.addOption(group);
        options.addOption(Option.builder("o1")
                .longOpt("option1")
                .requiredOption()
                .build());
        options.addOption(Option.builder("o2")
                .longOpt("option2")
                .hasArg()
                .argName("VALUE")
                .build());
    }

    @Test
    public void testCanProcess() {
        CommandLine cmd = TestUtils.createCommandLineWithParsedArgs("--g1", "--o1", "--g2 VALUE");

        Assertions.assertTrue(group.canProcess(cmd, "g1"), "Group should be able to process 'g1'");
        Assertions.assertTrue(group.canProcess(cmd, "g2"), "Group should be able to process 'g2'");
        Assertions.assertFalse(group.canProcess(cmd, "o1"), "Group should not be able to process 'o1'");
        Assertions.assertFalse(group.canProcess(cmd, "o2"), "Group should not be able to process 'o2'");
        Assertions.assertFalse(group.canProcess(cmd, "a1"), "Group should not be able to process 'a1'");
    }

    @Test
    public void testProcess() {
        CommandLine cmd = TestUtils.createCommandLineWithParsedArgs("--g1", "--o1", "--g2 VALUE");

        group.process(cmd, TestUtils.createListIterator("--g1", "--o1", "--g2 VALUE"));

        Assertions.assertEquals(1, cmd.getOptionValues("g1").length, "Group 'g1' should have been processed");
        Assertions.assertEquals(1, cmd.getOptionValues("o1").length, "Option 'o1' should not have been processed");
        Assertions.assertEquals(1, cmd.getOptionValues("g2").length, "Group 'g2' should have been processed");
        Assertions.assertEquals(1, cmd.getOptionValues("a1").length, "Argument 'a1' should not have been processed");
    }

    @Test
    public void testValidate() {
        CommandLine cmd = TestUtils.createCommandLineWithParsedArgs("--g1", "--o1", "--o2 VALUE");

        group.validate(cmd);

        Assertions.assertEquals(1, cmd.getOptionValues("g1").length, "Group 'g1' should have been validated");
        Assertions.assertEquals(1, cmd.getOptionValues("o1").length, "Option 'o1' should have been validated");
        Assertions.assertEquals(1, cmd.getOptionValues("o2").length, "Option 'o2' should have been validated");
        Assertions.assertEquals(0, cmd.getOptionValues("a1").length, "Argument 'a1' should not have been validated");
    }

    @Test
    public void testGetPreferredName() {
        Assertions.assertEquals("MyGroup", group.getPreferredName(), "Group name should be 'MyGroup'");
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals("My Group Description", group.getDescription(), "Group description should be 'My Group Description'");
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);

        Assertions.assertEquals("-MyGroup\n" +
                "  My Group Description\n" +
                "  (g1)\n" +
                "  (g2 VALUE)\n" +
                "  [arg1 VALUE]\n", buffer.toString(), "Usage string should be generated correctly");
    }

    @Test
    public void testHelpLines() {
        final List<String> helpLines = group.helpLines(0, Collections.emptySet(), null);

        Assertions.assertEquals(Collections.singletonList(
                "-MyGroup, My Group Description:" +
                        " (g1) required. " +
                        " (g2 VALUE) optional. argument:" +
                        " VALUE. " +
                        "[arg1 VALUE] optional. argument:" +
                        " VALUE."), helpLines, "Help lines should be generated correctly");
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();

        Assertions.assertEquals(3, options.size(), "Group should have 3 options");
        Assertions.assertEquals(group.findOption("g1"), options.get(0), "First option should be 'g1'");
        Assertions.assertEquals(group.findOption("g2"), options.get(1), "Second option should be 'g2'");
        Assertions.assertEquals(group.findOption("a1"), options.get(2), "Third option should be 'a1'");
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();

        Assertions.assertEquals(0, anonymous.size(), "Group should have no anonymous options");
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption("g1");
        Option option2 = group.findOption("g2");
        Option optionA1 = group.findOption("a1");
        Option optionO1 = group.findOption("o1");

        Assertions.assertNotNull(option1, "Group should have an option named 'g1'");
        Assertions.assertNotNull(option2, "Group should have an option named 'g2'");
        Assertions.assertNull(optionA1, "Group should not have an option named 'a1'");
        Assertions.assertNull(optionO1, "Group should not have an option named 'o1'");
    }

    @Test
    public void testGetMinimum() {
        Assertions.assertEquals(1, group.getMinimum(), "Group minimum should be 1");
    }

    @Test
    public void testGetMaximum() {
        Assertions.assertEquals(1, group.getMaximum(), "Group maximum should be 1");
    }

    @Test
    public void testIsRequired() {
        Assertions.assertTrue(group.isRequired(), "Group should be required");
    }

    @Test
    public void testDefaults() {
        CommandLine cmd = TestUtils.createCommandLine();

        group.defaults(cmd);

        Assertions.assertNull(cmd.getOptionValues("g1"), "Group 'g1' should not have been set by defaults");
        Assertions.assertNull(cmd.getOptionValues("g2"), "Group 'g2' should not have been set by defaults");
        Assertions.assertNull(cmd.getOptionValues("o1"), "Option 'o1' should not have been set by defaults");
        Assertions.assertNull(cmd.getOptionValues("o2"), "Option 'o2' should not have been set by defaults");
        Assertions.assertNull(cmd.getOptionValues("a1"), "Argument 'a1' should not have been set by defaults");
    }
}