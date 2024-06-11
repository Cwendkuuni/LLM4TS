package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        List<Option> options = Arrays.asList(
                new Option("o1", "option 1", true),
                new Option("o2", "option 2"),
                new Argument("a1", "argument 1", true),
                new Argument("a2", "argument 2")
        );

        Group group = new GroupImpl(options, "MyGroup", null, 0, 1, false);

        assertEquals("MyGroup", group.getPreferredName());
        assertEquals(4, group.getOptions().size());
        assertEquals(2, group.getAnonymous().size());
        assertEquals(Collections.unmodifiableSet(Arrays.asList("o1", "o2")), group.getTriggers());
        assertEquals(Collections.unmodifiableSet(Collections.emptySet()), group.getPrefixes());
    }

    @Test
    public void testCanProcess() {
        List<Option> options = Arrays.asList(
                new Option("o1", "option 1", true),
                new Option("o2", "option 2"),
                new Argument("a1", "argument 1", true),
                new Argument("a2", "argument 2")
        );

        Group group = new GroupImpl(options, "MyGroup", null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test empty command line
        assertFalse(group.canProcess(commandLine, null));

        // Test valid option
        commandLine.parseArgument(0, "o1");
        assertTrue(group.canProcess(commandLine, "o1"));

        // Test option not found
        assertFalse(group.canProcess(commandLine, "o3"));

        // Test anonymous argument
        assertFalse(group.canProcess(commandLine, "a1"));

        // Test valid argument
        commandLine.parseArgument(0, "a1");
        assertTrue(group.canProcess(commandLine, "a1"));
    }

    @Test
    public void testProcess() throws OptionException {
        List<Option> options = Arrays.asList(
                new Option("o1", "option 1", true),
                new Option("o2", "option 2"),
                new Argument("a1", "argument 1", true),
                new Argument("a2", "argument 2")
        );

        Group group = new GroupImpl(options, "MyGroup", null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.parseArgument(0, "o1");
        commandLine.parseArgument(1, "a1");
        commandLine.parseArgument(2, "a2");

        ListIterator<String> arguments = commandLine.getArguments().listIterator();

        group.process(commandLine, arguments);

        // o1 should have been processed
        assertEquals("o1", commandLine.getOptionValues("o1")[0]);

        // a1 should have been processed
        assertEquals("a1", commandLine.getOptionValues("a1")[0]);

        // a2 should have been processed
        assertEquals("a2", commandLine.getOptionValues("a2")[0]);

        // o2 should not have been processed
        assertNull(commandLine.getOptionValues("o2"));

        // Verify that the arguments list is empty
        assertFalse(arguments.hasNext());
    }

    @Test
    public void testValidate() throws OptionException {
        List<Option> options = Arrays.asList(
                new Option("o1", "option 1", true),
                new Option("o2", "option 2"),
                new Argument("a1", "argument 1", true),
                new Argument("a2", "argument 2")
        );

        Group group = new GroupImpl(options, "MyGroup", null, 0, 1, false);

        WriteableCommandLine commandLine = new WriteableCommandLine();

        // Test valid command line
        group.validate(commandLine);

        // Test missing option
        commandLine.clear();
        commandLine.parseArgument(0, "o2");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // Expected
        }

        // Test too many options
        commandLine.clear();
        commandLine.parseArgument(0, "o1");
        commandLine.parseArgument(1, "o1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // Expected
        }

        // Test missing anonymous argument
        commandLine.clear();
        commandLine.parseArgument(0, "o1");
        commandLine.parseArgument(1, "a1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // Expected
        }

        // Test too few anonymous arguments
        commandLine.clear();
        commandLine.parseArgument(0, "o1");
        commandLine.parseArgument(1, "a1");
        commandLine.parseArgument(2, "a1");
        commandLine.parseArgument(3, "a1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // Expected
        }
    }

    @Test
    public void testGetOptions() {
        List<Option> options = Arrays.asList(
                new Option("o1", "option 1", true),
                new Option("o2", "option 2"),
                new Argument("a1", "argument 1", true),
                new Argument("a2", "argument 2")
        );

        Group group = new GroupImpl(options, "MyGroup", null, 0, 1, false);

        List<Option> optionsList = new ArrayList<>(group.getOptions());

        assertEquals("o1", optionsList.get(0).getLongOpt());
        assertEquals("o2", optionsList.get(1).getLongOpt());
        assertEquals(2, optionsList.size());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> options = Arrays.asList(
                new Option("o1", "option 1", true),
                new Option("o2", "option 2"),
                new Argument("a1", "argument 1", true),
                new Argument("a2", "argument 2")
        );

        Group group = new GroupImpl(options, "MyGroup", null, 0, 1, false);

        List<Argument> anonymousList = new ArrayList<>((List<Argument>) group.getAnonymous());

        assertEquals("a1", anonymousList.get(0).getLongOpt());
        assertEquals("a2", anonymousList.get(1).getLongOpt());
        assertEquals(2, anonymousList.size());
    }

    @Test
    public void testFindOption() {
        List<Option> options = Arrays.asList(
                new Option("o1", "option 1", true),
                new Option("o2", "option 2"),
                new Argument("a1", "argument 1", true),
                new Argument("a2", "argument 2")
        );

        Group group = new GroupImpl(options, "MyGroup", null, 0, 1, false);

        Option option = group.findOption("o1");

        assertEquals("o1", option.getLongOpt());
        assertNull(group.findOption("o3"));
    }

    @Test
    public void testGetMinimumAndMaximum() {
        List<Option> options = Arrays.asList(
                new Option("o1", "option 1", true),
                new Option("o2", "option 2"),
                new Argument("a1", "argument 1", true),
                new Argument("a2", "argument 2")
        );

        Group group = new GroupImpl(options, "MyGroup", null, 1, 2, false);

        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
    }
}