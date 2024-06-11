
import static org.junit.Assert.*;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroupConstruction() {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals(0, optionGroup.getOptions().size());
    }

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("test", "testOption", false, "A test option");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_SameOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("test", "testOption", false, "A test option");
        Option option2 = new Option("test", "testOption", false, "A test option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelected_NullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        optionGroup.setSelected(option);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_OptionNotInGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("test", "testOption", false, "A test option");
        Option anotherOption = new Option("another", "anotherOption", false, "Another option");
        optionGroup.addOption(anotherOption);
        optionGroup.setSelected(option);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_OptionAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("test", "testOption", false, "A test option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        optionGroup.setSelected(option);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_GroupIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        OptionGroup requiredOptionGroup = new OptionGroup();
        Option option = new Option("test", "testOption", false, "A test option");
        optionGroup.addOption(option);
        optionGroup.setRequired(true);
        optionGroup.setSelected(option);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("test1", "testOption1", false, "Test option 1");
        Option option2 = new Option("test2", "testOption2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("test1", "testOption1", false, "Test option 1");
        Option option2 = new Option("test2", "testOption2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("test1", "testOption1", false, "Test option 1");
        Option option2 = new Option("test2", "testOption2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[--test1 Test option 1, --test2 Test option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}