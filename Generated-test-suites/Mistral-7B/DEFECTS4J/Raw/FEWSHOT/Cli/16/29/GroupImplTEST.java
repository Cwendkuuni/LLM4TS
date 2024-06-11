package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("a", "a", false, null, null));
        options.add(createOption("b", "b", false, null, null));
        options.add(createOption("c", "c", false, null, null));

        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(createOption("g", "g", false, null, null));
        groupOptions.add(options.get(0));
        groupOptions.add(options.get(1));

        Group group = new GroupImpl(groupOptions, "group", null, 1, 3);
        assertEquals("group", group.getName());
        assertEquals("", group.getDescription());
        assertEquals(groupOptions, group.getOptions());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertNull(group.getAnonymous());

        assertEquals(Collections.singletonList(group), group.helpLines(0, null, null));
        assertEquals(1, group.getOptions().size());
        assertEquals(2, group.getOptions().get(0).getTriggers().size());

        WriteableCommandLine commandLine = new WriteableCommandLine();
        try {
            group.process(commandLine, commandLine.iterator());
            fail("Expected OptionException");
        } catch (OptionException e) {
            // expected
        }

        Parser parser = new Parser();
        HelpFormatter formatter = new HelpFormatter();
        parser.parse(new String[]{"-g"}, null);
        formatter.printHelp("MyApp", parser.getCommandLine(), true);
        assertTrue(formatter.getOutput().contains("group"));

        // test that anonymous Arguments are processed correctly
        List<Option> anonymousOptions = new ArrayList<>();
        anonymousOptions.add(createArgument("arg1"));
        anonymousOptions.add(createArgument("arg2"));

        Group groupWithArgs = new GroupImpl(groupOptions, "group", null, 1, 3, anonymousOptions);
        String[] args = {"-g", "value1", "value2"};
        commandLine = new WriteableCommandLine(args);
        groupWithArgs.process(commandLine, commandLine.iterator());
        assertEquals("value1", commandLine.getOptionValue("a"));
        assertEquals("value2", commandLine.getOptionValue("b"));

    }

    @Test(expected = OptionException.class)
    public void testGroupImplInvalidUsage() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("a", "a", false, null, null));
        options.add(createOption("b", "b", false, null, null));
        options.add(createOption("c", "c", false, null, null));

        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(createOption("g", "g", false, null, null));
        groupOptions.add(options.get(0));
        groupOptions.add(options.get(1));
        groupOptions.add(options.get(1)); // duplicate option

        Group group = new GroupImpl(groupOptions, "group", null, 1, 3);

    }

    @Test(expected = OptionException.class)
    public void testGroupImplInvalidMinimum() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("a", "a", false, null, null));
        options.add(createOption("b", "b", false, null, null));
        options.add(createOption("c", "c", false, null, null));

        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(createOption("g", "g", false, null, null));
        groupOptions.add(options.get(0));
        groupOptions.add(options.get(1));

        Group group = new GroupImpl(groupOptions, "group", null, 0, 3);

    }

    @Test(expected = OptionException.class)
    public void testGroupImplInvalidMaximum() {
        List<Option> options = new ArrayList<>();
        options.add(createOption("a", "a", false, null, null));
        options.add(createOption("b", "b", false, null, null));
        options.add(createOption("c", "c", false, null, null));

        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(createOption("g", "g", false, null, null));
        groupOptions.add(options.get(0));
        groupOptions.add(options.get(1));

        Group group = new GroupImpl(groupOptions, "group", null, 1, 0);

    }

    private Option createOption(String longOpt, String opt, boolean required, String... alt) {
        OptionBuilder builder = OptionBuilder.withLongOpt(longOpt)
                .withShortOpt(opt)
                .isRequired(required);

        if (alt != null) {
            for (String a : alt) {
                builder.withAlt(a);
            }
        }

        return builder.createOption();
    }

    private Argument createArgument(String longArg) {
        return new ArgumentBuilder()
                .withLongOpt(longArg)
                .createArgument();
    }
}