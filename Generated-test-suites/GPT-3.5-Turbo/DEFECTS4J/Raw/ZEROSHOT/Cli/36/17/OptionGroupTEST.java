import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a");
        optionGroup.addOption(option);
        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(1, names.size());
        Assert.assertTrue(names.contains("a"));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        String selected = optionGroup.getSelected();
        Assert.assertEquals("a", selected);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedException() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        String selected = optionGroup.getSelected();
        Assert.assertNull(selected);
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
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        option1.setDescription("Option A");
        option2.setDescription("Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String toString = optionGroup.toString();
        Assert.assertEquals("[ -a Option A, -b Option B]", toString);
    }
}