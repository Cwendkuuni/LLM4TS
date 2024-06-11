import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "apple", false, "apple option");
        Option option2 = new Option("b", "banana", false, "banana option");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);
        assertEquals(group, options.getOptionGroup(option1));
        assertEquals(group, options.getOptionGroup(option2));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("c", "cherry", true, "cherry option");
        options.addOption(option);

        assertTrue(options.hasOption("c"));
        assertTrue(options.hasLongOption("cherry"));
        assertTrue(options.hasShortOption("c"));
        assertEquals(option, options.getOption("c"));
    }

    // Add more test cases as needed

}