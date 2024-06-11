import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "add option");
        OptionGroup group = new OptionGroup();
        group.addOption(option);
        assertTrue(group.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", false, "option A");
        Option option2 = new Option("b", false, "option B");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "build", false, "build option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "build", false, "build option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        assertEquals(option1.getKey(), group.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "build", false, "build option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        group.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "build", false, "build option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.setSelected(option1);
        assertEquals(option1.getKey(), group.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "add", false, "add option");
        Option option2 = new Option("b", "build", false, "build option");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        String expected = "[ -a add option, -b build option]";
        assertEquals(expected, group.toString());
    }
}