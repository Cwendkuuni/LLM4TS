import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();

        Option option = new Option("a", "option", true, "description");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
        assertTrue(options.hasOption("a"));
        assertFalse(options.hasOption("b"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("a"));
        assertNull(options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("abc", "option2", true, "description2");
        Option option3 = new Option("abcd", null, false, "description3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(1, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("a"));
        assertEquals(1, options.getMatchingOptions("abc").size());
        assertTrue(options.getMatchingOptions("abc").contains("abc"));
        assertEquals(2, options.getMatchingOptions("ab").size());
        assertTrue(options.getMatchingOptions("ab").contains("abc"));
        assertTrue(options.getMatchingOptions("ab").contains("abcd"));
        assertEquals(0, options.getMatchingOptions("abcde").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();

        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", false, "description2");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "option3", false, "description3")));
    }

    @Test
    public void testToString() {
        Options options = new Options();

        Option option1 = new Option("a", "option1", false, "description1");
        Option option2 = new Option("b", "option2", true, "description2");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {a=Option [ opt: a, longOpt: option1, hasArg: false, description: description1 ]} ] [ long {option1=Option [ opt: a, longOpt: option1, hasArg: false, description: description1 ], option2=Option [ opt: b, longOpt: option2, hasArg: true, description: description2 ]} ]";

        assertEquals(expected, options.toString());
    }
}