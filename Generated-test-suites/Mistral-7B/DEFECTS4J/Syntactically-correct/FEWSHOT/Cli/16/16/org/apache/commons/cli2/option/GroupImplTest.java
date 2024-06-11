package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setUp() {
        options = Arrays.asList(
                new Option("a", "anOption", true, "aDescription"),
                new Option("b", "anOptionB", false, "aDescriptionB"),
                new Option("c", "anOptionC", false, "aDescriptionC"),
                new Option("d", "anOptionD", false, "aDescriptionD"),
                new Option("e", "anOptionE", true, "aDescriptionE")
        );

        group = new GroupImpl(options, "myGroup", "a description for myGroup", 1, Integer.MAX_VALUE);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        // Test with valid options
        for (Option option : options) {
            assertTrue(group.canProcess(commandLine, option.getLongOpt()));
        }

        // Test with invalid option
        String invalidOption = "invalid";
        assertFalse(group.canProcess(commandLine, invalidOption));

        // Test with empty command line
        commandLine.clearArguments();
        assertFalse(group.canProcess(commandLine, ""));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        List<String> prefixesList = new ArrayList<>();
        prefixesList.add("--");
        group = new GroupImpl(options, "myGroup", "a description for myGroup", 1, Integer.MAX_VALUE, prefixesList);
        assertEquals(prefixesList, group.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = group.getTriggers();
        assertEquals(Collections.emptySet(), triggers);

        List<String> triggersList = Arrays.asList("--a", "--b", "--c", "--d", "--e");
        group = new GroupImpl(options, "myGroup", "a description for myGroup", 1, Integer.MAX_VALUE, triggersList);
        assertEquals(triggersList, group.getTriggers());
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        commandLine.addOption("a", true);
        commandLine.addOption("b");
        commandLine.addOption("c");
        commandLine.addOption("d");
        commandLine.addOption("e");

        group.process(commandLine, commandLine.listIterator());

        for (Option option : options) {
            Option processedOption = commandLine.findOption(option.getLongOpt());
            assertSame(option, processedOption);
        }
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        group.validate(commandLine);

        for (Option option : options) {
            option.validate(commandLine);
        }
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidOptions() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        group.validate(commandLine);

        commandLine.addOption("a");

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.emptySet(), null);

        String expectedUsage =
                "myGroup:\n" +
                "  a description for myGroup\n" +
                "  --anOption (anOptionalValue)\n" +
                "      aDescription\n" +
                "  --anOptionB\n" +
                "  --anOptionC\n" +
                "  --anOptionD\n" +
                "  --anOptionE (anOptionalValue)\n" +
                "      aDescriptionE";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        HelpLine expectedHelpLine = new HelpLineImpl("myGroup", 0, null);
        expectedHelpLine.setOptionSpec("", null, "a description for myGroup", null);

        List<HelpLine> expectedHelpLinesList = Arrays.asList(expectedHelpLine);

        assertEquals(expectedHelpLinesList, helpLines);

        List<HelpLine> helpLinesWithOptions = group.helpLines(0, Collections.singleton(DisplaySetting.DISPLAY_OPTION), null);

        HelpLine helpLineWithOptions = expectedHelpLine;
        helpLineWithOptions.setOptionSpec("--anOption ", null, "anOptionalValue", "aDescription");
        helpLineWithOptions.setOptionSpec("--anOptionB", null, null, null);
        helpLineWithOptions.setOptionSpec("--anOptionC", null, null, null);
        helpLineWithOptions.setOptionSpec("--anOptionD", null, null, null);
        helpLineWithOptions.setOptionSpec("--anOptionE ", null, "anOptionalValue", "aDescriptionE");

        assertEquals(Arrays.asList(helpLineWithOptions), helpLinesWithOptions);
    }

    @Test
    public void testFindOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        commandLine.addOption("a", true);
        commandLine.addOption("b");
        commandLine.addOption("c");

        Option optionA = group.findOption("--anOption");
        assertEquals(options.get(0), optionA);

        Option optionB = group.findOption("--anOptionB");
        assertEquals(options.get(1), optionB);

        Option optionC = group.findOption("--anOptionC");
        assertEquals(options.get(2), optionC);

        Option optionD = group.findOption("--anOptionD");
        assertEquals(options.get(3), optionD);

        Option optionE = group.findOption("--anOptionE");
        assertEquals(options.get(4), optionE);

        Option optionF = group.findOption("--anOptionF");
        assertNull(optionF);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine("test");

        group.defaults(commandLine);

        for (Option option : options) {
            option.defaults(commandLine);
        }

        for (Option option : options) {
            assertTrue(commandLine.hasOption(option.getLongOpt()));
        }
    }
}

class HelpLineImpl implements HelpLine {
    private final Group group;
    private final int depth;
    private OptionSpec optionSpec;

    public HelpLineImpl(Group group, int depth) {
        this.group = group;
        this.depth = depth;
    }

    public Group getGroup() {
        return group;
    }

    public int getDepth() {
        return depth;
    }

    public void setOptionSpec(String optionSpecText, String opt, String description, String valueDescription) {
        this.optionSpec = new OptionSpecImpl(optionSpecText, opt, description, valueDescription);
    }

    public OptionSpec getOptionSpec() {
        return optionSpec;
    }
}

class OptionSpecImpl implements OptionSpec {
    private final String optionSpecText;
    private final String opt;
    private final String description;
    private final String valueDescription;

    public OptionSpecImpl(String optionSpecText, String opt, String description, String valueDescription) {
        this.optionSpecText = optionSpecText;
        this.opt = opt;
        this.description = description;
        this.valueDescription = valueDescription;
    }

    public String getOptionSpecText() {
        return optionSpecText;
    }

    public String getLongOpt() {
        return opt;
    }

    public String getDescription() {
        return description;
    }

    public String getValueDescription() {
        return valueDescription;
    }
}