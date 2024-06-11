package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option 1");
        Option option2 = new Option("b", "beta", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "charlie", false, "Option 3");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("d", "delta", false, "Option 4");
        options.addOption(option);

        Option retrievedOption = options.getOption("d");

        assertEquals(option, retrievedOption);
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("e", "echo", false, "Option 5");
        options.addOption(option);

        assertTrue(options.hasOption("e"));
        assertFalse(options.hasOption("z"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "foxtrot", false, "Option 6");
        group.addOption(option);
        options.addOptionGroup(group);

        OptionGroup retrievedGroup = options.getOptionGroup(option);

        assertEquals(group, retrievedGroup);
    }
}