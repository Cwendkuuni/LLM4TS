package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionGroup;

public class OptionsTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        Option option1 = new Option("f", "foo", false, "enable foo");
        options.addOption(option1);
        Option option2 = new Option("b", "bar", true, "set bar value");
        options.addOption(option2);
        OptionGroup optionGroup = new OptionGroup();
        Option option3 = new Option("o", "option1", false, "option 1");
        optionGroup.addOption(option3);
        Option option4 = new Option("p", "option2", true, "option 2");
        optionGroup.addOption(option4);
        options.addOptionGroup(optionGroup);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "enable test");
        options.addOption(option);
        assertTrue(options.hasOption("t"));
    }

    @Test
    public void testAddOptionNoArg() {
        Option option = new Option("t", "test", false, "enable test");
        options.addOption(option);
        assertTrue(options.hasOption("t"));
        assertFalse(options.hasOption("test"));
    }

    @Test
    public void testAddOptionWithArg() {
        Option option = new Option("t", "test", true, "set test value");
        options.addOption(option);
        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "option 1");
        optionGroup.addOption(option1);
        Option option2 = new Option("p", "option2", true, "option 2");
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        OptionGroup retrievedOptionGroup = options.getOptionGroup(option2);
        assertNotNull(retrievedOptionGroup);
        assertTrue(retrievedOptionGroup.getOptions().contains(option1));
        assertTrue(retrievedOptionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option retrievedOption = options.getOption("f");
        assertNotNull(retrievedOption);
        assertEquals("foo", retrievedOption.getLongOpt());
    }

    @Test
    public void testHasOption() {
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("t"));
    }

    @Test
    public void testHasLongOption() {
        assertTrue(options.hasLongOption("foo"));
        assertFalse(options.hasLongOption("test"));
    }

    @Test
    public void testHasShortOption() {
        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("t"));
    }

    @Test
    public void testToString() {
        String expectedOutput = "[ Options: [ short {f=foo, b=bar} ] [ long {foo=foo, bar=bar} ]";
        assertEquals(expectedOutput, options.toString());
    }
}