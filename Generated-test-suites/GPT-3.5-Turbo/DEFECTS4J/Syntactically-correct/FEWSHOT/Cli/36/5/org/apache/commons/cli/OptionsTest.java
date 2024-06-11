import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "aaa", false, "option a"));
        group.addOption(new Option("b", "bbb", false, "option b"));

        Options options = new Options();
        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();

        options.addOption("a", "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", "ccc", true, "option c");

        assertTrue(options.getOptions().contains(new Option("a", null, false, "option a")));
        assertTrue(options.getOptions().contains(new Option("b", null, true, "option b")));
        assertTrue(options.getOptions().contains(new Option("c", "ccc", true, "option c")));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option optionA = new Option("a", "aaa", false, "option a");
        Option optionB = new Option("b", "bbb", false, "option b");

        options.addOption(optionA);
        options.addOption(optionB);

        assertEquals(optionA, options.getOption("aaa"));
        assertEquals(optionB, options.getOption("bbb"));
        assertNull(options.getOption("ccc"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption(new Option("aaa", "option aaa"));
        options.addOption(new Option("abb", "option abb"));
        options.addOption(new Option("ac", "option ac"));

        assertEquals(1, options.getMatchingOptions("aaa").size());
        assertEquals("aaa", options.getMatchingOptions("aaa").get(0));
        assertEquals(1, options.getMatchingOptions("abb").size());
        assertEquals("abb", options.getMatchingOptions("abb").get(0));
        assertEquals(0, options.getMatchingOptions("b").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", true, "option b");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("option a"));
        assertFalse(options.hasOption("c"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("a", "aaa", false, "option a");

        assertTrue(options.hasLongOption("aaa"));
        assertFalse(options.hasLongOption("bbb"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("a", "aaa", false, "option a");

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "aaa", false, "option a"));
        group.addOption(new Option("b", "bbb", false, "option b"));

        Options options = new Options();
        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(new Option("a", null, false, "option a")));
        assertEquals(group, options.getOptionGroup(new Option("b", null, false, "option b")));
        assertNull(options.getOptionGroup(new Option("c", null, false, "option c")));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("a", "aaa", false, "option a");
        options.addOption("b", "bbb", false, "option b");
        options.addOption("c", "ccc", false, "option c");

        String expected = "[ Options: [ short {a=Option{opt='a', longOpt='aaa', hasArg=false, description='option a'}, " +
                          "b=Option{opt='b', longOpt='bbb', hasArg=false, description='option b'}, " +
                          "c=Option{opt='c', longOpt='ccc', hasArg=false, description='option c'}} ]" +
                          " [ long {} ]]";
        assertEquals(expected, options.toString());
    }
}