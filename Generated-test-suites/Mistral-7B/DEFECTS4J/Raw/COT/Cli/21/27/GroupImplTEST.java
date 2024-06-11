package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder("option1")
                .longOpt("longOption1")
                .hasArg()
                .desc("Option 1 description")
                .build();
        Option option2 = new OptionBuilder("option2")
                .longOpt("longOption2")
                .hasArg()
                .desc("Option 2 description")
                .build();
        Option option3 = new OptionBuilder("option3")
                .longOpt("longOption3")
                .desc("Option 3 description")
                .build();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        List<Argument> arguments = new ArrayList<>();
        Argument arg1 = new ArgumentBuilder()
                .longOpt("arg1")
                .index(0)
                .name("arg1")
                .desc("Argument 1 description")
                .build();
        arguments.add(arg1);

        group = new GroupImpl(options, "Group", null, 1, Integer.MAX_VALUE, false);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess() {
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "longOption1"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "longOption2"));
        assertTrue(group.canProcess(commandLine, "option3"));
        assertTrue(group.canProcess(commandLine, "longOption3"));
        assertFalse(group.canProcess(commandLine, "unknownOption"));
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = group.getPrefixes();
        Assert.assertEquals(Collections.emptySet(), prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
        assertTrue(triggers.contains("option3"));
    }

    @Test
    public void testProcess() {
        group.process(commandLine, commandLine.listIterator());
        assertEquals(3, commandLine.getOptions().size());
        assertEquals(1, commandLine.getArguments().size());

        Option option1 = commandLine.getOptionByName("option1");
        assertEquals("value1", option1.getValue());

        Option option2 = commandLine.getOptionByName("option2");
        assertEquals("value2", option2.getValue());

        Option option3 = commandLine.getOptionByName("option3");
        assertNull(option3.getValue());

        Argument arg1 = commandLine.getArgumentByIndex(0);
        assertEquals("valueArg1", arg1.getValue());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() {
        group.setMinimum(2);
        group.validate(commandLine);

        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() {
        group.setMinimum(1);
        group.setMaximum(1);
        commandLine.addOption(new OptionBuilder("testOption")
                .longOpt("testLongOption")
                .hasArg()
                .build());

        group.validate(commandLine);

        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidOption() {
        group.setMinimum(1);
        group.setMaximum(1);
        commandLine.addOption(new OptionBuilder("invalidOption")
                .longOpt("invalidLongOption")
                .build());

        group.validate(commandLine);

        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
    }

    @Test
    public void testValidateWithValidOptions() {
        group.validate(commandLine);
        assertEquals(3, commandLine.getOptions().size());
        assertEquals(1, commandLine.getArguments().size());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, null, null);

        String expectedUsage = "Group:\n" +
                "Option 1 <value> - option1 (value is value1)\n" +
                "Option 2 <value> - option2 (value is value2)\n" +
                "Option 3 - option3\n" +
                "Arg1 <valueArg1> - arg1 (valueArg1 is valueArg1)\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = (List<HelpLine>) group.helpLines(0, null, null);

        String expectedHelpLine = "Group:";
        String expectedHelpLineDetailed = "Group:";

        for (HelpLine helpLine : helpLines) {
            if (helpLine.getHeadText().equals(expectedHelpLine)) {
                expectedHelpLineDetailed = helpLine.getLongText();
                break;
            }
        }

        assertEquals(expectedHelpLineDetailed, "Option 1:<option1> [value]: Option 1 description\n" +
                "Option 2:<option2> [value]: Option 2 description\n" +
                "Option 3:<option3>: Option 3 description\n" +
                "Arg1:<arg1> [valueArg1]: Argument 1 description");
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption("option1");
        assertNotNull(option1);
    }

    @Test
    public void testFindOptionNotFound() {
        Option option = group.findOption("unknownOption");
        assertNull(option);
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
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        List<Option> options = commandLine.getOptions();
        assertEquals(3, options.size());
        for (Option option: options) {
            assertNull(option.getValue());
        }

        List<Argument> arguments = commandLine.getArguments();
        assertEquals(0, arguments.size());
    }
}