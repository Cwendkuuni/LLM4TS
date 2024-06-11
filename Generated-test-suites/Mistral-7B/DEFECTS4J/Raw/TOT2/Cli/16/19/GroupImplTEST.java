package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;

import static org.junit.Assert.*;

import java.util.List;

public class GroupImplTest {

    @org.junit.Test
    public void testGroupImplWithBasicOptions() {
        final Options options = new Options();
        final Group group = new GroupImpl(
                asList(
                        new OptionBuilder("g")
                                .withDescription("Group Option")
                                .withLongOpt("group")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("o")
                                .withDescription("Option Option")
                                .withLongOpt("option")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("a")
                                .withDescription("Argument Argument")
                                .createArgument()
                                .isRequired(false)
                                .create()
                ),
                "Group",
                "This is a group",
                0,
                1);

        final DefaultParser parser = new DefaultParser();

        final CommandLine parsedCommandLine = parser.parse(options, new String[]{"g"});
        assertEquals("Group Option", parsedCommandLine.getOptionValue("g"));
        assertNull(parsedCommandLine.getOptionValue("o"));
        assertNull(parsedCommandLine.getOptionValue("a"));

        parsedCommandLine.setOptionValue("a", "test");
        assertEquals("test", parsedCommandLine.getOptionValue("a"));
    }

    @org.junit.Test
    public void testGroupImplWithMinimumOptions() {
        final Options options = new Options();
        final Group group = new GroupImpl(
                asList(
                        new OptionBuilder("g")
                                .withDescription("Group Option")
                                .withLongOpt("group")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("o")
                                .withDescription("Option Option")
                                .withLongOpt("option")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("a")
                                .withDescription("Argument Argument")
                                .createArgument()
                                .isRequired(false)
                                .create()
                ),
                "Group",
                "This is a group",
                1,
                1);

        final DefaultParser parser = new DefaultParser();

        final CommandLine parsedCommandLine = parser.parse(options, new String[]{"g"});
        assertEquals("Group Option", parsedCommandLine.getOptionValue("g"));
        assertNull(parsedCommandLine.getOptionValue("o"));
        assertNull(parsedCommandLine.getOptionValue("a"));

        parsedCommandLine.setOptionValue("g", "test");
        parsedCommandLine.setOptionValue("o", "test");

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    @org.junit.Test(expected = OptionException.class)
    public void testGroupImplWithMaximumOptions() {
        final Options options = new Options();
        final Group group = new GroupImpl(
                asList(
                        new OptionBuilder("g")
                                .withDescription("Group Option")
                                .withLongOpt("group")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("o")
                                .withDescription("Option Option")
                                .withLongOpt("option")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("a")
                                .withDescription("Argument Argument")
                                .createArgument()
                                .isRequired(false)
                                .create()
                ),
                "Group",
                "This is a group",
                1,
                2);

        final DefaultParser parser = new DefaultParser();

        final CommandLine parsedCommandLine = parser.parse(options, new String[]{"g", "g"});
        assertEquals("Group Option", parsedCommandLine.getOptionValue("g"));
        assertEquals("Group Option", parsedCommandLine.getOptionValue("g"));

        parsedCommandLine.setOptionValue("g", "test");
        parsedCommandLine.setOptionValue("g", "test2");

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    @org.junit.Test(expected = OptionException.class)
    public void testGroupImplWithMissingOptions() {
        final Options options = new Options();
        final Group group = new GroupImpl(
                asList(
                        new OptionBuilder("g")
                                .withDescription("Group Option")
                                .withLongOpt("group")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("o")
                                .withDescription("Option Option")
                                .withLongOpt("option")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("a")
                                .withDescription("Argument Argument")
                                .createArgument()
                                .isRequired(false)
                                .create()
                ),
                "Group",
                "This is a group",
                1,
                1);

        final DefaultParser parser = new DefaultParser();

        final CommandLine parsedCommandLine = parser.parse(options, new String[]{"o"});
        assertNull(parsedCommandLine.getOptionValue("g"));
        assertNull(parsedCommandLine.getOptionValue("o"));
        assertNull(parsedCommandLine.getOptionValue("a"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    @org.junit.Test(expected = OptionException.class)
    public void testGroupImplWithInvalidOption() {
        final Options options = new Options();
        final Group group = new GroupImpl(
                asList(
                        new OptionBuilder("g")
                                .withDescription("Group Option")
                                .withLongOpt("group")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("o")
                                .withDescription("Option Option")
                                .withLongOpt("option")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("a")
                                .withDescription("Argument Argument")
                                .createArgument()
                                .isRequired(false)
                                .create()
                ),
                "Group",
                "This is a group",
                1,
                1);

        final DefaultParser parser = new DefaultParser();

        final CommandLine parsedCommandLine = parser.parse(options, new String[]{"x"});
        assertNull(parsedCommandLine.getOptionValue("g"));
        assertNull(parsedCommandLine.getOptionValue("o"));
        assertNull(parsedCommandLine.getOptionValue("a"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    @org.junit.Test
    public void testGroupImplWithMixedOptionsAndArguments() {
        final Options options = new Options();
        final Group group = new GroupImpl(
                asList(
                        new OptionBuilder("g")
                                .withDescription("Group Option")
                                .withLongOpt("group")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("o")
                                .withDescription("Option Option")
                                .withLongOpt("option")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("a")
                                .withDescription("Argument Argument")
                                .createArgument()
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("i")
                                .withDescription("Integer Option")
                                .withLongOpt("integer")
                                .hasArg()
                                .isRequired(false)
                                .create()
                ),
                "Group",
                "This is a group"
        );

        final DefaultParser parser = new DefaultParser();

        final CommandLine parsedCommandLine = parser.parse(options, new String[]{"g", "o", "1"});
        assertEquals("Group Option", parsedCommandLine.getOptionValue("g"));
        assertEquals("Option Option", parsedCommandLine.getOptionValue("o"));
        assertEquals("1", parsedCommandLine.getOptionValue("integer"));
        assertNull(parsedCommandLine.getOptionValue("a"));

        parsedCommandLine.setOptionValue("a", "test");
        assertEquals("test", parsedCommandLine.getOptionValue("a"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    @org.junit.Test
    public void testGroupImplWithAnonymousArgument() {
        final Options options = new Options();
        final Group group = new GroupImpl(
                asList(
                        new OptionBuilder("g")
                                .withDescription("Group Option")
                                .withLongOpt("group")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("o")
                                .withDescription("Option Option")
                                .withLongOpt("option")
                                .isRequired(false)
                                .create(),
                        new OptionBuilder("a")
                                .withDescription("Argument Argument")
                                .createArgument()
                                .isRequired(false)
                                .create()
                ),
                "Group",
                "This is a group"
        );

        final DefaultParser parser = new DefaultParser();

        final CommandLine parsedCommandLine = parser.parse(options, new String[]{"g", "a", "test"});
        assertEquals("Group Option", parsedCommandLine.getOptionValue("g"));
        assertEquals("test", parsedCommandLine.getOptionValue("a"));

        parsedCommandLine.setOptionValue("g", "test");
        parsedCommandLine.setOptionValue("o", "test2");

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    private List<Object> asList(final Object... objects) {
        return new ArrayList<Object>(List.of(objects));
    }
}