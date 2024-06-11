import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optA", "Option A");

        optionGroup.addOption(option);

        Option optionFromGroup = optionGroup.getOptions().iterator().next();

        Assert.assertEquals(option, optionFromGroup);
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optA", "Option A");
        Option option2 = new Option("optB", "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = new ArrayList<>(optionGroup.getNames());

        Assert.assertEquals("optA", names.get(0));
        Assert.assertEquals("optB", names.get(1));
    }

    @Test
    public void testSetSelectedNull()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optA", "Option A");

        optionGroup.setSelected(option);

        Assert.assertEquals("optA", optionGroup.getSelected());

        optionGroup.setSelected(null);

        Assert.assertNull(optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optA", "Option A");
        Option option2 = new Option("optB", "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedSameOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optA", "Option A");

        optionGroup.setSelected(option);

        optionGroup.setSelected(option);

        Assert.assertEquals("optA", optionGroup.getSelected());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optA", "Option A");
        Option option2 = new Option("optB", "Option B");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-optA Option A, --optB Option B ]";

        Assert.assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testIsRequiredFalse()
    {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testIsRequiredTrue()
    {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }
}