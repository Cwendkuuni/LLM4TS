import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test case 1: Adding an option group with a required option
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        option1.setRequired(true);
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.getRequiredOptions().contains("a"));
        
        // Test case 2: Adding an option group with multiple options
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testAddOption() {
        // Test case 1: Adding an option with only a short name
        Options options = new Options();
        Option option = new Option("a", null, false, "Option 1");
        options.addOption(option);
        assertTrue(options.hasOption("a"));

        // Test case 2: Adding an option with a short name and a long name
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Option 1");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("option1"));
    }

    @Test
    public void testGetOption() {
        // Test case 1: Retrieving an option by its short name
        Options options = new Options();
        Option option = new Option("a", null, false, "Option 1");
        options.addOption(option);
        assertEquals(option, options.getOption("a"));

        // Test case 2: Retrieving an option by its long name
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Option 1");
        options.addOption(option);
        assertEquals(option, options.getOption("option1"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test case 1: Getting options that match a partial long name
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(1, options.getMatchingOptions("opt").size());
        assertTrue(options.getMatchingOptions("opt").contains("option1"));

        // Test case 2: Getting options that match a partial long name with multiple matches
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        Option option3 = new Option("c", "option3", false, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        assertEquals(2, options.getMatchingOptions("opt").size());
        assertTrue(options.getMatchingOptions("opt").contains("option1"));
        assertTrue(options.getMatchingOptions("opt").contains("option2"));
    }

    @Test
    public void testHasOption() {
        // Test case 1: Checking if an option exists by its short name
        Options options = new Options();
        Option option = new Option("a", null, false, "Option 1");
        options.addOption(option);
        assertTrue(options.hasOption("a"));

        // Test case 2: Checking if an option exists by its long name
        Options options = new Options();
        Option option = new Option("a", "option1", false, "Option 1");
        options.addOption(option);
        assertTrue(options.hasOption("option1"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test case 1: Getting the option group of an option
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option1));
    }
}