package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "opt", false, "Description");

        group.addOption(option);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "opt", false, "Description");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "opt", false, "Description");

        options.addOption(option);

        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "opt", false, "Description");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
    }

    // Include more test cases for other methods in the Options class...

}