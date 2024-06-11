import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option a");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        assertTrue(group.getOptions().contains(option1));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() throws AlreadySelectedException {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        assertEquals("[ -a option a, -b option b]", group.toString());
    }
}