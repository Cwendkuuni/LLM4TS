import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("test", "test description");
        
        group.addOption(option);
        
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("test", "test description");
        
        group.addOption(option);
        
        assertTrue(group.getNames().contains("test"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("test", "test description");
        
        group.addOption(option);
        
        assertTrue(group.getOptions().contains(option));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("test1", "test description");
        Option option2 = new Option("test2", "test description");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        
        assertEquals("test1", group.getSelected());
        
        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option2));
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        
        group.setRequired(true);
        
        assertTrue(group.isRequired());
    }
}