import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Test adding an option group
        // Prepare test data
        Option option1 = new Option("a", "alpha", false, "Test option alpha");
        Option option2 = new Option("b", "beta", false, "Test option beta");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        Options options = new Options();

        // Test the method
        options.addOptionGroup(group);

        // Verify the result
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        // Test adding an option
        // Prepare test data
        Option option = new Option("c", "gamma", true, "Test option gamma");
        Options options = new Options();

        // Test the method
        options.addOption(option);

        // Verify the result
        assertTrue(options.hasOption("c"));
        assertEquals(option, options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test getting matching options
        // Prepare test data
        Options options = new Options();
        options.addOption("d", "delta", true, "Test option delta");
        options.addOption("e", "epsilon", true, "Test option epsilon");

        // Test the method
        List<String> matchingOptions = options.getMatchingOptions("e");

        // Verify the result
        assertEquals(1, matchingOptions.size());
        assertEquals("epsilon", matchingOptions.get(0));
    }

    @Test
    public void testHasOption() {
        // Test checking for an option
        // Prepare test data
        Options options = new Options();
        options.addOption("f", "zeta", false, "Test option zeta");

        // Test the method
        boolean result = options.hasOption("f");

        // Verify the result
        assertTrue(result);
    }
}