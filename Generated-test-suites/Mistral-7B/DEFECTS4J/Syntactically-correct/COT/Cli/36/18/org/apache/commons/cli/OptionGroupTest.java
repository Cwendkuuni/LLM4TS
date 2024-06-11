import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", false, "o1");
        optionGroup.addOption(option);

        // Check if the option is added to the group
        Set<String> names = optionGroup.getNames();
        Assert.assertTrue(names.contains("option1"));

        // Check if the option is present in the group
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Set<String> names = optionGroup.getNames();
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("option1"));
        Assert.assertTrue(names.contains("option2"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertNotSame(option1, options.iterator().next());
        Assert.assertNotSame(option2, options.iterator().next());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_OptionAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelected_NullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", false, "o1");
        optionGroup.addOption(option);

        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelected_ValidOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("o1", optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--option1, --option2]", optionGroup.toString());
    }
}