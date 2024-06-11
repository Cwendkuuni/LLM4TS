

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option 1", false, "description");
        Option option2 = new Option("b", "option 2", false, "description");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1", false, "description");
        Option option2 = new Option("b", "option 2", true, "description");
        options.addOption(option1);
        options.addOption(option2);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("option 1"));
        assertTrue(options.hasLongOption("option 2"));
        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertEquals(option1, options.getOption("a"));
        assertEquals(option2, options.getOption("b"));
    }

    @Test
    public void testAddOptionWithDescription() {
        Options options = new Options();
        options.addOption("a", "option 1");
        options.addOption("b", false, "description");
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("option 1"));
        assertTrue(options.hasShortOption("a"));

        List<String> matchingOptions = options.getMatchingOptions("o");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("option 1"));
        assertTrue(matchingOptions.contains("option 2"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1", false, "description");
        Option option2 = new Option("b", "option 2", true, "description");
        options.addOption(option1);
        options.addOption(option2);
        Collection<Option> optionList = options.getOptions();
        assertEquals(2, optionList.size());
        assertTrue(optionList.contains(option1));
        assertTrue(optionList.contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option 1", false, "description");
        Option option2 = new Option("b", "option 2", false, "description");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        List requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(group));
    }

}

