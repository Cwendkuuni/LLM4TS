import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "A", false, "Option A");
        Option option2 = new Option("b", "B", false, "Option B");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "A", false, "Option A");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("A"));
        assertTrue(options.getOption("a").getDescription().equals("Option A"));
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        Option option = new Option("a", "A", true, "Option A");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("A"));
        assertTrue(options.hasOption("a"));
        assertTrue(options.getOption("a").getDescription().equals("Option A"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "A", false, "Option A");
        options.addOption(option);

        assertNotNull(options.getOption("a"));
        assertNotNull(options.getOption("A"));
        assertNull(options.getOption("b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "A", false, "Option A");
        Option option2 = new Option("abc", "ABC", false, "Option ABC");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertEquals(1, options.getMatchingOptions("A").size());
        assertEquals(1, options.getMatchingOptions("b").size());
        assertEquals(1, options.getMatchingOptions("abc").size());
        assertEquals(1, options.getMatchingOptions("ABC").size());
        assertEquals(0, options.getMatchingOptions("c").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "A", false, "Option A");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("A"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "A", false, "Option A");
        options.addOption(option);

        assertTrue(options.hasLongOption("A"));
        assertFalse(options.hasLongOption("a"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "A", false, "Option A");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("A"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "A", false, "Option A");
        Option option2 = new Option("b", "B", false, "Option B");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
        assertNull(options.getOptionGroup(new Option("c", "C", false, "Option C")));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("a", "A", false, "Option A");
        options.addOption(option);

        assertTrue(options.toString().contains("[ short "));
        assertTrue(options.toString().contains("[ long "));
    }
}