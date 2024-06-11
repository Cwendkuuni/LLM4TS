import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "description");
        Option option2 = new Option("b", "banana", false, "description");
        group.addOption(option1);
        group.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "description");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "description");
        options.addOption(option);

        assertEquals(option, options.getOption("t"));
        assertEquals(option, options.getOption("test"));
        assertNull(options.getOption("unknown"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption(new Option("a", "apple", false, "description"));

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("apple"));
        assertFalse(options.hasOption("unknown"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption(new Option("a", "apple", false, "description"));

        assertTrue(options.hasLongOption("apple"));
        assertFalse(options.hasLongOption("unknown"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption(new Option("a", "apple", false, "description"));

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("unknown"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "apple", false, "description");
        group.addOption(option);

        Options options = new Options();
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption(new Option("a", "apple", false, "description"));

        String expected = "[ Options: [ short {a=Option(a, apple, false, description)} ] [ long {} ]";
        assertEquals(expected, options.toString());
    }
}