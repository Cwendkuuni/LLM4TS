import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "A", false, "Test Option A");
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getNames().contains("a"));
        assertEquals(option, optionGroup.getOptions().iterator().next());
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "A", false, "Test Option A");
        Option optionB = new Option("b", "B", false, "Test Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        
        optionGroup.setSelected(optionA);
        assertEquals(optionA.getKey(), optionGroup.getSelected());
        
        optionGroup.setSelected(optionB);
        assertEquals(optionB.getKey(), optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "A", false, "Test Option A");
        Option optionB = new Option("b", "B", false, "Test Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        
        optionGroup.setSelected(optionA);
        optionGroup.setSelected(optionB);
    }
    
    @Test
    public void testSetSelected_ResetSelection() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "A", false, "Test Option A");
        optionGroup.addOption(optionA);
        
        optionGroup.setSelected(optionA);
        assertEquals(optionA.getKey(), optionGroup.getSelected());
        
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        assertNull(optionGroup.getSelected());
        
        Option optionA = new Option("a", "A", false, "Test Option A");
        optionGroup.addOption(optionA);
        
        optionGroup.setSelected(optionA);
        assertEquals(optionA.getKey(), optionGroup.getSelected());
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        assertTrue(optionGroup.getOptions().isEmpty());
        
        Option optionA = new Option("a", "A", false, "Test Option A");
        Option optionB = new Option("b", "B", false, "Test Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(optionA));
        assertTrue(optionGroup.getOptions().contains(optionB));
    }
    
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());
        
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option optionA = new Option("a", "A", false, "Test Option A");
        Option optionB = new Option("b", "B", false, "Test Option B");
        optionGroup.addOption(optionA);
        optionGroup.addOption(optionB);
        
        assertEquals("[-a, --b]", optionGroup.toString());
    }
}