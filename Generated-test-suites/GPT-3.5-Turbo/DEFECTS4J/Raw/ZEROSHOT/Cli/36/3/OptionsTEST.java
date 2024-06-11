import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption_shortName() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testAddOption_longName() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasOption("option"));
        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testAddOption_shortNameWithArgument() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option with argument");

        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testAddOption_longNameWithArgument() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option with argument");

        options.addOption(option);

        assertTrue(options.hasOption("option"));
        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);
        options.getRequiredOptions().add(option2);

        assertEquals(1, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption_existingOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");

        options.addOption(option);

        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testGetOption_nonExistingOption() {
        Options options = new Options();

        assertNull(options.getOption("option"));
    }

    @Test
    public void testGetMatchingOptions_perfectMatch() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");

        options.addOption(option);

        assertEquals(1, options.getMatchingOptions("option").size());
        assertTrue(options.getMatchingOptions("option").contains("option"));
    }

    @Test
    public void testGetMatchingOptions_partialMatch() {
        Options options = new Options();
        Option option1 = new Option("one", "option1", false, "Option 1");
        Option option2 = new Option("two", null, false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("o").size());
        assertTrue(options.getMatchingOptions("o").contains("one"));
        assertTrue(options.getMatchingOptions("o").contains("two"));
    }

    @Test
    public void testHasOption_existingOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testHasOption_nonExistingOption() {
        Options options = new Options();

        assertFalse(options.hasOption("option"));
    }

    @Test
    public void testHasLongOption_existingOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testHasLongOption_nonExistingOption() {
        Options options = new Options();

        assertFalse(options.hasLongOption("option"));
    }

    @Test
    public void testHasShortOption_existingOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testHasShortOption_nonExistingOption() {
        Options options = new Options();

        assertFalse(options.hasShortOption("o"));
    }

    @Test
    public void testGetOptionGroup_existingOption() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "Option");
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testGetOptionGroup_nonExistingOption() {
        Options options = new Options();

        assertNull(options.getOptionGroup(new Option("o", "option", false, "Option")));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {o1=Option 1, o2=Option 2} ] [ long {} ]", options.toString());
    }
}