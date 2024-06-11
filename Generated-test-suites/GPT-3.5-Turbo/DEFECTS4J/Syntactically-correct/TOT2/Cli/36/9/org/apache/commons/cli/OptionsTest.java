import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("option"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "opt1", true, "Option 1");
        Option option2 = new Option("b", "opt2", true, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("b"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("x", null, false, "Option X");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option1));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("f", "files", true, "Files option");
        Option option2 = new Option("d", "dirs", true, "Directories option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(1, options.getMatchingOptions("fi").size());
        assertEquals(2, options.getMatchingOptions("d").size());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("r", "required", true, "Required option");
        option1.setRequired(true);

        options.addOption(option1);

        assertEquals(1, options.getRequiredOptions().size());
    }

}