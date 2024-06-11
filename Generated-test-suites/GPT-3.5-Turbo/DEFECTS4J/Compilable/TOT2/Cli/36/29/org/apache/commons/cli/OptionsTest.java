package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Test
    public void testAddOptionGroup() {
        options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "test1", false, "Test 1 option");
        Option option2 = new Option("b", "test2", false, "Test 2 option");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOptionShort() {
        options = new Options();
        options.addOption("a", "Test option A");

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasLongOption("a"));
    }

    @Test
    public void testAddOptionLong() {
        options = new Options();
        options.addOption("test", "long", false, "Test long option");

        assertTrue(options.hasOption("test"));
        assertTrue(options.hasLongOption("long"));
    }

    @Test
    public void testAddOptionMultiple() {
        options = new Options();
        options.addOption("a", "Test option A");
        options.addOption("b", "Test option B");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }

    @Test
    public void testGetOption() {
        options = new Options();
        Option option = new Option("a", "test", false, "Test option");
        options.addOption(option);

        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testGetMatchingOptions() {
        options = new Options();
        options.addOption("test1", "Test option 1");
        options.addOption("test2", "Test option 2");

        assertEquals(2, options.getMatchingOptions("test").size());
    }

    @Test
    public void testGetRequiredOptions() {
        options = new Options();
        Option option1 = new Option("a", "test1", false, "Test option 1");
        Option option2 = new Option("b", "test2", false, "Test option 2");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getRequiredOptions().size());
    }
}