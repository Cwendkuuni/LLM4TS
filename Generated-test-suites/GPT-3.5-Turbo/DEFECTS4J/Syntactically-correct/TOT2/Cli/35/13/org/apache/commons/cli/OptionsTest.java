import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");

        options.addOption(option);

        assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("p", "path", false, "Path option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("f").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("d", "debug", false, "Debug option");

        options.addOption(option);

        assertTrue(options.hasOption("d"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("v", "verbose", false, "Verbose option");

        options.addOption(option);

        assertTrue(options.hasLongOption("verbose"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("l", "log", false, "Log option");

        options.addOption(option);

        assertTrue(options.hasShortOption("l"));
    }
}