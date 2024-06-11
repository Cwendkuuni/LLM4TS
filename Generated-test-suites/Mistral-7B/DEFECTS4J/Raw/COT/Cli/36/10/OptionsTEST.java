package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

/**
 * Test class for Options.
 */
public class OptionsTest
{
    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)}.
     */
    @org.junit.Test
    public void testAddOptionGroup()
    {
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test Group");
        Options options = new Options();

        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    /**
     * Test method for {@link Options#getOptionGroups()}.
     */
    @org.junit.Test
    public void testGetOptionGroups()
    {
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test Group");
        Options options = new Options();
        options.addOptionGroup(optionGroup);

        assertEquals(1, options.getOptionGroups().size());
        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    /**
     * Test method for {@link Options#addOption(String, String)}.
     */
    @org.junit.Test
    public void testAddOptionShort()
    {
        Options options = new Options();

        Option option = options.addOption("t", "test", false, "Test option");

        assertEquals("test", option.getKey());
        assertEquals("Test option", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("--test"));
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean)}.
     */
    @org.junit.Test
    public void testAddOptionShortWithArg()
    {
        Options options = new Options();

        Option option = options.addOption("t", "test", true, "Test option with argument");

        assertEquals("test", option.getKey());
        assertEquals("Test option with argument", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("--test"));
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @org.junit.Test
    public void testAddOptionLong()
    {
        Options options = new Options();

        Option option = options.addOption("--test", "test", false, "Test option");

        assertEquals("test", option.getLongOpt());
        assertEquals("Test option", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());

        assertTrue(options.hasOption("--test"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("t"));
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @org.junit.Test
    public void testAddOptionLongWithArg()
    {
        Options options = new Options();

        Option option = options.addOption("--test", "test", true, "Test option with argument");

        assertEquals("test", option.getLongOpt());
        assertEquals("Test option with argument", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());

        assertTrue(options.hasOption("--test"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("t"));
    }

    /**
     * Test method for {@link Options#addOption(Option)}.
     */
    @org.junit.Test
    public void testAddOption()
    {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");

        options.addOption(option);

        assertEquals("test", option.getKey());
        assertEquals("Test option", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("--test"));
    }

    /**
     * Test method for {@link Options#getOptions()}.
     */
    @org.junit.Test
    public void testGetOptions()
    {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        List<Option> optionsList = options.getOptions();

        assertEquals(1, optionsList.size());
        assertEquals(option, optionsList.get(0));
    }

    /**
     * Test method for {@link Options#getRequiredOptions()}.
     */
    @org.junit.Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Option option = new Option("t", "test", true, "Test option");
        options.addOption(option);

        List<Option> requiredOptionsList = options.getRequiredOptions();

        assertEquals(1, requiredOptionsList.size());
        assertEquals(option, requiredOptionsList.get(0));
    }

    /**
     * Test method for {@link Options#getOption(String)}.
     */
    @org.junit.Test
    public void testGetOption()
    {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        Option testOption = options.getOption("t");

        assertEquals(option, testOption);
        assertNull(options.getOption("--test"));
    }

    /**
     * Test method for {@link Options#getMatchingOptions(String)}.
     */
    @org.junit.Test
    public void testGetMatchingOptions()
    {
        Options options = new Options();
        Option option1 = new Option("t1", "test1", false, "Test option 1");
        Option option2 = new Option("t2", "test2", false, "Test option 2");
        Option option3 = new Option("t3", "test3", false, "Test option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("t");

        assertEquals(3, matchingOptions.size());
        assertTrue(matchingOptions.contains("t1"));
        assertTrue(matchingOptions.contains("t2"));
        assertTrue(matchingOptions.contains("t3"));

        List<String> matchingOptionsLong = options.getMatchingOptions("--test");

        assertEquals(3, matchingOptionsLong.size());
        assertTrue(matchingOptionsLong.contains("test1"));
        assertTrue(matchingOptionsLong.contains("test2"));
        assertTrue(matchingOptionsLong.contains("test3"));
    }

    /**
     * Test method for {@link Options#hasOption(String)}.
     */
    @org.junit.Test
    public void testHasOption()
    {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("--test"));
    }

    /**
     * Test method for {@link Options#hasLongOption(String)}.
     */
    @org.junit.Test
    public void testHasLongOption()
    {
        Options options = new Options();
        Option option = new Option("--test", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasLongOption("--test"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("t"));
    }

    /**
     * Test method for {@link Options#hasShortOption(String)}.
     */
    @org.junit.Test
    public void testHasShortOption()
    {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasShortOption("t"));
        assertTrue(options.hasOption("test"));
        assertFalse(options.hasOption("--test"));
    }

    /**
     * Test method for {@link Options#getOptionGroup(Option)}.
     */
    @org.junit.Test
    public void testGetOptionGroup()
    {
        OptionGroup optionGroup = new OptionGroup("testGroup", "Test Group");
        Option option = new Option("t", "test", false, "Test option");
        option.setOptionGroup(optionGroup);
        Options options = new Options();
        options.addOptionGroup(optionGroup);
        options.addOption(option);

        OptionGroup testOptionGroup = options.getOptionGroup(option);

        assertEquals(optionGroup, testOptionGroup);
    }
}