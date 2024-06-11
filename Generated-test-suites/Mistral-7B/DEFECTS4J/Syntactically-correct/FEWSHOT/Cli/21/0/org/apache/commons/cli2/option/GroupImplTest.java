package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private static final String GROUP_NAME = "group";
    private static final String GROUP_DESCRIPTION = "Test group";

    private static final String OPTION1_NAME = "option1";
    private static final String OPTION1_DESCRIPTION = "Test option 1";

    private static final String OPTION2_NAME = "option2";
    private static final String OPTION2_DESCRIPTION = "Test option 2";

    private static final String ARGUMENT_NAME = "argument";
    private static final String ARGUMENT_DESCRIPTION = "Test argument";

    private GroupImpl group;
    private List<Option> options;
    private List<Argument> arguments;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        options = new ArrayList<>();
        options.add(new OptionBuilder(OPTION1_NAME).withDescription(OPTION1_DESCRIPTION).createOption());
        options.add(new OptionBuilder(OPTION2_NAME).withDescription(OPTION2_DESCRIPTION).createOption());

        arguments = new ArrayList<>();
        arguments.add(new ArgumentBuilder().withName(ARGUMENT_NAME).withDescription(ARGUMENT_DESCRIPTION).create());

        commandLine = new DefaultCommandLine();

        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION, 0, 1, false);

        for (Option option : options) {
            option.setParent(group);
        }

        group.addArgument(arguments.get(0));
    }

    @Test
    public void testCanProcess() {
        // Test with valid options
        commandLine.parse(OPTION1_NAME.split(""));
        assertTrue(group.canProcess(commandLine, OPTION1_NAME));

        commandLine.parse(OPTION2_NAME.split(""));
        assertTrue(group.canProcess(commandLine, OPTION2_NAME));

        // Test with an invalid option
        commandLine.parse("invalid".split(""));
        assertFalse(group.canProcess(commandLine, OPTION1_NAME));
        assertFalse(group.canProcess(commandLine, OPTION2_NAME));

        // Test with an argument
        commandLine.parse(ARGUMENT_NAME);
        assertTrue(group.canProcess(commandLine, ARGUMENT_NAME));

        // Test with a non-option argument
        commandLine.parse("nonOptionArgument");
        assertFalse(group.canProcess(commandLine, OPTION1_NAME));
        assertFalse(group.canProcess(commandLine, OPTION2_NAME));

        // Test with empty arguments
        commandLine.parse("");
        assertFalse(group.canProcess(commandLine, OPTION1_NAME));
        assertFalse(group.canProcess(commandLine, OPTION2_NAME));

        // Test with no arguments
        commandLine.clear();
        assertTrue(group.canProcess(commandLine, OPTION1_NAME));
        assertTrue(group.canProcess(commandLine, OPTION2_NAME));
        assertTrue(group.canProcess(commandLine, ARGUMENT_NAME));
    }

    @Test
    public void testProcess() throws OptionException {
        // Test with valid options
        commandLine.parse(OPTION1_NAME.split(""));
        group.process(commandLine, commandLine.listIterator());

        assertEquals(OPTION1_NAME, commandLine.getOptionValue(OPTION1_NAME));
        assertEquals(OPTION1_DESCRIPTION, commandLine.getOptionDescription(OPTION1_NAME));
        assertNull(commandLine.getOptionValue(OPTION2_NAME));
        assertNull(commandLine.getOptionDescription(OPTION2_NAME));
        assertNull(commandLine.getArgumentValue(ARGUMENT_NAME));

        commandLine.parse(OPTION2_NAME.split(""));
        group.process(commandLine, commandLine.listIterator());

        assertEquals(OPTION2_NAME, commandLine.getOptionValue(OPTION2_NAME));
        assertEquals(OPTION2_DESCRIPTION, commandLine.getOptionDescription(OPTION2_NAME));
        assertNull(commandLine.getOptionValue(OPTION1_NAME));
        assertNull(commandLine.getOptionDescription(OPTION1_NAME));
        assertNull(commandLine.getArgumentValue(ARGUMENT_NAME));

        // Test with an argument
        commandLine.parse(ARGUMENT_NAME);
        group.process(commandLine, commandLine.listIterator());

        assertNull(commandLine.getOptionValue(OPTION1_NAME));
        assertNull(commandLine.getOptionDescription(OPTION1_NAME));
        assertNull(commandLine.getOptionValue(OPTION2_NAME));
        assertNull(commandLine.getOptionDescription(OPTION2_NAME));
        assertEquals(ARGUMENT_NAME, commandLine.getArgumentValue(ARGUMENT_NAME));
        assertEquals(ARGUMENT_DESCRIPTION, commandLine.getArgumentDescription(ARGUMENT_NAME));
    }

    @Test
    public void testValidate() throws OptionException {
        // Test with valid options
        commandLine.parse(OPTION1_NAME.split(""));
        group.validate(commandLine);

        commandLine.parse(OPTION2_NAME.split(""));
        group.validate(commandLine);

        // Test with no options
        commandLine.clear();
        group.validate(commandLine);

        // Test with invalid options
        commandLine.parse(OPTION1_NAME.split(""));
        commandLine.parse(OPTION1_NAME.split(""));
        group.validate(commandLine);

        // Test with too many options
        commandLine.parse(OPTION1_NAME.split(""));
        commandLine.parse(OPTION1_NAME.split(""));
        commandLine.parse(OPTION1_NAME.split(""));
        group.validate(commandLine);

        // Test with missing options
        commandLine.parse(OPTION1_NAME.split(""));
        commandLine.parse(OPTION2_NAME.split(""));
        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(GROUP_NAME, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(GROUP_DESCRIPTION, group.getDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(arguments, group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption(OPTION1_NAME);
        assertSame(options.get(0), option1);

        Option option2 = group.findOption(OPTION2_NAME);
        assertSame(options.get(1), option2);

        Option notExistentOption = group.findOption("notExistentOptionName");
        assertNull(notExistentOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        assertNull(commandLine.getOptionValue(OPTION1_NAME));
        assertNull(commandLine.getOptionValue(OPTION2_NAME));
        assertNull(commandLine.getArgumentValue(ARGUMENT_NAME));
    }
}