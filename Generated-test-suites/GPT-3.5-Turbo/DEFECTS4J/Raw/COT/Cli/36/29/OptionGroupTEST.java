
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "test option");
        optionGroup.addOption(option);
        
        Assert.assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "test option 1");
        Option option2 = new Option("p", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Assert.assertEquals(2, optionGroup.getNames().size());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "test option 1");
        Option option2 = new Option("p", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Assert.assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "test option 1");
        Option option2 = new Option("p", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        Assert.assertEquals("o", optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "test option 1");
        Option option2 = new Option("p", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        try {
            optionGroup.setSelected(option2);
            Assert.fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "test option 1");
        Option option2 = new Option("p", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        Assert.assertEquals("o", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o", "option1", false, "test option 1");
        Option option2 = new Option("p", "option2", false, "test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[-o test option 1, -p test option 2]";
        
        Assert.assertEquals(expected, optionGroup.toString());
    }
}
