package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Options.
 */
public class OptionsTest
{
    /**
     * Test for addOption method with single character short option and no argument.
     */
    @Test
    public void testAddOptionSingleCharShortNoArg()
    {
        Options options = new Options();
        options.addOption("a");

        assertEquals(1, options.size());
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    /**
     * Test for addOption method with single character short option and required argument.
     */
    @Test
    public void testAddOptionSingleCharShortArgRequired()
    {
        Options options = new Options();
        options.addOption("a", true);

        assertEquals(1, options.size());
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    /**
     * Test for addOption method with single character short option and optional argument.
     */
    @Test
    public void testAddOptionSingleCharShortArgOptional()
    {
        Options options = new Options();
        options.addOption("a", true, true);

        assertEquals(1, options.size());
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    /**
     * Test for addOption method with multiple character long option and no argument.
     */
    @Test
    public void testAddOptionLongNoArg()
    {
        Options options = new Options();
        options.addOption("a", "longOptionA");

        assertEquals(1, options.size());
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    /**
     * Test for addOption method with multiple character long option and required argument.
     */
    @Test
    public void testAddOptionLongArgRequired()
    {
        Options options = new Options();
        options.addOption("a", "longOptionA", true);

        assertEquals(1, options.size());
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    /**
     * Test for addOption method with multiple character long option and optional argument.
     */
    @Test
    public void testAddOptionLongArgOptional()
    {
        Options options = new Options();
        options.addOption("a", "longOptionA", true, "value");

        assertEquals(1, options.size());
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    /**
     * Test for addOptionGroup method.
     */
    @Test
    public void testAddOptionGroup()
    {
        OptionGroup group = new OptionGroup("MyGroup");
        group.setRequired(true);
        group.addOption(new Option("g", "groupOption", true, "Group option"));

        Options options = new Options();
        options.addOptionGroup(group);

        assertEquals(1, options.size());
        assertEquals(1, options.getOptions().size());
        assertTrue(options.hasOption("g"));
        assertTrue(options.getOptions().get(0).isRequired());
    }

    /**
     * Test for getOptions method.
     */
    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        options.addOption("a");
        options.addOption("b");

        List<Option> optionsList = options.getOptions();

        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0).hasArg());
        assertTrue(optionsList.get(1).hasArg());
    }

    /**
     * Test for getRequiredOptions method.
     */
    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Option optionA = new Option("a", true, true, "Required option A");
        options.addOption(optionA);

        List<Option> requiredOptionsList = options.getRequiredOptions();

        assertEquals(1, requiredOptionsList.size());
        assertEquals(optionA, requiredOptionsList.get(0));
    }

    /**
     * Test for getOption method.
     */
    @Test
    public void testGetOption()
    {
        Options options = new Options();
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        Option optionRetrieved = options.getOption("a");

        assertEquals(optionA, optionRetrieved);
    }

    /**
     * Test for getMatchingOptions method.
     */
    @Test
    public void testGetMatchingOptions()
    {
        Options options = new Options();
        Option optionA = new Option("a", "optionA", false, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        List<String> matchingOptions = options.getMatchingOptions("optio");

        assertEquals(1, matchingOptions.size());
        assertEquals("optionA", matchingOptions.get(0));
    }

    /**
     * Test for hasOption method.
     */
    @Test
    public void testHasOption()
    {
        Options options = new Options();
        Option optionA = new Option("a", "optionA", false, "Option A");
        options.addOption(optionA);

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    /**
     * Test for hasLongOption method.
     */
    @Test
    public void testHasLongOption()
    {
        Options options = new Options();
        Option optionA = new Option("a", "longOptionA", false, "Option A");
        options.addOption(optionA);

        assertTrue(options.hasLongOption("longOptionA"));
        assertFalse(options.hasLongOption("longOptionB"));
    }

    /**
     * Test for hasShortOption method.
     */
    @Test
    public void testHasShortOption()
    {
        Options options = new Options();
        Option optionA = new Option("a", "optionA", false, "Option A");
        options.addOption(optionA);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    /**
     * Test for getOptionGroup method.
     */
    @Test
    public void testGetOptionGroup()
    {
        OptionGroup group = new OptionGroup("MyGroup");
        group.setRequired(true);
        group.addOption(new Option("g", "groupOption", true, "Group option"));

        Options options = new Options();
        options.addOptionGroup(group);

        OptionGroup optionGroupRetrieved = options.getOptionGroup("MyGroup");

        assertEquals(group, optionGroupRetrieved);
    }

    /**
     * Test parse method with valid options.
     */
    @Test
    public void testParseValidOptions()
    {
        Options options = new Options();
        options.addOption("a");

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-a"};
        try
        {
            CommandLine cmd = parser.parse(options, args);
            assertEquals(args[0], cmd.getOptionValue("a"));
        }
        catch (ParseException e)
        {
            // should not throw an exception
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test parse method with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testParseInvalidOptions()
    {
        Options options = new Options();
        options.addOption("a");

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-x"};
        parser.parse(options, args);
    }
}