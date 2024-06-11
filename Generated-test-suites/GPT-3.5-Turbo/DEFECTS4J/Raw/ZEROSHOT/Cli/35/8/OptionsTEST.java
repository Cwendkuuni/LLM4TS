import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup();

        Option option1 = new Option("a", "apple", false, "apple description");
        group.addOption(option1);

        Option option2 = new Option("b", "banana", false, "banana description");
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();

        Option option = new Option("a", false, "option description");

        options.addOption(option);

        assertTrue(options.getOption("a").equals(option));
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();

        Option option = new Option("a", "apple", false, "option description");

        options.addOption(option);

        assertTrue(options.getOption("a").equals(option));
        assertTrue(options.getOption("apple").equals(option));
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
    }

    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();

        Option option = new Option("a", true, "option description");

        options.addOption(option);

        assertTrue(options.getOption("a").equals(option));
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();

        Option option1 = new Option("a", false, "option1 description");
        options.addOption(option1);

        Option option2 = new Option("b", false, "option2 description");
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();

        Option option1 = new Option("a", false, "option1 description");
        options.addOption(option1);

        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        Option option2 = new Option("b", false, "option2 description");
        group.addOption(option2);
        options.addOptionGroup(group);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option option1 = new Option("a", false, "option1 description");
        options.addOption(option1);

        Option option2 = new Option("b", false, "option2 description");
        options.addOption(option2);

        assertTrue(options.getOption("a").equals(option1));
        assertTrue(options.getOption("b").equals(option2));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option option1 = new Option("a", false, "option1 description");
        options.addOption(option1);

        Option option2 = new Option("abc", false, "option2 description");
        options.addOption(option2);

        Option option3 = new Option("bcd", false, "option3 description");
        options.addOption(option3);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("abc"));

        assertEquals(2, options.getMatchingOptions("b").size());
        assertTrue(options.getMatchingOptions("b").contains("abc"));
        assertTrue(options.getMatchingOptions("b").contains("bcd"));

        assertEquals(0, options.getMatchingOptions("c").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option option1 = new Option("a", false, "option1 description");
        options.addOption(option1);

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();

        Option option1 = new Option("a", false, "option1 description");
        options.addOption(option1);

        Option option2 = new Option("b", "banana", false, "option2 description");
        options.addOption(option2);

        assertFalse(options.hasLongOption("a"));
        assertTrue(options.hasLongOption("banana"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();

        Option option1 = new Option("a", false, "option1 description");
        options.addOption(option1);

        Option option2 = new Option("b", "banana", false, "option2 description");
        options.addOption(option2);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("banana"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();

        Option option1 = new Option("a", "apple", false, "option1 description");
        OptionGroup group1 = new OptionGroup();
        group1.addOption(option1);

        Option option2 = new Option("b", "banana", false, "option2 description");
        OptionGroup group2 = new OptionGroup();
        group2.addOption(option2);

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertTrue(options.getOptionGroup(option1).equals(group1));
        assertNull(options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Options options = new Options();

        Option option1 = new Option("a", "apple", false, "option1 description");
        options.addOption(option1);

        Option option2 = new Option("b", "banana", false, "option2 description");
        options.addOption(option2);

        assertEquals("[ Options: [ short {a: option1 description, b: option2 description} ] [ long {apple: option1 description, banana: option2 description} ]", options.toString());
    }
}