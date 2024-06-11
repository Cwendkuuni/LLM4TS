import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "apple", false, "Description");
        group.addOption(option);
        options.addOptionGroup(group);

        assertTrue("Options should contain the added option group", options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOptionNoArg() {
        options.addOption("a", "Description");

        assertTrue("Options should contain the added option", options.hasOption("a"));
        assertFalse("Added option should not require an argument", options.getOption("a").hasArg());
    }

    @Test
    public void testAddOptionWithArg() {
        options.addOption("a", true, "Description");

        assertTrue("Options should contain the added option", options.hasOption("a"));
        assertTrue("Added option should require an argument", options.getOption("a").hasArg());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        options.addOption("a", "apple", true, "Description");

        assertTrue("Options should contain the added option", options.hasOption("a"));
        assertTrue("Options should contain the added option", options.hasOption("apple"));
        assertTrue("Added option should require an argument", options.getOption("a").hasArg());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", false, "Description 1");
        Option option2 = new Option("b", "banana", true, "Description 2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue("Options should contain the added options", options.getOptions().contains(option1));
        assertTrue("Options should contain the added options", options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "apple", false, "Description 1");
        option1.setRequired(true);
        Option option2 = new Option("b", "banana", true, "Description 2");
        option2.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        assertTrue("Required options should be contained in the required options list", options.getRequiredOptions().contains(option1));
        assertTrue("Required options should be contained in the required options list", options.getRequiredOptions().contains(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("a", "apple", false, "Description 1");
        Option option2 = new Option("b", "banana", true, "Description 2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals("Options should return the option with the specified short name", option1, options.getOption("a"));
        assertEquals("Options should return the option with the specified long name", option2, options.getOption("banana"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "apple", false, "Description 1");
        Option option2 = new Option("ab", "banana", true, "Description 2");
        Option option3 = new Option("c", "carrot", true, "Description 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals("Options should return the options matching the partial name", Collections.singletonList("apple"), options.getMatchingOptions("a"));
        assertEquals("Options should return the options matching the partial name", Collections.singletonList("banana"), options.getMatchingOptions("ban"));
        assertTrue("Options should return an empty list if no options match the partial name", options.getMatchingOptions("d").isEmpty());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "apple", false, "Description");
        options.addOption(option);

        assertTrue("Options should contain the added option", options.hasOption("a"));
        assertTrue("Options should contain the added option", options.hasOption("apple"));
        assertFalse("Options should not contain an option that was not added", options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "apple", false, "Description");
        options.addOption(option);

        assertFalse("Options should not contain a long option that was not added", options.hasLongOption("b"));
        assertTrue("Options should contain the added long option", options.hasLongOption("apple"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "apple", false, "Description");
        options.addOption(option);

        assertFalse("Options should not contain a short option that was not added", options.hasShortOption("b"));
        assertTrue("Options should contain the added short option", options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Description 1");
        Option option2 = new Option("b", "banana", true, "Description 2");

        group.addOption(option1);
        group.addOption(option2);
        
        options.addOptionGroup(group);

        assertEquals("Options should return the option group that the specified option belongs to", group, options.getOptionGroup(option1));
        assertEquals("Options should return the option group that the specified option belongs to", group, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "Description 1");
        Option option2 = new Option("b", "banana", true, "Description 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        String expected = "[ Options: [ short {a=Option(option: a apple v=null), b=Option(option: b banana v=null)} ] [ long {apple=Option(option: a apple v=null), banana=Option(option: b banana v=null)} ]";
        
        assertEquals("Options should return the expected string representation", expected, options.toString());
    }
}