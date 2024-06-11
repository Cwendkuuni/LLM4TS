import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("t"));
        assertEquals(option, options.getOption("t"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
        assertTrue(options.getOptionGroup(option1).equals(group));
        assertTrue(options.getOptionGroup(option2).equals(group));
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        options.addOption("l", "long", true, "Long option");
        assertTrue(options.hasLongOption("long"));
        assertTrue(options.hasShortOption("l"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");
        options.addOption("o", "output", true, "Output option");
        List<String> matchingOptions = options.getMatchingOptions("o");
        assertEquals(1, matchingOptions.size());
        assertEquals("output", matchingOptions.get(0));
    }

}