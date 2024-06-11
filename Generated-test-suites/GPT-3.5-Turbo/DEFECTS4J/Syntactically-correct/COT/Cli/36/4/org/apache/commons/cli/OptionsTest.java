import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "description");
        Option option2 = new Option("p", "option2", true, "description");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
        assertTrue(options.getRequiredOptions().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "description");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.getOption("o").equals(option));
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("option"));
        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testAddOption_shortNameOnly() {
        Options options = new Options();
        options.addOption("o", "description");

        assertTrue(options.getOptions().size() == 1);
        assertTrue(options.getOption("o") != null);
        assertTrue(options.hasOption("o"));
        assertTrue(!options.hasLongOption("o"));
        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testAddOption_shortNameWithArg() {
        Options options = new Options();
        options.addOption("o", true, "description");

        assertTrue(options.getOptions().size() == 1);
        assertTrue(options.getOption("o") != null);
        assertTrue(options.hasOption("o"));
        assertTrue(!options.hasLongOption("o"));
        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testAddOption_shortAndLongName() {
        Options options = new Options();
        options.addOption("o", "option", true, "description");

        assertTrue(options.getOptions().size() == 1);
        assertTrue(options.getOption("o") != null);
        assertTrue(options.getOption("option") != null);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertTrue(options.hasLongOption("option"));
        assertTrue(options.hasShortOption("o"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("o", "option", false, "description");

        assertTrue(options.getMatchingOptions("o").size() == 1);
        assertTrue(options.getMatchingOptions("opt").size() == 1);
        assertTrue(options.getMatchingOptions("o1").isEmpty());
    }
}