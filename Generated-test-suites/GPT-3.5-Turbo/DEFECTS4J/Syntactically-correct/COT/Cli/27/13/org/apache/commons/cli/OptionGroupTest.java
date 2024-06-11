import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add option");
        OptionGroup result = optionGroup.addOption(option);
        Assert.assertEquals(optionGroup, result);
        Assert.assertTrue(optionGroup.getNames().contains(option.getKey()));
        Assert.assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getNames().size());
        Assert.assertTrue(optionGroup.getNames().contains(option1.getKey()));
        Assert.assertTrue(optionGroup.getNames().contains(option2.getKey()));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option1));
        Assert.assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithExistingOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2); // Should throw AlreadySelectedException
    }

    @Test
    public void testSetSelectedWithNullOption() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);

        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "Add option");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        Assert.assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[ -a Add option, -r Remove option]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
}