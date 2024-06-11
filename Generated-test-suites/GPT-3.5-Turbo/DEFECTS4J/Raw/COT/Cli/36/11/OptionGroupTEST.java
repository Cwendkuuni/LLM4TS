import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        OptionGroup result = group.addOption(option);

        Assert.assertEquals(group, result);
        Assert.assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        group.addOption(option1);
        group.addOption(option2);

        Assert.assertTrue(group.getNames().contains("a"));
        Assert.assertTrue(group.getNames().contains("r"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        group.addOption(option1);
        group.addOption(option2);

        Assert.assertTrue(group.getOptions().contains(option1));
        Assert.assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        Assert.assertEquals("a", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        Assert.assertEquals("a", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);

        Assert.assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--add Add option, --remove Remove option]";
        String result = group.toString();

        Assert.assertEquals(expected, result);
    }

}