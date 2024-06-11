package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link Parser} class.
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[])} method.
     */
    @org.junit.Test
    public void testParse() throws Exception {
        String[] args = {"-a", "1", "--b", "2", "-c", "3", "--d"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertFalse(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a"));
        assertEquals(1, cmd.getOptionValue("a").length());
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getOptionValue("b"));
        assertEquals(2, cmd.getOptionValue("b").length());
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.getOptionValue("c"));
        assertEquals(3, cmd.getOptionValue("c").length());
        assertFalse(cmd.hasOption("d"));

        // test with properties
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "test");

        cmd = parser.parse(options, args, properties, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a"));
        assertFalse(cmd.getOptionValue("a").length() > 1);
        assertEquals("test", cmd.getOptionValue("b"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} method.
     */
    @org.junit.Test
    public void testParseStopAtNonOption() throws Exception {
        String[] args = {"-a", "1", "--b", "2", "-c", "3", "--d", "testfile.txt"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals(Arrays.asList("testfile.txt"), cmd.getArgList());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)} method.
     */
    @org.junit.Test
    public void testParseWithProperties() throws Exception {
        String[] args = {"--help"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("h", false, "Help option");

        Properties properties = new Properties();
        properties.setProperty("help", "true");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.getOptionValue("h"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, boolean)} method.
     */
    @org.junit.Test
    public void testParseWithStopAtNonOptionAndRequiredOptions() throws Exception {
        String[] args = {"--help"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("c", false, "Required option C");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args, true, false);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            List requiredOptions = options.getRequiredOptions();
            assertEquals(Arrays.asList("c"), requiredOptions);
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, boolean)} method.
     */
    @org.junit.Test
    public void testParseWithOptionalArgsAndStopAtNonOption() throws Exception {
        String[] args = {"--help", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("h", false, "Help option");
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B")
                .setArgName("ARG")
                .setRequired(false);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, false);

        assertFalse(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals(Arrays.asList("arg1", "arg2"), cmd.getArgList());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, boolean)} method.
     */
    @org.junit.Test
    public void testParseWithMissingOption() throws Exception {
        String[] args = {"--help"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args, true, false);
            fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            assertEquals("Missing option: a", e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, boolean)} method.
     */
    @org.junit.Test
    public void testParseWithUnrecognizedOption() throws Exception {
        String[] args = {"--test"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args, true, false);
            fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: test", e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, boolean)} method.
     */
    @org.junit.Test
    public void testParseWithMissingArgument() throws Exception {
        String[] args = {"-a"};

        Options options = new Options();
        options.addOption("a", true, "Option A")
                .setArgName("ARG")
                .setRequired(true);

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args, true, false);
            fail("Expected MissingArgumentException");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: a", e.getMessage());
        }
    }
}