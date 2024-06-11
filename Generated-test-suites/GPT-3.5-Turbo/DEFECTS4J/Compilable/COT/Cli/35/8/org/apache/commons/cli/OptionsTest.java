package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Description for option a");
        Option option2 = new Option("b", "beta", false, "Description for option b");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.getOptionGroup(option1) == group);
        assertTrue(options.getOptionGroup(option2) == group);
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", false, "Description for option a");
        Option option2 = new Option("b", "beta", true, "Description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", true, "Description for option a");
        Option option2 = new Option("b", "beta", false, "Description for option b");
        OptionGroup group = new OptionGroup();
        Option option3 = new Option("c", "gamma", false, "Description for option c");
        Option option4 = new Option("d", "delta", true, "Description for option d");
        group.addOption(option3);
        group.addOption(option4);

        options.addOption(option1);
        options.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));
        assertFalse(options.getRequiredOptions().contains(option3));
        assertTrue(options.getRequiredOptions().contains(option4));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("h", "help", false, "Display help message");
        Option option2 = new Option("h1", "help1", false, "Display help1 message");
        Option option3 = new Option("h2", "help2", false, "Display help2 message");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("he").size());
        assertEquals(1, options.getMatchingOptions("help1").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", false, "Description for option a");
        Option option2 = new Option("b", "beta", true, "Description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", false, "Description for option a");
        Option option2 = new Option("b", "beta", true, "Description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("alpha"));
        assertTrue(options.hasLongOption("beta"));
        assertFalse(options.hasLongOption("gamma"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", false, "Description for option a");
        Option option2 = new Option("b", "beta", true, "Description for option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Description for option a");
        Option option2 = new Option("b", "beta", false, "Description for option b");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "gamma", false, "Description for option c")));
    }

}