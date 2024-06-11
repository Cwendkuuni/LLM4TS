import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testAddOption_shortName() {
        options.addOption("o", "Option");

        assertTrue(options.getOptions().contains(new Option("o", "Option", false, null)));
    }

    @Test
    public void testAddOption_shortName_hasArg() {
        options.addOption("o", true, "Option");

        assertTrue(options.getOptions().contains(new Option("o", null, true, "Option")));
    }

    @Test
    public void testAddOption_shortLongName_hasArg() {
        options.addOption("o", "option", true, "Option");

        assertTrue(options.getOptions().contains(new Option("o", "option", true, "Option")));
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertEquals(options.getOptions().size(), 1);
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option = new Option("o", "option", false, "Option");
        option.setRequired(true);
        options.addOption(option);

        assertEquals(options.getRequiredOptions().size(), 1);
        assertTrue(options.getRequiredOptions().contains(option.getKey()));
    }

    @Test
    public void testGetOption_shortName() {
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertNotNull(options.getOption("o"));
        assertEquals(options.getOption("o"), option);
    }

    @Test
    public void testGetOption_longName() {
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertNotNull(options.getOption("option"));
        assertEquals(options.getOption("option"), option);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o", "option", false, "Option");
        Option option2 = new Option("op", "option2", false, "Option2");
        Option option3 = new Option("opt", "option3", false, "Option3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(options.getMatchingOptions("o").size(), 1);
        assertTrue(options.getMatchingOptions("o").contains("option"));

        assertEquals(options.getMatchingOptions("op").size(), 1);
        assertTrue(options.getMatchingOptions("op").contains("option2"));

        assertEquals(options.getMatchingOptions("opt").size(), 1);
        assertTrue(options.getMatchingOptions("opt").contains("option3"));

        assertEquals(options.getMatchingOptions("option").size(), 2);
        assertTrue(options.getMatchingOptions("option").contains("option"));
        assertTrue(options.getMatchingOptions("option").contains("option2"));

        assertEquals(options.getMatchingOptions("opti").size(), 2);
        assertTrue(options.getMatchingOptions("opti").contains("option3"));
        assertTrue(options.getMatchingOptions("opti").contains("option"));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("unknown"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("o"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("option"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);
        options.addOptionGroup(group);

        assertEquals(options.getOptionGroup(option), group);
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertEquals(options.toString(), "[ Options: [ short {o=Option} ] [ long {} ]");
    }
}