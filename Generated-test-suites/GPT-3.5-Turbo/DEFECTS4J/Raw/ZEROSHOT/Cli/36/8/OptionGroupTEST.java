import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add option");

        optionGroup.addOption(option);

        assertEquals(optionGroup.getOptions().size(), 1);
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(optionGroup.getNames().size(), 2);
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("r"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(optionGroup.getOptions().size(), 2);
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);

        assertEquals(optionGroup.getSelected(), "a");

        try {
            optionGroup.setSelected(option2);
            fail("AlreadySelectedException should have been thrown");
        } catch (AlreadySelectedException e) {
            assertEquals(e.getOptionGroup(), optionGroup);
            assertEquals(e.getOption(), option2);
        }
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);

        assertEquals(optionGroup.getSelected(), "a");
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a Add option, -r Remove option]";

        assertEquals(optionGroup.toString(), expected);
    }

}