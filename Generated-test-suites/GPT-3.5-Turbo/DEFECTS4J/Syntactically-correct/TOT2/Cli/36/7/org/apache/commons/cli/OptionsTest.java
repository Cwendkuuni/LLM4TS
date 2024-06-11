import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

    // Test case for addOptionGroup method
    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option A");
        Option option2 = new Option("b", "beta", false, "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroups().contains(optionGroup));
    }

    // Test case for addOption method
    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "charlie", true, "Option C");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    // Test case for getOption method
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("d", "delta", false, "Option D");
        options.addOption(option);

        assertEquals(option, options.getOption("d"));
    }

    // Test case for getMatchingOptions method
    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("e", "echo", false, "Option E");
        Option option2 = new Option("f", "foxtrot", false, "Option F");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("echo").size());
    }

    // Test case for hasOption method
    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("g", "golf", true, "Option G");
        options.addOption(option);

        assertTrue(options.hasOption("g"));
    }

    // Test case for getOptionGroup method
    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("h", "hotel", false, "Option H");
        optionGroup.addOption(option);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option));
    }
}