import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "desc1");
        Option option2 = new Option("b", "option2", false, "desc2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "option", false, "desc");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("option"));
        assertTrue(options.hasShortOption("a"));
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option"));
        assertTrue(options.getOptionGroup(option) == null);
    }

    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", "option", true, "desc");
        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("option"));
        assertTrue(options.hasShortOption("a"));
        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("option"));
        assertTrue(options.getOptionGroup(option) == null);
    }

    @Test
    public void testAddOptionGroupWithRequiredOption() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "desc1");
        Option option2 = new Option("b", "option2", false, "desc2");
        group.setRequired(true);
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getRequiredOptions().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "desc1");
        Option option2 = new Option("b", "option2", false, "desc2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(0, options.getMatchingOptions("c").size());
        assertEquals(1, options.getMatchingOptions("o").size());
        assertEquals("option1", options.getMatchingOptions("o").get(0));
        assertEquals(2, options.getMatchingOptions("option").size());
        assertTrue(options.getMatchingOptions("option").contains("option1"));
        assertTrue(options.getMatchingOptions("option").contains("option2"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "desc1");
        Option option2 = new Option("b", "option2", false, "desc2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {a=option1, b=option2} ] [ long {option1, option2} ]", options.toString());
    }
}