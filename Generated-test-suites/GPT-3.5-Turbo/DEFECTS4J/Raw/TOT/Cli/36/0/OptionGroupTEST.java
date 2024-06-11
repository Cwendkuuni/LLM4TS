import org.junit.Test;
import org.junit.Assert;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertTrue(group.getOptions().contains(option1));
        Assert.assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        Assert.assertEquals(2, group.getNames().size());
        Assert.assertTrue(group.getNames().contains("a"));
        Assert.assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertTrue(group.getOptions().contains(option1));
        Assert.assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        Assert.assertEquals("a", group.getSelected());

        group.setSelected(option2);
        Assert.assertEquals("b", group.getSelected());

        try {
            group.setSelected(option1);
            Assert.fail("AlreadySelectedException should have been thrown");
        } catch (AlreadySelectedException e) {
            // AlreadySelectedException should be thrown here
        }
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        Assert.assertEquals("a", group.getSelected());

        group.setSelected(option2);
        Assert.assertEquals("b", group.getSelected());
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
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--a option A, --b option B]";
        Assert.assertEquals(expected, group.toString());
    }

}