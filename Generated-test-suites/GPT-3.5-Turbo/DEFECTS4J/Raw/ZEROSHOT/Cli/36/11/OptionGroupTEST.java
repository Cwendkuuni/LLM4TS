import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class OptionGroupTest {

    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();

        option1 = new Option("a", "option1", false, "Option 1");
        option2 = new Option("b", "option2", false, "Option 2");
        option3 = new Option("c", "option3", false, "Option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }

    @Test
    public void testAddOption() {
        Option option4 = new Option("d", "option4", false, "Option 4");
        optionGroup.addOption(option4);

        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(4, options.size());
        Assert.assertTrue(options.contains(option4));
    }

    @Test
    public void testGetNames() {
        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(3, names.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("b"));
        Assert.assertTrue(names.contains("c"));
    }

    @Test
    public void testGetOptions() {
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
        Assert.assertTrue(options.contains(option3));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("b", optionGroup.getSelected());

        Option option4 = new Option("d", "option4", false, "Option 4");
        optionGroup.setSelected(option4);
        Assert.assertEquals("d", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        Assert.assertEquals("a", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        Assert.assertEquals("b", optionGroup.getSelected());

        Option option4 = new Option("d", "option4", false, "Option 4");
        optionGroup.setSelected(option4);
        Assert.assertEquals("d", optionGroup.getSelected());
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
        String expected = "[-a Option 1, -b Option 2, -c Option 3]";
        String actual = optionGroup.toString();
        Assert.assertEquals(expected, actual);
    }
}