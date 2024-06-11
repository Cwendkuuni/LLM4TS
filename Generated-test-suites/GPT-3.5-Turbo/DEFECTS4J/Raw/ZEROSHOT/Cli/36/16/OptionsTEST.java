import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", true, "Remove option");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        Collection<OptionGroup> optionGroups = options.getOptionGroups();

        assertEquals(1, optionGroups.size());
        assertTrue(optionGroups.contains(optionGroup));
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("h", "help", false, "Help option");
        Option option2 = new Option("v", "verbose", false, "Verbose option");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        Collection<Option> optionCollection = options.getOptions();

        assertEquals(2, optionCollection.size());
        assertTrue(optionCollection.contains(option1));
        assertTrue(optionCollection.contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("o", "output", true, "Output option");

        Options options = new Options();
        options.addOption(option1);

        Option option = options.getOption("o");

        assertNotNull(option);
        assertEquals(option1, option);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("o", "output", true, "Output option");
        Option option2 = new Option("p", "print", true, "Print option");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        List<String> matchingOptions = options.getMatchingOptions("o");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("output"));
        assertTrue(matchingOptions.contains("o"));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o", "output", true, "Output option");

        Options options = new Options();
        options.addOption(option1);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("output"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("o", "output", true, "Output option");

        Options options = new Options();
        options.addOption(option1);

        assertTrue(options.hasLongOption("output"));
        assertFalse(options.hasLongOption("o"));
    }

    @Test
    public void testHasShortOption() {
        Option option1 = new Option("o", "output", true, "Output option");

        Options options = new Options();
        options.addOption(option1);

        assertTrue(options.hasShortOption("o"));
        assertFalse(options.hasShortOption("output"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", true, "Remove option");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        OptionGroup retrievedOptionGroup = options.getOptionGroup(option1);

        assertNotNull(retrievedOptionGroup);
        assertEquals(optionGroup, retrievedOptionGroup);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("h", "help", false, "Help option");
        Option option2 = new Option("v", "verbose", false, "Verbose option");

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {h=Option{opt=h, longOpt=help, hasArg=false, description='Help option'}} ] [ long {help=Option{opt=h, longOpt=help, hasArg=false, description='Help option'}, verbose=Option{opt=v, longOpt=verbose, hasArg=false, description='Verbose option'}} ]";

        assertEquals(expected, options.toString());
    }
}