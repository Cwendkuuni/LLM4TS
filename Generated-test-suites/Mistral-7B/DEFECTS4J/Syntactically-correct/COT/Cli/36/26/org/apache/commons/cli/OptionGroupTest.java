import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    /**
     * Test method for {@link OptionGroup#addOption(Option)}
     */
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1 description");

        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();

        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.iterator().next());
    }

    /**
     * Test method for {@link OptionGroup#getNames()}
     */
    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();

        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("option1"));
        Assert.assertTrue(names.contains("option2"));
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}
     * @throws AlreadySelectedException
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}
     */
    @Test
    public void testSetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        String selected = optionGroup.getSelected();

        Assert.assertEquals("option1", selected);
    }

    /**
     * Test method for {@link OptionGroup#setRequired(boolean)}
     */
    @Test
    public void testSetRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        boolean required = optionGroup.isRequired();

        Assert.assertTrue(required);
    }

    /**
     * Test method for {@link OptionGroup#toString()}
     */
    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description");
        Option option2 = new Option("option2", "Option 2 description");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--option1 Option 1 description, --option2 Option 2 description]";
        String toString = optionGroup.toString();

        Assert.assertEquals(expected, toString);
    }

    /**
     * Test method for {@link OptionGroup#addOption(Option)} with null option
     */
    @Test(expected = NullPointerException.class)
    public void testAddOptionNullOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;

        optionGroup.addOption(option);
    }
}