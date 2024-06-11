package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        group1.addOption(option1);
        group1.addOption(option2);
        
        options.addOptionGroup(group1);
        
        assertTrue(options.getOptionGroups().contains(group1));
        assertTrue(options.getOptionGroup(option1).equals(group1));
        assertTrue(options.getOptionGroup(option2).equals(group1));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("foo"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("bar"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(options.getOptions(), options.helpOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        option1.setRequired(true);
        option2.setRequired(true);
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.getRequiredOptions().contains(option1));
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(options.getOption("f"), option1);
        assertEquals(options.getOption("foo"), option1);
        assertEquals(options.getOption("b"), option2);
        assertEquals(options.getOption("bar"), option2);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("f1", "foo1", false, "");
        Option option3 = new Option("b", "bar", false, "");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        assertTrue(options.getMatchingOptions("f").contains("foo"));
        assertTrue(options.getMatchingOptions("f").contains("foo1"));
        assertTrue(options.getMatchingOptions("foo").contains("foo"));
        assertTrue(options.getMatchingOptions("foo").contains("foo1"));
        assertFalse(options.getMatchingOptions("f").contains("bar"));
        assertFalse(options.getMatchingOptions("foo").contains("bar"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("foo"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("bar"));
        assertFalse(options.hasOption("test"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasLongOption("foo"));
        assertTrue(options.hasLongOption("bar"));
        assertFalse(options.hasLongOption("test"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertTrue(options.hasShortOption("f"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("test"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        group1.addOption(option1);
        group1.addOption(option2);
        
        options.addOptionGroup(group1);
        
        assertEquals(options.getOptionGroup(option1), group1);
        assertEquals(options.getOptionGroup(option2), group1);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("f", "foo", false, "");
        Option option2 = new Option("b", "bar", false, "");
        
        options.addOption(option1);
        options.addOption(option2);
        
        assertEquals(options.toString(), "[ Options: [ short {f=Option{ opt=f, longOpt=foo, arg=false, description= }}} ] [ long {foo=Option{ opt=f, longOpt=foo, arg=false, description= }, bar=Option{ opt=b, longOpt=bar, arg=false, description= }} ]");
    }

}