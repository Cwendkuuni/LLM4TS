package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    private List<Option> options;
    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        options.add(new Option("a", "testOptionA", true, "Test Option A"));
        options.add(new Option("b", "testOptionB", false, "Test Option B"));
        options.add(new Option("c", "testOptionC", false, "Test Option C"));
        options.add(new Option("d", "testOptionD", false, "Test Option D"));
        options.add(new Argument("e", "testArgE", Integer.TYPE, "Test Argument E"));

        group = new GroupImpl(options, "testGroup", "Test Group", 1, 3, false);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testGroupConstruction() {
        assertEquals("testGroup", group.getPreferredName());
        assertEquals("Test Group", group.getDescription());
        assertEquals(options, group.getOptions());
        assertEquals(Collections.emptyList(), group.getAnonymous());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testCanProcess() {
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("c");

        // Test valid option
        assertTrue(group.canProcess(commandLine, "a"));

        // Test option not present
        assertFalse(group.canProcess(commandLine, "z"));

        // Test option not in group, but a parent option can process it
        assertTrue(group.canProcess(commandLine, "d"));

        // Test option with a different trigger, but the same option is present
        commandLine.addOption("a");
        assertTrue(group.canProcess(commandLine, "a"));

        // Test option with a different trigger, but not present
        assertFalse(group.canProcess(commandLine, "x"));

        // Test anonymous argument
        commandLine.addArgument("e", "10");
        assertTrue(group.canProcess(commandLine, "e"));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalid() {
        // Test too few options
        commandLine.addOption("a");
        commandLine.addOption("b");
        group.process(commandLine, commandLine.getArguments().listIterator());

        // Test too many options
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("a");
        commandLine.addOption("a");
        group.process(commandLine, commandLine.getArguments().listIterator());
    }

    @Test
    public void testProcessValid() {
        commandLine.addOption("a");
        commandLine.addOption("b");
        commandLine.addOption("c");
        group.process(commandLine, commandLine.getArguments().listIterator());

        Map<String, Option> expectedMap = new HashMap<>();
        expectedMap.put("a", options.get(0));
        expectedMap.put("b", options.get(1));
        expectedMap.put("c", options.get(2));

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));

        Map<String, Option> actualMap = new HashMap<>();
        for (Option option : commandLine.getOptions()) {
            actualMap.put(option.getOpt(), option);
        }

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testValidate() throws OptionException {
        commandLine.addOption("a");
        commandLine.addOption("b");

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalid() {
        commandLine.addOption("a");

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "testGroup (Test Group)\n";
        expectedUsage += "  testOptionA: Test Option A -Test Option A\n";
        expectedUsage += "  testOptionB: Test Option B\n";
        expectedUsage += "  testOptionC: Test Option C\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, null, null);

        assertEquals(1, helpLines.size());

        String expectedUsage = "testGroup\n";
        expectedUsage += "  testGroup:\n";
        expectedUsage += "    testOptionA: Test Option A -Test Option A\n";
        expectedUsage += "    testOptionB: Test Option B\n";
        expectedUsage += "    testOptionC: Test Option C\n";

        assertEquals(expectedUsage, helpLines.get(0).getHelpLine());
    }

    @Test
    public void testFindOption() {
        Option optionA = group.findOption("a");
        assertEquals(options.get(0), optionA);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        assertTrue(commandLine.hasOption("a", false));
        assertTrue(commandLine.hasOption("b", false));
        assertTrue(commandLine.hasOption("c", false));

        assertFalse(commandLine.hasOption("d"));
        assertFalse(commandLine.hasOption("e"));
    }
}