package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class GroupImplTest {

    @Test
    public void testNewGroup() {
        List options = new ArrayList();
        options.add(createTestOption("option1", "opt1", false, null));
        options.add(createTestOption("option2", "opt2", false, null));
        Group group = new GroupImpl(options, "groupName", "group description", 1, 1);

        assertEquals("groupName", group.getPreferredName());
        assertEquals("group description", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertNull(group.getAnonymous());
        assertEquals(Collections.unmodifiableSet(Collections.emptySet()), group.getPrefixes());
    }

    @Test
    public void testCanProcess() {
        List options = new ArrayList();
        options.add(createTestOption("option1", "opt1", false, null));
        options.add(createTestOption("option2", "opt2", false, null));
        Group group = new GroupImpl(options, "groupName", "group description", 1, 1);

        // test empty command line
        WriteableCommandLine commandLine = new WriteableCommandLine();
        assertFalse(group.canProcess(commandLine, null));

        // test valid option
        commandLine.parse("-g groupName option1Value");
        assertTrue(group.canProcess(commandLine, "option1"));

        // test valid option with bursting
        commandLine.parse("-g groupName option1Value option1Value option1Value option2Value");
        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));

        // test invalid option
        commandLine.parse("invalidOption");
        assertFalse(group.canProcess(commandLine, "option1"));

        // test anonymous argument
        commandLine.parse("-g groupName argValue");
        assertTrue(group.canProcess(commandLine, "argValue"));

        // test option with triggers
        options.set(0, createTestOption("option1", "opt1", false, "trigger1,trigger2"));
        group = new GroupImpl(options, "groupName", "group description", 1, 1);
        commandLine.parse("-g groupName trigger1");
        assertTrue(group.canProcess(commandLine, "option1"));
        commandLine.parse("-g groupName trigger2");
        assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testProcess() throws OptionException {
        List options = new ArrayList();
        options.add(createTestOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "groupName", "group description", 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        List arguments = commandLine.getArguments();

        // test empty command line
        assertFalse(group.canProcess(commandLine, null));

        // test valid option
        commandLine.parse("-g groupName option1Value");
        group.process(commandLine, arguments.listIterator());
        assertEquals("opt1Value", commandLine.getOptionValue("option1"));

        // test option with triggers
        options.set(0, createTestOption("option1", "opt1", false, "trigger1,trigger2"));
        group = new GroupImpl(options, "groupName", "group description", 1, 1);
        commandLine.parse("-g groupName trigger1");
        group.process(commandLine, arguments.listIterator());
        assertEquals("opt1Value", commandLine.getOptionValue("option1"));
        commandLine.parse("-g groupName trigger2");
        group.process(commandLine, arguments.listIterator());
        assertEquals("opt1Value", commandLine.getOptionValue("option1"));

        // test anonymous argument
        commandLine.parse("-g groupName argValue");
        group.process(commandLine, arguments.listIterator());
        assertEquals("argValue", commandLine.getArgument(0));
    }

    @Test
    public void testValidate() throws OptionException {
        List options = new ArrayList();
        options.add(createTestOption("option1", "opt1", true, null));
        Group group = new GroupImpl(options, "groupName", "group description", 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        // test empty command line
        assertFalse(group.canProcess(commandLine, null));
        try {
            group.validate(commandLine);
            fail("expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        // test valid command line with one option
        commandLine.parse("-g groupName option1Value");
        group.validate(commandLine);

        // test valid command line with two options
        commandLine.parse("-g groupName option1Value option1Value");
        group.validate(commandLine);

        // test too few options
        commandLine.parse("-g groupName");
        try {
            group.validate(commandLine);
            fail("expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        // test too many options
        commandLine.parse("-g groupName option1Value option1Value option1Value option1Value");
        try {
            group.validate(commandLine);
            fail("expected OptionException");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testAppendUsage() {
        List options = new ArrayList();
        options.add(createTestOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "groupName", "group description", 1, 1);

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage =
                "groupName:\n" +
                "  group description\n" +
                "\n" +
                "Options:\n" +
                "------\n" +
                "option1\n" +
                "  opt1\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List options = new ArrayList();
        options.add(createTestOption("option1", "opt1", false, null));
        Group group = new GroupImpl(options, "groupName", "group description", 1, 1);

        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = null;

        List helpLines = group.helpLines(depth, helpSettings, comp);

        String expectedUsage =
                "groupName:\n" +
                "  group description\n" +
                "\n" +
                "Options:\n" +
                "------\n" +
                "option1\n" +
                "  opt1\n";

        HelpLine helpLine = (HelpLine) helpLines.get(0);
        assertEquals(expectedUsage, helpLine.getHelp());

        List optionHelpLines = ((HelpLineImpl) helpLine).getHelpLines();
        assertEquals(1, optionHelpLines.size());
        String optionUsage = (String) optionHelpLines.get(0);
        assertEquals("opt1", optionUsage.substring(optionUsage.indexOf(" ") + 1));
    }

    private Option createTestOption(final String name, final String opt, final boolean required, final String... triggers) {
        return new OptionImpl(name, opt, required, null, null) {
            @Override
            public Set getTriggers() {
                return Collections.unmodifiableSet(Collections.singleton(triggers[0]));
            }
        };
    }
}