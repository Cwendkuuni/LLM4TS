import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Description");
        group.addOption(option1);
        assertTrue(group.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Description");
        group.addOption(option1);
        assertTrue(group.getNames().contains("a"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Description");
        group.addOption(option1);
        assertTrue(group.getOptions().contains(option1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Description");
        Option option2 = new Option("b", "beta", false, "Description");
        
        group.addOption(option1);
        group.addOption(option2);
        
        group.setSelected(option1);
        assertEquals("a", group.getSelected());
        
        assertThrows(AlreadySelectedException.class, () -> {
            group.setSelected(option2);
        });
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "alpha", false, "Description");
        Option option2 = new Option("b", "beta", false, "Description");
        group.addOption(option1);
        group.addOption(option2);
        
        String expected = "[ -a Description, -b Description ]";
        assertEquals(expected, group.toString());
    }
}