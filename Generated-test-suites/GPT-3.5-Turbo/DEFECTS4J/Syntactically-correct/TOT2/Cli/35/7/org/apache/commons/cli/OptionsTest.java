import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        options.addOptionGroup(optionGroup);
        assertTrue(options.hasOption("o"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        options.addOption(option);
        assertNotNull(options.getOption("o"));
    }

    @Test
    public void testRequiredOptions() {
        Options options = new Options();
        Option option = new Option("o", "option", false, "Test option");
        option.setRequired(true);
        options.addOption(option);
        assertEquals(1, options.getRequiredOptions().size());
    }

}