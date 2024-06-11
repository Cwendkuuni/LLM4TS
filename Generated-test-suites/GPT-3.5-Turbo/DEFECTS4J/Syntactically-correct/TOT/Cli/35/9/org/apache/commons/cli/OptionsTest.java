import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption_shortName_only() {
        options.addOption("h", "Displays help information");
        assertTrue(options.hasOption("h"));
        assertFalse(options.hasOption("help"));
    }

    @Test
    public void testAddOption_withArg() {
        options.addOption("f", true, "Specifies a file name");
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("file"));
    }

    @Test
    public void testAddOption_shortAndLongName_withArg() {
        options.addOption("v", "verbose", true, "Enables verbose mode");
        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("verbose"));
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "option1", false, "First option");
        Option option2 = new Option("b", "option2", false, "Second option");
        Option option3 = new Option("c", "option3", false, "Third option");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        options.addOptionGroup(optionGroup);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option1"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("option2"));
        assertTrue(options.hasOption("c"));
        assertTrue(options.hasOption("option3"));

        assertFalse(options.hasOption("d"));
        assertFalse(options.hasOption("option4"));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "option1", false, "First option");
        Option option2 = new Option("b", "option2", true, "Second option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option1, options.getOption("option1"));
        assertEquals(option2, options.getOption("b"));
        assertEquals(option2, options.getOption("option2"));

        assertNull(options.getOption("c"));
        assertNull(options.getOption("option3"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "option1", false, "First option");
        Option option2 = new Option("ab", "option2", true, "Second option");
        Option option3 = new Option("abc", "option3", false, "Third option");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertEquals(3, options.getMatchingOptions("abc").size());
        assertEquals(0, options.getMatchingOptions("d").size());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", "option1", false, "First option");
        options.addOption(option1);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option1"));

        assertFalse(options.hasOption("b"));
        assertFalse(options.hasOption("option2"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "option1", false, "First option");
        options.addOption(option1);

        assertTrue(options.hasLongOption("option1"));

        assertFalse(options.hasLongOption("a"));
        assertFalse(options.hasLongOption("option2"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", "option1", false, "First option");
        options.addOption(option1);

        assertTrue(options.hasShortOption("a"));

        assertFalse(options.hasShortOption("option1"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("a", "option1", false, "First option");
        Option option2 = new Option("b", "option2", false, "Second option");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));

        assertNull(options.getOptionGroup(new Option("c", "option3", false, "Third option")));
    }
}