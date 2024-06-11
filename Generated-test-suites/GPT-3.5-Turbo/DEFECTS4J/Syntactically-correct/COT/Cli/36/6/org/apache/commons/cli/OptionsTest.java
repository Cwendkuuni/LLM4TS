

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Alpha option");
        Option option2 = new Option("b", "beta", false, "Beta option");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", "beta", true, "Option B");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.hasLongOption("beta"));
        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Alpha option").setRequired(true));
        options.addOption(new Option("b", "beta", false, "Beta option").setRequired(true));
        options.addOption(new Option("c", "gamma", false, "Gamma option"));
        options.addOption(new Option("d", "delta", false, "Delta option"));

        assertEquals(2, options.getRequiredOptions().size());
        assertTrue(options.getRequiredOptions().contains("a"));
        assertTrue(options.getRequiredOptions().contains("b"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("a", "alpha", false, "Alpha option");
        options.addOption(option);

        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("alpha"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "alpha", false, "Alpha option");
        Option option2 = new Option("aa", "alpha2", false, "Another alpha option");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("alpha"));
        assertEquals(2, options.getMatchingOptions("al").size());
        assertTrue(options.getMatchingOptions("al").contains("alpha"));
        assertTrue(options.getMatchingOptions("al").contains("alpha2"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", "beta", true, "Option B");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", "beta", true, "Option B");

        String expected = "[ Options: [ short {a=Option [ opt=a, longOpt=null, hasArg=false, description=Option A ]} ] [ long {beta=Option [ opt=b, longOpt=beta, hasArg=true, description=Option B ]} ]";
        assertEquals(expected, options.toString());
    }
}
