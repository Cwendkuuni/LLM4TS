import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption_shortNameOnly() {
        String opt = "a";
        String description = "Option with short name only";

        options.addOption(opt, description);

        assertTrue(options.hasOption(opt));
    }

    @Test
    public void testAddOption_shortNameWithArgument() {
        String opt = "a";
        boolean hasArg = true;
        String description = "Option with short name and argument";

        options.addOption(opt, hasArg, description);

        assertTrue(options.hasOption(opt));
    }

    @Test
    public void testAddOption_shortAndLongNameWithArgument() {
        String opt = "a";
        String longOpt = "apple";
        boolean hasArg = true;
        String description = "Option with short and long name and argument";

        options.addOption(opt, longOpt, hasArg, description);

        assertTrue(options.hasOption(opt));
        assertTrue(options.hasOption(longOpt));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "apple", true, "Apple option");

        options.addOption(option);

        assertTrue(options.hasOption(option.getOpt()));
        assertTrue(options.hasOption(option.getLongOpt()));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", true, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "apple", true, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getRequiredOptions().contains(option1));
    }

    @Test
    public void testGetOption_matchingShortName() {
        String opt = "a";
        Option option = new Option(opt, "apple", true, "Apple option");

        options.addOption(option);

        assertEquals(option, options.getOption(opt));
    }

    @Test
    public void testGetOption_matchingLongName() {
        String longOpt = "apple";
        Option option = new Option("a", longOpt, true, "Apple option");

        options.addOption(option);

        assertEquals(option, options.getOption(longOpt));
    }

    @Test
    public void testGetOption_noMatch() {
        assertNull(options.getOption("x"));
    }

    @Test
    public void testGetMatchingOptions_perfectMatch() {
        String opt = "a";
        Option option = new Option(opt, "apple", true, "Apple option");

        options.addOption(option);

        assertEquals(Collections.singletonList(opt), options.getMatchingOptions(opt));
    }

    @Test
    public void testGetMatchingOptions_partialMatch() {
        String opt = "a";
        Option option = new Option(opt, "apple", true, "Apple option");
        Option option2 = new Option("a2", "apricot", true, "Apricot option");

        options.addOption(option);
        options.addOption(option2);

        assertEquals(Collections.singletonList(option.getOpt()), options.getMatchingOptions(opt));
    }

    @Test
    public void testGetMatchingOptions_noMatch() {
        assertEquals(Collections.emptyList(), options.getMatchingOptions("x"));
    }

    @Test
    public void testHasOption() {
        String opt = "a";
        Option option = new Option(opt, "apple", true, "Apple option");

        options.addOption(option);

        assertTrue(options.hasOption(opt));
    }

    @Test
    public void testHasLongOption() {
        String longOpt = "apple";
        Option option = new Option("a", longOpt, true, "Apple option");

        options.addOption(option);

        assertTrue(options.hasLongOption(longOpt));
    }

    @Test
    public void testHasShortOption() {
        String opt = "a";
        Option option = new Option(opt, "apple", true, "Apple option");

        options.addOption(option);

        assertTrue(options.hasShortOption(opt));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", true, "Apple option");
        Option option2 = new Option("b", "banana", false, "Banana option");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option{opt='a', longOpt='apple', hasArg=true, description='Apple option'}} ] [ long {apple=Option{opt='a', longOpt='apple', hasArg=true, description='Apple option'}, banana=Option{opt='b', longOpt='banana', hasArg=false, description='Banana option'}} ]";
        assertEquals(expected, options.toString());
    }
}