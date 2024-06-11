import org.junit.*;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add");
        Option option2 = new Option("r", "remove", false, "Remove");

        group.addOption(option1)
             .addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("r"));

        assertNotNull(options.getOptionGroup(option1));
        assertNotNull(options.getOptionGroup(option2));

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("v", "version", false, "Version");

        options.addOption(option);

        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("version"));

        assertEquals(option, options.getOption("v"));
        assertEquals(option, options.getOption("version"));
    }

    @Test
    public void testAddOptionWithHasArg() {
        Option option = new Option("o", "output", true, "Output");

        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("output"));
        assertTrue(option.hasArg());

        assertEquals(option, options.getOption("o"));
        assertEquals(option, options.getOption("output"));
    }

    @Test
    public void testAddOptionWithoutHasArg() {
        Option option = new Option("i", "input", false, "Input");

        options.addOption(option);

        assertTrue(options.hasOption("i"));
        assertTrue(options.hasOption("input"));
        assertFalse(option.hasArg());

        assertEquals(option, options.getOption("i"));
        assertEquals(option, options.getOption("input"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add");
        Option option2 = new Option("r", "remove", false, "Remove");

        options.addOption(option1)
               .addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "add", false, "Add");
        Option option2 = new Option("r", "remove", false, "Remove");
        OptionGroup group = new OptionGroup();

        group.addOption(option1)
             .addOption(option2);

        options.addOptionGroup(group)
               .addOption("h", "help", false, "Help");

        assertTrue(options.getRequiredOptions().contains(group));
        assertTrue(options.getRequiredOptions().contains("h"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "add", false, "Add");
        Option option2 = new Option("all", false, "All");
        Option option3 = new Option("r", "remove", false, "Remove");

        options.addOption(option1)
               .addOption(option2)
               .addOption(option3);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertEquals("a", options.getMatchingOptions("a").get(0));
        assertEquals(2, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("add"));
        assertTrue(options.getMatchingOptions("a").contains("all"));
        assertEquals(0, options.getMatchingOptions("abc").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("v", "version", false, "Version");

        options.addOption(option);

        assertTrue(options.hasOption("v"));
        assertTrue(options.hasOption("version"));
        assertFalse(options.hasOption("abc"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("v", "version", false, "Version");

        options.addOption(option);

        assertTrue(options.hasLongOption("version"));
        assertFalse(options.hasLongOption("v"));
        assertFalse(options.hasLongOption("abc"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("v", "version", false, "Version");

        options.addOption(option);

        assertTrue(options.hasShortOption("v"));
        assertFalse(options.hasShortOption("version"));
        assertFalse(options.hasShortOption("abc"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add");
        Option option2 = new Option("r", "remove", false, "Remove");

        group.addOption(option1)
             .addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("s", "search", false, "Search")));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Add");
        Option option2 = new Option("r", "remove", false, "Remove");

        options.addOption(option1)
               .addOption(option2);

        String expected = "[ Options: [ short {a=Add, r=Remove} ] [ long {add=Add, remove=Remove} ]";
        assertEquals(expected, options.toString());
    }

}