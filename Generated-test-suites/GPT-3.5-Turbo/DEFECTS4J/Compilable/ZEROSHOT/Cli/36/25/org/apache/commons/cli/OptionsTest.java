package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.getOption("a").equals(option1));
        assertTrue(options.getOption("b").equals(option2));
    }

    @Test
    public void testAddOptionWithDescription() {
        options.addOption("a", "option1", false, "This is option 1");
        options.addOption("b", "option2", true, "This is option 2");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));

        Option option1 = options.getOption("a");
        Option option2 = options.getOption("b");

        assertEquals("This is option 1", option1.getDescription());
        assertEquals("This is option 2", option2.getDescription());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        option1.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        Option result1 = options.getOption("a");
        Option result2 = options.getOption("b");

        assertEquals(option1, result1);
        assertEquals(option2, result2);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("apple", "This is option 1");
        Option option2 = new Option("banana", "This is option 2");
        Option option3 = new Option("orange", "This is option 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertEquals(2, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("a").contains("apple"));
        assertTrue(options.getMatchingOptions("o").contains("orange"));
        assertFalse(options.getMatchingOptions("b").contains("banana"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("option1"));
        assertTrue(options.hasLongOption("option2"));
        assertFalse(options.hasLongOption("option3"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        options.addOption(option1);
        options.addOption(option2);

        StringBuilder expected = new StringBuilder();
        expected.append("[ Options: [ short {a=option1, b=option2} ] [ long {} ]");

        assertEquals(expected.toString(), options.toString());
    }
}