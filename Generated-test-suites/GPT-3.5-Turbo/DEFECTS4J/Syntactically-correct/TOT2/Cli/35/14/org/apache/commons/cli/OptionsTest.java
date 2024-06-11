import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        String opt = "a";
        String description = "Test option";
        options.addOption(opt, description);
        assertTrue(options.hasOption(opt));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option = new Option("b", "bar", true, "Test option in group");
        group.addOption(option);
        options.addOptionGroup(group);
        assertTrue(options.getOptionGroup(option) == group);
    }

    @Test
    public void testAddOptionWithLongName() {
        Options options = new Options();
        String opt = "c";
        String longOpt = "foo";
        String description = "Test option with long name";
        options.addOption(opt, longOpt, true, description);
        assertTrue(options.hasOption(opt));
        assertTrue(options.hasLongOption(longOpt));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        String opt = "d";
        String description = "Test option";
        options.addOption(opt, description);
        assertNotNull(options.getOption(opt));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        String opt1 = "efoo";
        String opt2 = "ebar";
        String opt3 = "foo";
        options.addOption(opt1, "Test option 1");
        options.addOption(opt2, "Test option 2");
        options.addOption(opt3, "Test option 3");
        assertEquals(2, options.getMatchingOptions("e").size());
    }

    @Test
    public void testToString() {
        Options options = new Options();
        assertNotNull(options.toString());
    }

}