import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;
import java.util.Collection;

public class OptionGroupTest {
    
    @Test
    public void testAddOption_typical() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "add option");
        
        OptionGroup updatedOptionGroup = optionGroup.addOption(option);
        
        Collection<String> names = updatedOptionGroup.getNames();
        
        Assert.assertEquals(1, names.size());
        Assert.assertTrue(names.contains("a"));
    }

    @Test
    public void testAddOption_edge() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "add option");
        
        OptionGroup updatedOptionGroup = optionGroup.addOption(option);
        
        Collection<Option> options = updatedOptionGroup.getOptions();
        
        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames_typical() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<String> names = optionGroup.getNames();
        
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains("a"));
        Assert.assertTrue(names.contains("r"));
    }

    @Test
    public void testGetOptions_typical() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<Option> options = optionGroup.getOptions();
        
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected_typical() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        String selected = optionGroup.getSelected();
        
        Assert.assertEquals("a", selected);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_error() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected_typical() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        
        String selected = optionGroup.getSelected();
        
        Assert.assertEquals("a", selected);
    }

    @Test
    public void testSetRequired_typical() {
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.setRequired(true);
        
        boolean required = optionGroup.isRequired();
        
        Assert.assertTrue(required);
    }

    @Test
    public void testIsRequired_typical() {
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.setRequired(true);
        
        boolean required = optionGroup.isRequired();
        
        Assert.assertTrue(required);
    }

    @Test
    public void testToString_typical() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("r", "remove", false, "remove option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String toString = optionGroup.toString();
        
        Assert.assertEquals("[ -a add option, -r remove option]", toString);
    }

    @Test
    public void testToString_edge() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option(null, "remove", false, null);
        optionGroup.addOption(option);
        
        String toString = optionGroup.toString();
        
        Assert.assertEquals("[ --remove]", toString);
    }
}