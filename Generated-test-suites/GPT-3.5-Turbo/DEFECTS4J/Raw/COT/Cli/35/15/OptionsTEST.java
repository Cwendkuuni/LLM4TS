import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an OptionGroup
        OptionGroup group = new OptionGroup();
        // ... Add options to the group
        options.addOptionGroup(group);

        // Check if the OptionGroup was added correctly
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(group));
    }

    @Test
    public void testAddOption() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option
        Option option = new Option("opt", "description");
        options.addOption(option);

        // Check if the Option was added correctly
        Collection<Option> optionList = options.getOptions();
        assertTrue(optionList.contains(option));
    }

    @Test
    public void testGetOption() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option
        Option option = new Option("opt", "description");
        options.addOption(option);

        // Get the Option using the short name
        Option retrievedOption = options.getOption("opt");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testHasOption() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option
        Option option = new Option("opt", "description");
        options.addOption(option);

        // Check if the Option is present
        boolean hasOption = options.hasOption("opt");
        assertTrue(hasOption);
    }

    @Test
    public void testHasLongOption() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option
        Option option = new Option("opt", "description");
        options.addOption(option);

        // Check if the Long Option is present
        boolean hasLongOption = options.hasLongOption("opt");
        assertTrue(hasLongOption);
    }

    @Test
    public void testHasShortOption() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option
        Option option = new Option("opt", "description");
        options.addOption(option);

        // Check if the Short Option is present
        boolean hasShortOption = options.hasShortOption("opt");
        assertTrue(hasShortOption);
    }

    @Test
    public void testGetOptionGroup() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option
        Option option = new Option("opt", "description");
        options.addOption(option);

        // Create and add an OptionGroup
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        options.addOptionGroup(group);

        // Get the OptionGroup for the Option
        OptionGroup retrievedGroup = options.getOptionGroup(option);
        assertEquals(group, retrievedGroup);
    }

    @Test
    public void testToString() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option
        Option option = new Option("opt", "description");
        options.addOption(option);

        // Convert Options to String
        String optionsString = options.toString();

        // Check if the String representation is correct
        assertEquals("[ Options: [ short {opt=Option(opt: null, longOpt: null, hasArg: false, description: description, required: false, argName: null)} ] [ long {} ]", optionsString);
    }

    @Test
    public void testAddOptionWithArg() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option with an argument
        Option option = new Option("opt", true, "description");
        options.addOption(option);

        // Check if the Option was added correctly
        Collection<Option> optionList = options.getOptions();
        assertTrue(optionList.contains(option));

        // Check if the Option has an argument
        assertTrue(option.hasArg());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add an Option with a long option
        Option option = new Option("opt", "longOpt", true, "description");
        options.addOption(option);

        // Check if the Option was added correctly
        Collection<Option> optionList = options.getOptions();
        assertTrue(optionList.contains(option));

        // Check if the Option has a long option
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testAddOptionWithRequired() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add a required Option
        Option option = new Option("opt", "description");
        option.setRequired(true);
        options.addOption(option);

        // Check if the Option was added correctly
        Collection<Option> optionList = options.getOptions();
        assertTrue(optionList.contains(option));

        // Check if the Option is required
        assertTrue(option.isRequired());
    }

    @Test
    public void testAddOptionGroupWithRequiredOption() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add a required OptionGroup
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        options.addOptionGroup(group);

        // Check if the OptionGroup was added correctly
        Collection<OptionGroup> optionGroups = options.getOptionGroups();
        assertTrue(optionGroups.contains(group));

        // Check if the OptionGroup is required
        assertTrue(group.isRequired());
    }

    @Test
    public void testGetMatchingOptions() {
        // Create a new Options instance
        Options options = new Options();

        // Create and add Options with long options
        Option option1 = new Option("o1", "option1", true, "description1");
        Option option2 = new Option("o2", "option2", true, "description2");
        Option option3 = new Option("o3", "option3", true, "description3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        // Get the Options matching a partial name
        List<String> matchingOptions = options.getMatchingOptions("option");
        assertEquals(3, matchingOptions.size());
        assertTrue(matchingOptions.contains("option1"));
        assertTrue(matchingOptions.contains("option2"));
        assertTrue(matchingOptions.contains("option3"));
    }

}