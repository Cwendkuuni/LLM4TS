package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class GroupImplTest {

    private List<Option> options;
    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        options.add(createOption("option1", "opt1", false, false, null, null));
        options.add(createOption("option2", "opt2", false, false, null, null));
        group = new GroupImpl(options, null, null, 0, Integer.MAX_VALUE);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess() {
        // Test with valid options
        for (Option option : options) {
            assertTrue(group.canProcess(commandLine, option.getTriggers().iterator().next()));
        }

        // Test with empty command line
        assertFalse(group.canProcess(commandLine, null));

        // Test with invalid option
        assertFalse(group.canProcess(commandLine, "invalid"));
    }

    @Test
    public void testProcess() {
        // Test with valid options
        for (Option option : options) {
            commandLine.parseArgument(option.getTriggers().iterator().next());
            group.process(commandLine, commandLine.getArguments().listIterator());
            assertEquals(option.getValue(), commandLine.getValueForOption(option));
        }

        // Test with invalid option
        commandLine.parseArgument("invalid");
        assertFalse(commandLine.hasOption("invalid"));

        // Test with anonymous argument
        commandLine.parseArgument("--anonymous");
        Option anonymousOption = findAnonymousOption(group, "anonymous");
        assertEquals("-", anonymousOption.getValue());
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidOptions() {
        // Test with missing options
        commandLine.addOption(createOption("missing", "miss", false, false, null, null));
        for (int i = 0; i < group.getMinimum(); i++) {
            commandLine.parseArgument("option1");
        }
        group.process(commandLine, commandLine.getArguments().listIterator());

        // Test with too many options
        for (int i = 0; i < group.getMinimum(); i++) {
            commandLine.parseArgument("option1");
        }
        commandLine.parseArgument("option1");
        group.process(commandLine, commandLine.getArguments().listIterator());
    }

    @Test
    public void testValidate() {
        // Test with valid options
        for (Option option : options) {
            commandLine.parseArgument(option.getTriggers().iterator().next());
            group.validate(commandLine);
        }

        // Test with missing options
        commandLine.parseArgument("option1");
        for (int i = 0; i < group.getMinimum() - 1; i++) {
            commandLine.parseArgument("option1");
        }
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Option 'option1' is missing", e.getMessage());
        }

        // Test with too many options
        commandLine.parseArgument("option1");
        for (int i = 0; i < group.getMaximum() + 1; i++) {
            commandLine.parseArgument("option1");
        }
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Option 'option1' appears too many times", e.getMessage());
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);
        assertTrue(helpLines.isEmpty());
    }

    private Option createOption(String longOpt, String opt, boolean required, boolean hidden, String args, String metavar) {
        return new OptionBuilder()
                .withLongOpt(longOpt)
                .withShortOpt(opt)
                .withRequiredOption(required)
                .withHidden(hidden)
                .withArgName(args)
                .withMetavar(metavar)
                .withValueSeparator(' ')
                .create();
    }

    private Option findAnonymousOption(Group group, String name) {
        for (Option option : group.getAnonymous()) {
            if (option.getLongOpt().equals(name)) {
                return option;
            }
        }

        if (group instanceof GroupImpl) {
            for (Option option : ((GroupImpl) group).getOptions()) {
                if (option instanceof Argument) {
                    Argument argument = (Argument) option;
                    if (argument.getName().equals(name)) {
                        return argument;
                    }
                } else {
                    Option found = findAnonymousOption((Group) option, name);
                    if (found != null) {
                        return found;
                    }
                }
            }
        }

        return null;
    }
}