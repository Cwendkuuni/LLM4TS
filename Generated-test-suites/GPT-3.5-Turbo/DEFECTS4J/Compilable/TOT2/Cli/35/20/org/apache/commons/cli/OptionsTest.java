package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Description");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Description 1");
        Option option2 = new Option("b", "option2", true, "Description 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertEquals(option, options.getOption("t"));
        assertEquals(option, options.getOption("test"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", false, "An apple option");
        Option option2 = new Option("b", "banana", true, "A banana option");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("b").contains("banana"));
    }

    @Test
    public void testRequiredOptions() {
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);

        assertTrue(options.getRequiredOptions().contains(option.getKey()));
    }
}