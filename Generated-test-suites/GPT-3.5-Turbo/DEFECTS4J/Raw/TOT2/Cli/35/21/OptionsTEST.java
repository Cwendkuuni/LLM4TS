import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", "alpha", true, "Description");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("alpha"));
        assertTrue(options.getOption("a") == option);
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", true, "Description");
        Option option2 = new Option("b", "beta", false, "Description");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(group));
    }

    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();

        options.addOption("a", "Description");

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionWithShortNameAndArgument() {
        Options options = new Options();

        options.addOption("a", true, "Description");

        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        Options options = new Options();

        options.addOption("a", "alpha", true, "Description");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("alpha"));
    }

    @Test
    public void testAddRequiredOption() {
        Options options = new Options();

        options.addOption("a", true, "Description");
        options.addOption("b", true, "Description");
        options.addOption("c", false, "Description");
        options.addOption("d", false, "Description");

        List requiredOptions = options.getRequiredOptions();
        assertTrue(requiredOptions.contains("a"));
        assertTrue(requiredOptions.contains("b"));
        assertFalse(requiredOptions.contains("c"));
        assertFalse(requiredOptions.contains("d"));
    }

    @Test
    public void testGetOptionMatchingOptions() {
        Options options = new Options();
        options.addOption("a", "alpha", true, "Description");
        options.addOption("b", "beta", false, "Description");
        options.addOption("c", "charlie", true, "Description");

        List<String> matchingOptions = options.getMatchingOptions("al");
        assertTrue(matchingOptions.contains("alpha"));

        matchingOptions = options.getMatchingOptions("b");
        assertTrue(matchingOptions.contains("beta"));
    }

}