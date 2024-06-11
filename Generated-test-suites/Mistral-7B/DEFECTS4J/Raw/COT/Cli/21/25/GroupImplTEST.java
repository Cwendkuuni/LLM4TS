package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setup() {
        options = new ArrayList<>();
        options.add(new Option("o1", "Option 1", false, "o1"));
        options.add(new Option("o2", "Option 2", false, "o2"));
        options.add(new Option("o3", "Option 3", true, "o3"));
        options.add(new Argument("a1", "Argument 1", false, "arg1"));

        group = new GroupImpl(options, null, null, 0, Integer.MAX_VALUE, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(group);
        assertEquals(3, group.size());
        assertEquals(0, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertTrue(group.isOptional());
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("", true);

        // Test with valid option
        commandLine.setOption("o1", "value1");
        assertTrue(group.canProcess(commandLine, "o1"));

        // Test with a valid option trigger
        commandLine.setOption("o2", "value2");
        assertTrue(group.canProcess(commandLine, "o2"));

        // Test with an invalid option trigger
        assertFalse(group.canProcess(commandLine, "o4"));

        // Test with a valid argument
        commandLine.addArgument("arg1");
        assertTrue(group.canProcess(commandLine, "a1"));

        // Test with an invalid argument
        assertFalse(group.canProcess(commandLine, "arg2"));

        // Test with empty command line
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.containsAll(Arrays.asList("o1", "o2", "o3")));
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("", true);
        commandLine.setOption("o1", "value1");
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        // Test processing options
        group.process(commandLine, arguments);
        assertEquals("value1", commandLine.getOptionValue("o1"));

        // Test processing an argument
        arguments.next();
        group.process(commandLine, arguments);
        assertEquals("arg1", commandLine.getArgument("a1"));

        // Test processing an invalid option
        assertFalse(group.canProcess(commandLine, "o4"));
        try {
            group.process(commandLine, arguments);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("", true);

        // Test validation with valid options
        group.validate(commandLine);

        // Test validation with invalid options
        commandLine.addOption("o1");
        commandLine.addOption("o1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(ResourceConstants.TOO_MANY_OPTIONS, e.getMessage());
        }

        // Test validation with missing required options
        commandLine.removeOption("o3");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(ResourceConstants.MISSING_OPTION, e.getMessage());
        }
    }

    @Test
    public void testGetPreferredName() {
        assertNull(group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNull(group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "group: [o1 OPTION]\n" +
                "          [o2 OPTION]\n" +
                "          [o3 OPTION]\n" +
                "          arg1\n";
        String actualUsage = buffer.toString();
        assertEquals(expectedUsage, actualUsage);

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettings, null);

        expectedUsage = "group (NAME)\n" +
                "  o1 OPTION\n" +
                "  o2 OPTION\n" +
                "  o3 OPTION\n" +
                "  arg1\n";
        expectedUsage += "\n"; // newline for indentation of sub-options

        for (Option option : options) {
            expectedUsage += option.getHelpString();
            expectedUsage += "\n"; // newline for indentation of sub-options
        }

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, null, null);

        String expectedHelpLines = "group\n" +
                "  o1\n" +
                "  o2\n" +
                "  o3\n" +
                "  arg1\n";

        String actualHelpLines = "";
        for (HelpLine helpLine : helpLines) {
            actualHelpLines += helpLine.getShortOption() + "\n";
            actualHelpLines += helpLine.getLongOption() + "\n";
            actualHelpLines += helpLine.getDescription() + "\n";
        }

        assertEquals(expectedHelpLines, actualHelpLines);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.containsAll(group.options));
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertTrue(anonymous.contains(group.findOption("a1")));
    }

    @Test
    public void testFindOption() {
        Option o1 = group.findOption("o1");
        assertSame(options.get(0), o1);

        Option o3 = group.findOption("o3");
        assertSame(options.get(2), o3);

        Option a1 = group.findOption("a1");
        assertSame(group.findOption("arg1"), a1);

        Option o4 = group.findOption("o4");
        assertNull(o4);
    }

    @Test
    public void testGetMinimum() {
        GroupImpl groupWithMinimum = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE, false);
        assertEquals(1, groupWithMinimum.getMinimum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMinimum() {
        new GroupImpl(options, null, null, -1, Integer.MAX_VALUE, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMaximum() {
        new GroupImpl(options, null, null, 0, -1, false);
    }
}