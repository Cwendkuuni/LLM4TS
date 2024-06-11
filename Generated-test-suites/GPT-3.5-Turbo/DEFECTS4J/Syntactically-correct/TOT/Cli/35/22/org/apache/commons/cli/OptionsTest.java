import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class OptionsTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    // Test addOptionGroup() method
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();

        // Add options to the group
        Option option1 = new Option("a", "apple", false, "Description of option1");
        Option option2 = new Option("b", "banana", false, "Description of option2");

        group.addOption(option1);
        group.addOption(option2);

        // Add the group to the options object
        options.addOptionGroup(group);

        // Verify that the group is added to the options object
        List<OptionGroup> result = options.getOptionGroups();
        assertEquals(1, result.size());
        assertEquals(group, result.get(0));
    }

    // Test addOption() method with short name only
    @Test
    public void testAddOptionWithShortNameOnly() {
        options.addOption("v", "verbose", false, "Enable verbose logging");

        // Verify that the option is added to the options object
        Option result = options.getOption("v");
        assertNotNull(result);
        assertEquals("verbose", result.getLongOpt());
    }

    // Test addOption() method with short name and required argument
    @Test
    public void testAddOptionWithShortNameAndRequiredArgument() {
        options.addOption("f", true, "Input file");

        // Verify that the option is added to the options object
        Option result = options.getOption("f");
        assertNotNull(result);
        assertTrue(result.hasArg());
    }

    // Test addOption() method with short and long names and required argument
    @Test
    public void testAddOptionWithShortAndLongNamesAndRequiredArgument() {
        options.addOption("i", "input", true, "Input file");

        // Verify that the option is added to the options object
        Option result = options.getOption("i");
        assertNotNull(result);
        assertEquals("input", result.getLongOpt());
        assertTrue(result.hasArg());
    }

    // Test getOptions() method
    @Test
    public void testGetOptions() {
        options.addOption("h", "help", false, "Display help message");
        options.addOption("v", "verbose", false, "Enable verbose logging");

        // Verify that the options are retrieved correctly
        List<Option> result = options.getOptions();
        assertEquals(2, result.size());
        assertEquals("help", result.get(0).getLongOpt());
        assertEquals("verbose", result.get(1).getLongOpt());
    }

    // Test getRequiredOptions() method
    @Test
    public void testGetRequiredOptions() {
        options.addOption("h", "help", false, "Display help message");
        options.addOption("v", "verbose", false, "Enable verbose logging");
        options.addOption("f", true, "Input file");

        // Verify that the required options are retrieved correctly
        List result = options.getRequiredOptions();
        assertEquals(1, result.size());
        assertEquals("f", result.get(0));
    }

    // Test getOption() method with short name
    @Test
    public void testGetOptionWithShortName() {
        options.addOption("h", "help", false, "Display help message");
        options.addOption("v", "verbose", false, "Enable verbose logging");

        // Verify that the option is retrieved correctly
        Option result = options.getOption("h");
        assertNotNull(result);
        assertEquals("help", result.getLongOpt());
    }

    // Test getOption() method with long name
    @Test
    public void testGetOptionWithLongName() {
        options.addOption("h", "help", false, "Display help message");
        options.addOption("v", "verbose", false, "Enable verbose logging");

        // Verify that the option is retrieved correctly
        Option result = options.getOption("help");
        assertNotNull(result);
        assertEquals("help", result.getLongOpt());
    }

    // Test getMatchingOptions() method
    @Test
    public void testGetMatchingOptions() {
        options.addOption("file", true, "Input file");
        options.addOption("folder", true, "Input folder");

        // Verify that the matching options are retrieved correctly
        List<String> result = options.getMatchingOptions("f");
        assertEquals(2, result.size());
        assertTrue(result.contains("file"));
        assertTrue(result.contains("folder"));
    }

    // Test hasOption() method with existing option
    @Test
    public void testHasOptionWithExistingOption() {
        options.addOption("h", "help", false, "Display help message");

        // Verify that the existing option is detected correctly
        assertTrue(options.hasOption("h"));
    }

    // Test hasOption() method with non-existing option
    @Test
    public void testHasOptionWithNonExistingOption() {
        options.addOption("h", "help", false, "Display help message");

        // Verify that the non-existing option is detected correctly
        assertFalse(options.hasOption("v"));
    }

    // Test hasLongOption() method with existing long option
    @Test
    public void testHasLongOptionWithExistingLongOption() {
        options.addOption("h", "help", false, "Display help message");

        // Verify that the existing long option is detected correctly
        assertTrue(options.hasLongOption("help"));
    }

    // Test hasLongOption() method with non-existing long option
    @Test
    public void testHasLongOptionWithNonExistingLongOption() {
        options.addOption("h", "help", false, "Display help message");

        // Verify that the non-existing long option is detected correctly
        assertFalse(options.hasLongOption("verbose"));
    }

    // Test hasShortOption() method with existing short option
    @Test
    public void testHasShortOptionWithExistingShortOption() {
        options.addOption("h", "help", false, "Display help message");

        // Verify that the existing short option is detected correctly
        assertTrue(options.hasShortOption("h"));
    }

    // Test hasShortOption() method with non-existing short option
    @Test
    public void testHasShortOptionWithNonExistingShortOption() {
        options.addOption("h", "help", false, "Display help message");

        // Verify that the non-existing short option is detected correctly
        assertFalse(options.hasShortOption("v"));
    }

    // Test getOptionGroup() method with existing option group
    @Test
    public void testGetOptionGroupWithExistingOptionGroup() {
        OptionGroup group = new OptionGroup();
        options.addOptionGroup(group);

        // Verify that the existing option group is retrieved correctly
        assertEquals(group, options.getOptionGroup(group.getOptions().get(0)));
    }

    // Test getOptionGroup() method with non-existing option group
    @Test
    public void testGetOptionGroupWithNonExistingOptionGroup() {
        OptionGroup group = new OptionGroup();

        // Verify that the non-existing option group is detected correctly
        assertNull(options.getOptionGroup(group.getOptions().get(0)));
    }

    // Test toString() method
    @Test
    public void testToString() {
        options.addOption("h", "help", false, "Display help message");
        options.addOption("v", "verbose", false, "Enable verbose logging");

        // Verify the string representation of the options object
        String result = options.toString();
        String expected = "[ Options: [ short {h=help, v=verbose} ] [ long {help=help, verbose=verbose} ]";
        assertEquals(expected, result);
    }
}