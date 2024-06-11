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
     * Test method for {@link OptionGroup#addOption(Option)} .
     */
    @Test
    public void testAddOption()
    {
        OptionGroup group = new OptionGroup();
        Option option = new Option("option1", "Description for option1");
        group.addOption(option);

        Option addedOption = group.getOptions().iterator().next();
        Assert.assertEquals(option, addedOption);
    }

    /**
     * Test method for {@link OptionGroup#getNames()} .
     */
    @Test
    public void testGetNames()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        group.addOption(option1);
        group.addOption(option2);

        List<String> names = new ArrayList<>(group.getNames());
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("option1"));
        Assert.assertTrue(names.contains("option2"));
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)} .
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);

        group.setSelected(option2);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)} .
     */
    @Test
    public void testSetSelected()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        Assert.assertEquals("option1", group.getSelected());

        group.setSelected(option2);
        Assert.assertEquals("option2", group.getSelected());
    }

    /**
     * Test method for {@link OptionGroup#isRequired()} .
     */
    @Test
    public void testIsRequired()
    {
        OptionGroup group = new OptionGroup();
        Assert.assertFalse(group.isRequired());

        group.setRequired(true);
        Assert.assertTrue(group.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#toString()} .
     */
    @Test
    public void testToString()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--option1 Description for option1, --option2 Description for option2]";
        Assert.assertEquals(expected, group.toString());
    }
}