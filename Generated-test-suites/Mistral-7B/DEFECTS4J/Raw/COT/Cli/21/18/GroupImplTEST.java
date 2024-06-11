package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private GroupImpl group;
    private Option option1;
    private Option option2;
    private Option option3;
    private Argument argument;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();

        option1 = new Option("o1", "option 1", true, "o1");
        option1.setValueSeparator('=');
        option1.setRequired(false);

        options.add(option1);

        option2 = new Option("o2", "option 2", true, "o2");
        option2.setValueSeparator('=');
        option2.setRequired(true);

        options.add(option2);

        option3 = new Option("o3", "option 3");
        option3.setValueSeparator('=');
        option3.setRequired(false);

        List<Argument> anonymous = new ArrayList<>();
        argument = new Argument("arg", "argument");
        anonymous.add(argument);

        group = new GroupImpl(options, "group", null, 1, 3, false);
        group.setAnonymous(anonymous);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test with no arguments
        assertFalse(group.canProcess(commandLine, null));

        // Test with an option
        commandLine.addOption("o1", "value1");
        assertTrue(group.canProcess(commandLine, "o1"));

        // Test with a trigger that doesn't exist
        assertFalse(group.canProcess(commandLine, "nonexistent"));

        // Test with an anonymous argument
        commandLine.addArgument("arg");
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = group.getTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.containsAll(Collections.singletonList("o1")));
        assertTrue(triggers.containsAll(Collections.singletonList("o2")));
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        // Test with an empty command line
        group.process(commandLine, arguments);
        assertTrue(arguments.isEmpty());

        // Test with an option
        commandLine.addOption("o1", "value1");
        arguments.next(); // move past the command line prefix
        group.process(commandLine, arguments);
        assertEquals(option1, commandLine.getOption("o1"));
        assertEquals("value1", option1.getValue());

        // Test with an anonymous argument
        commandLine.addArgument("value");
        arguments.next(); // move past the anonymous argument
        group.process(commandLine, arguments);
        assertEquals(argument, commandLine.getOption("arg"));
        assertEquals("value", argument.getValue());

        // Test with an option that requires a value
        commandLine.addOption("o2", "value2");
        arguments.next(); // move past the option trigger
        arguments.next(); // move past the value separator
        arguments.next(); // move past the value
        group.process(commandLine, arguments);
        assertEquals(option2, commandLine.getOption("o2"));
        assertEquals("value2", option2.getValue());

        // Test with an option and an anonymous argument
        commandLine.addOption("o1", "value1");
        arguments.next(); // move past the option trigger
        commandLine.addArgument("value");
        group.process(commandLine, arguments);
        assertEquals(option1, commandLine.getOption("o1"));
        assertEquals("value1", option1.getValue());
        assertEquals(argument, commandLine.getOption("arg"));
        assertEquals("value", argument.getValue());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test with no options
        group.validate(commandLine);

        // Test with an option and the required minimum
        commandLine.addOption("o1", "value1");

        // Test with an option and less than the minimum
        commandLine.removeOption("o1");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with an option and more than the maximum
        commandLine.addOption("o3", "value3");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // Test with an anonymous argument
        commandLine.addArgument("value");

        // Test with no anonymous arguments and the required minimum
        group.anonymous.clear();
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNull(group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Map<DisplaySetting, Boolean> helpSettings = new LinkedHashMap<>();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_NAME, true);

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "group: [o1=VALUE] [o2=VALUE]";
        assertEquals(expectedUsage, buffer.toString());

        // Test with expanded options
        helpSettings.clear();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_EXPANDED, true);

        group.appendUsage(buffer, helpSettings, null);

        expectedUsage = "group: [o1=VALUE]\n" +
                "  o1: BOOLEAN_VALUE [ARG]...\n" +
                "o2: BOOLEAN_VALUE [ARG]...\n";
        assertEquals(expectedUsage, buffer.toString());

        // Test with optional arguments
        helpSettings.clear();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_ARGUMENT, true);

        group.appendUsage(buffer, helpSettings, null);

        expectedUsage = "group: [o1=VALUE] [o2=VALUE]\n" +
                "  o1: BOOLEAN_VALUE [ARG]...\n" +
                "  o2: BOOLEAN_VALUE [ARG]...\n" +
                "[arg]...\n";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        StringBuffer expectedUsage = new StringBuffer();
        expectedUsage.append("group: [o1=VALUE] [o2=VALUE]\n")
                .append("  o1: option 1\n")
                .append("  o1: BOOLEAN_VALUE [ARG]...\n")
                .append("  o2: option 2\n")
                .append("  o2: BOOLEAN_VALUE [ARG]...\n")
                .append("[arg]...\n");

        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        for (HelpLine helpLine : helpLines) {
            assertEquals(expectedUsage.toString(), helpLine.getLine());
        }
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetAnonymous() {
        List<Argument> anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertTrue(anonymous.contains(argument));
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("o1");
        assertSame(option1, foundOption);
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
        WriteableCommandLine commandLine = new WriteableCommandLine();

        group.defaults(commandLine);

        // Test with no options
        assertNull(commandLine.getOption("o1"));
        assertNull(commandLine.getOption("o2"));
        assertNull(commandLine.getOption("o3"));
        assertNull(commandLine.getOption("arg"));

        // Test with options
        commandLine.addOption("o1", false);
        group.defaults(commandLine);
        assertNull(commandLine.getOption("o1"));

        commandLine.addOptions(Collections.singletonList(option1));
        group.defaults(commandLine);
        assertEquals(option1.getDefaultValue(), option1.getValue());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test with no arguments
        assertFalse(group.looksLikeOption(commandLine, null));

        // Test with an option
        commandLine.addOption("o1", "value1");
        assertTrue(group.looksLikeOption(commandLine, "o1"));

        // Test with an anonymous argument
        commandLine.addArgument("value");
        assertFalse(group.looksLikeOption(commandLine, null));
    }
}