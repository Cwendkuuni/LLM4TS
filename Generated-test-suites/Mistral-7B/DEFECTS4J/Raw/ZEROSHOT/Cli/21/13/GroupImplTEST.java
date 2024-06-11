package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private CommandLine commandLine;
    private List<Option> options;

    @Before
    public void setUp() {
        commandLine = new TestUtils.DummyCommandLine();
        options = new ArrayList<>();
    }

    @After
    public void tearDown() {
        commandLine = null;
        options.clear();
    }

    @Test
    public void testCanProcess() {
        final String trigger1 = "trigger1";
        final String trigger2 = "trigger2";

        final Option option1 = new Option("option1", "opt1", false, "desc1");
        option1.setTriggers(Collections.singletonList(trigger1));

        final Option option2 = new Option("option2", "opt2", false, "desc2");
        option2.setTriggers(Collections.singletonList(trigger2));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option1);
        group.addOption(option2);

        // test option1
        assertTrue(group.canProcess(commandLine, trigger1));
        assertFalse(group.canProcess(commandLine, trigger2));

        // test option2
        assertFalse(group.canProcess(commandLine, trigger1));
        assertTrue(group.canProcess(commandLine, trigger2));

        // test other
        assertFalse(group.canProcess(commandLine, ""));
        assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test
    public void testGetTriggers() {
        final String trigger1 = "trigger1";
        final String trigger2 = "trigger2";

        final Option option1 = new Option("option1", "opt1", false, "desc1");
        option1.setTriggers(Collections.singletonList(trigger1));

        final Option option2 = new Option("option2", "opt2", false, "desc2");
        option2.setTriggers(Collections.singletonList(trigger2));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option1);
        group.addOption(option2);

        Set triggers = group.getTriggers();
        assertTrue(triggers.contains(trigger1));
        assertTrue(triggers.contains(trigger2));
        assertEquals(2, triggers.size());
    }

    @Test
    public void testProcess() {
        final String trigger1 = "trigger1";
        final String trigger2 = "trigger2";

        final Option option1 = new Option("option1", "opt1", false, "desc1");
        option1.setTriggers(Collections.singletonList(trigger1));

        final Option option2 = new Option("option2", "opt2", false, "desc2");
        option2.setTriggers(Collections.singletonList(trigger2));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option1);
        group.addOption(option2);

        // test option1
        commandLine.parseOption(trigger1);
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals(1, commandLine.getNumArgs());
        assertNull(commandLine.getArg(0));

        // test option2
        commandLine.parseOption(trigger2);
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals(1, commandLine.getNumArgs());
        assertNull(commandLine.getArg(0));

        // test other
        commandLine.parseOption("unknown");
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals(1, commandLine.getNumArgs());
        assertNull(commandLine.getArg(0));
    }

    @Test
    public void testValidate() throws OptionException {
        final String trigger1 = "trigger1";
        final String trigger2 = "trigger2";

        final Option option1 = new Option("option1", "opt1", false, "desc1");
        option1.setTriggers(Collections.singletonList(trigger1));

        final Option option2 = new Option("option2", "opt2", false, "desc2");
        option2.setTriggers(Collections.singletonList(trigger2));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option1);
        group.addOption(option2);

        commandLine.parseOption(trigger1);
        commandLine.parseOption(trigger2);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooFew() throws OptionException {
        final String trigger1 = "trigger1";

        final Option option = new Option("option1", "opt1", false, "desc1");
        option.setTriggers(Collections.singletonList(trigger1));

        final Group group = new GroupImpl(options, "group", null, 1, 0, false);
        group.addOption(option);

        commandLine.parseOption(trigger1);

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooMany() throws OptionException {
        final String trigger1 = "trigger1";

        final Option option = new Option("option1", "opt1", false, "desc1");
        option.setTriggers(Collections.singletonList(trigger1));

        final Group group = new GroupImpl(options, "group", null, 1, 2, false);
        group.addOption(option);

        commandLine.parseOption(trigger1);
        commandLine.parseOption(trigger1);

        group.validate(commandLine);
    }

    @Test
    public void testGetName() {
        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetDescription() {
        final Group group = new GroupImpl(options, "group", "desc", 1, 1, false);
        assertEquals("desc", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final String trigger1 = "trigger1";
        final String trigger2 = "trigger2";

        final Option option1 = new Option("option1", "opt1", false, "desc1");
        option1.setTriggers(Collections.singletonList(trigger1));

        final Option option2 = new Option("option2", "opt2", false, "desc2");
        option2.setTriggers(Collections.singletonList(trigger2));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option1);
        group.addOption(option2);

        final StringBuilder buffer = new StringBuilder();
        final HelpFormatter formatter = new HelpFormatter();

        group.appendUsage(buffer, Collections.emptySet(), null);
        final String output = buffer.toString();

        final String expected = "group:\n" +
                "|opt1  desc1\n";

        assertEquals(expected, output);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null);
        final String output2 = buffer.toString();

        assertEquals("group", output2);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null);
        final String output3 = buffer.toString();

        final String expected3 = "group:\n" +
                "opt1  desc1\n";

        assertEquals(expected3, output3);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_ARGUMENT), null);
        final String output4 = buffer.toString();

        final String expected4 = "";

        assertEquals(expected4, output4);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, "|");
        final String output5 = buffer.toString();

        final String expected5 = "group:\n";

        assertEquals(expected5, output5);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, "\n");
        final String output6 = buffer.toString();

        final String expected6 = "group:\n";

        assertEquals(expected6, output6);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, " ");
        final String output7 = buffer.toString();

        final String expected7 = "group:";

        assertEquals(expected7, output7);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null, " ");
        final String output8 = buffer.toString();

        final String expected8 = "group:";

        assertEquals(expected8, output8);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_NAME), null, "");
        final String output9 = buffer.toString();

        final String expected9 = "group";

        assertEquals(expected9, output9);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null, "");
        final String output10 = buffer.toString();

        final String expected10 = "";

        assertEquals(expected10, output10);

        buffer.setLength(0);

        group.appendUsage(buffer, Collections.singleton(DisplaySetting.DISPLAY_GROUP_EXPANDED), null, "\n");
        final String output11 = buffer.toString();

        final String expected11 = "";

        assertEquals(expected11, output11);
    }

    @Test
    public void testHelpLines() {
        final String trigger1 = "trigger1";
        final String trigger2 = "trigger2";

        final Option option1 = new Option("option1", "opt1", false, "desc1");
        option1.setTriggers(Collections.singletonList(trigger1));

        final Option option2 = new Option("option2", "opt2", false, "desc2");
        option2.setTriggers(Collections.singletonList(trigger2));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option1);
        group.addOption(option2);

        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);

        assertEquals(2, helpLines.size());
        final HelpLine helpLine1 = (HelpLine) helpLines.get(0);
        final HelpLine helpLine2 = (HelpLine) helpLines.get(1);

        assertEquals("group:", helpLine1.getHeader());
        assertEquals("opt1  desc1", helpLine1.getOptionSpec());
        assertEquals("", helpLine1.getArgumentSpec());

        assertEquals("group:", helpLine2.getHeader());
        assertEquals("opt2  desc2", helpLine2.getOptionSpec());
        assertEquals("", helpLine2.getArgumentSpec());
    }

    @Test
    public void testGetOptions() {
        final String trigger1 = "trigger1";
        final String trigger2 = "trigger2";

        final Option option1 = new Option("option1", "opt1", false, "desc1");
        option1.setTriggers(Collections.singletonList(trigger1));

        final Option option2 = new Option("option2", "opt2", false, "desc2");
        option2.setTriggers(Collections.singletonList(trigger2));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option1);
        group.addOption(option2);

        final List<Option> options = group.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetAnonymous() {
        final String trigger1 = "trigger1";

        final Option option = new Option("option1", "opt1", false, "desc1");
        option.setTriggers(Collections.singletonList(trigger1));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option);

        final List<Option> anonymous = group.getAnonymous();

        assertEquals(0, anonymous.size());

        final Argument argument = new Argument("argument1", "arg1");

        group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option);
        group.addArgument(argument);

        anonymous = group.getAnonymous();

        assertEquals(1, anonymous.size());
        assertTrue(anonymous.contains(argument));
    }

    @Test
    public void testFindOption() {
        final String trigger1 = "trigger1";
        final String trigger2 = "trigger2";

        final Option option1 = new Option("option1", "opt1", false, "desc1");
        option1.setTriggers(Collections.singletonList(trigger1));

        final Option option2 = new Option("option2", "opt2", false, "desc2");
        option2.setTriggers(Collections.singletonList(trigger2));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option1);
        group.addOption(option2);

        Option found = group.findOption(trigger1);
        assertEquals(option1, found);

        found = group.findOption(trigger2);
        assertEquals(option2, found);

        found = group.findOption("unknown");
        assertNull(found);
    }

    @Test
    public void testGetMinimum() {
        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        assertFalse(group.isRequired());

        group = new GroupImpl(options, "group", null, 1, 1, true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        final String trigger1 = "trigger1";

        final Option option = new Option("option1", "opt1", false, "desc1");
        option.setTriggers(Collections.singletonList(trigger1));

        final Group group = new GroupImpl(options, "group", null, 1, 1, false);
        group.addOption(option);

        group.defaults(commandLine);

        assertNull(commandLine.getArg(0));
    }
}