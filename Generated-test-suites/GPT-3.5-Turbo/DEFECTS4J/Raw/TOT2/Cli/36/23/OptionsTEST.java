import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "opt1", false, "Option 1");
        Option option2 = new Option("b", "opt2", false, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "opt3", false, "Option 3");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("d", "opt4", false, "Option 4");

        options.addOption(option);

        assertEquals(option, options.getOption("d"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("e", "opt5", false, "Option 5");

        options.addOption(option);

        assertTrue(options.hasOption("e"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option = new Option("f", "opt6", false, "Option 6");
        OptionGroup group = new OptionGroup();
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        Option option = new Option("g", "opt7", false, "Option 7");

        options.addOption(option);

        assertEquals("[ Options: [ short {g=Option: [ opt7 ]<Opt > } ] [ long {} ]", options.toString());
    }

    @Test
    public void testAddOptionShort() {
        Options options = new Options();

        options.addOption("h", "Option 8");

        assertTrue(options.hasOption("h"));
    }

    @Test
    public void testAddOptionShortWithArg() {
        Options options = new Options();

        options.addOption("i", true, "Option 9");

        assertTrue(options.hasOption("i"));
    }

    @Test
    public void testAddOptionLong() {
        Options options = new Options();

        options.addOption("j", "longopt", false, "Option 10");

        assertTrue(options.hasOption("j"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();

        options.addOption("k", "longopt1", false, "Option 11");

        assertTrue(options.hasLongOption("longopt1"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();

        options.addOption("l", "longopt2", false, "Option 12");

        assertTrue(options.hasShortOption("l"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("m", "longopt3", false, "Option 13");
        options.addOption("n", "longopt4", false, "Option 14");
        options.addOption("o", "longopt5", false, "Option 15");

        assertEquals(2, options.getMatchingOptions("longopt").size());
    }
}