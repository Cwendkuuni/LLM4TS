import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add");
        optionGroup.addOption(option);

        Assert.assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add");
        Option option2 = new Option("r", "remove");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getNames().size());
        Assert.assertTrue(optionGroup.getNames().contains("a"));
        Assert.assertTrue(optionGroup.getNames().contains("r"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add");
        Option option2 = new Option("r", "remove");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option1));
        Assert.assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "add");
        Option option2 = new Option("r", "remove");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("r", optionGroup.getSelected());
    }

    @Test
    public void testSetSelected_withAlreadySelectedOption() {
        Option option1 = new Option("a", "add");
        Option option2 = new Option("r", "remove");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            Assert.assertEquals(optionGroup, e.getGroup());
            Assert.assertEquals(option1, e.getOption());
        }
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "add");
        Option option2 = new Option("r", "remove");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("r", optionGroup.getSelected());
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
        Option option1 = new Option("a", "add");
        Option option2 = new Option("r", "remove");
        option1.setDescription("Add option");
        option2.setDescription("Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[--add Add option, --remove Remove option]", optionGroup.toString());
    }
}