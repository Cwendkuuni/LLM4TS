import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", false, "File option");
        optionGroup.addOption(option);
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", false, "File option");
        optionGroup.addOption(option);
        assertTrue(optionGroup.getNames().contains("f"));
    }

    @Test
    public void testSetSelected_validOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", false, "File option");
        optionGroup.addOption(option);
        try {
            optionGroup.setSelected(option);
            assertEquals("f", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSetSelected_alreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("o", "output", false, "Output option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Exception not thrown");
        } catch (AlreadySelectedException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testSetSelected_nullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "output", false, "Output option");
        optionGroup.addOption(option);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsOptional() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("o", "output", false, "Output option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expectedString = "[f -file, o -output]";
        assertEquals(expectedString, optionGroup.toString());
    }

}