package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseOptions;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link Parser} class.
 */
public class ParserTest {

    @SuppressWarnings("unchecked")
    private static final Options createOptions() {
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "Option value");
        options.addOption("h", "help", false, "Print help message.");
        options.addOptionGroup(new OptionGroup("g", "group")
                .addOption("g1", "Group Option 1", false)
                .addOption("g2", "Group Option 2", false));
        return options;
    }

    /**
     * Test parse method with valid arguments.
     *
     * @throws ParseException if parse fails.
     */
    @org.junit.Test
    public void testParseWithValidArguments() throws ParseException {
        final Options options = createOptions();
        final String[] arguments = new String[]{"-o", "value", "-g1"};
        final CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments);

        assertNotNull(commandLine);
        assertEquals(2, commandLine.getArgs().size());
        assertEquals("value", commandLine.getOptionValue("option"));
        assertNotNull(commandLine.getOption("help"));
        assertNotNull(commandLine.getOption("g1"));
    }

    /**
     * Test parse method with missing required option.
     *
     * @throws ParseException if parse fails.
     */
    @org.junit.Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        final Options options = createOptions();
        final String[] arguments = new String[]{"-g2"};

        final CommandLineParser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Test parse method with unrecognized option.
     */
    @org.junit.Test
    public void testParseWithUnrecognizedOption() {
        final Options options = createOptions();
        final String[] arguments = new String[]{"-x"};

        final CommandLineParser parser = new DefaultParser();

        assertThrows(UnrecognizedOptionException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Test parse method with extra arguments.
     *
     * @throws ParseException if parse fails.
     */
    @org.junit.Test
    public void testParseWithExtraArguments() throws ParseException {
        final Options options = createOptions();
        final String[] arguments = new String[]{"-o", "value", "--"};

        CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments);

        assertNotNull(commandLine);
        assertEquals(1, commandLine.getArgs().size());
        assertEquals("value", commandLine.getOptionValue("option"));
        assertNotNull(commandLine.getOption("help"));
        assertNotNull(commandLine.getOption("g1"));
    }

    /**
     * Test parse method with missing argument for required option.
     *
     * @throws ParseException if parse fails.
     */
    @org.junit.Test
    public void testParseWithMissingArgumentForRequiredOption()
            throws ParseException {
        final Options options = createOptions();
        final String[] arguments = new String[]{"-o"};

        final CommandLineParser parser = new DefaultParser();

        assertThrows(MissingArgumentException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Test parse method with no arguments.
     *
     * @throws ParseException if parse fails.
     */
    @org.junit.Test
    public void testParseWithNoArguments() throws ParseException {
        final Options options = createOptions();
        final CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, new String[0]);

        assertNotNull(commandLine);
        assertEquals(0, commandLine.getArgs().size());
        assertNotNull(commandLine.getOption("help"));
        assertNull(commandLine.getOptionValue("option"));
        assertNull(commandLine.getOption("g1"));
        assertNull(commandLine.getOption("g2"));
    }

    /**
     * Test parse method with the help flag.
     *
     * @throws ParseException if parse fails.
     */
    @org.junit.Test
    public void testParseWithHelpFlag() throws ParseException {
        final Options options = createOptions();
        final String[] arguments = new String[]{"-h"};

        CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(options, arguments);

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("TestParser", options);
        String expectedHelp = "TestParser:\n"
                + "Options:\n"
                + "   -o,--option <VALUE>  Option value\n"
                + "   -g1,--groupOption1     Group Option 1\n"
                + "   -g2,--groupOption2     Group Option 2\n"
                + "   -h,--help             Print this message.\n";
        assertEquals(expectedHelp, commandLine.getHelpMessage());
    }

    /**
     * Test parse method with properties.
     *
     * @throws ParseException if parse fails.
     */
    @org.junit.Test
    public void testParseWithProperties() throws ParseException {
        final Options options = createOptions();
        final Properties properties = new Properties();
        properties.setProperty("option", "value");
        properties.setProperty("group", "groupValue");

        final CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, new String[0], properties);

        assertEquals("value", commandLine.getOptionValue("option"));
        assertEquals("groupValue", commandLine.getOptionValue("group"));
        assertNotNull(commandLine.getOption("help"));
        assertNull(commandLine.getOptionValue("g1"));
        assertNull(commandLine.getOptionValue("g2"));
    }
}