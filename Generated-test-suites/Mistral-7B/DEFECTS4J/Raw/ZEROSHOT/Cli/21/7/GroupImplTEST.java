package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
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

    private List<Option> options;
    private Group group;

    @Before
    public void setUp() {
        // Create a list of Options
        options = new ArrayList<>();
        options.add(new Option("a", "arg1", false, "A test argument"));
        options.add(new Option("b", "arg2", true, "An optional test argument"));
        options.add(new Option("c", null, false, "A required test argument"));
        options.add(new Option("d", "arg4", false, "Another test argument"));

        // Create a Group
        group = new GroupImpl(options, "TestGroup", "Test group for options", 1, 3, true);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("TestCommand");

        // Test valid options
        for (Option option : options) {
            assertTrue(group.canProcess(commandLine, option.getLongOpt()));
            assertTrue(group.canProcess(commandLine, option.getShortOpt()));
        }

        // Test invalid option
        assertFalse(group.canProcess(commandLine, "x"));

        // Test anonymous argument
        assertFalse(group.canProcess(commandLine, "arg"));

        // Test valid option with value
        commandLine.parseArgument(optionAtIndex(0).getLongOpt(), "value1");
        assertTrue(group.canProcess(commandLine, optionAtIndex(0).getLongOpt()));

        // Test valid option with value and a following option
        commandLine.parseArgument(optionAtIndex(2).getLongOpt(), "value3");
        assertTrue(group.canProcess(commandLine, optionAtIndex(0).getLongOpt()));
        assertTrue(group.canProcess(commandLine, optionAtIndex(2).getLongOpt()));

        // Test maximum number of options
        commandLine.parseArgument(optionAtIndex(0).getLongOpt(), "value1");
        commandLine.parseArgument(optionAtIndex(1).getLongOpt(), "value2");
        commandLine.parseArgument(optionAtIndex(2).getLongOpt(), "value3");
        assertFalse(group.canProcess(commandLine, optionAtIndex(3).getLongOpt()));
    }

    @Test
    public void testGetPrefixes() {
        assertTrue(group.getPrefixes().isEmpty());
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = group.getTriggers();

        assertEquals(4, triggers.size());
        assertTrue(triggers.containsAll(Arrays.asList(new String[]{"a", "b", "c", "d"})));
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine("TestCommand");
        ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

        // Test no options or arguments
        group.process(commandLine, arguments);
        assertTrue(commandLine.getArgs().size() == 0);

        // Test valid options
        commandLine.parseArgument(optionAtIndex(0).getLongOpt(), "value1");
        commandLine.parseArgument(optionAtIndex(1).getLongOpt(), "value2");
        group.process(commandLine, arguments);

        assertEquals(2, commandLine.getArgs().size());
        assertEquals("value1", commandLine.getArg(0));
        assertEquals("value2", commandLine.getArg(1));

        // Test anonymous argument
        commandLine.parseArgument("arg");
        group.process(commandLine, arguments);

        assertEquals(3, commandLine.getArgs().size());
        assertEquals("arg", commandLine.getArg(2));

        // Test invalid option
        commandLine.parseArgument("x");
        group.process(commandLine, arguments);

        assertEquals(4, commandLine.getArgs().size());
        assertNull(commandLine.getArg(3));

        // Test valid option with value and a following option
        commandLine.parseArgument(optionAtIndex(2).getLongOpt(), "value3");
        group.process(commandLine, arguments);

        assertEquals(5, commandLine.getArgs().size());
        assertEquals("value1", commandLine.getArg(0));
        assertEquals("value2", commandLine.getArg(1));
        assertEquals("arg", commandLine.getArg(2));
        assertEquals("value3", commandLine.getArg(3));
        assertNull(commandLine.getArg(4));
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLine("TestCommand");

        // Test no options
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            assertEquals(ResourceConstants.MISSING_OPTION, e.getMessage());
        }

        // Test all options present
        for (Option option : options) {
            commandLine.parseArgument(option.getLongOpt(), "value");
        }
        group.validate(commandLine);

        // Test missing option
        try {
            commandLine.parseArgument(optionAtIndex(1).getLongOpt(), "value");
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(ResourceConstants.MISSING_OPTION, e.getMessage());
        }

        // Test too many options
        commandLine.parseArgument(optionAtIndex(0).getLongOpt(), "value1");
        commandLine.parseArgument(optionAtIndex(1).getLongOpt(), "value2");
        commandLine.parseArgument(optionAtIndex(2).getLongOpt(), "value3");

        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals(ResourceConstants.TOO_MANY_OPTIONS, e.getMessage());
        }

        // Test valid options with values
        Map<String, String> arguments = new HashMap<>();
        arguments.put(optionAtIndex(0).getLongOpt(), "value1");
        arguments.put(optionAtIndex(1).getLongOpt(), "value2");
        commandLine.setParsedArgs(new WriteableCommandLine.ArgList(arguments.values()));

        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("TestGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test group for options", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("TestGroup:\n" +
                     "  Test group for options\n" +
                     "\n" +
                     "Options:\n" +
                     "=========\n" +
                     "a\targ1\tA test argument\n" +
                     "b\targ2\tAn optional test argument\n" +
                     "c\tA required test argument\n" +
                     "d\targ4\tAnother test argument\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        int depth = 1;
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<String> comp = null;

        List<HelpLine> helpLines = group.helpLines(depth, helpSettings, comp);

        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);

        assertEquals("TestGroup:", helpLine.getHeader());
        assertEquals("Test group for options\n", helpLine.getBody());

        List<HelpLine> helpLinesOptions = helpLine.getChildHelpLines();
        assertEquals(4, helpLinesOptions.size());

        for (HelpLine optionHelpLine : helpLinesOptions) {
            Option option = findOptionByName(optionHelpLine.getHeader());
            assertEquals(option.getLongOpt(), optionHelpLine.getHeader());
            assertEquals(option.getDescription(), optionHelpLine.getBody());
        }
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(Collections.emptyList(), group.getAnonymous());
    }

    private Option findOptionByName(String name) {
        for (Option option : options) {
            if (option.getLongOpt().equals(name) || option.getShortOpt().equals(name)) {
                return option;
            }
        }
        return null;
    }

    private Option optionAtIndex(int index) {
        return options.get(index);
    }
}