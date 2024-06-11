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
        // Test normal case with required option group
        Option option1 = new Option("a", "alpha", false, "Option a");
        Option option2 = new Option("b", "beta", false, "Option b");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroup(option1) == optionGroup);
        assertTrue(options.getOptionGroup(option2) == optionGroup);
        assertTrue(options.getOption("a") == option1);
        assertTrue(options.getOption("alpha") == option1);
        assertTrue(options.getOption("b") == option2);
        assertTrue(options.getOption("beta") == option2);
        assertTrue(options.getOption("c") == null);
        assertTrue(options.getOption("gamma") == null);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("alpha"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("beta"));
        assertFalse(options.hasOption("c"));
        assertFalse(options.hasOption("gamma"));
    }

    @Test
    public void testAddOption() {
        // Test normal case with option having both short and long names
        options.addOption("a", "alpha", true, "Option a");

        assertTrue(options.getOption("a").getOpt().equals("a"));
        assertTrue(options.getOption("alpha").getLongOpt().equals("alpha"));
        assertTrue(options.getOption("b") == null);
        assertTrue(options.getOption("beta") == null);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("alpha"));
        assertFalse(options.hasOption("b"));
        assertFalse(options.hasOption("beta"));

        // Test case with option having only short name
        options.addOption("b", false, "Option b");

        assertTrue(options.getOption("b").getOpt().equals("b"));
        assertTrue(options.getOption("a").getLongOpt().equals("alpha"));
        assertTrue(options.getOption("c") == null);
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));

        // Test case with option having only long name
        options.addOption("c", true, "Option c");

        assertTrue(options.getOption("c").getOpt().equals("c"));
        assertTrue(options.getOption("a").getLongOpt().equals("alpha"));
        assertTrue(options.getOption("b").getLongOpt() == null);
        assertTrue(options.hasOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        // Test case with multiple matching options
        options.addOption("a", "alpha", true, "Option Alpha");
        options.addOption("b", "beta", false, "Option Beta");
        options.addOption("c", "gamma", false, "Option Gamma");

        assertTrue(options.getMatchingOptions("a").contains("alpha"));
        assertTrue(options.getMatchingOptions("a").size() == 1);
        assertTrue(options.getMatchingOptions("g").contains("gamma"));
        assertTrue(options.getMatchingOptions("g").size() == 1);
        assertTrue(options.getMatchingOptions("al").contains("alpha"));
        assertTrue(options.getMatchingOptions("al").size() == 1);
        assertTrue(options.getMatchingOptions("b").contains("beta"));
        assertTrue(options.getMatchingOptions("b").size() == 1);
        assertTrue(options.getMatchingOptions("o").size() == 0);
    }

    @Test
    public void testGetOption() {
        // Test normal case with short name
        Option option1 = new Option("a", "alpha", true, "Option a");
        options.addOption(option1);

        assertTrue(options.getOption("a") == option1);
        assertTrue(options.getOption("-a") == option1);
        assertTrue(options.getOption("-A") == option1);
        assertTrue(options.getOption("alpha") == option1);
        assertTrue(options.getOption("-alpha") == option1);
        assertTrue(options.getOption("-ALPHA") == option1);

        // Test normal case with long name
        Option option2 = new Option("b", "beta", true, "Option b");
        options.addOption(option2);

        assertTrue(options.getOption("b") == option2);
        assertTrue(options.getOption("-b") == option2);
        assertTrue(options.getOption("-B") == option2);
        assertTrue(options.getOption("beta") == option2);
        assertTrue(options.getOption("-beta") == option2);
        assertTrue(options.getOption("-BETA") == option2);

        // Test case with option not found
        assertNull(options.getOption("c"));
        assertNull(options.getOption("-c"));
        assertNull(options.getOption("-C"));
        assertNull(options.getOption("gamma"));
        assertNull(options.getOption("-gamma"));
        assertNull(options.getOption("-GAMMA"));
    }

    @Test
    public void testHasOption() {
        // Test normal case with short name
        Option option1 = new Option("a", "alpha", true, "Option a");
        options.addOption(option1);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("-a"));
        assertTrue(options.hasOption("-A"));
        assertTrue(options.hasOption("alpha"));
        assertTrue(options.hasOption("-alpha"));
        assertTrue(options.hasOption("-ALPHA"));

        // Test normal case with long name
        Option option2 = new Option("b", "beta", true, "Option b");
        options.addOption(option2);

        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("-b"));
        assertTrue(options.hasOption("-B"));
        assertTrue(options.hasOption("beta"));
        assertTrue(options.hasOption("-beta"));
        assertTrue(options.hasOption("-BETA"));

        // Test case with option not found
        assertFalse(options.hasOption("c"));
        assertFalse(options.hasOption("-c"));
        assertFalse(options.hasOption("-C"));
        assertFalse(options.hasOption("gamma"));
        assertFalse(options.hasOption("-gamma"));
        assertFalse(options.hasOption("-GAMMA"));
    }

    @Test
    public void testOptionGroup() {
        Option option1 = new Option("a", "alpha", true, "Option a");
        Option option2 = new Option("b", "beta", true, "Option b");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertTrue(options.getOptionGroup(option1) == optionGroup);
        assertTrue(options.getOptionGroup(option2) == optionGroup);

        assertTrue(options.hasOption("a"));
        assertFalse(options.getOption("a").isRequired());
        assertFalse(options.hasOption("b"));
        assertFalse(options.getOption("b").isRequired());

        assertFalse(options.hasOption("c"));

        // Test required option
        Option option3 = new Option("c", "gamma", true, "Option c");
        option3.setRequired(true);

        optionGroup.addOption(option3);

        assertTrue(options.hasOption("c"));
        assertTrue(options.getOption("c").isRequired());

        assertFalse(options.hasOption("d"));

        // Test removing option group
        options.removeOptionGroup(optionGroup);

        assertFalse(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
        assertFalse(options.hasOption("c"));

        assertTrue(options.getOption("a") == null);
        assertTrue(options.getOption("b") == null);
        assertTrue(options.getOption("c") == null);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "alpha", false, "Option a");
        Option option2 = new Option("b", "beta", true, "Option b");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a:Option a, b:Option b} ] [ long {}]";
        String actual = options.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testRequiredOptions() {
        Option option1 = new Option("a", "alpha", true, "Option a");
        Option option2 = new Option("b", "beta", false, "Option b");

        options.addOption(option1);
        options.addOption(option2);

        options.getOption("a").setRequired(true);

        assertTrue(options.getRequiredOptions().contains("a"));
        assertTrue(options.getRequiredOptions().size() == 1);
        assertFalse(options.getRequiredOptions().contains("b"));

        options.getOption("a").setRequired(false);

        assertFalse(options.getRequiredOptions().contains("a"));
        assertTrue(options.getRequiredOptions().size() == 0);
    }
}