import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    // Test addOption() method
    @Test
    public void testAddOption() {
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        Option option3 = new Option("c");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }

    // Test getNames() method
    @Test
    public void testGetNames() {
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        Option option3 = new Option("c");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection<String> names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        assertTrue(names.contains("c"));
    }

    // Test setSelected() method with valid option
    @Test
    public void testSetSelectedWithValidOption() throws AlreadySelectedException {
        Option option1 = new Option("a");
        Option option2 = new Option("b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    // Test setSelected() method with already selected option
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
        Option option1 = new Option("a");
        Option option2 = new Option("b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2); // should throw AlreadySelectedException
    }

    // Test getSelected() method
    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a");
        Option option2 = new Option("b");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());
    }

    // Test setRequired() method
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    // Test toString() method
    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        Option option3 = new Option("c", "option3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        String expected = "[-a option1, -b option2, -c option3]";
        assertEquals(expected, optionGroup.toString());
    }

}