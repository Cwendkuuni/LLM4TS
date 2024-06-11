package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

/**
 * Test class for Options.
 */
public class OptionsTest
{
    /**
     * Test method for {@link Options#Options()}.
     */
    @org.junit.Test
    public void testOptions()
    {
        Options options = new Options();
        assertEquals(new LinkedHashMap<String, Option>(), options.shortOpts);
        assertEquals(new LinkedHashMap<String, Option>(), options.longOpts);
        assertEquals(new ArrayList<Object>(), options.requiredOpts);
        assertEquals(new HashMap<String, OptionGroup>(), options.optionGroups);
    }

    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)}.
     */
    @org.junit.Test
    public void testAddOptionGroup()
    {
        OptionGroup group = new OptionGroup();
        group.setName("group");
        group.setRequired(false);
        Options options = new Options();

        options.addOptionGroup(group);

        assertEquals(1, options.optionGroups.size());
        assertSame(group, options.optionGroups.get("group"));
        assertFalse(options.requiredOpts.contains(group));

        // Test adding a required group
        group.setRequired(true);
        options.addOptionGroup(group);
        assertTrue(options.requiredOpts.contains(group));
    }

    /**
     * Test method for {@link Options#getOptionGroups()}.
     */
    @org.junit.Test
    public void testGetOptionGroups()
    {
        OptionGroup group = new OptionGroup();
        group.setName("group");
        group.setRequired(false);
        Options options = new Options();
        options.addOptionGroup(group);

        List<OptionGroup> result = options.getOptionGroups();
        assertEquals(1, result.size());
        assertSame(group, result.get(0));
    }

    /**
     * Test method for {@link Options#addOption(String, String)}.
     */
    @org.junit.Test
    public void testAddOptionStringString()
    {
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Option description");

        options.addOption("o");

        assertEquals(1, options.shortOpts.size());
        assertSame(option, options.shortOpts.get("o"));

        options.addOption("o", "longOpt");

        assertEquals(2, options.shortOpts.size());
        assertSame(option, options.shortOpts.get("o"));
        assertEquals("longOpt", options.longOpts.get("longOpt").getLongOpt());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @org.junit.Test
    public void testAddOptionStringStringBooleanString()
    {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");

        options.addOption("o", null, true, "Option description");

        assertEquals(1, options.shortOpts.size());
        assertSame(option, options.shortOpts.get("o"));
        assertTrue(option.isRequired());

        options.addOption("o", "longOpt", true, "Option description");

        assertEquals(2, options.shortOpts.size());
        assertSame(option, options.shortOpts.get("o"));
        assertEquals("longOpt", options.longOpts.get("longOpt").getLongOpt());
        assertFalse(option.isRequired());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} with null arguments.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testAddOptionStringStringBooleanStringNull()
    {
        Options options = new Options();
        options.addOption(null, null, false, null);
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} with null long opt.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testAddOptionStringStringBooleanStringNullLongOpt()
    {
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Option description");
        options.addOption("o", null, true, "Option description");
        options.addOption("o", "longOpt", true, "Option description");
        options.shortOpts.remove("o");

        options.addOption("o", null, true, "Option description");
    }

    /**
     * Test method for {@link Options#addOption(Option)}.
     */
    @org.junit.Test
    public void testAddOptionOption()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();

        options.addOption(option);

        assertEquals(1, options.shortOpts.size());
        assertSame(option, options.shortOpts.get("o"));
        assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link Options#getOptions()}.
     */
    @org.junit.Test
    public void testGetOptions()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        List<Option> result = options.getOptions();
        assertEquals(1, result.size());
        assertSame(option, result.get(0));
    }

    /**
     * Test method for {@link Options#getRequiredOptions()}.
     */
    @org.junit.Test
    public void testGetRequiredOptions()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        List<Option> result = options.getRequiredOptions();
        assertEquals(1, result.size());
        assertSame(option, result.get(0));
    }

    /**
     * Test method for {@link Options#getOption(String)}.
     */
    @org.junit.Test
    public void testGetOptionString()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        Option result = options.getOption("o");
        assertSame(option, result);
    }

    /**
     * Test method for {@link Options#getOption(String)} with null argument.
     */
    @org.junit.Test
    public void testGetOptionStringNull()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        Option result = options.getOption(null);
        assertNull(result);
    }

    /**
     * Test method for {@link Options#getOption(String)} with non-existent option.
     */
    @org.junit.Test
    public void testGetOptionStringNonExistent()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        Option result = options.getOption("p");
        assertNull(result);
    }

    /**
     * Test method for {@link Options#getMatchingOptions(String)}.
     */
    @org.junit.Test
    public void testGetMatchingOptionsString()
    {
        Option option1 = new Option("o", "opt1", true, "Option 1 description");
        Option option2 = new Option("o", "opt2", true, "Option 2 description");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        List<String> result = options.getMatchingOptions("o");
        assertEquals(2, result.size());
        assertTrue(result.contains("opt1"));
        assertTrue(result.contains("opt2"));
    }

    /**
     * Test method for {@link Options#hasOption(String)}.
     */
    @org.junit.Test
    public void testHasOptionString()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("opt"));
        assertFalse(options.hasOption("p"));
        assertFalse(options.hasOption(""));
    }

    /**
     * Test method for {@link Options#hasLongOption(String)}.
     */
    @org.junit.Test
    public void testHasLongOptionString()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        assertTrue(options.hasLongOption("--opt"));
        assertFalse(options.hasLongOption("--non-opt"));
    }

    /**
     * Test method for {@link Options#hasShortOption(String)}.
     */
    @org.junit.Test
    public void testHasShortOptionString()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("p"));
    }

    /**
     * Test method for {@link Options#getOptionGroup(Option)}.
     */
    @org.junit.Test
    public void testGetOptionGroupOption()
    {
        OptionGroup group = new OptionGroup();
        group.setName("group");
        group.setRequired(false);
        Option option = new Option("o", "opt", true, "Option description", group);
        Options options = new Options();
        options.addOptionGroup(group);
        options.addOption(option);

        OptionGroup result = options.getOptionGroup(option);
        assertSame(group, result);
    }

    /**
     * Test method for {@link Options#getOptionGroup(Option)} with null argument.
     */
    @org.junit.Test
    public void testGetOptionGroupOptionNull()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        OptionGroup result = options.getOptionGroup(null);
        assertNull(result);
    }

    /**
     * Test method for {@link Options#getOptionGroup(Option)} with non-existent option.
     */
    @org.junit.Test
    public void testGetOptionGroupOptionNonExistent()
    {
        Option option = new Option("o", "opt", true, "Option description");
        Options options = new Options();
        options.addOption(option);

        OptionGroup result = options.getOptionGroup(new Option("p", "p"));
        assertNull(result);
    }

    /**
     * Test method for {@link Options#toString()}.
     */
    @org.junit.Test
    public void testToString()
    {
        Option option1 = new Option("o", "opt1", true, "Option 1 description");
        Option option2 = new Option("o", "opt2", true, "Option 2 description");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        String result = options.toString();

        String expected = "Options:\n [" +
                " short {o=OPT1,opt1=} ]\n [" +
                " long {--opt1=,--opt2=} ]\n";

        assertEquals(expected, result);
    }
}