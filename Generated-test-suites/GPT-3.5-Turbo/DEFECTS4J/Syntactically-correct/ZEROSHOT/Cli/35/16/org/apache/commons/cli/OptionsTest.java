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
    public void testAddOption() {
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("option"));
        assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Option 1 description");
        Option option2 = new Option("o2", "option2", true, "Option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("o2"));
        assertEquals(group, options.getOptionGroup(option1));
    }

    @Test
    public void testAddOptionShortName() {
        options.addOption("o", "Option description");
        assertTrue(options.hasOption("o"));
        assertFalse(options.hasLongOption("Option description"));
        assertNull(options.getOption("Option description"));
    }

    @Test
    public void testAddOptionShortNameHasArg() {
        options.addOption("o", true, "Option description");
        assertTrue(options.hasOption("o"));
        assertFalse(options.hasLongOption("Option description"));
        assertNull(options.getOption("Option description"));
    }

    @Test
    public void testAddOptionShortAndLongNameHasArg() {
        options.addOption("o", "option", true, "Option description");
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("option"));
        assertNull(options.getOption("Option description"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1 description");
        Option option2 = new Option("o2", "option2", true, "Option 2 description");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1 description");
        Option option2 = new Option("o2", "option2", true, "Option 2 description");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("o1"));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        assertEquals(option, options.getOption("o"));
        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("opt1", "option1", true, "Option 1 description");
        Option option2 = new Option("opt2", "option2", true, "Option 2 description");
        options.addOption(option1);
        options.addOption(option2);
        assertEquals(2, options.getMatchingOptions("opt").size());
        assertTrue(options.getMatchingOptions("opt").contains("option1"));
        assertTrue(options.getMatchingOptions("opt").contains("option2"));
        assertEquals(1, options.getMatchingOptions("option").size());
        assertTrue(options.getMatchingOptions("option").contains("option1"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("unknown"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("unknown"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);
        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("unknown"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Option 1 description");
        Option option2 = new Option("o2", "option2", true, "Option 2 description");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option1));
        assertNull(options.getOptionGroup(option2));
    }
}