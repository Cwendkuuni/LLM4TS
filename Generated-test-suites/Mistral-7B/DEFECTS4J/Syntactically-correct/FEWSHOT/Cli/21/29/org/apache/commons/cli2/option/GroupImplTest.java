package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.Arguments;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {

    private static final String GROUP_NAME = "testGroup";
    private static final String GROUP_DESCRIPTION = "test group description";
    private static final boolean GROUP_REQUIRED = false;
    private static final int GROUP_MINIMUM = 1;
    private static final int GROUP_MAXIMUM = 3;

    private List<Option> options;
    private List<Argument> anonymous;
    private List<Option> filteredOptions;
    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        anonymous = new ArrayList<>();

        Option option1 = new OptionBuilder("option1")
                .withLongOpt("long-option1")
                .withDescription("Option 1")
                .withRequiredOption(false)
                .build();
        options.add(option1);

        Option option2 = new OptionBuilder("option2")
                .withLongOpt("long-option2")
                .withDescription("Option 2")
                .withRequiredOption(true)
                .build();
        options.add(option2);

        Argument arg1 = new ArgumentBuilder()
                .withName("arg1")
                .withDescription("Argument 1")
                .build();
        anonymous.add(arg1);

        Argument arg2 = new ArgumentBuilder()
                .withName("arg2")
                .withDescription("Argument 2")
                .build();
        anonymous.add(arg2);

        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION, GROUP_MINIMUM, GROUP_MAXIMUM, GROUP_REQUIRED);
        commandLine = new CommandLineParser().parse("-t test " + String.join(" ", Arrays.asList(args()))).getCommandLine();
    }

    @Test
    public void testCanProcess() throws OptionException {
        // Test with valid options
        for (Option option : options) {
            assertTrue(group.canProcess(commandLine, option.getLongOpt()));
        }

        // Test with invalid options
        assertFalse(group.canProcess(commandLine, "invalidOption"));
        assertFalse(group.canProcess(commandLine, ""));

        // Test with anonymous arguments
        for (Argument arg : anonymous) {
            assertTrue(group.canProcess(commandLine, arg.getName()));
        }
    }

    @Test
    public void testValidate() throws OptionException {
        // Test with valid options
        group.validate(commandLine);

        // Test with too few options
        commandLine.removeOption("option1");
        assertFalse(group.validate(commandLine).getMessage().isEmpty());

        // Test with too many options
        commandLine.addOption(option2);
        assertFalse(group.validate(commandLine).getMessage().isEmpty());

        // Test with invalid options
        commandLine.addOption(new OptionBuilder("invalidOption")
                .withLongOpt("long-invalidOption")
                .build());
        assertFalse(group.validate(commandLine).getMessage().isEmpty());

        // Test with invalid anonymous arguments
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3", "--arg4"});
        assertFalse(group.validate(commandLine).getMessage().isEmpty());
    }

    @Test
    public void testProcess() throws OptionException {
        // Test with valid options
        List<String> arguments = Arrays.asList("--option1", "value1", "--option2", "value2");
        commandLine.parse(arguments.toArray(new String[0]));
        group.process(commandLine, commandLine.listIterator());

        for (Option option : options) {
            assertEquals(option.getDefaultValue(), option.getValue(commandLine));
        }

        // Test with anonymous arguments
        assertEquals(Arrays.asList("value1", "value2"), anonymousValues());

        // Test with invalid options
        arguments = Arrays.asList("--option1", "value1", "--invalidOption");
        commandLine.parse(arguments.toArray(new String[0]));
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.listIterator()));

        // Test with missing required options
        arguments = Arrays.asList("--option1", "value1");
        commandLine.parse(arguments.toArray(new String[0]));
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.listIterator()));

        // Test with too many options
        arguments = Arrays.asList("--option1", "value1", "--option1", "value2");
        commandLine.parse(arguments.toArray(new String[0]));
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.listIterator()));
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(anonymous, group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption("option1");
        assertNotNull(option1);
        assertEquals("option1", option1.getLongOpt());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(GROUP_MINIMUM, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(GROUP_MAXIMUM, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testHelpLines() {
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        final Comparator<Option> optionComparator = null;
        List<HelpLine> helpLines = group.helpLines(0, helpSettings, optionComparator);

        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertEquals(GROUP_NAME, helpLine.getHeadText());
        assertEquals(GROUP_DESCRIPTION, helpLine.getShortDescription());
        assertEquals(GROUP_NAME + ": [options]", helpLine.getLongDescription());

        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpLines = group.helpLines(0, helpSettings, optionComparator);

        assertEquals(options.size() + anonymous.size(), helpLines.size());
        for (int i = 0; i < options.size(); i++) {
            Option option = options.get(i);
            HelpLine helpLine = helpLines.get(i + 1);
            assertEquals(option.getLongOpt(), helpLine.getHeadText());
            assertEquals(option.getShortDescription(), helpLine.getShortDescription());
            assertEquals(option.getLongDescription(), helpLine.getLongDescription());
        }

        for (int i = options.size(); i < helpLines.size(); i++) {
            HelpLine helpLine = helpLines.get(i);
            assertEquals("", helpLine.getHeadText());
            assertEquals(anonymous.get(i - options.size()).getName(), helpLine.getOpt());
            assertEquals(anonymous.get(i - options.size()).getLongName(), helpLine.getLongOpt());
            assertEquals(anonymous.get(i - options.size()).getDescription(), helpLine.getShortDescription());
            assertEquals(anonymous.get(i - options.size()).getLongDescription(), helpLine.getLongDescription());
        }
    }

    @Test
    public void testAppendUsage() {
        final StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);

        String expectedUsage = String.format("%s: %s\n%s", "testGroup", "test group description",
                "[options]\n");

        assertEquals(expectedUsage, buffer.toString());

        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);

        buffer.setLength(0);
        group.appendUsage(buffer, helpSettings, null);

        expectedUsage = String.format("%s: %s\n%s\n", "testGroup", "test group description",
                "[%s]\n");

        for (Option option : options) {
            expectedUsage = String.format("%s%s: %s\n%s\n%s\n", expectedUsage, option.getLongOpt(),
                    option.getShortDescription(), option.getLongDescription(),
                    Arrays.toString(option.getValues()));
        }

        for (Argument arg : anonymous) {
            expectedUsage = String.format("%s%s: %s\n", expectedUsage, arg.getName(),
                    arg.getDescription());
        }

        assertEquals(expectedUsage, buffer.toString());
    }

    private List<Object> anonymousValues() {
        List<Object> values = new ArrayList<>();
        for (Argument arg : anonymous) {
            values.add(arg.getValue(commandLine));
        }
        return values;
    }

    private static Arguments args() {
        return new Arguments();
    }
}

class HelpLineImpl extends HelpLine {
    private final GroupImpl group;

    public HelpLineImpl(GroupImpl group, int depth) {
        super(group.getName(), null, group.getDescription(), depth);
        this.group = group;
    }

    @Override
    public String getOpt() {
        throw new UnsupportedOperationException();
    }
}

class ReverseStringComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return -o1.compareTo(o2);
    }
}