package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.Strings;
import org.apache.commons.cli2.util.DefaultHelpFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.powermock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GroupImpl.class})
public class GroupImplTest {

    private CommandLine commandLine;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        formatter = new DefaultHelpFormatter();
    }

    @Test
    public void testGroupWithValidOptions() throws ParseException {
        final List<Option> options = Arrays.asList(
                optionWithName("a").required(false),
                optionWithName("b").required(true),
                optionWithName("c").required(false),
                optionWithName("d").required(false)
        );

        final GroupImpl group = new GroupImpl(options, null, "My Group", 0, 1);

        commandLine.parse(group.parseArguments(Strings.toStringArray(new String[]{"--a", "--b", "--c"})));
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertFalse(commandLine.hasOption("d"));

        group.printHelp("MyGroup", formatter, null, false, false);
        assertEquals("My Group:\n" +
                "options:\n" +
                "  -a, --a\t\t\toptional\n" +
                "  -b, --b\t\t\trequired\n" +
                "  -c, --c\t\t\toptional\n" +
                "  -d, --d\t\t\toptional\n", formatter.printHelp(group));
    }

    @Test
    public void testGroupWithInvalidOptions() throws ParseException {
        final List<Option> options = Arrays.asList(
                optionWithName("a").required(false),
                optionWithName("b").required(true),
                optionWithName("c").required(false),
                optionWithName("d").required(false)
        );

        final GroupImpl group = new GroupImpl(options, null, "My Group", 1, 1);

        try {
            commandLine.parse(group.parseArguments(Strings.toStringArray(new String[]{"--a", "--a"})));
            fail("Expected ParseException to be thrown.");
        } catch (ParseException e) {
            // expected
        }

        try {
            commandLine.parse(group.parseArguments(Strings.toStringArray(new String[]{"--a"})));
            fail("Expected ParseException to be thrown.");
        } catch (ParseException e) {
            // expected
        }

        group.printHelp("MyGroup", formatter, null, false, false);
        assertEquals("My Group:\n" +
                "options:\n" +
                "  -a, --a\t\t\toptional\n" +
                "  -b, --b\t\t\trequired\n" +
                "  -c, --c\t\t\toptional\n" +
                "  -d, --d\t\t\toptional\n", formatter.printHelp(group));
    }

    @Test
    public void testGroupWithEmptyOptions() throws ParseException {
        final List<Option> options = Collections.emptyList();

        final GroupImpl group = new GroupImpl(options, null, "My Group", 0, 1);

        commandLine.parse(group.parseArguments(Strings.toStringArray(new String[]{})));
        assertTrue(commandLine.isEmpty());

        group.printHelp("MyGroup", formatter, null, false, false);
        assertEquals("My Group:\n", formatter.printHelp(group));
    }

    @Test
    public void testGroupWithAnonymousArguments() throws ParseException {
        final List<Option> options = Arrays.asList(
                optionWithName("arg").args(1).type(Integer.TYPE),
                new Option("x", "x") {
                    @Override
                    public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
                        commandLine.addArgument(new Argument(this.getName(), Integer.parseInt(args.next().getValue())));
                    }
                }
        );

        final GroupImpl group = new GroupImpl(options, null, "My Group", 0, Integer.MAX_VALUE);

        commandLine.parse(group.parseArguments(Strings.toStringArray(new String[]{"--arg=1", "--x=2"})));
        assertEquals(1, commandLine.getArgValue("arg"));
        assertEquals(2, commandLine.getArgValue("x"));

        group.printHelp("MyGroup", formatter, null, false, false);
        assertEquals("My Group:\n" +
                "options:\n" +
                "  -arg ARG\t\trequired\n" +
                "  -x, --x X\t\toptional\n", formatter.printHelp(group));
    }

    @Test
    public void testGroupWithNestedGroup() throws ParseException {
        final Option groupOption = optionWithName("group")
                .args(1)
                .required(false)
                .group(new GroupImpl(Arrays.asList(
                        optionWithName("sub_group_arg").args(1).type(Integer.TYPE),
                        optionWithName("sub_group_x").required(true),
                        optionWithName("sub_group_y").required(false)
                ), null, "My SubGroup", 0, 1));

        final List<Option> options = Arrays.asList(groupOption);

        final GroupImpl group = new GroupImpl(options, null, "My Group", 0, 1);

        commandLine.parse(group.parseArguments(Strings.toStringArray(new String[]{"--group=1", "--sub_group_arg=2", "--sub_group_x", "--sub_group_y"})));
        assertEquals(1, commandLine.getArgValue("group"));
        assertTrue(commandLine.hasOption("sub_group_x"));
        assertFalse(commandLine.hasOption("sub_group_y"));

        commandLine.parse(group.parseArguments(Strings.toStringArray(new String[]{"--group=1", "--sub_group_arg=2"})));
        assertEquals(1, commandLine.getArgValue("group"));
        assertFalse(commandLine.hasOption("sub_group_x"));
        assertFalse(commandLine.hasOption("sub_group_y"));

        group.printHelp("MyGroup", formatter, null, false, false);
        assertEquals("My Group:\n" +
                "options:\n" +
                "  --group GROUP\toptional\n" +
                "\targuments:\n" +
                "    GROUP\n" +
                "  My SubGroup:\n" +
                "    options:\n" +
                "      -sub_group_arg ARG\trequired\n" +
                "      -sub_group_x\trequired\n" +
                "      -sub_group_y\toptional\n", formatter.printHelp(group));
    }

    private Option optionWithName(final String name) {
        return new Option(name, null) {
            @Override
            public String getName() {
                return name;
            }
        };
    }

    @powermock.MockInvoke
    private static HelpFormatter getHelpFormatter() {
        return new DefaultHelpFormatter();
    }
}