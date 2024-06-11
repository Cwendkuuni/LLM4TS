import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest {

    /**
     * Test addOption method.
     */
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    /**
     * Test getNames method.
     */
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("option1"));
        Assert.assertTrue(names.contains("option2"));
    }

    /**
     * Test setSelected method with null option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setSelected(null);
    }

    /**
     * Test setSelected method with already selected option.
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(option1);
    }

    /**
     * Test setSelected method with a new option.
     */
    @Test
    public void testSetSelectedNewOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(option2);

        Assert.assertEquals("option2", optionGroup.getSelected());
    }

    /**
     * Test isRequired method.
     */
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    /**
     * Test setRequired method.
     */
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);

        Assert.assertFalse(optionGroup.isRequired());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--option1 Option 1, --option2 Option 2]";

        String actual = optionGroup.toString();

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test setSelected method with empty OptionGroup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedEmptyOptionGroup() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setSelected(null);
    }

    /**
     * Test setSelected method with multiple options.
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedMultipleOptions() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("option1", "Option 1");
        Option option2 = new Option("option2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    /**
     * Test setSelected method with same option twice.
     */
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedSameOptionTwice() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("option1", "Option 1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    /**
     * Test addOption method with same option twice.
     */
    @Test
    public void testAddOptionSameOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("option1", "Option 1");

        optionGroup.addOption(option1);
        optionGroup.addOption(option1);

        Collection<Option> options = optionGroup.getOptions();

        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.contains(option1));
    }
}