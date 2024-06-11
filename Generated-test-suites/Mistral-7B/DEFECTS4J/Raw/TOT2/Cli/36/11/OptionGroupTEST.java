package test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "Option description");

        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();

        assertEquals(1, options.size());
        Option actualOption = options.iterator().next();
        assertEquals("optionName", actualOption.getKey());
        assertEquals("Option description", actualOption.getDescription());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        Iterator<String> namesIterator = names.iterator();

        assertEquals("option1", namesIterator.next());
        assertEquals("option2", namesIterator.next());
    }

    @Test
    public void testSetSelectedAndGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Test setting and getting null selected option
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());

        // Test setting and getting selected option
        optionGroup.setSelected(option1);
        assertEquals("option1", optionGroup.getSelected());

        // Test setting the same selected option twice
        optionGroup.setSelected(option1);
        assertEquals("option1", optionGroup.getSelected());

        // Test setting a different option as selected
        optionGroup.setSelected(option2);
        assertEquals("option2", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
}