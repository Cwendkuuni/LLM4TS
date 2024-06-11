import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("a", "Add option a", true);
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("x", "Option x", false));
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("abc", "Option abc", false);
        options.addOption("abcd", "Option abcd", false);

        assertEquals(2, options.getMatchingOptions("ab").size());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("c", "Option c", false);

        assertNotNull(options.getOption("c"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("d", "Option d", false);

        assertEquals("[ Options: [ short {d=Option d,} ] [ long {} ]", options.toString());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("e", "Option e", false);

        assertTrue(options.hasOption("e"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("f", "Option f", false);

        assertTrue(options.hasLongOption("f"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("g", "Option g", false);

        assertTrue(options.hasShortOption("g"));
    }

    @Test
    public void testAddOptionShortOnly() {
        Options options = new Options();
        options.addOption("h", "Option h");

        assertTrue(options.hasOption("h"));
    }

    @Test
    public void testAddOptionLongAndShort() {
        Options options = new Options();
        options.addOption("i", "long", true, "Option i");

        assertTrue(options.hasOption("i"));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addOption("j", "Option j", false);
        options.getOption("j").setRequired(true);

        assertEquals(1, options.getRequiredOptions().size());
    }
}