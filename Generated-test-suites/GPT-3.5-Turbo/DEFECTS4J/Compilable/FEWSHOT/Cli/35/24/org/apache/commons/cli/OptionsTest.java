package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "apple", false, "description1");
        Option option2 = new Option("b", "banana", true, "description2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    @Test
    public void testAddOptionStringString() {
        Options options = new Options();
        options.addOption("a", "apple");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("apple"));
    }

    @Test
    public void testAddOptionStringBooleanString() {
        Options options = new Options();
        options.addOption("a", true, "description");

        assertTrue(options.hasOption("a"));
        assertTrue(options.getOption("a").hasArg());
        assertEquals("description", options.getOption("a").getDescription());
    }

    @Test
    public void testAddOptionStringStringBooleanString() {
        Options options = new Options();
        options.addOption("a", "apple", true, "description");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("apple"));
        assertTrue(options.getOption("a").hasArg());
        assertEquals("description", options.getOption("a").getDescription());
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("b", "banana", false, "description2");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("apple"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("banana"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("a"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("apple"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(option2, options.getOption("banana"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("ab", "beta", false, "description2");
        Option option3 = new Option("ab", "alpha", true, "description2");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(3, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("a"));
        assertTrue(options.getMatchingOptions("a").contains("apple"));
        assertTrue(options.getMatchingOptions("a").contains("ab"));

        assertEquals(2, options.getMatchingOptions("ab").size());
        assertTrue(options.getMatchingOptions("ab").contains("ab"));
        assertTrue(options.getMatchingOptions("ab").contains("alpha"));
        assertFalse(options.getMatchingOptions("ab").contains("beta"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("banana"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("apple"));
        assertTrue(options.hasLongOption("banana"));
        assertFalse(options.hasLongOption("a"));
        assertFalse(options.hasLongOption("b"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("apple"));
        assertFalse(options.hasShortOption("banana"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("a", "apple", true, "description1");
        Option option2 = new Option("b", "banana", false, "description2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("a", "apple");
        options.addOption("b", true, "description");

        StringBuilder expected = new StringBuilder();
        expected.append("[ Options: [ short {a=Option{opt=a, longOpt='apple', hasArg=false, description='null', required=false}} ] ");
        expected.append("[ long {apple=Option{opt=a, longOpt='apple', hasArg=false, description='null', required=false}}, ");
        expected.append("null=Option{opt=null, longOpt='null', hasArg=false, description='null', required=false}} ]");

        assertEquals(expected.toString(), options.toString());
    }
}