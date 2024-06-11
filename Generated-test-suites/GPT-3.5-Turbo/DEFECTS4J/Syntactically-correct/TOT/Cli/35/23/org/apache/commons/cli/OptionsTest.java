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
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", false, "option1");
        Option option2 = new Option("b", false, "option2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", false, "option1");

        options.addOption(option1);

        assertTrue(options.getOptions().contains(option1));
    }

    @Test
    public void testAddOptionWithDescription() {
        options.addOption("a", "description");

        Option option1 = options.getOption("a");
        assertNotNull(option1);
        assertEquals("a", option1.getOpt());
        assertEquals("description", option1.getDescription());
    }

    @Test
    public void testAddOptionWithHasArg() {
        options.addOption("a", true, "description");

        Option option1 = options.getOption("a");
        assertNotNull(option1);
        assertTrue(option1.hasArg());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        options.addOption("a", "long", true, "description");

        Option option1 = options.getOption("a");
        assertNotNull(option1);
        assertEquals("a", option1.getOpt());
        assertEquals("long", option1.getLongOpt());
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", false, "option1");
        options.addOption(option1);

        Option option = options.getOption("a");

        assertNotNull(option);
        assertEquals("a", option.getOpt());
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", false, "option1");
        Option option2 = new Option("b", false, "option2");
        Option option3 = new Option("c", false, "option3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOpts = options.getMatchingOptions("a");

        assertEquals(1, matchingOpts.size());
        assertTrue(matchingOpts.contains("a"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", false, "option1");
        options.addOption(option1);

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "long", false, "option1");
        options.addOption(option1);

        assertTrue(options.hasLongOption("long"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("a", false, "option1");
        options.addOption(option1);

        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", false, "option1");
        group.addOption(option);
        options.addOptionGroup(group);

        OptionGroup result = options.getOptionGroup(option);

        assertEquals(group, result);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", false, "option1");
        options.addOption(option1);

        String str = options.toString();

        assertTrue(str.contains("[ Options: [ short {a=Option"));
    }
}