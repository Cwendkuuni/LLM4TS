import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
        } catch (AlreadySelectedException e) {
            fail("Exception should not be thrown");
        }

        assertEquals("b", optionGroup.getSelected());
    }

    @Test
    public void testSetSelected_secondSelect() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("a", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            fail("Exception should be thrown");
        } catch (AlreadySelectedException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[ -a option1, -b option2 ]";

        assertEquals(expected, optionGroup.toString());
    }

}