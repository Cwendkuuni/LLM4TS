import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "option-a", false, "Option A"));
        group.addOption(new Option("b", "option-b", true, "Option B"));

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();

        Option option = new Option("a", "option-a", false, "Option A");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option option = new Option("a", "option-a", false, "Option A");

        options.addOption(option);

        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option option1 = new Option("abc", "option-abc", false, "Option ABC");
        Option option2 = new Option("abd", "option-abd", false, "Option ABD");
        Option option3 = new Option("abe", "option-abe", false, "Option ABE");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("ab");

        assertTrue(matchingOptions.contains("option-abc"));
        assertTrue(matchingOptions.contains("option-abd"));
        assertTrue(matchingOptions.contains("option-abe"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option option = new Option("a", "option-a", false, "Option A");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option-a"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();

        Option option = new Option("a", "option-a", false, "Option A");

        options.addOption(option);

        assertTrue(options.hasLongOption("option-a"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();

        Option option = new Option("a", "option-a", false, "Option A");

        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "option-a", false, "Option A"));
        group.addOption(new Option("b", "option-b", true, "Option B"));

        options.addOptionGroup(group);

        Option optionA = options.getOption("a");
        Option optionB = options.getOption("b");

        assertEquals(group, options.getOptionGroup(optionA));
        assertEquals(group, options.getOptionGroup(optionB));
    }

}