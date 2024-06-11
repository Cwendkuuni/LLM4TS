import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;

public class OptionGroupTest {
    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("b"));

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            Assert.fail("Unexpected AlreadySelectedException");
        }

        Assert.assertEquals("a", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            Assert.fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            Assert.assertEquals(optionGroup, e.getOptionGroup());
            Assert.assertEquals(option1, e.getOption());
        }

        try {
            optionGroup.setSelected(option2);
            Assert.fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            Assert.assertEquals(optionGroup, e.getOptionGroup());
            Assert.assertEquals(option2, e.getOption());
        }
    }

    @Test
    public void testSetSelectedWithNullOption() {
        Option option1 = new Option("a", "option1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        try {
            optionGroup.setSelected(null);
        } catch (AlreadySelectedException e) {
            Assert.fail("Unexpected AlreadySelectedException");
        }

        Assert.assertNull(optionGroup.getSelected());
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
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedString = "[-a option1, -b option2]";

        Assert.assertEquals(expectedString, optionGroup.toString());
    }
}