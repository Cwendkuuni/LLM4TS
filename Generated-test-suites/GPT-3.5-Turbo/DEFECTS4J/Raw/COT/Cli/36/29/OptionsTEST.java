import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", false, "Option b");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOptionGroup(option1));
        assertTrue(options.hasOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", false, "Option a");
        Option option2 = new Option("b", "banana", true, "Option b");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("banana"));
        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("apple"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(option2, options.getOption("banana"));
        assertTrue(options.hasLongOption("apple"));
        assertTrue(options.hasLongOption("banana"));
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", false, "Option a");
        Option option2 = new Option("b", "banana", true, "Option b");
        Option option3 = new Option("c", false, "Option c");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("a");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("a"));
        assertTrue(matchingOptions.contains("apple"));

        matchingOptions = options.getMatchingOptions("b");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("b"));
        assertTrue(matchingOptions.contains("banana"));

        matchingOptions = options.getMatchingOptions("c");
        assertEquals(1, matchingOptions.size());
        assertTrue(matchingOptions.contains("c"));

        matchingOptions = options.getMatchingOptions("d");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option a");

        options.addOption(option1);

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("a", "apple", false, "Option a");

        options.addOption(option1);

        assertTrue(options.hasLongOption("apple"));
        assertFalse(options.hasLongOption("banana"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option a");

        options.addOption(option1);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option a");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", true, "Option b");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option option a} ] [ long {null=Option option a, null=Option option b} ]";
        assertEquals(expected, options.toString());
    }
}