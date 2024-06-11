package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.apache.commons.cli.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link Parser} class.
 */
public class ParserTest
{

    /**
     * Test method for {@link Parser#Parser()}.
     */
    @Test
    public void testParser()
    {
        Parser parser = new DefaultParser();
        assertNotNull(parser);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParse() throws Exception
    {
        String[] args = {"-a", "1", "--b", "2", "-c", "3", "--no-c"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, false);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.getOptionValue("a"));
        assertEquals((String) commandLine.getOptionValue("a"), "1");

        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.getOptionValue("b"));
        assertEquals((String) commandLine.getOptionValue("b"), "2");

        assertFalse(commandLine.hasOption("d"));

        assertFalse(commandLine.hasOption("c"));
        assertEquals(null, commandLine.getOptionValue("c"));

        List<String> argsList = commandLine.getArgs();
        assertEquals(3, argsList.size());
        assertEquals("3", argsList.get(0));
        assertEquals("--no-c", argsList.get(1));
        assertEquals("2", argsList.get(2));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithProperties() throws Exception
    {
        String[] args = {"-a", "1", "--b=2", "--c=3"};

        Properties properties = new Properties();
        properties.setProperty("b", "4");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, properties);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.getOptionValue("a"));
        assertEquals((String) commandLine.getOptionValue("a"), "1");

        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.getOptionValue("b"));
        assertEquals((String) commandLine.getOptionValue("b"), "4");

        assertTrue(commandLine.hasOption("c"));
        assertTrue(commandLine.getOptionValue("c"));
        assertEquals((String) commandLine.getOptionValue("c"), "3");
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, String[])}.
     */
    @Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        String[] args = {"-a", "1", "--b", "2", "-c", "3", "--no-c", "4", "5"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, true);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.getOptionValue("a"));
        assertEquals((String) commandLine.getOptionValue("a"), "1");

        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.getOptionValue("b"));
        assertEquals((String) commandLine.getOptionValue("b"), "2");

        assertTrue(commandLine.hasOption("c"));
        assertEquals(null, commandLine.getOptionValue("c"));

        List<String> argsList = commandLine.getArgs();
        assertEquals(5, argsList.size());
        assertEquals("3", argsList.get(0));
        assertEquals("--no-c", argsList.get(1));
        assertEquals("4", argsList.get(2));
        assertEquals("5", argsList.get(3));
        assertEquals("5", argsList.get(4));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, String[], String[])}.
     */
    @Test
    public void testParseWithStopAtNonOptionAndUnknownArgs() throws Exception
    {
        String[] args = {"-a", "1", "--b", "2", "-c", "3", "--no-c", "4", "5", "x", "y"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, true, null, null);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.getOptionValue("a"));
        assertEquals((String) commandLine.getOptionValue("a"), "1");

        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.getOptionValue("b"));
        assertEquals((String) commandLine.getOptionValue("b"), "2");

        assertTrue(commandLine.hasOption("c"));
        assertEquals(null, commandLine.getOptionValue("c"));

        List<String> argsList = commandLine.getArgs();
        assertEquals(4, argsList.size());
        assertEquals("3", argsList.get(0));
        assertEquals("--no-c", argsList.get(1));
        assertEquals("4", argsList.get(2));
        assertEquals("5", argsList.get(3));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @Test
    public void testParseWithPropertiesAndStopAtNonOption() throws Exception
    {
        String[] args = {"-a", "1", "--b=2", "--c=3"};

        Properties properties = new Properties();
        properties.setProperty("b", "4");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, properties, true);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.getOptionValue("a"));
        assertEquals((String) commandLine.getOptionValue("a"), "1");

        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.getOptionValue("b"));
        assertEquals((String) commandLine.getOptionValue("b"), "2");

        assertTrue(commandLine.hasOption("c"));
        assertTrue(commandLine.getOptionValue("c"));
        assertEquals((String) commandLine.getOptionValue("c"), "3");

        List<String> argsList = commandLine.getArgs();
        assertEquals(0, argsList.size());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean, String[])}.
     */
    @Test
    public void testParseWithPropertiesAndStopAtNonOptionAndUnknownArgs() throws Exception
    {
        String[] args = {"-a", "1", "--b=2", "--c=3", "x", "y"};

        Properties properties = new Properties();
        properties.setProperty("b", "4");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, properties, true, null);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.getOptionValue("a"));
        assertEquals((String) commandLine.getOptionValue("a"), "1");

        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.getOptionValue("b"));
        assertEquals((String) commandLine.getOptionValue("b"), "2");

        assertTrue(commandLine.hasOption("c"));
        assertTrue(commandLine.getOptionValue("c"));
        assertEquals((String) commandLine.getOptionValue("c"), "3");

        List<String> argsList = commandLine.getArgs();
        assertEquals(0, argsList.size());
    }
}