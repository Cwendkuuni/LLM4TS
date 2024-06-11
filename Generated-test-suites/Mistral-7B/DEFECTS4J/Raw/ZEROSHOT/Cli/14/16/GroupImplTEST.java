package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.DummyOption;
import org.apache.commons.cli2.test.DummyOptionBuilder;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private static final String GROUP_NAME = "group";
    private static final String GROUP_DESCRIPTION = "A test group";
    private static final int GROUP_MINIMUM = 3;
    private static final int GROUP_MAXIMUM = 5;

    private List<Option> options;
    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();

        final Option option1 = new DummyOptionBuilder()
                .withLongOpt("option1")
                .withDescription("An option in the group")
                .build();
        options.add(option1);

        final Option option2 = new DummyOptionBuilder()
                .withLongOpt("option2")
                .withDescription("Another option in the group")
                .build();
        options.add(option2);

        final Option option3 = new DummyOptionBuilder()
                .withLongOpt("option3")
                .withDescription("Yet another option in the group")
                .build();
        options.add(option3);

        final Argument anonymousArg1 = new DummyArgumentBuilder()
                .withLongOpt("anonymous1")
                .withDescription("An anonymous argument")
                .build();
        options.add(anonymousArg1);

        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION, GROUP_MINIMUM, GROUP_MAXIMUM);
        commandLine = TestUtils.getDummyCommandLine();
    }

    @Test
    public void testCanProcess() {
        // test option 1
        assertTrue(group.canProcess(commandLine, "option1"));

        // test option 2
        assertTrue(group.canProcess(commandLine, "option2"));

        // test option 3
        assertTrue(group.canProcess(commandLine, "option3"));

        // test anonymous argument
        assertTrue(group.canProcess(commandLine, "anonymous1"));

        // test non-existent option
        assertFalse(group.canProcess(commandLine, "nonexistent"));
    }

    @Test
    public void testGetName() {
        assertEquals(GROUP_NAME, group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(GROUP_DESCRIPTION, group.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(GROUP_MINIMUM, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(GROUP_MAXIMUM, group.getMaximum());
    }

    @Test(expected = OptionException.class)
    public void testValidate() {
        commandLine.parse(group.getOptions().toArray(new Option[0]));

        group.validate(commandLine);

        // the following should throw an exception
        commandLine.parse(group.getOptions().toArray(new Option[0]));
    }

    @Test
    public void testProcess() {
        // test option 1
        group.process(commandLine, commandLine.listIterator());

        assertEquals(GROUP_NAME, commandLine.getOptionNames().get(0));
        assertEquals(option1.getValue(), commandLine.getOptionValue(option1.getLongOpt()));

        // test option 2
        group.process(commandLine, commandLine.listIterator());

        assertEquals(GROUP_NAME, commandLine.getOptionNames().get(1));
        assertEquals(option2.getValue(), commandLine.getOptionValue(option2.getLongOpt()));

        // test option 3
        group.process(commandLine, commandLine.listIterator());

        assertEquals(GROUP_NAME, commandLine.getOptionNames().get(2));
        assertEquals(option3.getValue(), commandLine.getOptionValue(option3.getLongOpt()));

        // test anonymous argument
        group.process(commandLine, commandLine.listIterator());

        assertEquals("anonymous1", commandLine.getArg(0));

        // test exception when processing an extra option
        group.process(commandLine, commandLine.listIterator());

        assertFalse(commandLine.hasOption(option1.getLongOpt()));
        assertFalse(commandLine.hasOption(option2.getLongOpt()));
        assertFalse(commandLine.hasOption(option3.getLongOpt()));
        assertNull(commandLine.getArg(0));

        final Map<String, String> optNamesAndValues = new HashMap<>();
        optNamesAndValues.put(option1.getLongOpt(), option1.getValue());
        optNamesAndValues.put(option2.getLongOpt(), option2.getValue());
        optNamesAndValues.put(option3.getLongOpt(), option3.getValue());

        assertEquals(optNamesAndValues, commandLine.getAllOptions());
    }

    @Test
    public void testGetTriggers() {
        final Set<String> triggers = group.getTriggers();

        assertEquals(options.size(), triggers.size());

        for (final Option option : options) {
            assertTrue(triggers.containsAll(option.getTriggers()));
        }
    }

    @Test
    public void testGetPrefixes() {
        final Set<String> prefixes = group.getPrefixes();

        assertEquals(options.size(), prefixes.size());

        for (final Option option : options) {
            assertTrue(prefixes.containsAll(option.getPrefixes()));
        }
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(new ArrayList<>(Arrays.asList(anonymousArg1)), group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        final Option option1 = group.findOption("option1");
        final Option option2 = group.findOption("option2");
        final Option option3 = group.findOption("option3");

        assertSame(option1, option1);
        assertSame(option2, option2);
        assertSame(option3, option3);
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals(
                "[group (An test group)]",
                buffer.toString());

        final Set<DisplaySetting> helpSettings = new TreeSet<>();

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        group.appendUsage(buffer, helpSettings, null);

        assertEquals(
                "[group (An test group)]\n" +
                        "|--option1\n" +
                        "|   An option in the group\n" +
                        "|--option2\n" +
                        "|   Another option in the group\n" +
                        "|--option3\n" +
                        "|   Yet another option in the group\n",
                buffer.toString());

        helpSettings.clear();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        group.appendUsage(buffer, helpSettings, null);

        assertEquals(
                "[group (An test group)]\n" +
                        "|--option1\n" +
                        "|   An option in the group\n" +
                        "|--option2\n" +
                        "|   Another option in the group\n" +
                        "|--option3\n" +
                        "|   Yet another option in the group\n" +
                        "|--anonymous1\n" +
                        "|   An anonymous argument\n",
                buffer.toString());
    }

    @Test
    public void testHelpLines() {
        assertEquals(Collections.emptyList(), group.helpLines(0, Collections.emptySet(), null));

        final Set<DisplaySetting> helpSettings = new TreeSet<>();

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        final List<HelpLine> helpLines = group.helpLines(1, helpSettings, null);

        final HelpLine helpLine = helpLines.get(0);

        assertEquals(GROUP_NAME, helpLine.getHeadText());
        assertEquals(GROUP_DESCRIPTION, helpLine.getShortText());
        assertEquals(null, helpLine.getLongText());

        final List<HelpLine> subHelpLines = helpLine.getSubHelpLines();

        assertEquals(3, subHelpLines.size());

        final HelpLine option1HelpLine = subHelpLines.get(0);

        assertEquals("option1", option1HelpLine.getHeadText());
        assertEquals("An option in the group", option1HelpLine.getShortText());
        assertEquals(null, option1HelpLine.getLongText());

        final HelpLine option2HelpLine = subHelpLines.get(1);

        assertEquals("option2", option2HelpLine.getHeadText());
        assertEquals("Another option in the group", option2HelpLine.getShortText());
        assertEquals(null, option2HelpLine.getLongText());

        final HelpLine option3HelpLine = subHelpLines.get(2);

        assertEquals("option3", option3HelpLine.getHeadText());
        assertEquals("Yet another option in the group", option3HelpLine.getShortText());
        assertEquals(null, option3HelpLine.getLongText());

        helpSettings.clear();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        final List<HelpLine> expandedHelpLines = group.helpLines(1, helpSettings, null);

        final HelpLine expandedHelpLine = expandedHelpLines.get(0);

        assertEquals(GROUP_NAME, expandedHelpLine.getHeadText());
        assertEquals(GROUP_DESCRIPTION, expandedHelpLine.getShortText());
        assertEquals(
                "An test group\n" +
                        "|--option1\n" +
                        "|   An option in the group\n" +
                        "|--option2\n" +
                        "|   Another option in the group\n" +
                        "|--option3\n" +
                        "|   Yet another option in the group\n" +
                        "|--anonymous1\n" +
                        "|   An anonymous argument\n",
                expandedHelpLine.getLongText());
    }
}