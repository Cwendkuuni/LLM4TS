package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli. Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseOptions;
import org.apache.commons.cli.TestUtilities;
import org.apache.commons.cli.Utilities;

/**
 * ParserTest class to test all methods in Parser.java
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test
    public void testParse() throws Exception {
        String[] args = new String[]{"-a", "--optB=valueB", "--optC=valueC", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optB", true, "Option B");
        options.addOption("optC", true, "Option C");
        parser.parse(options, args);

        CommandLine cmd = parser.getCommandLine();
        assertEquals(args.length, cmd.getArgs().size());
        assertEquals(3, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("optB"));
        assertTrue(cmd.hasOption("optC"));
        assertEquals("valueB", cmd.getOptionValue("optB"));
        assertEquals("valueC", cmd.getOptionValue("optC"));
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @org.junit.Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[]{"-a", "--optB=valueB", "--optC=valueC", "--prop1=valueProp1", "--prop2=valueProp2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optB", true, "Option B");
        options.addOption("optC", true, "Option C");
        options.addOptions(ParseOptions.storeTrue(options, "prop1"));
        options.addOptions(ParseOptions.storeTrue(options, "prop2"));
        parser.setHelpMarker("TEST");

        Properties properties = new Properties();
        properties.setProperty("TEST.prop1", "valueProp1");
        properties.setProperty("TEST.prop2", "valueProp2");

        CommandLine cmd = parser.parse(options, args, properties);

        assertEquals(args.length, cmd.getArgs().size());
        assertEquals(3, cmd.getOptions().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("optB"));
        assertTrue(cmd.hasOption("optC"));
        assertEquals("valueB", cmd.getOptionValue("optB"));
        assertEquals("valueC", cmd.getOptionValue("optC"));
        assertEquals("valueProp1", cmd.getOptionValue("prop1"));
        assertEquals("valueProp2", cmd.getOptionValue("prop2"));
        assertFalse(cmd.hasOption("prop1"));
        assertFalse(cmd.hasOption("prop2"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @org.junit.Test
    public void testParseWithStopAtNonOption() throws Exception {
        String[] args = new String[]{"--optB=valueB", "--optC=valueC", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optB", true, "Option B");
        options.addOption("optC", true, "Option C");

        List<String> parsedArgs = parser.parse(options, args, true).getArgs();

        assertEquals(2, parsedArgs.size());
        assertEquals("arg1", parsedArgs.get(0));
        assertEquals("arg2", parsedArgs.get(1));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test(expected = ParseException.class)
    public void testParseWithUnkownOption() throws Exception {
        String[] args = new String[]{"-z", "--optB=valueB", "--optC=valueC"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optB", true, "Option B");
        options.addOption("optC", true, "Option C");

        parser.parse(options, args);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @org.junit.Test
     public void testParseWithStopAtNonOptionAndProperties() throws Exception {
        String[] args = new String[]{"--optB=valueB", "--optC=valueC", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optB", true, "Option B");
        options.addOption("optC", true, "Option C");
        options.addOptions(ParseOptions.storeTrue(options, "prop1"));
        options.addOptions(ParseOptions.storeTrue(options, "prop2"));
        parser.setHelpMarker("TEST");

        Properties properties = new Properties();
        properties.setProperty("TEST.prop1", "valueProp1");
        properties.setProperty("TEST.prop2", "valueProp2");

        CommandLine cmd = parser.parse(options, args, properties, true);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @org.junit.Test(expected = ParseException.class)
    public void testParseWithUnkownOptionAndProperties() throws Exception {
        String[] args = new String[]{"--z", "--optB=valueB", "--optC=valueC"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optB", true, "Option B");
        options.addOption("optC", true, "Option C");
        options.addOptions(ParseOptions.storeTrue(options, "prop1"));
        options.addOptions(ParseOptions.storeTrue(options, "prop2"));
        parser.setHelpMarker("TEST");

        Properties properties = new Properties();
        properties.setProperty("TEST.prop1", "valueProp1");
        properties.setProperty("TEST.prop2", "valueProp2");

        parser.parse(options, args, properties);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @org.junit.Test(expected = MissingOptionException.class)
    public void testParseWithMissingOption() throws Exception {
        String[] args = new String[]{"--opt1", "--opt2=value2", "--opt3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("opt1", true, "Option 1");
        options.addOption("opt2", true, "Option 2");
        options.addOption("opt3", true, "Option 3");

        List<String> requiredOptions = parser.getRequiredOptions();

        parser.parse(options, args, false);

        assertEquals(3, requiredOptions.size());
        assertEquals("opt1", requiredOptions.get(0));
        assertEquals("opt3", requiredOptions.get(1));

        throw new MissingOptionException(requiredOptions);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @org.junit.Test(expected = MissingOptionException.class)
    public void testParseWithMissingOptionAndProperties() throws Exception {
        String[] args = new String[]{"--opt1", "--opt2=value2", "--opt3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("opt1", true, "Option 1");
        options.addOption("opt2", true, "Option 2");
        options.addOption("opt3", true, "Option 3");
        options.addOptions(ParseOptions.storeTrue(options, "prop1"));
        options.addOptions(ParseOptions.storeTrue(options, "prop2"));
        parser.setHelpMarker("TEST");

        Properties properties = new Properties();
        properties.setProperty("TEST.prop1", "valueProp1");
        properties.setProperty("TEST.prop2", "valueProp2");

        List<String> requiredOptions = parser.getRequiredOptions();

        parser.parse(options, args, properties, false);

        assertEquals(3, requiredOptions.size());
        assertEquals("opt1", requiredOptions.get(0));
        assertEquals("opt3", requiredOptions.get(1));

        throw new MissingOptionException(requiredOptions);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @org.junit.Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws Exception {
        String[] args = new String[]{"--opt1", "--opt2", "--opt3=value3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("opt1", true, "Option 1");
        options.addOption("opt2", true, "Option 2");
        options.addOption("opt3", true, "Option 3");
        options.addOption("opt3", true, "Option 3");

        CommandLine cmd = parser.parse(options, args, false);

        Option opt3 = cmd.getOption("opt3");
        assertEquals(2, opt3.getValues().length);

        throw new MissingArgumentException(opt3, 1);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @org.junit.Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgumentAndProperties() throws Exception {
        String[] args = new String[]{"--opt1", "--opt2", "--opt3=value3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("opt1", true, "Option 1");
        options.addOption("opt2", true, "Option 2");
        options.addOption("opt3", true, "Option 3");
        options.addOption("opt3", true, "Option 3");
        options.addOptions(ParseOptions.storeTrue(options, "prop1"));
        options.addOptions(ParseOptions.storeTrue(options, "prop2"));
        parser.setHelpMarker("TEST");

        Properties properties = new Properties();
        properties.setProperty("TEST.prop1", "valueProp1");
        properties.setProperty("TEST.prop2", "valueProp2");

        CommandLine cmd = parser.parse(options, args, properties, false);

        Option opt3 = cmd.getOption("opt3");
        assertEquals(2, opt3.getValues().length);

        throw new MissingArgumentException(opt3, 1);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseWithNullOptions() throws Exception {
        String[] args = new String[]{"-a", "--optB=valueB", "--optC=valueC"};
        Parser parser = new DefaultParser();

        parser.parse(null, args);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseWithNullOptionsAndProperties() throws Exception {
        String[] args = new String[]{"-a", "--optB=valueB", "--optC=valueC"};
        Parser parser = new DefaultParser();

        Properties properties = new Properties();

        parser.parse(null, args, properties, false);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseWithNullArgs() throws Exception {
        String[] args = null;
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.parse(options, args);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseWithNullArgsAndProperties() throws Exception {
        String[] args = null;
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();

        parser.parse(options, args, properties, false);
    }
}