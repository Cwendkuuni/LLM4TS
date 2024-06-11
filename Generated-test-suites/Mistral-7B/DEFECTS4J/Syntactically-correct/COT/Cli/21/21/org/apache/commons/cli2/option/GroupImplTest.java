package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.*;
import org.apache.commons.cli2.option.GroupImpl;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.option.OptionBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class GroupImplTest {

    private List<Option> options;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("This is option 1")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("This is option 2")
                .create());
        options.add(OptionBuilder.withLongOpt("option3")
                .withDescription("This is option 3")
                .create());
    }

    @Test
    public void testGroupImplConstructor() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);
        assertNotNull(group);
        assertEquals("MyGroup", group.getPreferredName());
        assertNull(group.getDescription());
        assertEquals(0, group.getMinimum());
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
        assertFalse(group.isRequired());
        assertEquals(options, group.getOptions());
        assertEmpty(group.getAnonymous());
        assertNotNull(group.optionMap);
        assertNotNull(group.prefixes);
    }

    @Test
    public void testCanProcess() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test with valid trigger
        assertTrue(group.canProcess(commandLine, "option1"));

        // Test with invalid trigger when there is an option with the same trigger
        String invalidTrigger = "option1";
        commandLine.parseArgument(invalidTrigger);
        assertFalse(group.canProcess(commandLine, invalidTrigger));

        // Test with invalid trigger when there is no option with the same trigger
        commandLine.clearArguments();
        assertFalse(group.canProcess(commandLine, "invalidTrigger"));

        // Test with an argument
        commandLine.parseArgument("argument");
        assertFalse(group.canProcess(commandLine, "option1"));

        // Test with bursting
        String burstingTrigger = "option1";
        commandLine.parseArgument(burstingTrigger);
        assertTrue(group.canProcess(commandLine, "option2"));
    }

    @Test
    public void testGetPrefixes() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);
        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPrefixesUnmodifiableSet() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);
        group.prefixes.add("prefix");
    }

    @Test
    public void testGetTriggers() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);
        Set<String> triggers = group.getTriggers();
        assertEquals(3, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
        assertTrue(triggers.contains("option3"));
    }

    @Test
    public void testProcess() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test with valid trigger and no arguments
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertFalse(commandLine.hasOption("option1"));

        // Test with valid trigger and one argument
        commandLine.parseArgument("option1");
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertTrue(commandLine.hasOption("option1"));

        // Test with valid trigger and multiple arguments
        commandLine.clearArguments();
        commandLine.parseArgument("option1");
        commandLine.parseArgument("option1");
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.getOptionValues("option1").length > 1);

        // Test with invalid trigger
        commandLine.clearArguments();
        commandLine.parseArgument("invalidTrigger");
        assertFalse(group.canProcess(commandLine, "invalidTrigger"));
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertFalse(commandLine.hasOption("invalidTrigger"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOption() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        commandLine.parseArgument("invalidOption");
        group.process(commandLine, commandLine.getArguments().listIterator());
    }

    @Test
    public void testValidate() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        // Test with minimum number of options
        group.validate(commandLine);

        // Test with minimum number of options + 1
        commandLine.parseArgument("option1");
        group.validate(commandLine);

        // Test with maximum number of options
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            arguments.add("option" + (i + 1));
        }
        commandLine.parseArguments(arguments.toArray(new String[0]));
        group.validate(commandLine);

        // Test with maximum number of options + 1
        arguments.add("invalidOption");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidOption() {
        GroupImpl group = new GroupImpl(options, "MyGroup", null, 0, Integer.MAX_VALUE, false);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        commandLine.parseArgument("invalidOption");
        group.validate(commandLine);
    }
}