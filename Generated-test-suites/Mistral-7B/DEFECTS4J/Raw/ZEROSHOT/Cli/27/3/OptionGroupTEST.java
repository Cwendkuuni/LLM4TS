import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "description1");
        Option option2 = new Option("b", "option2", false, "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals(2, optionGroup.getNames().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "description1");
        Option option2 = new Option("b", "option2", false, "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Iterator<String> iterator = optionGroup.getNames().iterator();
        String name1 = iterator.next();
        String name2 = iterator.next();

        assertEquals(option1.getKey(), name1);
        assertEquals(option2.getKey(), name2);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "description1");
        Option option2 = new Option("b", "option2", false, "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(option1, optionGroup.getOptions().iterator().next());
        assertEquals(option2, optionGroup.getOptions().iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "option", true, "description");

        optionGroup.setSelected(option);
        optionGroup.setSelected(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "description1");
        Option option2 = new Option("b", "option2", true, "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        try {
            optionGroup.setSelected(option2);
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testSetSelectedAndGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", true, "description1");
        Option option2 = new Option("b", "option2", true, "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        assertEquals(option1.getKey(), optionGroup.getSelected());
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
        Option option1 = new Option("a", "option1", true, "description1");
        Option option2 = new Option("b", "option2", false, "description2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a description1, --option1 description1, -b description2, --option2 description2]";
        assertEquals(expected, optionGroup.toString());
    }
}