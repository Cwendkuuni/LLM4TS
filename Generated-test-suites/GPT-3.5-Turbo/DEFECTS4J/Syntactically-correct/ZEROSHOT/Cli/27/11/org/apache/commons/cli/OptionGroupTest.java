import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

import java.util.Collection;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "An option");
        OptionGroup group = optionGroup.addOption(option);

        Collection options = group.getOptions();
        Assert.assertEquals(1, options.size());

        Option addedOption = options.iterator().next();
        Assert.assertEquals(option, addedOption);
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection names = optionGroup.getNames();
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("o1"));
        Assert.assertTrue(names.contains("o2"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection options = optionGroup.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("o1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            Assert.fail("Should not have thrown AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option2);
            Assert.fail("Should have thrown AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected exception
        }
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("o1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            Assert.fail("Should not have thrown AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option2);
            Assert.fail("Should have thrown AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected exception
        }

        Assert.assertEquals("o1", optionGroup.getSelected());
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
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o1 Option 1, -o2 Option 2]";
        Assert.assertEquals(expected, optionGroup.toString());
    }
}