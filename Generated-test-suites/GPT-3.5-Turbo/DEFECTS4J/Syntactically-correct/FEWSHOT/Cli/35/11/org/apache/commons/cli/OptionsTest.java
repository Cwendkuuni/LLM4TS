package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option");

        group.addOption(option);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testAddOptionWithDescription() {
        Option option = new Option("o", "option", false, "option description");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("o"));
        assertEquals("option description", option.getDescription());
    }

    @Test
    public void testAddOptionWithHasArg() {
        Option option = new Option("o", "option", true, "option description");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertEquals(true, option.hasArg());
        assertEquals("option description", option.getDescription());
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("o", "option1");
        Option option2 = new Option("a", "option2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("o"));
        assertEquals(option2, options.getOption("a"));
    }

    @Test
    public void testGetOptionWithLeadingHyphens() {
        Option option = new Option("o", "option");
        options.addOption(option);

        assertEquals(option, options.getOption("--o"));
        assertEquals(option, options.getOption("-o"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("f", "file");
        Option option2 = new Option("o", "output");
        options.addOption(option1);
        options.addOption(option2);

        List<String> matchingOptions = options.getMatchingOptions("f");
        assertTrue(matchingOptions.contains("file"));
        assertEquals(1, matchingOptions.size());

        matchingOptions = options.getMatchingOptions("o");
        assertTrue(matchingOptions.contains("output"));
        assertEquals(1, matchingOptions.size());

        matchingOptions = options.getMatchingOptions("n");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("a"));
    }

    @Test
    public void testHasOptionWithLeadingHyphens() {
        Option option = new Option("o", "option");
        options.addOption(option);

        assertTrue(options.hasOption("--o"));
        assertTrue(options.hasOption("-o"));
        assertFalse(options.hasOption("--a"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("output"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option");
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("f"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option");

        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("o", "output", true, "output option");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {f=Option{ opt='-f', longOpt='file', hasArg=true, description='file option'}}, {o=Option{ opt='-o', longOpt='output', hasArg=true, description='output option'}} ] [ long {file=Option{ opt='-f', longOpt='file', hasArg=true, description='file option'}, output=Option{ opt='-o', longOpt='output', hasArg=true, description='output option'}} ]";
        assertEquals(expected, options.toString());
    }
}