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
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        group1.addOption(option1);
        group1.addOption(option2);

        OptionGroup group2 = new OptionGroup();
        Option option3 = new Option("c", "option3", false, "Option 3");
        Option option4 = new Option("d", "option4", true, "Option 4");
        group2.addOption(option3);
        group2.addOption(option4);

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertEquals(4, options.getOptions().size());
        assertEquals(2, options.getOptionGroups().size());
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("option2"));

    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
        assertNull(options.getOption("option2"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("a", null, false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("option2"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "Option 1");
        Option option2 = new Option("b", "banana", true, "Option 2");
        Option option3 = new Option("c", "cat", false, "Option 3");
        Option option4 = new Option("d", "dog", true, "Option 4");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(1, options.getMatchingOptions("b").size());
        assertEquals(2, options.getMatchingOptions("c").size());
        assertEquals(1, options.getMatchingOptions("d").size());
        assertEquals(0, options.getMatchingOptions("e").size());
    }
}