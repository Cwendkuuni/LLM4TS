import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", false, "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", false, "option 2");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", false, "option 2");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", false, "option 2");
        option1.setRequired(true);

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", false, "option 2");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("apple", false, "apple");
        Option option2 = new Option("banana", false, "banana");
        Option option3 = new Option("cherry", false, "cherry");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(0, options.getMatchingOptions("a").size());
        assertEquals(2, options.getMatchingOptions("b").size());
        assertEquals(1, options.getMatchingOptions("c").size());
        assertEquals("cherry", options.getMatchingOptions("c").get(0));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", false, "option 2");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("apple", false, "apple");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("apple"));
        assertFalse(options.hasLongOption("a"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("apple", false, "apple");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("apple"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", false, "option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", false, "option 1");
        Option option2 = new Option("b", false, "option 2");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=option 1, b=option 2} ] [ long {} ]";
        assertEquals(expected, options.toString());
    }

}