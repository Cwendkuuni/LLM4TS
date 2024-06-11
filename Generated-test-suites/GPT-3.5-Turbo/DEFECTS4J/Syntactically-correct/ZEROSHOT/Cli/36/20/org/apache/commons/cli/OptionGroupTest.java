import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "apple", false, "An apple option");
        OptionGroup result = optionGroup.addOption(option);
        
        assertNotNull(result);
        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getOptions().contains(option));
    }
    
    @Test
    public void testAddOption_NullInput() {
        OptionGroup result = optionGroup.addOption(null);
        
        assertNotNull(result);
        assertEquals(0, optionGroup.getNames().size());
        assertEquals(0, optionGroup.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "apple", false, "An apple option");
        Option option2 = new Option("b", "banana", false, "A banana option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<String> names = optionGroup.getNames();
        
        assertNotNull(names);
        assertEquals(2, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }
    
    @Test
    public void testGetNames_EmptyGroup() {
        Collection<String> names = optionGroup.getNames();
        
        assertNotNull(names);
        assertEquals(0, names.size());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "apple", false, "An apple option");
        Option option2 = new Option("b", "banana", false, "A banana option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<Option> options = optionGroup.getOptions();
        
        assertNotNull(options);
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testGetOptions_EmptyGroup() {
        Collection<Option> options = optionGroup.getOptions();
        
        assertNotNull(options);
        assertEquals(0, options.size());
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "An apple option");
        Option option2 = new Option("b", "banana", false, "A banana option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        assertEquals("a", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_OptionAlreadySelected() throws AlreadySelectedException {
        Option option1 = new Option("a", "apple", false, "An apple option");
        Option option2 = new Option("b", "banana", false, "A banana option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetSelected_NullInput() throws AlreadySelectedException {
        optionGroup.setSelected(null);
        
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "apple", false, "An apple option");
        Option option2 = new Option("b", "banana", false, "A banana option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        
        assertEquals("a", optionGroup.getSelected());
    }
    
    @Test
    public void testGetSelected_NoOptionSelected() {
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testSetRequired_False() {
        optionGroup.setRequired(false);
        
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "apple", false, "An apple option");
        Option option2 = new Option("b", "banana", false, "A banana option");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String result = optionGroup.toString();
        
        assertNotNull(result);
        assertEquals("[ -a An apple option, -b A banana option]", result);
    }
    
    @Test
    public void testToString_EmptyGroup() {
        String result = optionGroup.toString();
        
        assertNotNull(result);
        assertEquals("[]", result);
    }
}