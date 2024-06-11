package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Test for the Apache Commons CLI Options class.
 */
public class OptionsTest
{
    /**
     * Test for the basic usage of Options.
     */
    @Test
    public void testBasicUsage()
    {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this help message.");
        options.addOption("v", "verbose", false, "Print verbose output.");
        options.addOption("a", "append", true, "Append text to FILE, instead of overwriting it.");

        CommandLine parsed = null;
        String[] args = new String[] { "TestApp", "--help", "--verbose", "-a", "test.txt", "arg1", "arg2" };
        try
        {
            parsed = new DefaultParser().parse(options, args);
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
            assertFalse(true);
            return;
        }

        Map<String, String> parsedArgs = parsed.getOptionMap();
        assertEquals(true, parsedArgs.getBoolean("help"));
        assertEquals(true, parsedArgs.getBoolean("verbose"));
        assertEquals("test.txt", parsedArgs.get("a"));
        assertNull(parsedArgs.get("i")); // no option "-i" present in the command line

        List<Option> optionsList = options.helpOptions();
        assertEquals(3, optionsList.size());

        Option helpOption = optionsList.get(0);
        assertEquals("h", helpOption.getOpt());
        assertEquals("help", helpOption.getLongOpt());
        assertFalse(helpOption.hasArg());
        assertEquals("Print this help message.", helpOption.getDescription());

        Option verboseOption = optionsList.get(1);
        assertEquals("v", verboseOption.getOpt());
        assertEquals("verbose", verboseOption.getLongOpt());
        assertFalse(verboseOption.hasArg());
        assertEquals("Print verbose output.", verboseOption.getDescription());

        Option appendOption = optionsList.get(2);
        assertEquals("a", appendOption.getOpt());
        assertEquals("append", appendOption.getLongOpt());
        assertTrue(appendOption.hasArg());
        assertEquals("Append text to FILE, instead of overwriting it.", appendOption.getDescription());
    }

    /**
     * Test for the usage of Options with OptionGroup.
     */
    @Test
    public void testOptionGroups()
    {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group1");
        group.setRequired(true);
        group.addOption(new Option("g1", "group1Option1", false, "Option 1 in group1."));
        group.addOption(new Option("g1", "group1Option2", false, "Option 2 in group1."));

        options.addOptionGroup(group);
        options.addOption("h", "help", false, "Print this help message.");

        CommandLine parsed = null;
        String[] args = new String[] { "TestApp", "--help", "--group1Option1", "--group1Option2" };
        try
        {
            parsed = new DefaultParser().parse(options, args);
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
            assertFalse(true);
            return;
        }

        Map<String, String> parsedArgs = parsed.getOptionMap();
        assertEquals(true, parsedArgs.getBoolean("help"));
        assertTrue(group.isRequired());
        assertTrue(group.isSelected(parsed));

        List<Option> optionsList = options.helpOptions();
        assertEquals(3, optionsList.size());

        Option helpOption = optionsList.get(0);
        assertEquals("h", helpOption.getOpt());
        assertEquals("help", helpOption.getLongOpt());
        assertFalse(helpOption.hasArg());
        assertEquals("Print this help message.", helpOption.getDescription());

        List<Option> groupOptionsList = group.getOptions();
        assertEquals(2, groupOptionsList.size());

        Option group1Option1 = groupOptionsList.get(0);
        assertEquals("g1", group1Option1.getOpt());
        assertEquals("group1Option1", group1Option1.getLongOpt());
        assertFalse(group1Option1.hasArg());
        assertEquals("Option 1 in group1.", group1Option1.getDescription());

        Option group1Option2 = groupOptionsList.get(1);
        assertEquals("g1", group1Option2.getOpt());
        assertEquals("group1Option2", group1Option2.getLongOpt());
        assertFalse(group1Option2.hasArg());
        assertEquals("Option 2 in group1.", group1Option2.getDescription());
    }
}