import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testOptionGroupWithValidOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "This is option 1");
        Option option2 = new Option("option2", "This is option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("option1", optionGroup.getSelected());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testOptionGroupWithDuplicateSelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option", "This is an option");

        optionGroup.addOption(option);
        optionGroup.setSelected(option);

        try {
            optionGroup.setSelected(option);
            fail("Should have thrown AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected
        }

        assertEquals("option", optionGroup.getSelected());
        assertTrue(optionGroup.getOptions().contains(option));
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testOptionGroupWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;

        optionGroup.addOption(option);
    }

    @Test
    public void testOptionGroupWithRequiredGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option", "This is an option");

        optionGroup.setRequired(true);
        optionGroup.addOption(option);

        try {
            optionGroup.setSelected(null);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testOptionGroupToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "This is option 1");
        Option option2 = new Option("option2", "This is option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        String expected = "[-option1 This is option 1, --option2 This is option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}