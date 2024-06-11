package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "Description 1");
        Option option2 = new Option("b", "bbb", false, "Description 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("aaa"));
        assertTrue(options.hasOption("bbb"));
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("aaa"));
        assertTrue(options.hasOption("bbb"));
        
        Option option1 = options.getOption("a");
        Option option2 = options.getOption("b");
        assertNotNull(option1);
        assertNotNull(option2);
        assertTrue(option1.hasArg());
        assertFalse(option2.hasArg());
        assertEquals("a", option1.getOpt());
        assertEquals("aaa", option1.getLongOpt());
        assertEquals("b", option2.getOpt());
        assertEquals("bbb", option2.getLongOpt());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        
        assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("c", "ccc", false, "Description 3"));
        options.addOptionGroup(group);
        
        assertEquals(3, options.getRequiredOptions().size());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        
        assertNotNull(options.getOption("a"));
        assertNotNull(options.getOption("b"));
        assertNotNull(options.getOption("aaa"));
        assertNotNull(options.getOption("bbb"));
        
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        options.addOption(new Option("ab", null, false, "Description 3"));
        options.addOption(new Option("abc", null, false, "Description 3"));
        
        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(2, options.getMatchingOptions("ab").size());
        assertEquals(1, options.getMatchingOptions("abc").size());
        assertEquals(0, options.getMatchingOptions("d").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("aaa"));
        assertTrue(options.hasOption("bbb"));
        
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        
        assertTrue(options.hasLongOption("aaa"));
        assertTrue(options.hasLongOption("bbb"));
        
        assertFalse(options.hasLongOption("ccc"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "Description 1");
        Option option2 = new Option("b", "bbb", false, "Description 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        
        options.addOption(new Option("a", "aaa", false, "Description 1"));
        options.addOption(new Option("b", "bbb", true, "Description 2"));
        
        String expected = "[ Options: [ short {a=Option: { opt: a  long-opt: aaa  has-arg: false  description: Description 1 }, b=Option: { opt: b  long-opt: bbb  has-arg: true  description: Description 2 }} ] [ long {aaa=Option: { opt: a  long-opt: aaa  has-arg: false  description: Description 1 }, bbb=Option: { opt: b  long-opt: bbb  has-arg: true  description: Description 2 }} ]";
        
        assertEquals(expected, options.toString());
    }
}