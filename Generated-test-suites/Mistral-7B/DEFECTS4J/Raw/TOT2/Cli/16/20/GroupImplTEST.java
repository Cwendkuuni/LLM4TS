import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setUp() {
        options = Arrays.asList(
                new Option("a", "argument A", false),
                new Option("b", "argument B", true, 3),
                new Option("c", "argument C", false, 1),
                new Option("d", "argument D", false),
                new Option("e", "argument E", false),
                new Option("f", "argument F", true),
                new Group("g", "group G") {
                    {
                        setMinimum(1);
                        setMaximum(3);
                        this.options = Arrays.asList(
                                new Option("h", "group H"),
                                new Option("i", "group I")
                        );
                    }
                }
        );

        List<Argument> anonymousArgs = new ArrayList<>();
        anonymousArgs.add(new Argument("j", "anonymous argument J", true));
        anonymousArgs.add(new Argument("k", "anonymous argument K", true));

        group = new GroupImpl(options, "groupName", "Group description", 1, 5) {
            {
                this.anonymous = Collections.unmodifiableList(anonymousArgs);
            }
        };
    }

    @Test
    public void testGetName() {
        assertEquals("groupName", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Group description", group.getDescription());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine cmd = new WriteableCommandLine();

        // Test with a valid option
        cmd.addOption("--a");
        assertTrue(group.canProcess(cmd, "a"));

        // Test with a non-valid option
        cmd.clear();
        assertFalse(group.canProcess(cmd, "z"));

        // Test with an argument
        cmd.addOptionArguments("--j", "value");
        assertTrue(group.canProcess(cmd, "j"));

        // Test with an option with a minimum number of occurrences
        cmd.addOption("--c");
        cmd.addOption("--c");
        cmd.addOption("--c");
        assertTrue(group.canProcess(cmd, "c"));

        // Test with an option with a maximum number of occurrences
        cmd.addOption("--b");
        cmd.addOption("--b");
        cmd.addOption("--b");
        assertTrue(group.canProcess(cmd, "b"));
        assertFalse(group.canProcess(cmd, "b"));

        // Test with an option in a group
        cmd.clear();
        cmd.addOption("--g");
        cmd.addOption("--h");
        cmd.addOption("--i");
        assertTrue(group.canProcess(cmd, "h"));
        assertTrue(group.canProcess(cmd, "i"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> expectedPrefixes = new HashSet<>(Arrays.asList("g"));
        Set<String> actualPrefixes = group.getPrefixes();

        assertEquals(expectedPrefixes, actualPrefixes);
    }

    @Test
    public void testGetTriggers() {
        Set<String> expectedTriggers = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));
        Set<String> actualTriggers = new HashSet<>(group.getTriggers());

        assertEquals(expectedTriggers, actualTriggers);
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine();

        // Test with valid options
        cmd.addOption("--a");
        cmd.addOption("--b");
        cmd.addOptionArguments("--j", "value1");
        cmd.addOptionArguments("--j", "value2");

        group.process(cmd, cmd.listIterator());

        Map<String, Option> actualOptionMap = group.getOptionMap();

        Option aOption = actualOptionMap.get("a");
        assertTrue(aOption.isSet());

        Option bOption = actualOptionMap.get("b");
        assertTrue(bOption.isSet());

        Option jOption1 = actualOptionMap.get("j");
        assertEquals("value1", jOption1.getValue());

        Option jOption2 = actualOptionMap.get("j");
        assertEquals("value2", jOption2.getValue());

        // Test with an invalid option
        cmd.clear();
        cmd.addOption("--x");
        cmd.addOptionArguments("--k", "value");
        cmd.addOption("--c");
        cmd.addOption("--c");

        List<String> errors = new ArrayList<>();
        group.process(cmd, cmd.listIterator(), errors);

        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine();

        // Test with a valid command line
        cmd.addOption("--a");
        cmd.addOption("--b");
        cmd.addOptionArguments("--j", "value1");
        cmd.addOptionArguments("--j", "value2");
        cmd.addOption("--d");
        cmd.addOption("--c");
        cmd.addOptionArguments("--k", "value");

        group.validate(cmd);

        // Test with a missing option
        cmd.clear();
        cmd.addOption("--a");
        cmd.addOption("--c");

        List<String> errors = new ArrayList<>();
        group.validate(cmd);

        List<String> expectedErrors = new ArrayList<>(Arrays.asList(
                "Missing option: --d",
                "This option '--d' is required."
        ));

        assertEquals(expectedErrors, errors);

        // Test with an extra option
        cmd.clear();
        cmd.addOption("--a");
        cmd.addOption("--b");
        cmd.addOption("--e");
        cmd.addOptionArguments("--j", "value1");
        cmd.addOptionArguments("--j", "value2");
        cmd.addOption("--d");
        cmd.addOption("--c");
        cmd.addOptionArguments("--k", "value");

        List<String> expectedError = new ArrayList<>(Arrays.asList(
                "Extra option: --e",
                "This option '--e' is not recognized."
        ));

        errors.clear();
        group.validate(cmd);

        assertEquals(expectedError, errors);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "--groupName\n" +
                "group description\n" +
                "\n" +
                "arguments:\n" +
                "  --a {boolean} argument A (false)\n" +
                "  --b {boolean} argument B (true)\n" +
                "  --c {integer} argument C (1)\n" +
                "  --d {boolean}\n" +
                "  --e {boolean}\n" +
                "  --f {boolean}\n" +
                "  --g {group}\n" +
                "    group G\n" +
                "      arguments:\n" +
                "        --h {}\n" +
                "        --i {}\n" +
                "  --j {string} anonymous argument J (true)\n" +
                "  --k {string} anonymous argument K (true)\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        String expectedUsage = "--groupName\n" +
                "group description\n" +
                "\n" +
                "arguments:\n" +
                "  --a\n" +
                "      argument A (false)\n" +
                "  --b\n" +
                "      argument B (true)\n" +
                "  --c <integer>\n" +
                "      argument C (1)\n" +
                "  --d\n" +
                "      \n" +
                "  --e\n" +
                "      \n" +
                "  --f\n" +
                "      \n" +
                "  --g\n" +
                "      group G\n" +
                "        arguments:\n" +
                "          --h\n" +
                "          --i\n";

        assertEquals(expectedUsage, helpLines.get(0).getShortOpt() + "\n" +
                helpLines.get(0).getLongOpt() + "\n" +
                helpLines.get(0).getDescription());

        for (HelpLine helpLine : helpLines.subList(1, helpLines.size())) {
            assertNull(helpLine.getShortOpt());
            assertNull(helpLine.getLongOpt());
            assertEquals("", helpLine.getDescription());
        }
    }
}