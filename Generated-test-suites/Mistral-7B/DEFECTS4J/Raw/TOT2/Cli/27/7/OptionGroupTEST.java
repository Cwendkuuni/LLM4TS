import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "optionDescription");
        optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assertEquals(1, options.size());
        Option optionFromGroup = (Option) options.iterator().next();
        assertEquals(option, optionFromGroup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        optionGroup.addOption(option);
    }

    @Test
    public void testAddDuplicateOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "optionDescription");
        optionGroup.addOption(option);
        try {
            optionGroup.addOption(option);
        } catch (IllegalArgumentException e) {
            assertEquals("Option optionName has already been added to the group", e.getMessage());
        }
    }

    // Test other methods and edge cases here

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option1 description");
        Option option2 = new Option("option2", "option2 description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals("[--option1 option1 description, --option2 option2 description]", optionGroup.toString());
    }
}