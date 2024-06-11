import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file option");
        options.addOption(option);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasLongOption("output"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "Output file option");
        Option option2 = new Option("i", "input", true, "Input file option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("i"));
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file option");
        options.addOption(option);
        List<Option> optionList = new ArrayList<>(options.getOptions());
        assertEquals(1, optionList.size());
        assertEquals("o", optionList.get(0).getOpt());
    }

    // Test edge case for getOption method
    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file option");
        options.addOption(option);
        Option retrievedOption = options.getOption("o");
        assertEquals("o", retrievedOption.getOpt());
    }

    // Test for an exception scenario when adding a duplicate option
    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOption() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file option");
        options.addOption(option);
        options.addOption(option);
    }

    // Add more test cases for other methods as needed

}