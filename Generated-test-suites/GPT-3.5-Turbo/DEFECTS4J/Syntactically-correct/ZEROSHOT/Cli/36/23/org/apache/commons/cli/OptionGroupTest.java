import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "optionA", true, "Option A");
        group.addOption(option);
        Collection<String> names = group.getNames();
        Assert.assertEquals(1, names.size());
        Assert.assertTrue(names.contains("a"));
        Collection<Option> options = group.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.contains(option));
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "optionA", true, "Option A");
        Option optionB = new Option("b", "optionB", true, "Option B");
        group.addOption(optionA);
        group.addOption(optionB);
        group.setSelected(optionA);
        group.setSelected(optionB);
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "optionA", true, "Option A");
        Option optionB = new Option("b", "optionB", true, "Option B");
        group.addOption(optionA);
        group.addOption(optionB);
        group.setSelected(optionA);
        String selected = group.getSelected();
        Assert.assertEquals("a", selected);
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        Assert.assertTrue(group.isRequired());
        group.setRequired(false);
        Assert.assertFalse(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", "optionA", true, "Option A");
        Option optionB = new Option("b", "optionB", true, "Option B");
        group.addOption(optionA);
        group.addOption(optionB);
        String result = group.toString();
        Assert.assertEquals("[ -a Option A, -b Option B]", result);
    }
}