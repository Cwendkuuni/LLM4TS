import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "build", false, "Build option");
        Option option3 = new Option("c", "create", false, "Create option");

        OptionGroup result = optionGroup.addOption(option1).addOption(option2).addOption(option3);

        Assert.assertSame(optionGroup, result);
        Assert.assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "build", false, "Build option");
        Option option3 = new Option("c", "create", false, "Create option");

        optionGroup.addOption(option1).addOption(option2).addOption(option3);
        Collection<String> names = optionGroup.getNames();

        Assert.assertEquals(3, names.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("b"));
        Assert.assertTrue(names.contains("c"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "build", false, "Build option");
        Option option3 = new Option("c", "create", false, "Create option");

        optionGroup.addOption(option1).addOption(option2).addOption(option3);
        Collection<Option> options = optionGroup.getOptions();

        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
        Assert.assertTrue(options.contains(option3));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "build", false, "Build option");
        Option option3 = new Option("c", "create", false, "Create option");

        optionGroup.addOption(option1).addOption(option2).addOption(option3);

        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("b", optionGroup.getSelected());

        optionGroup.setSelected(option3);
        Assert.assertEquals("c", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "build", false, "Build option");
        Option option3 = new Option("c", "create", false, "Create option");

        optionGroup.addOption(option1).addOption(option2).addOption(option3);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2); // Already selected, should throw exception
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "build", false, "Build option");
        Option option3 = new Option("c", "create", false, "Create option");

        optionGroup.addOption(option1).addOption(option2).addOption(option3);

        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("b", optionGroup.getSelected());

        optionGroup.setSelected(option3);
        Assert.assertEquals("c", optionGroup.getSelected());
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
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("b", "build", false, "Build option");
        Option option3 = new Option("c", "create", false, "Create option");

        optionGroup.addOption(option1).addOption(option2).addOption(option3);

        String expected = "[-a Add option, -b Build option, -c Create option]";
        String actual = optionGroup.toString();

        Assert.assertEquals(expected, actual);
    }
}