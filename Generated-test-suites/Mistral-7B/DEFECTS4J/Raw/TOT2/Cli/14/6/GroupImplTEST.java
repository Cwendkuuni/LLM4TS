package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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

/**
 * Tests for {@link GroupImpl}
 */
public class GroupImplTest {

    private List<Option> options;
    private Group group;

    @Before
    public void setUp() {
        // given
        options = new ArrayList<>();
        options.add(new Option("opt1", "Option 1", false));
        options.add(new Option("opt2", "Option 2", true));
        options.add(new Option("opt3", "Option 3", false));
        options.add(new Option("opt4", "Option 4", false));

        group = new GroupImpl(options, "Group", "Test Group", 0, Integer.MAX_VALUE);
    }

    @Test
    public void testCanProcessWithValidOptions() {
        // when
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");

        // then
        assertTrue(group.canProcess(cmd, "opt1"));
        assertTrue(group.canProcess(cmd, "opt2"));
        assertTrue(group.canProcess(cmd, "opt3"));
        assertTrue(group.canProcess(cmd, "opt4"));
    }

    @Test
    public void testCanProcessWithInvalidOption() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");

        // when
        String invalidOption = "invalidOption";

        // then
        assertFalse(group.canProcess(cmd, invalidOption));
    }

    @Test
    public void testCanProcessWithBurstedOption() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");
        cmd.addOption(new Option("opt2", "Option 2", true));

        // when
        String burstedOption = "opt2";

        // then
        assertTrue(group.canProcess(cmd, burstedOption));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithInvalidBurstedOption() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");
        cmd.addOption(new Option("opt2", "Option 2", true));

        // when
        String invalidBurstedOption = "invalidOption";

        // then
        group.canProcess(cmd, invalidBurstedOption);
    }

    @Test
    public void testCanProcessWithArgument() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");
        cmd.addArgument("arg");

        // when
        String argument = "arg";

        // then
        assertTrue(group.canProcess(cmd, argument));
    }

    @Test
    public void testGetTriggers() {
        // when
        Set<String> triggers = group.getTriggers();

        // then
        assertEquals(Collections.unmodifiableSet(options.stream()
                .map(Option::getTriggers)
                .flatMap(Set::stream)
                .collect(Collectors.toSet())), triggers);
    }

    @Test
    public void testGetPrefixes() {
        // when
        Set<String> prefixes = group.getPrefixes();

        // then
        assertEquals(Collections.unmodifiableSet(options.stream()
                .map(Option::getPrefixes)
                .flatMap(Set::stream)
                .collect(Collectors.toSet())), prefixes);
    }

    @Test
    public void testProcessWithValidOptions() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");

        // when
        List<String> arguments = new ArrayList<>();
        group.process(cmd, arguments.listIterator());

        // then
        assertEquals(Collections.unmodifiableList(options), arguments);

        // and
        verifyOptionsProcessed(cmd);
    }

    @Test
    public void testProcessWithInvalidOption() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");
        cmd.addOption(new Option("opt5", "Invalid Option", false));

        // when
        List<String> arguments = new ArrayList<>();
        group.process(cmd, arguments.listIterator());

        // then
        assertTrue(arguments.isEmpty());

        // and
        assertFalse(cmd.hasOption("opt5"));
    }

    @Test
    public void testProcessWithBurstedOption() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");
        cmd.addOption(new Option("opt2", "Option 2", true));

        // when
        List<String> arguments = new ArrayList<>();
        group.process(cmd, arguments.listIterator());

        // then
        assertEquals(Collections.unmodifiableList(Arrays.asList("opt2")), arguments);

        // and
        assertTrue(cmd.hasOption("opt2"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidBurstedOption() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");
        cmd.addOption(new Option("opt5", "Invalid Option", false));

        // when
        List<String> arguments = new ArrayList<>();
        group.process(cmd, arguments.listIterator());

        // then
        assertEquals(Collections.emptyList(), arguments);

        // and
        assertFalse(cmd.hasOption("opt5"));
    }

    @Test
    public void testProcessWithArgument() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");
        cmd.addArgument("arg");

        // when
        List<String> arguments = new ArrayList<>();
        group.process(cmd, arguments.listIterator());

        // then
        assertTrue(arguments.isEmpty());

        // and
        assertFalse(cmd.hasOption("arg"));
    }

    @Test
    public void testValidateWithValidOptions() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");

        // when
        group.validate(cmd);

        // then
        assertEquals(Collections.unmodifiableList(options), cmd.getOptions());

        // and
        assertEquals(Collections.unmodifiableList(options), cmd.getArguments());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");

        // when
        group.validate(cmd);

        // then
        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.getArguments().isEmpty());

        // and
        throw new AssertionError("Expected OptionException");
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");
        cmd.addOption(new Option("opt1", "Option 1", false));
        cmd.addOption(new Option("opt2", "Option 2", true));
        cmd.addOption(new Option("opt5", "Invalid Option", false));

        // when
        group.validate(cmd);

        // then
        assertTrue(cmd.getOptions().size() == 2);
        assertTrue(cmd.getArguments().isEmpty());

        // and
        throw new AssertionError("Expected OptionException");
    }

    @Test
    public void testGetMinimum() {
        // when
        int minimum = group.getMinimum();

        // then
        assertEquals(minimum, 0);
    }

    @Test
    public void testGetMaximum() {
        // when
        int maximum = group.getMaximum();

        // then
        assertEquals(maximum, Integer.MAX_VALUE);
    }

    @Test
    public void testIsRequired() {
        // when
        boolean isRequired = group.isRequired();

        // then
        assertEquals(isRequired, false);
    }

    @Test
    public void testDefaults() {
        // given
        WriteableCommandLine cmd = new WriteableCommandLine("cmd");

        // when
        group.defaults(cmd);

        // then
        assertFalse(cmd.hasOption("opt1"));
        assertFalse(cmd.hasOption("opt2"));
        assertFalse(cmd.hasOption("opt3"));
        assertFalse(cmd.hasOption("opt4"));
    }

    private void verifyOptionsProcessed(WriteableCommandLine cmd) {
        for (Option option : options) {
            assertTrue(cmd.hasOption(option.getLongOpt()));
            assertTrue(cmd.hasOption(option.getShortOpt()));
            if (option.getValueSeparator() != null) {
                assertEquals(option.getValueSeparator(), cmd.getOptionValue(option.getLongOpt()));
            } else {
                assertTrue(cmd.hasOptionValue(option.getLongOpt()));
            }
        }
    }
}