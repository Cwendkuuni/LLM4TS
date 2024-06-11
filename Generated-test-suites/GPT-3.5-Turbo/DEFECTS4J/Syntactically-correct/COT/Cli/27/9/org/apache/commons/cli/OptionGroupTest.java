
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);

        Collection<String> names = optionGroup.getNames();
        Assert.assertTrue(names.contains("a"));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("r"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("a", optionGroup.getSelected());

            optionGroup.setSelected(option2);
            Assert.assertEquals("r", optionGroup.getSelected());

            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            Assert.fail("Exception not expected");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", false, "Remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);  // Should throw AlreadySelectedException
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option1);

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("a", optionGroup.getSelected());

            optionGroup.setSelected(null);
            Assert.assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            Assert.fail("Exception not expected");
        }
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
    public void testIsRequired() {
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
        optionGroup.addOption(option1);

        Assert.assertEquals("[-a Add option]", optionGroup.toString());
    }

}
