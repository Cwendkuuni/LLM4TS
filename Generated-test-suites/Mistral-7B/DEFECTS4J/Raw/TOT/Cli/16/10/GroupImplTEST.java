package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        List<Option> groupOptions = Arrays.asList(
                new Option("g", "group", true, "group").setValueRequired(true),
                new Option("a", "anonymous", true, "anonymous"),
                new Option("i", "option", true, "option value")
        );
        List<Option> groupAnonymous = Arrays.asList(
                new Argument("anonymous_arg")
        );
        options = new Options();
        options.addGroup(new Group("group", "Group", groupOptions, groupAnonymous.toArray(new Option[0])));
    }

    @Test
    public void testCanProcessWithValidOptions() throws OptionException {
        String[] args = {"-g", "group", "-a", "anonymous", "-i", "option_value"};
        CommandLine commandLine = new DefaultParser().parse(options, args);

        assertTrue(options.getOption("group").isSet());
        assertTrue(options.getOption("group").getValue().equals("group"));
        assertTrue(options.getOption("anonymous").isSet());
        assertNull(options.getOption("anonymous").getValue());
        assertTrue(options.getOption("option").isSet());
        assertTrue(options.getOption("option").getValue().equals("option_value"));
        assertTrue(options.getAnonymousArguments().get(0).isSet());
        assertTrue(options.getAnonymousArguments().get(0).getValue().equals("anonymous_arg"));
    }

    @Test
    public void testCanProcessWithInvalidOption() throws OptionException {
        String[] args = {"-g", "group", "-x", "invalid_option"};
        CommandLine commandLine = new DefaultParser().parse(options, args);

        assertFalse(options.getOption("invalid_option").isSet());
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithMissingRequiredOption() throws OptionException {
        String[] args = {"-g", "group"};
        CommandLine commandLine = new DefaultParser().parse(options, args);

        assertFalse(options.getOption("group").getValue().equals("group"));
        throw new OptionException(commandLine, "group", "Value not present");
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithInvalidNumberOfOptions() throws OptionException {
        String[] args = {"-g", "group", "-a", "anonymous", "-i", "option_value", "-i", "option_value_2"};
        CommandLine commandLine = new DefaultParser().parse(options, args);

        throw new OptionException(commandLine, "group", "Number of values does not match expected number");
    }

    @Test
    public void testHelp() {
        String help = new HelpFormatter().printHelp("GroupImplTest", options, true);

        assertTrue(help.contains("Group"));
        assertTrue(help.contains("group"));
        assertTrue(help.contains("anonymous"));
        assertTrue(help.contains("option"));
        assertTrue(help.contains("anonymous_arg"));
    }

    @Test
    public void testValidate() throws OptionException {
        String[] args = {"-g", "group", "-a", "anonymous", "-i", "option_value"};
        CommandLine commandLine = new DefaultParser().parse(options, args);

        options.validate(commandLine);

        assertTrue(options.getOption("group").isSet());
        assertTrue(options.getOption("group").getValue().equals("group"));
        assertTrue(options.getOption("anonymous").isSet());
        assertNull(options.getOption("anonymous").getValue());
        assertTrue(options.getOption("option").isSet());
        assertTrue(options.getOption("option").getValue().equals("option_value"));
        assertTrue(options.getAnonymousArguments().get(0).isSet());
        assertTrue(options.getAnonymousArguments().get(0).getValue().equals("anonymous_arg"));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingRequiredOption() throws OptionException {
        String[] args = {"-g", "group"};
        CommandLine commandLine = new DefaultParser().parse(options, args);

        options.validate(commandLine);

        throw new OptionException(commandLine, "group", "Value not present");
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidNumberOfOptions() throws OptionException {
        String[] args = {"-g", "group", "-a", "anonymous", "-i", "option_value", "-i", "option_value_2"};
        CommandLine commandLine = new DefaultParser().parse(options, args);

        throw new OptionException(commandLine, "group", "Number of values does not match expected number");
    }
}