import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    // Test addOptionGroup() method

    @Test
    public void testAddOptionGroup() {
        // Create an OptionGroup with three options
        OptionGroup group = new OptionGroup()
                .addOption(new Option("a", "option-a", false, "Option A"))
                .addOption(new Option("b", "option-b", false, "Option B"))
                .addOption(new Option("c", "option-c", false, "Option C"));

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOption("a").getGroup() == group);
        assertTrue(options.getOption("b").getGroup() == group);
        assertTrue(options.getOption("c").getGroup() == group);
    }

    // Test addOption() methods

    @Test
    public void testAddOption_withShortNameAndDescription() {
        options.addOption("a", "Option A");

        assertTrue(options.hasOption("a"));
        assertEquals("Option A", options.getOption("a").getDescription());
    }

    @Test
    public void testAddOption_withShortNameHasArgAndDescription() {
        options.addOption("a", true, "Option A");

        assertTrue(options.hasOption("a"));
        assertEquals(true, options.getOption("a").hasArg());
        assertEquals("Option A", options.getOption("a").getDescription());
    }

    @Test
    public void testAddOption_withShortNameLongNameHasArgAndDescription() {
        options.addOption("a", "option-a", true, "Option A");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option-a"));
        assertEquals("Option A", options.getOption("a").getDescription());
    }

    @Test
    public void testAddOption_withOption() {
        Option option = new Option("a", "option-a", true, "Option A");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option-a"));
        assertEquals("Option A", options.getOption("a").getDescription());
        assertEquals(option, options.getOption("a"));
    }

    // Test getOptions() method

    @Test
    public void testGetOptions() {
        options.addOption("a", "Option A");
        options.addOption("b", "Option B");

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(new Option("a", "Option A")));
        assertTrue(options.getOptions().contains(new Option("b", "Option B")));
    }

    // Test getRequiredOptions() method

    @Test
    public void testGetRequiredOptions() {
        OptionGroup group = new OptionGroup()
                .addOption(new Option("a", "option-a", false, "Option A"))
                .addOption(new Option("b", "option-b", false, "Option B"))
                .addOption(new Option("c", "option-c", false, "Option C"));

        options.addOptionGroup(group);

        assertEquals(1, options.getRequiredOptions().size());
    }

    // Test getOption() method

    @Test
    public void testGetOption_withShortName() {
        options.addOption("a", "Option A");

        assertTrue(options.hasOption("a"));
        assertEquals("Option A", options.getOption("a").getDescription());
    }

    @Test
    public void testGetOption_withLongName() {
        options.addOption("a", "option-a", true, "Option A");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option-a"));
        assertEquals("Option A", options.getOption("a").getDescription());
    }

    // Test getMatchingOptions() method

    @Test
    public void testGetMatchingOptions() {
        options.addOption("apple", "Apple description");
        options.addOption("banana", "Banana description");
        options.addOption("blueberry", "Blueberry description");

        assertEquals(2, options.getMatchingOptions("b").size());
        assertTrue(options.getMatchingOptions("b").contains("banana"));
        assertTrue(options.getMatchingOptions("b").contains("blueberry"));
    }

    // Test hasOption() method

    @Test
    public void testHasOption_withShortName() {
        options.addOption("a", "Option A");

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testHasOption_withLongName() {
        options.addOption("a", "option-a", true, "Option A");

        assertTrue(options.hasOption("option-a"));
    }

    // Test hasLongOption() method

    @Test
    public void testHasLongOption() {
        options.addOption("a", "option-a", true, "Option A");

        assertTrue(options.hasLongOption("option-a"));
    }

    // Test hasShortOption() method

    @Test
    public void testHasShortOption() {
        options.addOption("a", "option-a", true, "Option A");

        assertTrue(options.hasShortOption("a"));
    }

    // Test getOptionGroup() method

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup()
                .addOption(new Option("a", "option-a", false, "Option A"))
                .addOption(new Option("b", "option-b", false, "Option B"));

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(new Option("a", "option-a")));
        assertEquals(group, options.getOptionGroup(new Option("b", "option-b")));
    }

    // Test toString() method

    @Test
    public void testToString() {
        options.addOption("a", "Option A");
        options.addOption("b", "Option B");

        String expected = "[ Options: [ short {a=Option A, b=Option B} ] [ long {Option A=Option A, Option B=Option B} ]";
        assertEquals(expected, options.toString());
    }

}