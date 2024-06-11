import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup
 */
public class OptionGroupTest {

    /**
     * Test method for {@link OptionGroup#addOption(Option)}
     */
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    /**
     * Test method for {@link OptionGroup#getNames()}
     */
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("opt1"));
        Assert.assertTrue(names.contains("opt2"));
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}
     */
    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Test setting null option
        Assert.assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("opt1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // Test re-selecting the same option
            optionGroup.setSelected(option1);
            Assert.assertEquals("opt1", optionGroup.getSelected());
        }

        try {
            optionGroup.setSelected(option2);
            Assert.assertEquals("opt2", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            Assert.fail("Expected AlreadySelectedException but got: " + e.getMessage());
        }

        // Test selecting an unavailable option
        Option option3 = new Option("opt3", "Option 3");
        optionGroup.addOption(option3);

        try {
            optionGroup.setSelected(option3);
            Assert.fail("Expected AlreadySelectedException but got: " + optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // Expected AlreadySelectedException
        }
    }

    /**
     * Test method for {@link OptionGroup#getSelected()}
     */
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("opt1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("opt2", optionGroup.getSelected());
    }

    /**
     * Test method for {@link OptionGroup#setRequired(boolean)}
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
     * Test method for {@link OptionGroup#isRequired()}
     */
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#toString()}
     */
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--opt1 Option 1, --opt2 Option 2]";
        String actual = optionGroup.toString();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for edge cases and exceptions handling
     * @throws AlreadySelectedException
     */
    @Test
    public void testEdgeCasesAndExceptions() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");
        Option option3 = new Option("opt3", "Option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        Assert.assertEquals("opt1", optionGroup.getSelected());

        // Test adding the same option twice
        optionGroup.addOption(option1);
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
        Assert.assertTrue(options.contains(option1));

        optionGroup.setSelected(option2);
        Assert.assertEquals("opt2", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option3);
            Assert.fail("Expected AlreadySelectedException but got: " + optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // Expected AlreadySelectedException
        }

        // Test selecting an option that does not exist
        String nonExistentOption = "nonExistentOption";
        try {
            optionGroup.setSelected(new Option(nonExistentOption, ""));
            Assert.fail("Expected AlreadySelectedException but got: " + optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // Expected AlreadySelectedException
        }

        // Test setting an option group as selected
        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup.addOption(optionGroup2);
        try {
            optionGroup.setSelected(optionGroup2);
            Assert.fail("Expected AlreadySelectedException but got: " + optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // Expected AlreadySelectedException
        }
    }
}