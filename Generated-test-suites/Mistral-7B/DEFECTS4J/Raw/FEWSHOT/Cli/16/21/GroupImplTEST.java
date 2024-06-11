package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.options.OptionBuilder;
import org.apache.commons.cli2.util.Arguments;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    @Test
    public void testCreateGroup() throws OptionException {
        final Parser parser = new Parser();
        final List<Option> options = new ArrayList<>();
        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        options.add(option1);
        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create();
        options.add(option2);

        final Group group = new GroupImpl(options, "group", "Group of options", 1, 1);
        assertNotNull(group);
        assertEquals("group", group.getPreferredName());
        assertEquals("Group of options", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(1, group.getMaximum());
        assertTrue(group.isRequired());
        assertEquals(options, group.getOptions());
        assertNull(group.getAnonymous());
        assertEquals(Collections.emptyMap(), group.getOptionMap());
        assertEquals(Collections.emptySet(), group.getPrefixes());
    }

    @Test
    public void testCanProcess() throws Exception {
        final Parser parser = new Parser();
        final List<Option> options = new ArrayList<>();
        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        options.add(option1);
        final Group group = new GroupImpl(options, "group", "Group of options", 1, 1);

        final WriteableCommandLine commandLine = new WriteableCommandLine("TestCommand");

        // null argument
        assertFalse(group.canProcess(commandLine, null));

        // no argument
        assertFalse(group.canProcess(commandLine, ""));

        // option1
        assertFalse(group.canProcess(commandLine, "option1"));

        // option2
        assertFalse(group.canProcess(commandLine, "option2"));

        // bursting
        commandLine.parse(Arrays.asList("--group", "option1"));
        assertTrue(group.canProcess(commandLine, "option1"));

        // argument
        commandLine.parse(Arrays.asList("--group", "arg"));
        assertFalse(group.canProcess(commandLine, "arg"));

        // anonymous argument
        commandLine.parse(Arrays.asList("--group", "arg1", "arg2"));
        assertTrue(group.canProcess(commandLine, "arg1"));
        assertTrue(group.canProcess(commandLine, "arg2"));
    }

    @Test
    public void testGetPrefixes() throws Exception {
        final Parser parser = new Parser();
        final List<Option> options = new ArrayList<>();
        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        options.add(option1);
        final Group group = new GroupImpl(options, "group", "Group of options", 1, 1);

        Set prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());

        final Option optionWithPrefix = OptionBuilder.withLongOpt("prefix-option")
                .withDescription("Option with prefix")
                .create();
        options.add(optionWithPrefix);
        group = new GroupImpl(options, "group", "Group of options", 1, 1);
        prefixes = group.getPrefixes();
        assertEquals(Collections.singleton("prefix"), prefixes);
    }

    @Test
    public void testProcess() throws Exception {
        final Parser parser = new Parser();
        final List<Option> options = new ArrayList<>();
        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        options.add(option1);
        final Group group = new GroupImpl(options, "group", "Group of options", 1, 1);

        final WriteableCommandLine commandLine = new WriteableCommandLine("TestCommand");

        // no arguments
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertTrue(commandLine.getOptions().isEmpty());

        // option1
        commandLine.parse(Arrays.asList("--group", "option1"));
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOptionByName("option1"));

        // option2
        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create();
        options.add(option2);
        group = new GroupImpl(options, "group", "Group of options", 1, 1);
        commandLine.parse(Arrays.asList("--group", "option1", "option2"));
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(2, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOptionByName("option1"));
        assertEquals(option2, commandLine.getOptionByName("option2"));

        // argument
        final Arguments arguments = new Arguments();
        arguments.addArgument("arg1");
        commandLine.setArguments(arguments);
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getArgument("arg1").isSet());

        // anonymous argument
        final Option optionAnonymous = OptionBuilder.withLongOpt("anonymous")
                .withDescription("Anonymous option")
                .create(new Arguments());
        options.add(optionAnonymous);
        group = new GroupImpl(options, "group", "Group of options", 1, 1);
        commandLine.parse(Arrays.asList("--group", "option1", "anonymous=arg"));
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(3, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOptionByName("option1"));
        assertEquals(optionAnonymous, commandLine.getOptionByName("anonymous"));
        assertTrue(commandLine.getArgument("arg").isUnbound());

        // bursted option1 and anonymous argument
        commandLine.parse(Arrays.asList("--group=option1", "arg1", "anonymous=arg"));
        group.process(commandLine, commandLine.getArguments().listIterator());
        assertEquals(3, commandLine.getOptions().size());
        assertEquals(option1, commandLine.getOptionByName("option1"));
        assertEquals(optionAnonymous, commandLine.getOptionByName("anonymous"));
        assertTrue(commandLine.getArgument("arg1").isSet());
        assertTrue(commandLine.getArgument("arg").isUnbound());
    }

    @Test
    public void testValidate() throws Exception {
        final Parser parser = new Parser();
        final List<Option> options = new ArrayList<>();
        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create();
        options.add(option1);
        final Group group = new GroupImpl(options, "group", "Group of options", 1, 1);

        final WriteableCommandLine commandLine = new WriteableCommandLine("TestCommand");

        // no arguments
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // option1
        commandLine.parse(Arrays.asList("--group", "option1"));
        group.validate(commandLine);

        // option1 and option2
        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create();
        options.add(option2);
        group = new GroupImpl(options, "group", "Group of options", 1, 1);
        commandLine.parse(Arrays.asList("--group", "option1", "option2"));
        group.validate(commandLine);

        // option1, option2 and anonymous argument
        final Option anonymousOption = OptionBuilder.withLongOpt("anonymous")
                .withDescription("Anonymous option")
                .create(new Arguments());
        options.add(anonymousOption);
        group = new GroupImpl(options, "group", "Group of options", 1, 1);
        commandLine.parse(Arrays.asList("--group", "option1", "option2", "anonymous=arg"));
        group.validate(commandLine);

        // option1, option2 and two anonymous arguments
        commandLine.parse(Arrays.asList("--group", "option1", "option2", "anonymous=arg1", "anonymous=arg2"));
        group.validate(commandLine);

        // option1, option2 and too many anonymous arguments
        commandLine.parse(Arrays.asList("--group", "option1", "option2", "anonymous=arg1", "anonymous=arg2", "anonymous=arg3"));
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }
}

class Arguments {
    private final List<String> arguments;

    Arguments() {
        this.arguments = new ArrayList<>();
    }

    void addArgument(String argument) {
        this.arguments.add(argument);
    }

    Iterator<String> listIterator() {
        return this.arguments.listIterator();
    }

    Argument getArgument(String name) {
        return new Arguments() {
            @Override
            public boolean isSet() {
                return Arrays.asList(getArguments()).contains(name);
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getValue() {
                return null;
            }

            @Override
            public String[] getValues() {
                return new String[0];
            }

            @Override
            public Arguments getValuesAsArguments() {
                return this;
            }

            @Override
            public Iterator<String> iterator() {
                return Collections.emptyIterator();
            }

            @Override
            public String[] getArguments() {
                return this.arguments.toArray(new String[0]);
            }
        };
    }
}