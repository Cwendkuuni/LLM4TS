
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", false, "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);

        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(1, optionGroups.size());
        assertSame(optionGroup, optionGroups.get(0));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Option A");
        options.addOption(option);

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        assertTrue(optionList.contains(option));
    }

    @Test
    public void testAddOptionWithNameAndDescription() {
        Options options = new Options();
        options.addOption("a", "Option A", false, "Option A description");

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        Option option = optionList.iterator().next();
        assertEquals("a", option.getOpt());
        assertEquals("Option A", option.getLongOpt());
    }

    @Test
    public void testAddOptionWithNameAndHasArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        Option option = optionList.iterator().next();
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddOptionWithAllParams() {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        Collection<Option> optionList = options.getOptions();
        assertEquals(1, optionList.size());
        Option option = optionList.iterator().next();
        assertEquals("a", option.getOpt());
        assertEquals("optionA", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option A", option.getDescription());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Option A");
        options.addOption(option);

        Option foundOption = options.getOption("a");
        assertSame(option, foundOption);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("ab", false, "Option AB");

        List<String> matchingOptions = options.getMatchingOptions("a");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("a"));
        assertTrue(matchingOptions.contains("ab"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Option A");
        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Option A");
        options.addOption(option);

        assertFalse(options.hasLongOption("a"));
        assertFalse(options.hasLongOption("b"));

        options.addOption("opt", "option", false, "Option");
        assertTrue(options.hasLongOption("opt"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Option A");
        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", false, "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);

        OptionGroup foundOptionGroup1 = options.getOptionGroup(option1);
        OptionGroup foundOptionGroup2 = options.getOptionGroup(option2);

        assertSame(optionGroup, foundOptionGroup1);
        assertSame(optionGroup, foundOptionGroup2);
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", false, "Option B");
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option A, b=Option B} ] [ long {null=null, null=null} ]";
        assertEquals(expected, options.toString());
    }
}

