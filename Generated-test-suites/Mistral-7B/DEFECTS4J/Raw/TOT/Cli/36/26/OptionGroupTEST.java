import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroupWithValidOptions() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "Option 1", false, "Help for Option 1");
        Option option2 = new Option("o2", "Option 2", false, "Help for Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        String selectedOption = optionGroup.getSelected();

        assertNull(optionGroup.getSelected());
        optionGroup.setSelected(option1);
        assertEquals("o1", selectedOption);
        assertEquals(option1, optionGroup.getOption(option1.getKey()));
        assertEquals(option2, optionGroup.getOption(option2.getKey()));
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertEquals(2, optionGroup.getOptions().size());
        assertEquals(2, optionGroup.getNames().size());
        assertFalse(optionGroup.isRequired());
        assertNotNull(optionGroup.toString());
    }

    // Add test cases for edge cases and error scenarios here
}