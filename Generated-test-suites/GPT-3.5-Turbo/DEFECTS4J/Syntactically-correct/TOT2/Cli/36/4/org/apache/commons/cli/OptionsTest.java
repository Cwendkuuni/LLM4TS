import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        Option option1 = new Option("a", "option", false, "Description");
        Option option2 = new Option("b", "other", true, "Description");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("c", "optionC", false, "Description");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testToString() {
        Options options = new Options();

        String expected = "[ Options: [ short {} ] [ long {} ]";
        assertEquals(expected, options.toString());
    }

    // Add more test cases for other methods in the Options class

}