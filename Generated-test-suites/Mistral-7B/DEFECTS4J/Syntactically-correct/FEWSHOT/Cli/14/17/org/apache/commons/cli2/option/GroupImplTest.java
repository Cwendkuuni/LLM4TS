package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class GroupImplTest {
    private List<Option> options;
    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        options = new ArrayList<>();
        commandLine = new CommandLine();

        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Option("a", "arg", true, "Argument");

        options.add(option1);
        options.add(option2);
        options.add(option3);

        group = new GroupImpl(options, "MyGroup", "My Group", 1, Integer.MAX_VALUE);
    }

    @Test
    public void testCanProcess() {
        assertEquals(0, group.canProcess(commandLine, null));
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, "o"));
        assertFalse(group.canProcess(commandLine, "opt1"));
        assertFalse(group.canProcess(commandLine, "opt2"));
        assertFalse(group.canProcess(commandLine, "arg"));
        assertTrue(group.canProcess(commandLine, "opt1"));
        assertTrue(group.canProcess(commandLine, "opt2"));
        assertTrue(group.canProcess(commandLine, "arg"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.emptySet(), group.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        Set triggers = group.getTriggers();
        assertTrue(triggers.containsAll(Arrays.asList("o", "opt1", "opt2", "arg")));
        assertEquals(triggers.size(), 4);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyTriggers() {
        Set triggers = group.getTriggers();
        triggers.remove("o");
    }

    @Test
    public void testProcess() throws OptionException {
        group.process(commandLine, commandLine.listIterator());

        assertEquals(1, commandLine.getNumOptions());
        assertEquals("opt1", commandLine.getOptionValue("opt1"));
        assertNull(commandLine.getOptionValue("opt2"));
        assertNull(commandLine.getOptionValue("arg"));

        group.process(commandLine, commandLine.listIterator());

        assertEquals(2, commandLine.getNumOptions());
        assertEquals("opt1", commandLine.getOptionValue("opt1"));
        assertEquals("opt2", commandLine.getOptionValue("opt2"));
        assertNull(commandLine.getOptionValue("arg"));

        group.process(commandLine, commandLine.listIterator());

        assertEquals(3, commandLine.getNumOptions());
        assertEquals("opt1", commandLine.getOptionValue("opt1"));
        assertEquals("opt2", commandLine.getOptionValue("opt2"));
        assertEquals("value", commandLine.getOptionValue("arg"));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidOption() throws OptionException {
        group.process(commandLine, commandLine.listIterator());
        group.process(commandLine, commandLine.listIterator());
        commandLine.listIterator().next();
        commandLine.listIterator().next();

        group.process(commandLine, commandLine.listIterator());
    }

    @Test
    public void testValidate() throws OptionException {
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateMissingOption() throws OptionException {
        commandLine.addOption(new Option("o", "opt1", false, "Option 1"));
        group.validate(commandLine);

        commandLine.removeOption("opt2");

        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test(expected = OptionException.class)
    public void testValidateTooManyOptions() throws OptionException {
        commandLine.addOption(new Option("o", "opt1", false, "Option 1"));
        commandLine.addOption(new Option("o", "opt2", false, "Option 2"));
        group.validate(commandLine);

        commandLine.addOption(new Option("o", "opt3", false, "Option 3"));

        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, null, null);

        String expected = "(My Group)\n" +
                "  My Group:\n" +
                "    My Group\n" +
                "    (optional)\n" +
                "    opt1: Option 1\n" +
                "    opt2: Option 2\n" +
                "    arg: Argument\n";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(0, null, null);

        String expected = "My Group\n" +
                "  My Group:\n" +
                "    My Group\n" +
                "    (optional)\n" +
                "  opt1: Option 1\n" +
                "    This option has no argument and is optional.\n";

        assertEquals(1, helpLines.size());
        assertEquals(expected, helpLines.get(0).getUsageLine());

        expected = "opt2: Option 2\n" +
                "  This option can appear one or more times.";

        HelpLine helpLine2 = helpLines.get(1);
        assertEquals(expected, helpLine2.getUsageLine());
        assertEquals("opt2", helpLine2.getOptionSpec().getLongOpt());
        assertEquals("Option 2", helpLine2.getOptionSpec().getOpt());
        assertEquals(null, helpLine2.getOptionSpec().getArgName());

        expected = "arg: Argument\n" +
                "  This option's argument will be assigned to the value.\n";

        HelpLine helpLine3 = helpLines.get(2);
        assertEquals(expected, helpLine3.getUsageLine());
        assertEquals("arg", helpLine3.getOptionSpec().getLongOpt());
        assertEquals("arg", helpLine3.getOptionSpec().getOpt());
        assertEquals("value", helpLine3.getOptionSpec().getArgName());
    }

    @Test
    public void testGetOptions() {
        List<Option> getOptions = group.getOptions();

        assertEquals(3, getOptions.size());
        assertSame(option1, getOptions.get(0));
        assertSame(option2, getOptions.get(1));
        assertSame(option3, getOptions.get(2));
    }

    @Test
    public void testGetAnonymous() {
        List<Option> getAnonymous = group.getAnonymous();

        assertEquals(1, getAnonymous.size());
        assertSame(option3, getAnonymous.get(0));
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("arg");

        assertEquals(option3, foundOption);
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

        assertFalse(commandLine.hasOption("opt1"));
        assertFalse(commandLine.hasOption("opt2"));
        assertNull(commandLine.getOptionValue("arg"));
    }
}

class HelpLineImpl implements HelpLine {
    private final Group option;
    private final int depth;

    public HelpLineImpl(Group option, int depth) {
        this.option = option;
        this.depth = depth;
    }

    @Override
    public String getUsageLine() {
        StringBuilder usageLine = new StringBuilder();

        for (int i = 0; i < depth; i++) {
            usageLine.append("    ");
        }

        usageLine.append(option.getPreferredName());
        usageLine.append(": ");
        usageLine.append(option.getDescription());

        return usageLine.toString();
    }
}

class ReverseStringComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2) * -1;
    }
}