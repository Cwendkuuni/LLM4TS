import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);

        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1)
                  .addOption(option2);

        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1)
                  .addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected_validOption() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1)
                  .addOption(option2);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            fail("Exception thrown for valid option selection");
        }

        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_alreadySelectedOption() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1)
                  .addOption(option2);

        optionGroup.setSelected(option1);

        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected_resetSelection() {
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);

        optionGroup.setSelected(option);

        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() {
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);

        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option);

        assertEquals("a", optionGroup.getSelected());
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
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1)
                  .addOption(option2);

        String expected = "[ -a option a, -b option b]";

        assertEquals(expected, optionGroup.toString());
    }
}