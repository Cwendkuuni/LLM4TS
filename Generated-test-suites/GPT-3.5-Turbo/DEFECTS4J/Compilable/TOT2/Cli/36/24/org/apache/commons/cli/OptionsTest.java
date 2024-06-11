package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertEquals(option, options.getOption("t"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasOption("t"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasLongOption("test"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasShortOption("t"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

}