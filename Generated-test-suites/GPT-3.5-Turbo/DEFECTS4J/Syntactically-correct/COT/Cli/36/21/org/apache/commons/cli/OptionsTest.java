
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Option");
        option.setRequired(true);

        options.addOption(option);

        assertTrue(options.getRequiredOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Option");

        options.addOption(option);

        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("opt").size());
        assertEquals(2, options.getMatchingOptions("option").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Option");

        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertEquals(optionGroup, options.getOptionGroup(option1));
        assertEquals(optionGroup, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "Option");

        options.addOption(option);

        assertEquals("[ Options: [ short {a=Option} ] [ long {option=Option} ]", options.toString());
    }
}
