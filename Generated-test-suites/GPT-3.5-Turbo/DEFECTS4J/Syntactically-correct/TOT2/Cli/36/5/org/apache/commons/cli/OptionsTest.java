import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "charlie", true, "Option Charlie");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("c"));
    }

    @Test
    public void testAddOptionWithInvalidKey() {
        Options options = new Options();
        Option option = new Option("c", "charlie", true, "Option Charlie");

        options.addOption(option);

        assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("d", "delta", false, "Option Delta");

        options.addOption(option);
        Option retrievedOption = options.getOption("d");

        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("e", "elephant", true, "Option Elephant");
        Option option2 = new Option("f", "fox", true, "Option Fox");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("e").size());
        assertTrue(options.getMatchingOptions("e").contains("elephant"));
        assertFalse(options.getMatchingOptions("elephant").contains("elephant"));
        assertEquals(0, options.getMatchingOptions("z").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("g", "golf", true, "Option Golf");

        options.addOption(option);

        assertTrue(options.hasOption("g"));
        assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("h", "hotel", true, "Option Hotel");
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

}