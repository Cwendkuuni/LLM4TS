
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", true, "file option");
        optionGroup.addOption(option);

        Assert.assertEquals(1, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option));
        Assert.assertEquals("f", optionGroup.getNames().iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("s", "size", true, "size option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getNames().size());
        Assert.assertTrue(optionGroup.getNames().contains("f"));
        Assert.assertTrue(optionGroup.getNames().contains("s"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("s", "size", true, "size option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option1));
        Assert.assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("s", "size", true, "size option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("f", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("s", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("s", "size", true, "size option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testSetSelected_NullOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "file option");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("s", "size", true, "size option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("f", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("s", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", true, "file option");
        Option option2 = new Option("s", "size", true, "size option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[ -f file option, -s size option]";
        Assert.assertEquals(expectedString, optionGroup.toString());
    }
}
