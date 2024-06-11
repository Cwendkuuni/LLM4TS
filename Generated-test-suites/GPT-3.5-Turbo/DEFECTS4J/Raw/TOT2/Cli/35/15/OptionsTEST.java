import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
        assertEquals(option, options.getOption("test"));
    }

    @Test
    public void testAddOptionGroup() {
        Option option1 = new Option("a", "alpha", false, "Alpha option");
        Option option2 = new Option("b", "beta", false, "Beta option");
        Option option3 = new Option("g", "gamma", false, "Gamma option");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        options.addOptionGroup(group);

        assertFalse(options.hasOption("a"));
        assertTrue(options.hasOption("alpha"));
        assertTrue(options.hasOption("beta"));
        assertTrue(options.hasOption("gamma"));
    }

    @Test
    public void testAddOptionWithShortNameAndDescription() {
        options.addOption("d", "description");

        assertTrue(options.hasShortOption("d"));
        assertEquals("description", options.getOption("d").getDescription());
    }

    @Test
    public void testGetMatchingOptions() {
        options.addOption("o", "option", true, "Option");
        options.addOption("o1", "otherOption", true, "Other Option");

        List<String> matchingOptions = options.getMatchingOptions("o");

        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("option"));
        assertTrue(matchingOptions.contains("otherOption"));
    }

    @Test
    public void testHasLongOption() {
        options.addOption("l", "longOption", false, "Long option");

        assertTrue(options.hasLongOption("longOption"));
        assertFalse(options.hasLongOption("l"));
        assertFalse(options.hasLongOption("long"));
    }

    @Test
    public void testHasShortOption() {
        options.addOption("s", "shortOption", false, "Short option");

        assertTrue(options.hasShortOption("s"));
        assertFalse(options.hasShortOption("shortOption"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option = new Option("o", "option", false, "Option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        String expected = "[ Options: [ short {s=Option [ opt: s longOpt: null hasArg: false description: Option }] ] [ long {} ]";
        assertEquals(expected, options.toString());
    }

}