import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "test", true, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "test1", true, "Test option 1");
        Option option2 = new Option("b", "test2", false, "Test option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "test", true, "Test option");
        options.addOption(option);
        assertEquals(option, options.getOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "test1", true, "Test option 1");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);

        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option1));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "test1", true, "Test option 1");
        Option option2 = new Option("abc", "test2", false, "Test option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertEquals(2, options.getMatchingOptions("ab").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "test", true, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "test", true, "Test option");
        options.addOption(option);
        assertTrue(options.hasLongOption("test"));
        assertFalse(options.hasLongOption("notfound"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "test", true, "Test option");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

}