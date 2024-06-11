import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        Option option2 = new Option("opt2", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("opt1"));
        assertTrue(options.hasOption("opt2"));
        assertSame(group, options.getOptionGroup(option1));
        assertSame(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOptionWithShortName() {
        Options options = new Options();

        options.addOption("o", "Option");

        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("Option"));
        Option option = options.getOption("o");
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Option", option.getDescription());
    }

    @Test
    public void testAddOptionWithShortNameAndArg() {
        Options options = new Options();

        options.addOption("o", true, "Option with argument");

        assertTrue(options.hasOption("o"));
        assertFalse(options.hasOption("Option"));
        Option option = options.getOption("o");
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option with argument", option.getDescription());
    }

    @Test
    public void testAddOptionWithShortAndLongNameAndArg() {
        Options options = new Options();

        options.addOption("o", "option", true, "Option with argument");

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        Option option = options.getOption("o");
        assertEquals("o", option.getOpt());
        assertEquals("option", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option with argument", option.getDescription());

        option = options.getOption("option");
        assertEquals("o", option.getOpt());
        assertEquals("option", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option with argument", option.getDescription());
    }

    @Test
    public void testAddOption() {
        Options options = new Options();

        Option option = new Option("o", "option", true, "Option with argument");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertSame(option, options.getOption("o"));
        assertSame(option, options.getOption("option"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        Option option2 = new Option("opt2", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        Option option2 = new Option("opt2", "option2", false, "Option 2");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        List requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertTrue(requiredOptions.contains(option1));
        assertFalse(requiredOptions.contains(option2));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertSame(option1, options.getOption("o"));
        assertSame(option1, options.getOption("option1"));
        assertSame(option2, options.getOption("option2"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        Option option2 = new Option("opt2", "option2", false, "Option 2");
        Option option3 = new Option("foo", "foo-option", false, "Foo option");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("opt1"));
        assertTrue(matchingOptions.contains("option1"));

        matchingOptions = options.getMatchingOptions("foo");
        assertEquals(1, matchingOptions.size());
        assertTrue(matchingOptions.contains("foo-option"));

        matchingOptions = options.getMatchingOptions("bar");
        assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("option"));
        assertFalse(options.hasOption("foo"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("opt", "option", false, "Option");
        options.addOption(option);

        assertTrue(options.hasLongOption("option"));
        assertFalse(options.hasLongOption("opt"));
        assertFalse(options.hasLongOption("foo"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("opt", "option", false, "Option");
        options.addOption(option);

        assertFalse(options.hasShortOption("option"));
        assertTrue(options.hasShortOption("opt"));
        assertFalse(options.hasShortOption("foo"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        Option option2 = new Option("opt2", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);

        assertSame(group, options.getOptionGroup(option1));
        assertSame(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("opt3", "option3", false, "Option 3")));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        Option option2 = new Option("opt2", "option2", false, "Option 2");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals("[ Options: [ short {opt1=opt1, opt2=opt2} ] [ long {option1=opt1, option2=opt2} ]", options.toString());
    }
}