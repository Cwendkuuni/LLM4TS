import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        Assert.assertEquals(0, optionGroup.getOptions().size());

        optionGroup.addOption(option1);

        Assert.assertEquals(1, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option1));

        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getOptions().size());
        Assert.assertTrue(optionGroup.getOptions().contains(option2));

        optionGroup.addOption(option1);
        
        // Adding the same option again should not affect the size
        Assert.assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<String> names = optionGroup.getNames();

        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Assert.assertNull(optionGroup.getSelected());

        Option option1 = new Option("a", "option1", false, "Option 1");
        optionGroup.setSelected(option1);

        Assert.assertEquals("a", optionGroup.getSelected());

        Option option2 = new Option("b", "option2", false, "Option 2");
        optionGroup.setSelected(option2);

        Assert.assertEquals("b", optionGroup.getSelected());

        Option option3 = new Option("c", "option3", false, "Option 3");

        try {
            optionGroup.setSelected(option3);
            Assert.fail("AlreadySelectedException not thrown");
        } catch (AlreadySelectedException e) {
            Assert.assertEquals("Option '-c' was already selected in this group", e.getMessage());
        }

        optionGroup.setSelected(null);

        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);

        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[-[a] Option 1, --[b] Option 2]";

        Assert.assertEquals(expectedString, optionGroup.toString());
    }
}