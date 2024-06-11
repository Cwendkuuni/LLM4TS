import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an option group
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "aa", false, "Option a");
        Option option2 = new Option("b", "bb", false, "Option b");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(2, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        // Test adding an option with short name only
        Options options = new Options();
        Option option = new Option("c", "cc", false, "Option c");

        options.addOption(option);

        assertTrue(options.hasOption("c"));
    }

    @Test
    public void testGetOption() {
        // Test getting an option by short name
        Options options = new Options();
        Option option = new Option("d", "dd", false, "Option d");
        options.addOption(option);

        assertEquals(option, options.getOption("d"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test getting options with a matching partial name
        Options options = new Options();
        Option option1 = new Option("e", "ee", false, "Option e");
        Option option2 = new Option("example", null, false, "Example option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("e").size());
    }

    @Test
    public void testRequiredOptions() {
        // Test adding and getting required options
        Options options = new Options();
        Option option = new Option("f", "ff", false, "Option f");
        option.setRequired(true);

        options.addOption(option);

        assertTrue(options.getRequiredOptions().contains("f"));
    }

    @Test
    public void testHasOption() {
        // Test checking if an option exists
        Options options = new Options();
        Option option = new Option("g", "gg", false, "Option g");
        options.addOption(option);

        assertTrue(options.hasOption("g"));
    }

    @Test
    public void testGetOptionGroup() {
        // Test getting the option group of an option
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("h", "hh", false, "Option h");
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }
}