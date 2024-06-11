import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "alpha", true, "Description for option 1");
        Option option2 = new Option("b", "beta", false, "Description for option 2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "charlie", true, "Description for option 3");

        options.addOption(option);

        assertTrue(options.hasOption("c"));
        assertEquals(option, options.getOption("c"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("d", "delta", true, "Description for option 4");
        Option option2 = new Option("d", "dog", false, "Description for option 5");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("d").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("e", "echo", true, "Description for option 6");

        options.addOption(option);

        assertTrue(options.hasOption("e"));
        assertFalse(options.hasOption("f"));
    }

    @Test
    public void testToString() {
        Options options = new Options();

        assertEquals("[Options: [ short {} ] [ long {} ]", options.toString());
    }
}