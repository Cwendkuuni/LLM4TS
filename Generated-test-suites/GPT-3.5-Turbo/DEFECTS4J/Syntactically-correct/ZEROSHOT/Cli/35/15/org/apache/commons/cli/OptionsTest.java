import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        options.addOptionGroup(group);

        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(1, optionGroups.size());
        assertTrue(optionGroups.contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        options.addOption(option);

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        assertTrue(optionList.contains(option));
    }

    @Test
    public void testAddOption_shortName_noArg() {
        Options options = new Options();
        options.addOption("o", "an option");

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        Option option = optionList.iterator().next();
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("an option", option.getDescription());
    }

    @Test
    public void testAddOption_shortName_withArg() {
        Options options = new Options();
        options.addOption("o", true, "an option");

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        Option option = optionList.iterator().next();
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("an option", option.getDescription());
    }

    @Test
    public void testAddOption_shortAndLongName_withArg() {
        Options options = new Options();
        options.addOption("o", "option", true, "an option");

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        Option option = optionList.iterator().next();
        assertEquals("o", option.getOpt());
        assertEquals("option", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("an option", option.getDescription());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        options.addOption(option);

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        assertTrue(optionList.contains(option));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        option.setRequired(true);
        options.addOption(option);

        List requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        options.addOption(option);

        Option retrievedOption = options.getOption("o");
        assertNotNull(retrievedOption);
        assertEquals("o", retrievedOption.getOpt());
        assertEquals("option", retrievedOption.getLongOpt());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "option 1");
        Option option2 = new Option("b", "option2", false, "option 2");
        options.addOption(option1);
        options.addOption(option2);

        List<String> matchingOptions = options.getMatchingOptions("o");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("option1"));
        assertTrue(matchingOptions.contains("option2"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("invalid"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        options.addOption(option);

        assertFalse(options.hasLongOption("o"));
        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("invalid"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        options.addOption(option);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("option"));
        assertFalse(options.hasShortOption("invalid"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "option", false, "an option");
        group.addOption(option);
        options.addOptionGroup(group);

        OptionGroup retrievedGroup = options.getOptionGroup(option);
        assertNotNull(retrievedGroup);
        assertEquals(group, retrievedGroup);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "option 1");
        Option option2 = new Option("p", "option2", true, "option 2");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {o=Option - o: option 1, p=Option - p: option 2 (required: false, hasArg: true)] [ long {option1=Option - o: option 1, option2=Option - p: option 2 (required: false, hasArg: true)]";
        assertEquals(expected, options.toString());
    }

}