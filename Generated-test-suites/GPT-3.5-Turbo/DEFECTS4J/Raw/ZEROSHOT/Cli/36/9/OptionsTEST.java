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
        OptionGroup group1 = new OptionGroup();
        OptionGroup group2 = new OptionGroup();

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertTrue(options.getOptionGroups().contains(group1));
        assertTrue(options.getOptionGroups().contains(group2));
    }

    @Test
    public void testAddOption_String_String() {
        String opt = "a";
        String description = "Option A";

        options.addOption(opt, description);

        assertTrue(options.hasOption(opt));
        assertEquals(description, options.getOption(opt).getDescription());
    }

    @Test
    public void testAddOption_String_boolean_String() {
        String opt = "a";
        boolean hasArg = true;
        String description = "Option A";

        options.addOption(opt, hasArg, description);

        assertTrue(options.hasOption(opt));
        assertEquals(hasArg, options.getOption(opt).hasArg());
        assertEquals(description, options.getOption(opt).getDescription());
    }

    @Test
    public void testAddOption_String_String_boolean_String() {
        String opt = "a";
        String longOpt = "optionA";
        boolean hasArg = true;
        String description = "Option A";

        options.addOption(opt, longOpt, hasArg, description);

        assertTrue(options.hasOption(opt));
        assertTrue(options.hasOption(longOpt));
        assertEquals(hasArg, options.getOption(opt).hasArg());
        assertEquals(description, options.getOption(opt).getDescription());
    }

    @Test
    public void testAddOption_Option() {
        Option opt = new Option("a", "optionA", true, "Option A");

        options.addOption(opt);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));
        assertEquals(opt, options.getOption("a"));
    }

    @Test
    public void testGetOptions() {
        Option opt1 = new Option("a", "optionA", true, "Option A");
        Option opt2 = new Option("b", "optionB", false, "Option B");

        options.addOption(opt1);
        options.addOption(opt2);

        assertTrue(options.getOptions().contains(opt1));
        assertTrue(options.getOptions().contains(opt2));
    }

    @Test
    public void testGetRequiredOptions() {
        Option opt1 = new Option("a", "optionA", true, "Option A");
        Option opt2 = new Option("b", "optionB", false, "Option B");

        options.addOption(opt1.isRequired());
        options.addOption(opt2);

        assertTrue(options.getRequiredOptions().contains("a"));
    }

    @Test
    public void testGetOption() {
        Option opt1 = new Option("a", "optionA", true, "Option A");

        options.addOption(opt1);

        assertEquals(opt1, options.getOption("a"));
        assertEquals(opt1, options.getOption("optionA"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option opt1 = new Option("a", "optionA", true, "Option A");
        Option opt2 = new Option("b", "optionB", false, "Option B");

        options.addOption(opt1);
        options.addOption(opt2);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertEquals(0, options.getMatchingOptions("c").size());
    }

    @Test
    public void testHasOption() {
        Option opt1 = new Option("a", "optionA", true, "Option A");

        options.addOption(opt1);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testHasLongOption() {
        Option opt1 = new Option("a", "optionA", true, "Option A");

        options.addOption(opt1);

        assertTrue(options.hasLongOption("optionA"));
        assertFalse(options.hasLongOption("a"));
    }

    @Test
    public void testHasShortOption() {
        Option opt1 = new Option("a", "optionA", true, "Option A");

        options.addOption(opt1);

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("optionA"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group1 = new OptionGroup();
        OptionGroup group2 = new OptionGroup();

        Option opt1 = new Option("a", "optionA", true, "Option A");
        Option opt2 = new Option("b", "optionB", false, "Option B");

        group1.addOption(opt1);
        group2.addOption(opt2);

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        assertEquals(group1, options.getOptionGroup(opt1));
        assertEquals(group2, options.getOptionGroup(opt2));
    }

    @Test
    public void testToString() {
        Option opt1 = new Option("a", "optionA", true, "Option A");
        Option opt2 = new Option("b", "optionB", false, "Option B");

        options.addOption(opt1);
        options.addOption(opt2);

        String expected = "[ Options: [ short {a=Option A, b=Option B} ] [ long {optionA=Option A, optionB=Option B} ]";

        assertEquals(expected, options.toString());
    }
}