import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOptionAndGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "This is option1.");
        Option option2 = new Option("o2", "option2", false, "This is option2.");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();
        assertEquals(names.size(), 2);
        assertTrue(names.containsAll(Arrays.asList("o1", "o2")));
    }

    @Test
    public void testAddOptionAndGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "This is option1.");
        Option option2 = new Option("o2", "option2", false, "This is option2.");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();
        assertEquals(options.size(), 2);
        assertTrue(options.containsAll(Arrays.asList(option1, option2)));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "This is option1.");
        Option option2 = new Option("o2", "option2", false, "This is option2.");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Should throw AlreadySelectedException.");
        } catch (AlreadySelectedException e) {
            // Expected
        }

        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getOpt(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Should not throw AlreadySelectedException.");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedTwiceWithSameOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o1", "option1", false, "This is option1.");
        optionGroup.addOption(option);

        optionGroup.setSelected(option);
        optionGroup.setSelected(option);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setRequired(false);
        assertFalse(optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "This is option1.");
        Option option2 = new Option("o2", "option2", false, "This is option2.");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 This is option1., -o2 This is option2.]";
        assertEquals(expected, optionGroup.toString());
    }
}