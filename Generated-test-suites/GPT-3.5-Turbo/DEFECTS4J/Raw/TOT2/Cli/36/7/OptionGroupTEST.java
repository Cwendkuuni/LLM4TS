import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("o", "desc");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        
        assertTrue(group.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("o1", "desc1");
        Option option2 = new Option("o2", "desc2");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getNames().contains("o1"));
        assertTrue(group.getNames().contains("o2"));
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("o1", "desc1");
        Option option2 = new Option("o2", "desc2");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        
        assertEquals("o1", group.getSelected());
        
        assertThrows(AlreadySelectedException.class, () -> {
            group.setSelected(option2);
        });
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        
        assertTrue(group.isRequired());
        
        group.setRequired(false);
        assertFalse(group.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "desc1");
        Option option2 = new Option("o2", "desc2");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        assertEquals("[ -o1 desc1, -o2 desc2]", group.toString());
    }

}