import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.AlreadySelectedException;
import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    private Option option1;
    private Option option2;
    private Option option3;
    
    @Before
    public void setup() {
        optionGroup = new OptionGroup();
        
        option1 = new Option("a", "alpha", false, "Option A");
        option2 = new Option("b", "beta", false, "Option B");
        option3 = new Option("c", "gamma", false, "Option C");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }
    
    @Test
    public void testAddOption() {
        Option option4 = new Option("d", "delta", false, "Option D");
        optionGroup.addOption(option4);
        
        assertEquals(4, optionGroup.getOptions().size());
        assertEquals(option4, optionGroup.getOptions().toArray()[3]);
    }
    
    @Test
    public void testGetNames() {
        assertEquals(3, optionGroup.getNames().size());
        assertEquals(true, optionGroup.getNames().contains("a"));
        assertEquals(true, optionGroup.getNames().contains("b"));
        assertEquals(true, optionGroup.getNames().contains("c"));
    }
    
    @Test
    public void testGetOptions() {
        assertEquals(3, optionGroup.getOptions().size());
        assertEquals(option1, optionGroup.getOptions().toArray()[0]);
        assertEquals(option2, optionGroup.getOptions().toArray()[1]);
        assertEquals(option3, optionGroup.getOptions().toArray()[2]);
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("b", optionGroup.getSelected());
        
        optionGroup.setSelected(option3);
        assertEquals("c", optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testSetSelectedNull() throws AlreadySelectedException {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertEquals(true, optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertEquals(false, optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        assertEquals("[Option A, Option B, Option C]", optionGroup.toString());
    }
}