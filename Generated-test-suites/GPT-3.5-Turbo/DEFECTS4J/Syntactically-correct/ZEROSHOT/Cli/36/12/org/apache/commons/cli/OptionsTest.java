import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", true, "Add option");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("add"));
        assertFalse(options.hasShortOption("a"));
        assertTrue(options.getOption("a").equals(option));
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "add", true, "Add option");
        Option option2 = new Option("r", "remove", true, "Remove option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("r"));
        assertTrue(options.hasLongOption("add"));
        assertTrue(options.hasLongOption("remove"));
        assertFalse(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("r"));
        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
    }

    @Test
    public void testAddOptionWithDescriptionOnly() {
        options.addOption("h", "help", false, "Help option");
        assertTrue(options.hasOption("h"));
        assertFalse(options.hasLongOption("help"));
        assertTrue(options.hasShortOption("h"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", true, "Add option");
        Option option2 = new Option("r", "remove", true, "Remove option");
        options.addOption(option1);
        options.addOption(option2);
        List<Option> optionList = options.getOptions();
        assertEquals(2, optionList.size());
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "add", true, "Add option");
        Option option2 = new Option("ad", "admin", true, "Admin option");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("a");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("add"));
        assertTrue(matchingOptions.contains("admin"));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "add", true, "Add option");
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        group.addOption(option1);
        options.addOptionGroup(group);
        List requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(group));
    }

}