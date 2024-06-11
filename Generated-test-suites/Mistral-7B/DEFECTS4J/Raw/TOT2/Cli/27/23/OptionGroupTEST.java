import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "description for option1");
        optionGroup.addOption(option);

        Assert.assertEquals(1, optionGroup.getOptions().size());
        Assert.assertEquals("option1", optionGroup.getOptions().iterator().next().getOpt());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "description for option1");
        Option option2 = new Option("option2", "description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(2, names.size());
        Assert.assertEquals("option1", names.iterator().next());
        Assert.assertEquals("option2", names.iterator().next());
    }

    @Test
    public void testSetSelected_Valid() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "description for option1");
        Option option2 = new Option("option2", "description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("option1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("option2", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "description for option1");
        Option option2 = new Option("option2", "description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "description for option1");
        Option option2 = new Option("option2", "description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.iterator().next());
        Assert.assertEquals(option2, options.iterator().next());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setRequired(false);
        Assert.assertFalse(optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "description for option1");
        Option option2 = new Option("option2", "description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--option1 description for option1, --option2 description for option2]";
        Assert.assertEquals(expected, optionGroup.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelected_Null() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "description for option1");
        optionGroup.addOption(option);

        optionGroup.setSelected(null);
    }

    @Test
    public void testToString_Empty() {
        OptionGroup optionGroup = new OptionGroup();
        String expected = "[ ]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
}