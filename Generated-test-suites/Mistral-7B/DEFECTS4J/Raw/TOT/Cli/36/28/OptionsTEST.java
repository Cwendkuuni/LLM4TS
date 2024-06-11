package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Tests for {@link Options} class.
 *
 * @author <a href="mailto:gene@thesourceforge.net">Gene Wirchenko</a>
 */
public class OptionsTest
{
    /**
     * Test for {@link Options#addOptionGroup(OptionGroup)} method.
     */
    @Test
    public void testAddOptionGroup()
    {
        // create an OptionGroup
        OptionGroup group = new OptionGroup("");
        group.setName("test-group");
        group.setRequired(false);

        final String[] args = new String[]{"--test-group"};

        // create an Options instance
        Options options = new Options();

        // add the OptionGroup
        options.addOptionGroup(group);

        // parse the command line
        Options parsedOptions = null;
        try
        {
            parsedOptions = new DefaultParser().parse(options, args);
        }
        catch (ParseException e)
        {
            fail("Failed to parse options: " + e.getMessage());
        }

        // check that the OptionGroup was added correctly
        assertEquals(group, parsedOptions.getOptionGroup("test-group"));
        assertTrue(group.isParsed(parsedOptions));
    }

    /**
     * Test for {@link Options#addOptionGroup(OptionGroup)} method with required option group.
     */
    @Test
    public void testAddOptionGroupRequired()
    {
        // create an OptionGroup
        OptionGroup group = new OptionGroup("");
        group.setName("test-group");
        group.setRequired(true);

        final String[] args = new String[0];

        // create an Options instance
        Options options = new Options();

        // add the OptionGroup
        options.addOptionGroup(group);

        // parse the command line
        Options parsedOptions = null;
        try
        {
            parsedOptions = new DefaultParser().parse(options, args);
        }
        catch (ParseException e)
        {
            fail("Failed to parse options: " + e.getMessage());
        }

        // check that an error was thrown because the required OptionGroup was not provided
        assertTrue(parsedOptions.getParseErrors().size() > 0);
        assertSame(parsedOptions.getParseErrors().get(0).getOptionSpec(), group.getOptionSpec());
    }

    /**
     * Test for {@link Options#getOptionGroups()} method.
     */
    @Test
    public void testGetOptionGroups()
    {
        // create an OptionGroup
        OptionGroup group = new OptionGroup("");
        group.setName("test-group");

        // create an Options instance and add the OptionGroup
        Options options = new Options();
        options.addOptionGroup(group);

        // get the list of OptionGroups and check that the added OptionGroup is present
        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(group));
    }

    /**
     * Test for {@link Options#getOption(String)} method.
     */
    @Test
    public void testGetOption()
    {
        // create an Option
        Option option = new Option("t", "test", false, "Test option");

        // create an Options instance and add the Option
        Options options = new Options();
        options.addOption(option);

        // get the Option and check that it matches the one added
        Option parsedOption = options.getOption("t");
        assertSame(option, parsedOption);
    }

    /**
     * Test for {@link Options#getOption(String)} method with non-existent option.
     */
    @Test
    public void testGetOptionNonexistent()
    {
        // create an Options instance
        Options options = new Options();

        // try to get a non-existent Option and check that it returns null
        assertNull(options.getOption("nonexistent"));
    }

    /**
     * Test for {@link Options#getOptions()} method.
     */
    @Test
    public void testGetOptions()
    {
        // create an Option
        Option option = new Option("t", "test", false, "Test option");

        // create an Options instance and add the Option
        Options options = new Options();
        options.addOption(option);

        // parse the command line
        Options parsedOptions = null;
        try
        {
            parsedOptions = new DefaultParser().parse(options, new String[0]);
        }
        catch (ParseException e)
        {
            fail("Failed to parse options: " + e.getMessage());
        }

        // get the list of Options and check that the added Option is present
        List<Option> optionsList = options.getOptions();
        assertTrue(optionsList.contains(option));

        // check that the parsed Options also contain the added Option
        List<Option> parsedOptionsList = parsedOptions.getOptions();
        assertTrue(parsedOptionsList.contains(option));
    }

    /**
     * Test for {@link Options#helpOptions()} method.
     */
    @Test
    public void testHelpOptions()
    {
        // create an Option
        Option option = new Option("t", "test", false, "Test option");

        // create an Options instance and add the Option
        Options options = new Options();
        options.addOption(option);

        // parse the command line
        Options parsedOptions = null;
        try
        {
            parsedOptions = new DefaultParser().parse(options, new String[0]);
        }
        catch (ParseException e)
        {
            fail("Failed to parse options: " + e.getMessage());
        }

        // get the list of Options for help and check that it contains the added Option
        List<Option> helpOptions = options.helpOptions();
        assertTrue(helpOptions.contains(option));

        // check that the parsed Options also contain the added Option for help
        List<Option> parsedHelpOptions = parsedOptions.helpOptions();
        assertTrue(parsedHelpOptions.contains(option));
    }

    /**
     * Test for {@link Options#getRequiredOptions()} method.
     */
    @Test
    public void testGetRequiredOptions()
    {
        // create an Option
        Option option = new Option("t", "test", false, "Test option");

        // create an Options instance and add the Option
        Options options = new Options();
        options.addOption(option);

        // create an OptionGroup with a required Option
        OptionGroup group = new OptionGroup("");
        Option requiredOption = new Option("r", "required", true, "Required option");
        group.addOption(requiredOption);
        options.addOptionGroup(group);

        // parse the command line
        Options parsedOptions = null;
        try
        {
            parsedOptions = new DefaultParser().parse(options, new String[0]);
        }
        catch (ParseException e)
        {
            fail("Failed to parse options: " + e.getMessage());
        }

        // get the list of required Options and check that it contains the required Option
        List<Option> requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.contains(requiredOption));

        // check that the parsed Options also contain the required Option
        List<Option> parsedRequiredOptions = parsedOptions.getRequiredOptions();
        assertTrue(parsedRequiredOptions.contains(requiredOption));
    }

    /**
     * Test for {@link Options#getMatchingOptions(String)} method.
     */
    @Test
    public void testGetMatchingOptions()
    {
        // create an Options instance with several Options
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");
        options.addOption("d", "optionD", false, "Option D");

        // test with a perfect match
        List<String> matchingOptions = options.getMatchingOptions("optionD");
        assertEquals(1, matchingOptions.size());
        assertEquals("optionD", matchingOptions.get(0));

        // test with a partial match
        matchingOptions = options.getMatchingOptions("optio");
        assertEquals(3, matchingOptions.size());
        assertTrue(matchingOptions.contains("optionA"));
        assertTrue(matchingOptions.contains("optionB"));
        assertTrue(matchingOptions.contains("optionC"));

        // test with an empty string
        matchingOptions = options.getMatchingOptions("");
        assertEquals(4, matchingOptions.size());
        assertTrue(matchingOptions.contains("optionA"));
        assertTrue(matchingOptions.contains("optionB"));
        assertTrue(matchingOptions.contains("optionC"));
        assertTrue(matchingOptions.contains("optionD"));
    }

    /**
     * Test for {@link Options#hasOption(String)} method.
     */
    @Test
    public void testHasOption()
    {
        // create an Options instance with several Options
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");
        options.addOption("d", "optionD", false, "Option D");

        // test with a perfect match
        assertTrue(options.hasOption("optionD"));
        assertTrue(options.hasOption("optionD"));

        // test with a partial match
        assertTrue(options.hasOption("optio"));
        assertTrue(options.hasOption("optionA"));
        assertTrue(options.hasOption("optionB"));
        assertTrue(options.hasOption("optionC"));

        // test with non-existent option
        assertFalse(options.hasOption("nonexistent"));
    }

    /**
     * Test for {@link Options#hasShortOption(String)} method.
     */
    @Test
    public void testHasShortOption()
    {
        // create an Options instance with several Options
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");
        options.addOption("d", "optionD", false, "Option D");

        // test with a short option match
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("a"));

        // test with a non-existent short option
        assertFalse(options.hasShortOption("nonexistent"));
    }

    /**
     * Test for {@link Options#hasLongOption(String)} method.
     */
    @Test
    public void testHasLongOption()
    {
        // create an Options instance with several Options
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");
        options.addOption("d", "optionD", false, "Option D");

        // test with a long option match
        assertTrue(options.hasLongOption("optionD"));
        assertTrue(options.hasLongOption("optionD"));

        // test with a non-existent long option
        assertFalse(options.hasLongOption("nonexistent"));
    }

    /**
     * Test for {@link Options#hasOptionGroup(String)} method.
     */
    @Test
    public void testHasOptionGroup()
    {
        // create an OptionGroup
        OptionGroup group = new OptionGroup("");
        group.setName("test-group");

        // create an Options instance and add the OptionGroup
        Options options = new Options();
        options.addOptionGroup(group);

        // test with the exact OptionGroup name
        assertTrue(options.hasOptionGroup("test-group"));
        assertTrue(options.hasOptionGroup("test-group"));

        // test with a non-existent OptionGroup name
        assertFalse(options.hasOptionGroup("nonexistent"));
    }

    /**
     * Test for {@link Options#toString()} method.
     */
    @Test
    public void testToString()
    {
        // create an Option
        Option option = new Option("t", "test", false, "Test option");

        // create an Options instance and add the Option
        Options options = new Options();
        options.addOption(option);

        // check that the toString() method returns a valid representation of the Options instance
        String optionsString = options.toString();
        assertTrue(optionsString.contains("Options:"));
        assertTrue(optionsString.contains("short"));
        assertTrue(optionsString.contains("test"));
        assertTrue(optionsString.contains("long"));
        assertTrue(optionsString.contains("false"));
        assertTrue(optionsString.contains("Test option"));
    }
}