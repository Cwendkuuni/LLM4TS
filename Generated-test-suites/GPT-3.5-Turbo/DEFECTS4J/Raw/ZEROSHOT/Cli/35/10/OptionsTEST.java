import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import java.util.Collection;
import java.util.List;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        Options options = new Options();
        options.addOptionGroup(group);
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(1, optionGroups.size());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option", false, "Description");
        Options options = new Options();
        options.addOption(option);
        Collection<Option> optionCollection = options.getOptions();
        assertEquals(1, optionCollection.size());
    }

    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();
        options.addOption("a", "Description");
        Collection<Option> optionCollection = options.getOptions();
        assertEquals(1, optionCollection.size());
    }

    @Test
    public void testAddOptionWithShortNameAndArg() {
        Options options = new Options();
        options.addOption("a", true, "Description");
        Collection<Option> optionCollection = options.getOptions();
        assertEquals(1, optionCollection.size());
    }

    @Test
    public void testAddOptionWithShortAndLongName() {
        Options options = new Options();
        options.addOption("a", "option", false, "Description");
        Collection<Option> optionCollection = options.getOptions();
        assertEquals(1, optionCollection.size());
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "option", false, "Description");
        Options options = new Options();
        options.addOption(option);
        Option result = options.getOption("a");
        assertNotNull(result);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "Description");
        Option option2 = new Option("b", "banana", false, "Description");
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        List<String> result = options.getMatchingOptions("a");
        assertEquals(1, result.size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "option", false, "Description");
        Options options = new Options();
        options.addOption(option);
        boolean result = options.hasOption("a");
        assertTrue(result);
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "option", false, "Description");
        Options options = new Options();
        options.addOption(option);
        boolean result = options.hasLongOption("option");
        assertTrue(result);
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "option", false, "Description");
        Options options = new Options();
        options.addOption(option);
        boolean result = options.hasShortOption("a");
        assertTrue(result);
    }

    @Test
    public void testGetOptionGroup() {
        Option option = new Option("a", "option", false, "Description");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        Options options = new Options();
        options.addOptionGroup(group);
        OptionGroup result = options.getOptionGroup(option);
        assertNotNull(result);
    }

}