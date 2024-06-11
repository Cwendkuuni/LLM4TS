package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test
    public void testParse() throws Exception
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "arg1,arg2", "Option D");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")).addOption("e", false, "Option E");
        options.addOptionGroup(new OptionGroup("group2", "Group 2")).addOption("f", false, "Option F");
        options.addRequiredOption("req1", true, "Required Option 1");
        options.addRequiredOption("req2", false, "Required Option 2");

        // set up parser
        Parser parser = new DefaultParser();

        // test parse with valid arguments
        String[] args = {"--a", "--b", "-c", "--d,arg1", "arg3", "--group1", "--e", "--group2", "--f"};
        CommandLine cmd = parser.parse(options, args, false);

        // check options
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals(Arrays.asList("arg1", "arg3"), cmd.getOptionValues("d"));
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("f"));
        assertFalse(cmd.hasOption("g"));
        assertNull(cmd.getOptionValue("g"));

        // check required options
        assertTrue(cmd.hasOption("req1"));
        assertNotNull(cmd.getOptionValues("req1"));
        assertTrue(cmd.hasOption("req2"));
        assertNull(cmd.getOptionValues("req2"));

        // test parse with missing required options
        args = new String[]{"--a", "--b"};
        try
        {
            cmd = parser.parse(options, args, false);
            fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertTrue(missingOptions.contains("req1"));
            assertTrue(missingOptions.contains("req2"));
        }

        // test parse with unrecognized options
        args = new String[]{"--g", "arg"};
        try
        {
            cmd = parser.parse(options, args, false);
            fail();
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: g", e.getMessage());
        }

        // test parse with missing argument
        args = new String[]{"--d"};
        try
        {
            cmd = parser.parse(options, args, false);
            fail();
        }
        catch (MissingArgumentException e)
        {
            assertEquals("Option D requires an argument.", e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @org.junit.Test
    public void testParseWithProperties() throws Exception
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "arg1,arg2", "Option D");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")).addOption("e", false, "Option E");
        options.addOptionGroup(new OptionGroup("group2", "Group 2")).addOption("f", false, "Option F");
        options.addRequiredOption("req1", true, "Required Option 1");
        options.addRequiredOption("req2", false, "Required Option 2");

        // set up parser
        Parser parser = new DefaultParser();

        // test parse with valid arguments and properties
        Properties properties = new Properties();
        properties.setProperty("d", "arg3");
        CommandLine cmd = parser.parse(options, new String[0], properties, false);

        // check options
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals(Arrays.asList("arg3"), cmd.getOptionValues("d"));
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("f"));
        assertFalse(cmd.hasOption("g"));
        assertNull(cmd.getOptionValue("g"));

        // check required options
        assertTrue(cmd.hasOption("req1"));
        assertNotNull(cmd.getOptionValues("req1"));
        assertTrue(cmd.hasOption("req2"));
        assertNull(cmd.getOptionValues("req2"));

        // test parse with missing required options and properties
        properties.clear();
        try
        {
            cmd = parser.parse(options, new String[0], properties, false);
            fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertTrue(missingOptions.contains("req1"));
            assertTrue(missingOptions.contains("req2"));
        }

        // test parse with unrecognized options and properties
        properties.setProperty("g", "value");
        try
        {
            cmd = parser.parse(options, new String[0], properties, false);
            fail();
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: g", e.getMessage());
        }

        // test parse with missing argument and properties
        properties.setProperty("d", "arg4");
        try
        {
            cmd = parser.parse(options, new String[0], properties, false);
            fail();
        }
        catch (MissingArgumentException e)
        {
            assertEquals("Option D requires an argument.", e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @org.junit.Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "arg1,arg2", "Option D");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")).addOption("e", false, "Option E");
        options.addOptionGroup(new OptionGroup("group2", "Group 2")).addOption("f", false, "Option F");
        options.addRequiredOption("req1", true, "Required Option 1");
        options.addRequiredOption("req2", false, "Required Option 2");

        // set up parser
        Parser parser = new DefaultParser();

        // test parse with valid arguments and stop at non-option
        String[] args = {"--a", "--b", "-c", "--d,arg1", "arg3", "--group1", "--e", "--group2", "--f", "nonoption"};
        CommandLine cmd = parser.parse(options, args, true);

        // check options
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals(Arrays.asList("arg1", "arg3"), cmd.getOptionValues("d"));
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("f"));
        assertFalse(cmd.hasOption("g"));
        assertNull(cmd.getOptionValue("g"));

        // check args
        List<String> argsList = Arrays.asList(cmd.getArgs());
        assertEquals(Arrays.asList("nonoption"), argsList);

        // test parse with missing required options and stop at non-option
        args = new String[]{"--a", "--b"};
        try
        {
            cmd = parser.parse(options, args, true);
            fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertTrue(missingOptions.contains("req1"));
            assertTrue(missingOptions.contains("req2"));
        }

        // test parse with unrecognized options and stop at non-option
        args = new String[]{"--g", "arg"};
        try
        {
            cmd = parser.parse(options, args, true);
            fail();
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: g", e.getMessage());
        }

        // test parse with missing argument and stop at non-option
        args = new String[]{"--d"};
        try
        {
            cmd = parser.parse(options, args, true);
            fail();
        }
        catch (MissingArgumentException e)
        {
            assertEquals("Option D requires an argument.", e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @org.junit.Test
    public void testParseWithPropertiesAndStopAtNonOption() throws Exception
    {
        // create options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", "arg1,arg2", "Option D");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")).addOption("e", false, "Option E");
        options.addOptionGroup(new OptionGroup("group2", "Group 2")).addOption("f", false, "Option F");
        options.addRequiredOption("req1", true, "Required Option 1");
        options.addRequiredOption("req2", false, "Required Option 2");

        // set up parser
        Parser parser = new DefaultParser();

        // test parse with valid arguments, properties and stop at non-option
        Properties properties = new Properties();
        properties.setProperty("d", "arg3");
        String[] args = {"--a", "--b", "-c", "--d,arg1", "arg3", "--group1", "--e", "--group2", "--f", "nonoption"};
        CommandLine cmd = parser.parse(options, args, properties, true);

        // check options
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals(Arrays.asList("arg1", "arg3"), cmd.getOptionValues("d"));
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("f"));
        assertFalse(cmd.hasOption("g"));
        assertNull(cmd.getOptionValue("g"));

        // check required options
        assertTrue(cmd.hasOption("req1"));
        assertNotNull(cmd.getOptionValues("req1"));
        assertTrue(cmd.hasOption("req2"));
        assertNull(cmd.getOptionValues("req2"));

        // check args
        List<String> argsList = Arrays.asList(cmd.getArgs());
        assertEquals(Arrays.asList("nonoption"), argsList);

        // check properties
        List<String> propertyNames = Arrays.asList(properties.stringPropertyNames());
        assertEquals(Arrays.asList("d"), propertyNames);
        assertEquals("arg3", properties.getProperty("d"));

        // test parse with missing required options and properties and stop at non-option
        properties.clear();
        try
        {
            cmd = parser.parse(options, new String[0], properties, true);
            fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertTrue(missingOptions.contains("req1"));
            assertTrue(missingOptions.contains("req2"));
        }

        // test parse with unrecognized options and properties and stop at non-option
        properties.setProperty("g", "value");
        try
        {
            cmd = parser.parse(options, new String[0], properties, true);
            fail();
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: g", e.getMessage());
        }

        // test parse with missing argument and properties and stop at non-option
        properties.setProperty("d", "arg4");
        try
        {
            cmd = parser.parse(options, new String[0], properties, true);
            fail();
        }
        catch (MissingArgumentException e)
        {
            assertEquals("Option D requires an argument.", e.getMessage());
        }
    }
}