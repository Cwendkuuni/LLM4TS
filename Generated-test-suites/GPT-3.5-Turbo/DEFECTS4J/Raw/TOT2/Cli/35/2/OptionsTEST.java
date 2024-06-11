import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an option group
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Description 1");
        Option option2 = new Option("o2", "opt2", false, "Description 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        // Test adding a basic option
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Description");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testAddOptionWithArgs() {
        // Test adding an option with arguments
        Options options = new Options();
        Option option = new Option("o", "opt", true, "Description");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testAddOptionWithoutArgs() {
        // Test adding an option without arguments
        Options options = new Options();
        options.addOption("o", "Description");
        assertTrue(options.hasOption("o"));
        assertEquals("o", options.getOption("o").getKey());
    }

    @Test
    public void testGetMatchingOptions() {
        // Test getting matching options
        Options options = new Options();
        options.addOption("opt1", "Description 1");
        options.addOption("opt2", "Description 2");
        options.addOption("optional", "Description 3");
        assertEquals(2, options.getMatchingOptions("opt").size());
    }

    @Test
    public void testGetRequiredOptions() {
        // Test getting required options
        Options options = new Options();
        Option option = new Option("o", "opt", false, "Description");
        option.setRequired(true);
        options.addOption(option);
        assertTrue(options.getRequiredOptions().contains("o"));
    }

    @Test
    public void testGetOptions() {
        // Test getting options
        Options options = new Options();
        Option option1 = new Option("o1", "opt1", false, "Description 1");
        Option option2 = new Option("o2", "opt2", false, "Description 2");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getOptions().size());
    }

    @Test
    public void testHasOption() {
        // Test checking if option exists
        Options options = new Options();
        options.addOption("o", "Description");
        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("notexist"));
    }
}