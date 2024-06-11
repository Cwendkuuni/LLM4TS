import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("t"));
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        group1.addOption(new Option("o1", "option1", false, "Option 1"));
        group1.addOption(new Option("o2", "option2", false, "Option 2"));

        OptionGroup group2 = new OptionGroup();
        group2.addOption(new Option("o3", "option3", true, "Option 3"));
        group2.addOption(new Option("o4", "option4", true, "Option 4"));

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("o2"));
        assertTrue(options.hasOption("o3"));
        assertTrue(options.hasOption("o4"));
        assertFalse(options.hasOption("o5"));
    }

    @Test
    public void testAddOptionWithoutArg() {
        options.addOption("o", "option", false, "Option");

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("t"));
    }

    @Test
    public void testAddOptionWithArg() {
        options.addOption("o", "option", true, "Option");

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("t"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(0, options.getRequiredOptions().size());

        options.addOptionGroup(new OptionGroup()
                .addOption(option1)
                .addOption(option2));

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option1));
        assertFalse(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("o1"));
        assertEquals(option1, options.getOption("option1"));
        assertEquals(option2, options.getOption("o2"));
        assertEquals(option2, options.getOption("option2"));
        assertNull(options.getOption("t"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("o").contains("o1"));
        assertEquals(1, options.getMatchingOptions("option").size());
        assertTrue(options.getMatchingOptions("option").contains("option1"));
        assertEquals(0, options.getMatchingOptions("t").size());
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("o1"));
        assertTrue(options.hasOption("option1"));
        assertTrue(options.hasOption("o2"));
        assertTrue(options.hasOption("option2"));
        assertFalse(options.hasOption("t"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("option1"));
        assertTrue(options.hasLongOption("option2"));
        assertFalse(options.hasLongOption("option3"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasShortOption("o1"));
        assertTrue(options.hasShortOption("o2"));
        assertFalse(options.hasShortOption("o3"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group1 = new OptionGroup()
                .addOption(new Option("o1", "option1", false, "Option 1"))
                .addOption(new Option("o2", "option2", true, "Option 2"));

        OptionGroup group2 = new OptionGroup()
                .addOption(new Option("o3", "option3", false, "Option 3"))
                .addOption(new Option("o4", "option4", true, "Option 4"));

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertEquals(group1, options.getOptionGroup(group1.getOptions().get(0)));
        assertEquals(group2, options.getOptionGroup(group2.getOptions().get(0)));
        assertNull(options.getOptionGroup(new Option("o5", "option5", false, "Option 5")));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {o1=Option 1, o2=Option 2} ] [ long {option1=Option 1, option2=Option 2} ]",
                options.toString());
    }
}