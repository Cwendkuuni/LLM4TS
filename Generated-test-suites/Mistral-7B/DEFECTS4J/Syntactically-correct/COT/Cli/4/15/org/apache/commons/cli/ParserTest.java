package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TestUtilities;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test the {@code Parser} class.
 */
public class ParserTest {

    private static final String[] ARGS = {"--option1", "-o1", "-a", "arg1",
            "--option2=value2", "--option3", "--noOption"};
    private CommandLineParser parser;
    private Options options;
    private Properties properties;

    /**
     * Sets up the {@code Parser}, {@code Options}, and {@code Properties}.
     */
    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
        properties = new Properties();
        options.addRequiredOption("o1", true, "o1help");
        options.addOption("option1", true, "option1help");
        options.addOption("option2", true, "option2help", true, "defaultValue2");
        options.addOptionGroup(new OptionGroup("group", "Group options"));
        options.addOption("groupOption1", true, "groupOption1help");
        options.addOption("groupOption2", true, "groupOption2help");
        properties.setProperty("option3", "value3");
    }

    /**
     * Tests the {@code Parser#parse(Options, String[])} method.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseWithOptions() throws ParseException {
        CommandLine cmd = parser.parse(options, ARGS);
        assertEquals(3, cmd.getArgs().length);
        assertEquals("arg1", cmd.getOptionValue("o1"));
        assertFalse(cmd.hasOption("noOption"));
        assertSame(options.getOption("option1"), cmd.getOption("option1"));
        assertSame(options.getOption("option2"), cmd.getOption("option2"));
        assertEquals("value2", cmd.getOptionValue("option2"));
        assertSame(options.getOptionGroup("group"), cmd.getOptionGroup("group"));
        assertSame(options.getOption("groupOption1"), cmd.getOption("groupOption1"));
        assertSame(options.getOption("groupOption2"), cmd.getOption("groupOption2"));
    }

    /**
     * Tests the {@code Parser#parse(Options, String[], Properties)} method.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseWithOptionsAndProperties() throws ParseException {
        CommandLine cmd = parser.parse(options, ARGS, properties);
        assertEquals(3, cmd.getArgs().length);
        assertEquals("arg1", cmd.getOptionValue("o1"));
        assertFalse(cmd.hasOption("noOption"));
        assertEquals("defaultValue2", cmd.getOptionValue("option2"));
        assertSame(properties.getProperty("option3"), cmd.getOptionValue("option3"));
    }

    /**
     * Tests the {@code Parser#parse(Options, String[], boolean)} method.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        CommandLine cmd = parser.parse(options, ARGS, true);
        assertEquals(4, cmd.getArgs().length);
        assertEquals("arg1", cmd.getOptionValue("o1"));
        assertFalse(cmd.hasOption("noOption"));
        assertNull(cmd.getOptionValue("option2"));
    }

    /**
     * Tests the edge cases for the {@code Parser#parse(Options, String[], boolean)} method.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseWithEdgeCases() throws ParseException {
        options.clearOptions();
        options.addRequiredOption("o1", true, "o1help");

        CommandLine cmd1 = parser.parse(options, new String[0], false);
        assertEquals(0, cmd1.getArgs().length);
        assertNull(cmd1.getOptionValue("o1"));

        CommandLine cmd2 = parser.parse(options, new String[]{"--"}, false);
        assertTrue(Arrays.equals(new String[0], cmd2.getArgs()));
        assertNull(cmd2.getOptionValue("o1"));

        CommandLine cmd3 = parser.parse(options, new String[]{"--o1"}, false);
        assertEquals(0, cmd3.getArgs().length);
        assertEquals("", cmd3.getOptionValue("o1"));

        CommandLine cmd4 = parser.parse(options, new String[]{"--o1=value"}, false);
        assertEquals(0, cmd4.getArgs().length);
        assertEquals("value", cmd4.getOptionValue("o1"));

        CommandLine cmd5 = parser.parse(options, new String[]{"--o1=value1", "--o1=value2"}, false);
        assertEquals(0, cmd5.getArgs().length);
        assertEquals("value2", cmd5.getOptionValue("o1"));
    }

    /**
     * Tests the edge cases for the {@code Parser#parse(Options, String[], Properties)} method.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseWithPropertiesEdgeCases() throws ParseException {
        options.clearOptions();
        options.addRequiredOption("o1", true, "o1help");

        Properties properties = new Properties();
        properties.setProperty("o1", "value1");

        CommandLine cmd1 = parser.parse(options, new String[0], properties);
        assertEquals(0, cmd1.getArgs().length);
        assertEquals("value1", cmd1.getOptionValue("o1"));

        CommandLine cmd2 = parser.parse(options, new String[]{"--"}, properties);
        assertTrue(Arrays.equals(new String[0], cmd2.getArgs()));
        assertNull(cmd2.getOptionValue("o1"));

        CommandLine cmd3 = parser.parse(options, new String[]{"--o1"}, properties);
        assertEquals(0, cmd3.getArgs().length);
        assertEquals("", cmd3.getOptionValue("o1"));

        CommandLine cmd4 = parser.parse(options, new String[]{"--o1=value"}, properties);
        assertEquals(0, cmd4.getArgs().length);
        assertEquals("value", cmd4.getOptionValue("o1"));

        CommandLine cmd5 = parser.parse(options, new String[]{"--o1=value1", "--o1=value2"}, properties);
        assertEquals(0, cmd5.getArgs().length);
        assertEquals("value2", cmd5.getOptionValue("o1"));

        CommandLine cmd6 = parser.parse(options, new String[]{"--o1=value1", "--o1=value2"}, null);
        assertEquals(0, cmd6.getArgs().length);
        assertEquals("value2", cmd6.getOptionValue("o1"));
    }

    /**
     * Tests the exception handling scenarios.
     */
    @Test(expected = ParseException.class)
    public void testParseWithExceptions() throws ParseException {
        options.clearOptions();
        options.addRequiredOption("o1", true, "o1help");
        options.addOption("noOption", false, "noOptionhelp");

        parser.parse(options, ARGS, false); // Missing 'o1' should throw an exception

        // options with an unrecognized option
        options.clearOptions();
        options.addRequiredOption("o1", true, "o1help");
        parser.parse(options, new String[]{"--unrecognizedOption"}, false);

        // options with mismatched number of values
        options.clearOptions();
        options.addRequiredOption("o1", true, "o1help");
        options.addOption("o2", true, "o2help", true);
        parser.parse(options, new String[]{"--o1", "--o1=value1", "--o2=value1"}, false);

        // options with a missing value
        options.clearOptions();
        options.addRequiredOption("o1", true, "o1help");
        options.addOption("o2", true, "o2help", true);
        parser.parse(options, new String[]{"--o1", "--o2"}, false);

        // options with mismatched values types
        options.clearOptions();
        options.addRequiredOption("o1", true, "o1help");
        options.addOption("o2", true, "o2help", true, "value");
        parser.parse(options, new String[]{"--o1", "--o2=1"}, false);

        // options with incorrect boolean values
        options.clearOptions();
        options.addRequiredOption("o1", true, "o1help");
        options.addOption("o2", true, "o2help", false, "false");
        parser.parse(options, new String[]{"--o1=value1", "--o2=true"}, false);
    }
}