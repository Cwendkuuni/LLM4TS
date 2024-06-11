package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        List<Option> options = new ArrayList<>();
        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        assertEquals("group", group.getName());
        assertEquals("group description", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertTrue(Collections.isEmpty(group.getAnonymous()));
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.unmodifiableMap(Collections.singletonMap("", group)), group.getOptionMap());
        assertEquals(Collections.unmodifiableSet(Collections.emptySet()), group.getPrefixes());
    }

    @Test
    public void testCanProcess() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("option1", "option1 description", true, "o");
        Option option2 = new Option("option2", "option2 description", false, "d");
        Argument arg = new Argument("arg", "argument description", false);

        options.add(option1);
        options.add(option2);
        options.add(arg);

        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        // valid option
        assertEquals(option1, group.findOption("o"));
        assertTrue(group.canProcess(commandLine, "o"));

        // option with trigger
        assertEquals(option1, group.findOption("option1"));
        assertTrue(group.canProcess(commandLine, "option1"));

        // invalid option
        assertFalse(group.canProcess(commandLine, "invalid"));

        // option with bursting
        assertEquals(option2, group.findOption("d"));
        assertTrue(group.canProcess(commandLine, "d"));

        // anonymous argument
        assertTrue(group.canProcess(commandLine, "arg"));

        // empty command line
        assertFalse(group.canProcess(commandLine, ""));
    }

    @Test
    public void testProcess() throws OptionException {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("option1", "option1 description", true, "o");
        Option option2 = new Option("option2", "option2 description", false, "d");
        Argument arg = new Argument("arg", "argument description", false);

        options.add(option1);
        options.add(option2);
        options.add(arg);

        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        List<String> arguments = Arrays.asList("o", "arg", "d");

        group.process(commandLine, arguments.listIterator());

        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertFalse(commandLine.hasOption("invalid"));
        assertTrue(commandLine.hasArgument("arg"));
    }

    @Test
    public void testValidate() throws OptionException {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("option1", "option1 description", true, "o");
        Option option2 = new Option("option2", "option2 description", false, "d");
        Argument arg = new Argument("arg", "argument description", false);

        options.add(option1);
        options.add(option2);
        options.add(arg);

        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        // valid command line
        group.validate(commandLine);

        // missing option
        commandLine.clear();
        commandLine.addOption("test");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        // too many options
        commandLine.clear();
        commandLine.addOption("o");
        commandLine.addOption("o");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        // missing argument
        commandLine.clear();
        commandLine.addOption("o");
        commandLine.addArgument("arg");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        // too few arguments
        commandLine.clear();
        commandLine.addOption("o");
        commandLine.addOption("o");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testAppendUsage() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("option1", "option1 description", true, "o");
        Option option2 = new Option("option2", "option2 description", false, "d");
        Argument arg = new Argument("arg", "argument description", false);

        options.add(option1);
        options.add(option2);
        options.add(arg);

        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        StringBuilder buffer = new StringBuilder();

        // no settings
        group.appendUsage(buffer, null, null, "");
        assertEquals("group description\n", buffer.toString());

        // required group and options
        Map<DisplaySetting, Boolean> helpSettings = new HashMap<>();
        helpSettings.put(DisplaySetting.DISPLAY_OPTIONAL, false);
        helpSettings.put(DisplaySetting.DISPLAY_OPTIONAL_CHILD_GROUP, false);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_NAME, false);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_EXPANDED, false);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_ARGUMENT, false);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_OUTER, false);

        group.appendUsage(buffer, helpSettings, null, "");
        assertEquals("group description\n", buffer.toString());

        // optional group and options
        helpSettings.clear();
        helpSettings.put(DisplaySetting.DISPLAY_OPTIONAL, true);

        group.appendUsage(buffer, helpSettings, null, "");
        assertEquals("[group]\n", buffer.toString());

        // named group and options
        helpSettings.clear();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_NAME, true);

        group.appendUsage(buffer, helpSettings, null, "");
        assertEquals("group:\ngroup description\n", buffer.toString());

        // expanded group and options
        helpSettings.clear();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_EXPANDED, true);

        group.appendUsage(buffer, helpSettings, null, "");
        assertEquals("group description\n\n--option1 <option1>\n--option2\narg:\n", buffer.toString());

        // group with argument
        helpSettings.clear();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_ARGUMENT, true);

        group.appendUsage(buffer, helpSettings, null, "");
        assertEquals("group description\n\n--option1 <option1>\n--option2\narg: arg\n", buffer.toString());

        // group with separator
        helpSettings.clear();
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_ARGUMENT, true);
        helpSettings.put(DisplaySetting.DISPLAY_GROUP_NAME, true);

        group.appendUsage(buffer, helpSettings, new ReverseStringComparator(), "|");
        assertEquals("group:\ngroup description\n\n--option1 <option1>\n--option2\narg: arg\n", buffer.toString());

        // group with sorted options
        Map<DisplaySetting, Boolean> helpSettingsSorted = new HashMap<>(helpSettings);
        helpSettingsSorted.put(DisplaySetting.DISPLAY_SORT_ORDER, true);

        group.appendUsage(buffer, helpSettingsSorted, null, "");
        assertEquals("group description\n\n--option1 <option1>\n--option2\narg: arg\n", buffer.toString());

        // group with sorted options and separator
        helpSettingsSorted.put(DisplaySetting.DISPLAY_GROUP_NAME, true);

        group.appendUsage(buffer, helpSettingsSorted, new ReverseStringComparator(), "|");
        assertEquals("group:\ngroup description\n\n--option2\n--option1 <option1>\narg: arg\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<Option> options = new ArrayList<>();
        Option option1 = new Option("option1", "option1 description", true, "o");
        Option option2 = new Option("option2", "option2 description", false, "d");
        Argument arg = new Argument("arg", "argument description", false);

        options.add(option1);
        options.add(option2);
        options.add(arg);

        Group group = new GroupImpl(options, "group", "group description", 1, 1, false);

        List<HelpLine> helpLines = group.helpLines(1, null, null);

        assertEquals(5, helpLines.size());

        HelpLine helpLine0 = helpLines.get(0);
        assertEquals("group description\n", helpLine0.getHelp());

        HelpLine helpLine1 = helpLines.get(1);
        assertEquals("--option1 <option1>\n", helpLine1.getHelp());
        assertEquals("option1 description", helpLine1.getLongOpt());
        assertEquals("o", helpLine1.getOpt());
        assertEquals("true", helpLine1.getRequired());

        HelpLine helpLine2 = helpLines.get(2);
        assertEquals("--option2\n", helpLine2.getHelp());
        assertEquals("option2 description", helpLine2.getLongOpt());
        assertEquals("d", helpLine2.getOpt());
        assertEquals("false", helpLine2.getRequired());

        HelpLine helpLine3 = helpLines.get(3);
        assertEquals("arg:\n", helpLine3.getHelp());
        assertEquals("argument description", helpLine3.getLongOpt());
        assertEquals("", helpLine3.getOpt());
        assertEquals("false", helpLine3.getRequired());

        HelpLine helpLine4 = helpLines.get(4);
        assertEquals("", helpLine4.getHelp());
        assertEquals(null, helpLine4.getLongOpt());
        assertEquals("", helpLine4.getOpt());
        assertEquals("false", helpLine4.getRequired());
    }
}