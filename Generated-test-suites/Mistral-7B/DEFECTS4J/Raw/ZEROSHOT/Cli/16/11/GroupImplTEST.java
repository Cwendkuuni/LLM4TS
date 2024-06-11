package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.help.HelpLine;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.annotation.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.apache.commons.cli2.CliFactory.getDefaultParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GroupImpl.class})
public class GroupImplTest {

    private List<Option> options;
    private Group group;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final List<Option> optionList = new ArrayList<>();
        options = optionList;

        final Option option1 = TestUtils.createMockOption("opt1", null, false, null, null);
        final Option option2 = TestUtils.createMockOption("opt2", null, false, null, null);
        final Option option3 = TestUtils.createMockOption("opt3", null, false, null, null);
        final Argument argument = TestUtils.createMockArgument("arg1", null, null);

        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(argument);

        group = new GroupImpl(options, "group", "group description", 1, 1);
    }

    @Test
    public void testCanProcess() {
        final WriteableCommandLine commandLine = TestUtils.createMockCommandLine();

        // test with valid option
        assertTrue(group.canProcess(commandLine, "opt1"));

        // test with invalid option
        assertFalse(group.canProcess(commandLine, "invalidOption"));

        // test with anonymous argument
        final String[] args = {"arg1"};
        commandLine.parse(args);
        assertTrue(group.canProcess(commandLine, "arg1"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();

        assertNotNull(prefixes);
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();

        assertNotNull(triggers);
        assertEquals(3, triggers.size());
        assertTrue(triggers.containsAll(Arrays.asList("opt1", "opt2", "opt3")));
    }

    @Test
    public void testProcess() {
        final WriteableCommandLine commandLine = TestUtils.createMockCommandLine();
        final ListIterator<String> arguments = commandLine.getArguments();

        // test with valid option
        arguments.next();
        assertTrue(group.canProcess(commandLine, "opt1"));
        group.process(commandLine, arguments);
        assertEquals(3, commandLine.getOptions().size());

        // test with invalid option
        arguments.next();
        assertFalse(group.canProcess(commandLine, "invalidOption"));
        group.process(commandLine, arguments);
        assertEquals(3, commandLine.getOptions().size());

        // test with anonymous argument
        arguments.next();
        assertTrue(group.canProcess(commandLine, "arg1"));
        group.process(commandLine, arguments);
        assertEquals(3, commandLine.getOptions().size());
        assertEquals(1, commandLine.getValues("arg1").size());
    }

    @Test
    public void testValidate() throws OptionException {
        final WriteableCommandLine commandLine = TestUtils.createMockCommandLine();

        group.validate(commandLine);
        assertNull(commandLine.getError());

        // test with too few options
        commandLine.clear();
        commandLine.parse(new String[0]);
        assertNotNull(commandLine.getError());
        assertEquals("Missing required option 'group'", commandLine.getError().getMessage());

        // test with too many options
        commandLine.clear();
        commandLine.parse(new String[]{"opt1", "opt1", "opt1"});
        assertNotNull(commandLine.getError());
        assertEquals("Too many options. The maximum number of options is 1.", commandLine.getError().getMessage());

        // test with valid options
        commandLine.clear();
        commandLine.parse(new String[]{"opt1", "opt2"});
        assertNull(commandLine.getError());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("group description", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();
        final Set<DisplaySetting> helpSettings = new TreeSet<>();

        group.appendUsage(buffer, helpSettings, null);

        final String expectedUsage =
                "[group (group description)] \n" +
                "opt1\n" +
                "opt2\n" +
                "opt3\n" +
                "arg1\n";
        assertEquals(expectedUsage, buffer.toString());

        // test with expanded group
        final Set<DisplaySetting> helpSettingsExpanded = new TreeSet<>(helpSettings);
        helpSettingsExpanded.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettingsExpanded, null);

        final String expectedUsageExpanded =
                "[group (group description)] \n" +
                "  opt1\n" +
                "  opt2\n" +
                "  opt3\n" +
                "arg1\n";
        assertEquals(expectedUsageExpanded, buffer.toString());

        // test with named group
        final Set<DisplaySetting> helpSettingsNamed = new TreeSet<>(helpSettings);
        helpSettingsNamed.add(DisplaySetting.DISPLAY_GROUP_NAME);

        group.appendUsage(buffer, helpSettingsNamed, null);

        final String expectedUsageNamed =
                "group: [group description]\n" +
                "  opt1\n" +
                "  opt2\n" +
                "  opt3\n" +
                "arg1\n";
        assertEquals(expectedUsageNamed, buffer.toString());

        // test with named and expanded group
        final Set<DisplaySetting> helpSettingsNamedExpanded = new TreeSet<>(helpSettingsNamed);
        helpSettingsNamedExpanded.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettingsNamedExpanded, null);

        final String expectedUsageNamedExpanded =
                "group: [group description]\n" +
                "  opt1\n" +
                "  opt2\n" +
                "  opt3\n" +
                "arg1\n";
        assertEquals(expectedUsageNamedExpanded, buffer.toString());

        // test with outer group
        final Set<DisplaySetting> helpSettingsOuter = new TreeSet<>(helpSettingsNamedExpanded);
        helpSettingsOuter.add(DisplaySetting.DISPLAY_GROUP_OUTER);

        group.appendUsage(buffer, helpSettingsOuter, null);

        final String expectedUsageOuter =
                "[group: group description]\n" +
                "  opt1\n" +
                "  opt2\n" +
                "  opt3\n" +
                "arg1\n";
        assertEquals(expectedUsageOuter, buffer.toString());

        // test with optional group
        final Set<DisplaySetting> helpSettingsOptional = new TreeSet<>(helpSettingsList);
        helpSettingsOptional.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettingsOptional.add(DisplaySetting.DISPLAY_OPTIONAL);

        group.appendUsage(buffer, helpSettingsOptional, null);

        final String expectedUsageOptional =
                "[group: group description] [opt1]\n" +
                "  opt2\n" +
                "  opt3\n" +
                "arg1\n";
        assertEquals(expectedUsageOptional, buffer.toString());

        // test with optional and named group
        final Set<DisplaySetting> helpSettingsOptionalNamed = new TreeSet<>(helpSettingsOptional);
        helpSettingsOptionalNamed.add(DisplaySetting.DISPLAY_GROUP_NAME);

        group.appendUsage(buffer, helpSettingsOptionalNamed, null);

        final String expectedUsageOptionalNamed =
                "group: [group description] [opt1]\n" +
                "  opt2\n" +
                "  opt3\n" +
                "arg1\n";
        assertEquals(expectedUsageOptionalNamed, buffer.toString());

        // test with optional and named and expanded group
        final Set<DisplaySetting> helpSettingsOptionalNamedExpanded = new TreeSet<>(helpSettingsOptionalNamed);
        helpSettingsOptionalNamedExpanded.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettingsOptionalNamedExpanded, null);

        final String expectedUsageOptionalNamedExpanded =
                "group: [group description] [opt1]\n" +
                "  opt1\n" +
                "  opt2\n" +
                "  opt3\n" +
                "arg1\n";
        assertEquals(expectedUsageOptionalNamedExpanded, buffer.toString());

        // test with optional and named and expanded and outer group
        final Set<DisplaySetting> helpSettingsOptionalNamedExpandedOuter = new TreeSet<>(helpSettingsOptionalNamedExpanded);
        helpSettingsOptionalNamedExpandedOuter.add(DisplaySetting.DISPLAY_GROUP_OUTER);

        group.appendUsage(buffer, helpSettingsOptionalNamedExpandedOuter, null);

        final String expectedUsageOptionalNamedExpandedOuter =
                "[group: group description] [opt1]\n" +
                "  opt1\n" +
                "  opt2\n" +
                "  opt3\n" +
                "arg1\n";
        assertEquals(expectedUsageOptionalNamedExpandedOuter, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final int depth = 0;
        final Set<DisplaySetting> helpSettings = new TreeSet<>();
        final Comparator<Option> comparator = null;

        final List<HelpLine> helpLines = group.helpLines(depth, helpSettings, comparator);

        assertEquals(4, helpLines.size());
        assertEquals("group: [group description]\n" +
                     "  opt1\n" +
                     "  opt2\n" +
                     "  opt3\n" +
                     "arg1\n", helpLines.get(0).getHelpText());
        assertEquals("opt1\n", helpLines.get(1).getHelpText());
        assertEquals("opt2\n", helpLines.get(2).getHelpText());
        assertEquals("opt3\n", helpLines.get(3).getHelpText());
    }

    @Test
    public void testFindOption() {
        final Option foundOption = group.findOption("opt1");

        assertNotNull(foundOption);
        assertEquals("opt1", foundOption.getLongOpt());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(getDefaultParser().parse(new String[0]));

        final WriteableCommandLine commandLine = getDefaultParser().parse(new String[0]);
        assertTrue(commandLine.hasOption(group.getPreferredName()));

        for (final Option option : group.getOptions()) {
            assertTrue(option.isDefaultValueSet());
        }

        for (final Option option : group.getAnonymous()) {
            assertTrue(option.isDefaultValueSet());
        }
    }
}