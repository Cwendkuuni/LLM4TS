import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.exceptions.OptionException;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @Test
    public void testBasicGroup() throws OptionException {
        final String[] args = {"-g", "foo", "-a", "1", "-b", "2", "--", "arg"};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("a", null, false, "an option"),
                new Option("b", null, true, "an option with an argument").argName("ARG"),
                new Option("g", "g", false, "a group")
            ),
            "group",
            "This is a group."
        );
        options.addOptionGroup(group);

        parser.parse(args, options);

        final CommandLine commandLine = parser.getParseResult();

        assertEquals("foo", group.getName());
        assertEquals("This is a group.", group.getDescription());
        assertEquals(2, group.getOptions().size());
        assertFalse(group.isRequired());
        assertTrue(group.canProcess(commandLine, "g"));
        assertNull(group.getValue(commandLine));
        assertEquals(2, group.getMinimum());
        assertEquals(0, group.getMaximum());

        final List groupOptions = group.getOptions();
        assertTrue(groupOptions.get(0) instanceof Option);
        assertTrue(groupOptions.get(1) instanceof Option);

        final Option optionA = (Option) groupOptions.get(0);
        assertEquals("an option", optionA.getLongOpt());
        assertEquals(false, optionA.getValue().isSet());
        assertFalse(optionA.canTakeValue());

        final Option optionB = (Option) groupOptions.get(1);
        assertEquals("an option with an argument", optionB.getLongOpt());
        assertEquals("ARG", optionB.getArgName());
        assertEquals(1, optionB.getValue().size());
        assertTrue(optionB.getValue().get(0).equals("1"));

        assertEquals(1, commandLine.getOptions().size());
        assertEquals("arg", commandLine.getOptions().get(0).getValue().get(0));
    }

    @Test
    public void testGroupWithMinimum() throws OptionException {
        final String[] args = {"-g", "--"};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("a", null, false, "an option"),
                new Option("b", null, true, "an option with an argument").argName("ARG"),
                new Option("g", "g", false, "a group")
            ),
            "group",
            "This is a group.",
            1
        );
        options.addOptionGroup(group);

        parser.parse(args, options);

        final CommandLine commandLine = parser.getParseResult();

        assertEquals("group", group.getName());
        assertEquals("This is a group.", group.getDescription());
        assertEquals(2, group.getOptions().size());
        assertTrue(group.isRequired());
        assertFalse(group.canProcess(commandLine, "g"));
        assertNull(group.getValue(commandLine));
        assertEquals(1, group.getMinimum());
        assertEquals(0, group.getMaximum());
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMaximum() throws OptionException {
        final String[] args = {"-g", "-a", "-b", "arg"};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("a", null, false, "an option"),
                new Option("b", null, true, "an option with an argument").argName("ARG"),
                new Option("g", "g", false, "a group")
            ),
            "group",
            "This is a group.",
            1,
            2
        );
        options.addOptionGroup(group);

        parser.parse(args, options);
    }

    @Test
    public void testGroupWithMaximumAndMultipleOptions() throws OptionException {
        final String[] args = {"-g", "-a", "-b", "-a", "-b", "--"};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("a", null, false, "an option"),
                new Option("b", null, true, "an option with an argument").argName("ARG"),
                new Option("g", "g", false, "a group")
            ),
            "group",
            "This is a group.",
            1,
            3
        );
        options.addOptionGroup(group);

        parser.parse(args, options);

        final CommandLine commandLine = parser.getParseResult();

        final int numberOfOptions = commandLine.getOptions().size();
        assertEquals(3, numberOfOptions);
    }

    @Test
    public void testGroupWithAnonymousArguments() throws OptionException {
        final String[] args = {"--arg1", "arg2", "--arg2", "arg3"};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("g", "g", false, "a group"),
                new Option("arg1", "arg1", true, "an argument").argName("ARG1"),
                new Option("arg2", "arg2", true, "an argument").argName("ARG2")
            ),
            "group"
        );
        group.addAnonymousArguments(true, "arg");
        options.addOptionGroup(group);

        parser.parse(args, options);

        final CommandLine commandLine = parser.getParseResult();

        final int numberOfAnonymousArguments = commandLine.getAnonymousArguments().size();
        assertEquals(2, numberOfAnonymousArguments);

        final String anonymousArgument1 = commandLine.getAnonymousArguments().get(0);
        assertEquals("arg2", anonymousArgument1);

        final String anonymousArgument2 = commandLine.getAnonymousArguments().get(1);
        assertEquals("arg3", anonymousArgument2);
    }

    @Test
    public void testGroupWithAnonymousArgumentsAndOptions() throws OptionException {
        final String[] args = {"--arg1", "arg2", "-a", "--arg2", "arg3", "--", "arg4"};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("g", "g", false, "a group"),
                new Option("a", null, false, "an option"),
                new Option("arg1", "arg1", true, "an argument").argName("ARG1"),
                new Option("arg2", "arg2", true, "an argument").argName("ARG2")
            ),
            "group"
        );
        group.addAnonymousArguments(true, "arg");
        options.addOptionGroup(group);

        parser.parse(args, options);

        final CommandLine commandLine = parser.getParseResult();

        final int numberOfAnonymousArguments = commandLine.getAnonymousArguments().size();
        assertEquals(2, numberOfAnonymousArguments);

        final String anonymousArgument1 = commandLine.getAnonymousArguments().get(0);
        assertEquals("arg2", anonymousArgument1);

        final String anonymousArgument2 = commandLine.getAnonymousArguments().get(1);
        assertEquals("arg3", anonymousArgument2);

        final int numberOfOptions = commandLine.getOptions().size();
        assertEquals(3, numberOfOptions);

        final Option optionA = commandLine.getOption("a");
        assertTrue(optionA.getValue().isSet());

        final Option optionArg1 = commandLine.findOption("arg1");
        assertEquals("arg2", optionArg1.getValue().get(0));

        final Option optionArg2 = commandLine.findOption("arg2");
        assertEquals("arg3", optionArg2.getValue().get(0));
    }

    @Test
    public void testGroupWithAnonymousArgumentsAndOptionsBursted() throws OptionException {
        final String[] args = {"--arg1", "arg2", "-g", "arg3", "--arg2", "arg4", "--", "arg5"};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("g", null, false, "a group"),
                new Option("arg1", "arg1", true, "an argument").argName("ARG1"),
                new Option("arg2", "arg2", true, "an argument").argName("ARG2")
            ),
            "group"
        );
        group.addAnonymousArguments(true, "arg");
        options.addOptionGroup(group);

        parser.parse(args, options);

        final CommandLine commandLine = parser.getParseResult();

        final int numberOfAnonymousArguments = commandLine.getAnonymousArguments().size();
        assertEquals(3, numberOfAnonymousArguments);

        final String anonymousArgument1 = commandLine.getAnonymousArguments().get(0);
        assertEquals("arg2", anonymousArgument1);

        final String anonymousArgument2 = commandLine.getAnonymousArguments().get(1);
        assertEquals("arg3", anonymousArgument2);

        final String anonymousArgument3 = commandLine.getAnonymousArguments().get(2);
        assertEquals("arg4", anonymousArgument3);

        final int numberOfOptions = commandLine.getOptions().size();
        assertEquals(1, numberOfOptions);

        final Option optionG = commandLine.findOption("g");
        assertEquals("arg3", optionG.getValue().get(0));

        final Option optionArg1 = commandLine.findOption("arg1");
        assertEquals("arg2", optionArg1.getValue().get(0));

        final Option optionArg2 = commandLine.findOption("arg2");
        assertEquals("arg4", optionArg2.getValue().get(0));
    }

    @Test
    public void testGroupWithAnonymousArgumentsAndOptionsBurstedAndValueSet() throws OptionException {
        final String[] args = {"--arg1", "arg2", "-g", "value", "--arg2", "arg4", "--", "arg5"};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("g", null, false, "a group"),
                new Option("arg1", "arg1", true, "an argument").argName("ARG1"),
                new Option("arg2", "arg2", true, "an argument").argName("ARG2")
            ),
            "group"
        );
        group.addAnonymousArguments(true, "arg");
        options.addOptionGroup(group);

        parser.parse(args, options);

        final CommandLine commandLine = parser.getParseResult();

        final int numberOfAnonymousArguments = commandLine.getAnonymousArguments().size();
        assertEquals(3, numberOfAnonymousArguments);

        final String anonymousArgument1 = commandLine.getAnonymousArguments().get(0);
        assertEquals("arg2", anonymousArgument1);

        final String anonymousArgument2 = commandLine.getAnonymousArguments().get(1);
        assertEquals("arg4", anonymousArgument2);

        final String anonymousArgument3 = commandLine.getAnonymousArguments().get(2);
        assertEquals("arg5", anonymousArgument3);

        final int numberOfOptions = commandLine.getOptions().size();
        assertEquals(1, numberOfOptions);

        final Option optionG = commandLine.findOption("g");
        assertEquals("value", optionG.getValue().get(0));

        final Option optionArg1 = commandLine.findOption("arg1");
        assertEquals("arg2", optionArg1.getValue().get(0));

        final Option optionArg2 = commandLine.findOption("arg2");
        assertEquals("arg4", optionArg2.getValue().get(0));
    }

    @Test
    public void testHelp() {
        final String[] args = {};

        final DefaultParser parser = new DefaultParser();
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
            List.of(
                new Option("g", "g", false, "a group")
            ),
            "group"
        );
        options.addOptionGroup(group);

        parser.setHelpFormatter(new HelpFormatter());

        final CommandLine commandLine = parser.parse(args, options);
        parser.printHelp("test", options);

        final String help = TestUtils.getHelpText(parser, "test", options, null, null, null, null);

        final int helpLinesSize = help.split("\n").length;
        assertEquals(49, helpLinesSize);
    }
}