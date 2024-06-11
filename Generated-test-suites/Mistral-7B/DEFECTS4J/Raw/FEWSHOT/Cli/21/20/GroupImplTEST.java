package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private Group group;
    private Option option;
    private Argument arg;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(option = new Option("opt1", "Option 1", true, "o1"));
        options.add(option = new Option("opt2", "Option 2", false, "o2"));
        options.add(arg = new Argument("arg1", 1, "a1", "a"));

        group = new GroupImpl(options, "Group", "Group Description", 1, 2, true);
    }

    @Test
    public void testCanProcessWithValidOption() {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");

        assertTrue(group.canProcess(cmd, "opt1"));
    }

    @Test
    public void testCanProcessWithInvalidOption() {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("invalidOption");

        assertFalse(group.canProcess(cmd, "invalidOption"));
    }

    @Test
    public void testCanProcessWithBurstingOption() {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");
        cmd.addOption("opt2");

        assertTrue(group.canProcess(cmd, "opt1"));
        assertTrue(group.canProcess(cmd, "opt2"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithInvalidBurstingOption() {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");
        cmd.addOption("opt1");

        group.canProcess(cmd, "opt1");
    }

    @Test
    public void testCanProcessWithAnonymousArgument() {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addArgument("arg1");

        assertTrue(group.canProcess(cmd, "arg1"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = group.getPrefixes();

        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = group.getTriggers();

        assertTrue(triggers.isEmpty());
    }

    @Test
    public void testProcessWithValidOptions() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");
        cmd.addOption("opt2");

        group.process(cmd, cmd.getArguments().listIterator());

        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.getOptionValue("opt1"));
        assertTrue(cmd.getOptionValue("opt2") == null);
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOption() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");
        cmd.addOption("invalidOption");

        group.process(cmd, cmd.getArguments().listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithMissingOption() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");

        group.process(cmd, cmd.getArguments().listIterator());
    }

    @Test
    public void testProcessWithAnonymousArgument() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addArgument("arg1");

        group.process(cmd, cmd.getArguments().listIterator());

        assertEquals(1, cmd.getOptions().size());
        assertNull(cmd.getOptionValue("opt1"));
        assertNull(cmd.getOptionValue("opt2"));
        assertTrue(cmd.hasOption("opt1"));
        assertTrue(cmd.hasOption("opt2"));
        assertEquals(1, cmd.getArguments().size());
        assertEquals("a", cmd.getArgumentValue("arg1"));
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");

        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingOption() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine("command");

        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");

        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() throws OptionException {
        WriteableCommandLine cmd = new WriteableCommandLine("command");
        cmd.addOption("opt1");
        cmd.addOption("opt1");

        group.validate(cmd);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();

        group.appendUsage(buffer, helpSettings, null);

        String expected = "Group Description\n" +
                "[, --group]\n" +
                "opt1 OPTION\n" +
                "  Option 1\n" +
                "  This is option 1\n" +
                "opt2\n" +
                "  Option 2\n" +
                "  This is option 2\n" +
                "arg1\n" +
                "  Argument\n" +
                "  This is argument";

        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, null, null);

        assertEquals(3, helpLines.size());

        HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        assertEquals("Group Description\n", helpLine1.getHeader());
        assertEquals("group", helpLine1.getSummary());
        assertThat(helpLine1.getOptions(), is(instanceOf(List.class)));
        assertSame(option, helpLine1.getOptions().get(0));
        assertSame(arg, helpLine1.getOptions().get(1));

        HelpLine helpLine2 = (HelpLine) helpLines.get(1);
        assertEquals("opt1", helpLine2.getSummary());
        assertEquals("Option 1\n  This is option 1", helpLine2.getDescription());
        assertThat(helpLine2.getOptions(), is(instanceOf(List.class)));
        assertNull(helpLine2.getOptions().size());

        HelpLine helpLine3 = (HelpLine) helpLines.get(2);
        assertEquals("opt2", helpLine3.getSummary());
        assertEquals("Option 2\n  This is option 2", helpLine3.getDescription());
        assertThat(helpLine3.getOptions(), is(instanceOf(List.class)));
        assertNull(helpLine3.getOptions().size());
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();

        assertEquals(2, options.size());
        assertSame(option, options.get(0));
        assertSame(option, options.get(1));
    }

    @Test
    public void testGetAnonymous() {
        List<Argument> arguments = group.getAnonymous();

        assertEquals(1, arguments.size());
        assertSame(arg, arguments.get(0));
    }

    @Test
    public void testFindOption() {
        Option foundOption = group.findOption("opt1");

        assertSame(option, foundOption);
    }

    @Test
    public void testGetMinimum() {
        int minimum = group.getMinimum();

        assertEquals(1, minimum);
    }

    @Test
    public void testGetMaximum() {
        int maximum = group.getMaximum();

        assertEquals(2, maximum);
    }

    @Test
    public void testIsRequired() {
        boolean isRequired = group.isRequired();

        assertTrue(isRequired);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine cmd = new WriteableCommandLine("command");

        group.defaults(cmd);

        assertEquals(1, cmd.getOptions().size());
        assertTrue(cmd.getOptionValue("opt1"));
        assertFalse(cmd.getOptionValue("opt2") == null);
        assertNull(cmd.getArgumentValue("arg1"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLine("command");

        assertTrue(group.looksLikeOption(cmd, "opt1"));
        assertTrue(group.looksLikeOption(cmd, "opt2"));
        assertFalse(group.looksLikeOption(cmd, "arg1"));
    }
}

class ReverseStringComparator {
    // empty for test purpose
}