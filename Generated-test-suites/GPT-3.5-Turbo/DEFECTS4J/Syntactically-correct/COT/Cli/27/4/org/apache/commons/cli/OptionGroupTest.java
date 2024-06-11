import org.junit.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "Option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        Assert.assertEquals(1, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option = new Option("o", "option", true, "Option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        Assert.assertEquals(1, optionGroup.getNames().size());
        Assert.assertTrue(optionGroup.getNames().contains("o"));
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "option", true, "Option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        Assert.assertEquals(1, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1).addOption(option2);
        optionGroup.setSelected(option1);
        Assert.assertEquals("o", optionGroup.getSelected());
        optionGroup.setSelected(option2);
        Assert.assertEquals("p", optionGroup.getSelected());
    }

    @Test(expected=AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() {
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1).addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSelectedIsNullWhenSetSelectedWithNullOption() {
        Option option1 = new Option("o", "option1", true, "Option 1");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        Assert.assertEquals("o", optionGroup.getSelected());
        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("p", "option2", true, "Option 2");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1).addOption(option2);
        String expected = "[-o Option 1, -p Option 2]";
        Assert.assertEquals(expected, optionGroup.toString());
    }

}