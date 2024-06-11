import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");

        optionGroup.addOption(option);

        Assert.assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("g", "get", false, "Get option");

        optionGroup.addOption(option);

        Assert.assertTrue(optionGroup.getNames().contains("g"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "Test option");

        optionGroup.addOption(option);

        Assert.assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("s", "select", false, "Select option");
        Option option2 = new Option("d", "deselect", false, "Deselect option");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Assert.assertEquals("s", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            Assert.fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test", false, "Test option 1");
        Option option2 = new Option("v", "verify", false, "Verify option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[ -t Test option 1, -v Verify option 2]";
        String actual = optionGroup.toString();

        Assert.assertEquals(expected, actual);
    }
}