import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("f", "foo", false, "Option 1");
        option2 = new Option("b", "bar", false, "Option 2");
    }
    
    @Test
    public void testAddOption() {
        optionGroup.addOption(option1);
        assertEquals(1, optionGroup.getOptions().size());
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
    }
    
    @Test
    public void testGetNames() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getNames().size());
        assertTrue(optionGroup.getNames().contains("f"));
        assertTrue(optionGroup.getNames().contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        assertNull(optionGroup.getSelected());
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
    }
    
    @Test(expected=AlreadySelectedException.class)
    public void testSetSelectedException() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }
    
    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testIsRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[ -f Option 1, --bar Option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}