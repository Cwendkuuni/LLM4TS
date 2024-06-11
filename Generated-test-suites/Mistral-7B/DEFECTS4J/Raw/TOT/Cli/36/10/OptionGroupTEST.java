import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(options.size(), 2);
        assertEquals(options.contains(option1), true);
        assertEquals(options.contains(option2), true);
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertEquals(names.size(), 2);
        assertEquals(names.contains("o1"), true);
        assertEquals(names.contains("o2"), true);
    }

    @Test
    public void testSetSelectedNull()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSame()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o1", "option one");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(optionGroup.getSelected(), "o1");
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferent()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option");

        optionGroup.setRequired(true);
        assertEquals(optionGroup.isRequired(), true);

        optionGroup.setRequired(false);
        assertEquals(optionGroup.isRequired(), false);
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option one");
        Option option2 = new Option("o2", "option two");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(optionGroup.toString(), "[--option-one, --option-two]");
    }
}