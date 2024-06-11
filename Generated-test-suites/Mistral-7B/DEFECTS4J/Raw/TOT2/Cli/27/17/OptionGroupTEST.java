import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("testOption", "Test Option", false, "test");

        optionGroup.addOption(option);

        Collection options = optionGroup.getOptions();
        assertEquals(1, options.size());
        Option optionFromGroup = (Option) options.iterator().next();
        assertEquals(option, optionFromGroup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;

        optionGroup.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyOptionKey() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("", "Test Option", false, "test");

        optionGroup.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullDescription() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("testOption", null, false, "test");

        optionGroup.addOption(option);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("testOption1", "Test Option 1", false, "test1");
        Option option2 = new Option("testOption2", "Test Option 2", false, "test2");
        Option option3 = new Option("testOption3", "Test Option 3", false, "test3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("testOption1"));
        assertTrue(names.contains("testOption2"));
        assertTrue(names.contains("testOption3"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("testOption1", "Test Option 1", false, "test1");
        Option option2 = new Option("testOption2", "Test Option 2", false, "test2");
        Option option3 = new Option("testOption3", "Test Option 3", false, "test3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Collection options = optionGroup.getOptions();
        assertEquals(3, options.size());
        Option optionFromGroup1 = (Option) options.iterator().next();
        assertEquals(option1, optionFromGroup1);

        Option optionFromGroup2 = (Option) options.iterator().next();
        assertEquals(option2, optionFromGroup2);

        Option optionFromGroup3 = (Option) options.iterator().next();
        assertEquals(option3, optionFromGroup3);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("testOption1", "Test Option 1", false, "test1");
        Option option2 = new Option("testOption2", "Test Option 2", false, "test2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            // this is the expected exception
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;

        try {
            optionGroup.setSelected(option);
        } catch (AlreadySelectedException e) {
            // this is the expected exception
        }
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("testOption1", "Test Option 1", false, "test1");
        Option option2 = new Option("testOption2", "Test Option 2", false, "test2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String actual = optionGroup.toString();
        String expected = "[-testOption1 Test Option 1, -testOption2 Test Option 2]";
        assertEquals(expected, actual);
    }
}