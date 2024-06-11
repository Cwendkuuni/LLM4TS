import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionKey", "Option description");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assert (options.size() == 1);
        Option actualOption = options.iterator().next();
        assertEquals("optionKey", actualOption.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optionKey1", "Option description 1");
        Option option2 = new Option("optionKey1", "Option description 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
    }

    @Test
    public void testAddOptionToNullOptionGroup() {
        Option option = new Option("optionKey", "Option description");
        OptionGroup optionGroup = null;
        try {
            optionGroup.addOption(option);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAddOptionToEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionKey", "Option description");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assert (options.size() == 1);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionKey", "Option description");
        optionGroup.setSelected(option);
        optionGroup.setSelected(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optionKey1", "Option description 1");
        Option option2 = new Option("optionKey2", "Option description 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedNonExistingOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optionKey1", "Option description 1");
        Option option2 = new Option("optionKey2", "Option description 2");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }
}