package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
    
    private Options options;
    private OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        options = new Options();
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOptionGroup() {
        Option option = new Option("f", "foo", true, "foo option");
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);
        
        assertEquals(optionGroup, options.getOptionGroup(option));
        assertTrue(options.getOptionGroups().contains(optionGroup));
    }
    
    @Test
    public void testAddOptionWithShortName() {
        options.addOption("f", true, "foo option");
        
        assertTrue(options.hasShortOption("f"));
    }
    
    @Test
    public void testAddOptionWithShortAndLongName() {
        options.addOption("f", "foo", true, "foo option");
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("foo"));
    }
    
    @Test
    public void testAddOptionWithoutArgument() {
        options.addOption("f", "foo", false, "foo option without argument");
        
        Option option = options.getOption("f");
        assertFalse(option.hasArg());
        assertEquals("foo option without argument", option.getDescription());
    }
    
    @Test
    public void testGetOption() {
        options.addOption("f", "foo", true, "foo option");
        
        Option option = options.getOption("f");
        assertEquals("f", option.getOpt());
        assertEquals("foo", option.getLongOpt());
    }
    
    @Test
    public void testGetMatchingOptions() {
        options.addOption("foo", true, "foo option");
        options.addOption("foobar", true, "foobar option");
        options.addOption("bar", true, "bar option");
        
        assertEquals(2, options.getMatchingOptions("foo").size());
        assertTrue(options.getMatchingOptions("foo").contains("foo"));
        assertTrue(options.getMatchingOptions("foo").contains("foobar"));
    }
    
    @Test
    public void testToString() {
        options.addOption("f", true, "foo option");
        
        assertEquals("[ Options: [ short {f=Option: { f, null, true, foo option } } ] [ long {foo=Option: { f, foo, true, foo option } } ]", options.toString());
    }
}