package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option opt = new Option("a", "test", false, "Test option");
        options.addOption(opt);
        assertTrue(options.getOptions().contains(opt));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "optionA", false, "Option A"));
        group.addOption(new Option("b", "optionB", false, "Option B"));
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option opt = new Option("c", "testOption", false, "Test option");
        options.addOption(opt);
        assertEquals(opt, options.getOption("c"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option opt = new Option("d", "testOption", false, "Test option");
        options.addOption(opt);
        assertTrue(options.hasOption("d"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option opt = new Option("e", "testOption", false, "Test option");
        options.addOption(opt);
        assertTrue(options.hasLongOption("testOption"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option opt = new Option("f", "testOption", false, "Test option");
        options.addOption(opt);
        assertTrue(options.hasShortOption("f"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option opt = new Option("g", "testOption", false, "Test option");
        group.addOption(opt);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(opt));
    }
}