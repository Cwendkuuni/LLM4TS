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
        group.setRequired(true);

        Option option1 = new Option("a", "option1", true, "description1");
        Option option2 = new Option("b", "option2", false, "description2");

        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.getRequiredOptions().contains(group));
    }

    @Test
    public void testAddOptionStringString() {
        options.addOption("a", "description");

        assertEquals(1, options.getOptions().size());
        assertFalse(options.getOptions().isEmpty());
    }

    @Test
    public void testAddOptionStringBooleanString() {
        options.addOption("a", true, "description");

        assertEquals(1, options.getOptions().size());
        assertTrue(options.getOptions().contains(new Option("a", null, true, "description")));
    }

    @Test
    public void testAddOptionStringStringBooleanString() {
        options.addOption("a", "b", true, "description");

        assertEquals(1, options.getOptions().size());
        assertTrue(options.getOptions().contains(new Option("a", "b", true, "description")));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "b", true, "description");

        options.addOption(option);

        assertEquals(1, options.getOptions().size());
        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOptions() {
        assertEquals(0, options.getOptions().size());
    }

    @Test
    public void testGetRequiredOptions() {
        assertEquals(0, options.getRequiredOptions().size());
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "b", true, "description");

        options.addOption(option);

        assertEquals(option, options.getOption("a"));
        assertEquals(option, options.getOption("b"));
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("a", "aaa", true, "description1");
        Option option2 = new Option("b", "bbb", true, "description2");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("a").size());
        assertTrue(options.getMatchingOptions("a").contains("aaa"));
        assertTrue(options.getMatchingOptions("a").contains("aaa"));

        assertEquals(0, options.getMatchingOptions("c").size());
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "b", true, "description");

        options.addOption(option);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Option option1 = new Option("a", "aaa", true, "description1");
        Option option2 = new Option("b", "bbb", true, "description2");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.hasLongOption("aaa"));
        assertTrue(options.hasLongOption("bbb"));
        assertFalse(options.hasLongOption("ccc"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "b", true, "description");

        options.addOption(option);

        assertTrue(options.hasShortOption("a"));
        assertTrue(options.hasShortOption("b"));
        assertFalse(options.hasShortOption("c"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "aaa", true, "description1");
        Option option2 = new Option("b", "bbb", true, "description2");

        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
        assertNull(options.getOptionGroup(new Option("c", "ccc", true, "description3")));
    }

    @Test
    public void testToString() {
        assertEquals("[ Options: [ short {} ] [ long {} ]]", options.toString());
    }
}