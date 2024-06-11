import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.*;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<String> names = optionGroup.getNames();
        
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<String> names = optionGroup.getNames();
        
        assertEquals(2, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection<Option> options = optionGroup.getOptions();
        
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        try {
            optionGroup.setSelected(option1);
            String selected = optionGroup.getSelected();
            assertEquals("a", selected);
            
            optionGroup.setSelected(option2);
            selected = optionGroup.getSelected();
            assertEquals("b", selected);
        } catch (AlreadySelectedException e) {
            fail("AlreadySelectedException should not be thrown");
        }
    }

    @Test
    public void testSetSelected_Exception() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1);
        
        try {
            optionGroup.setSelected(option2);
            fail("AlreadySelectedException should be thrown");
        } catch (AlreadySelectedException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        optionGroup.addOption(option1);
        
        try {
            optionGroup.setSelected(option1);
            String selected = optionGroup.getSelected();
            assertEquals("a", selected);
            
            optionGroup.setSelected(option2);
            selected = optionGroup.getSelected();
            assertEquals("b", selected);
        } catch (AlreadySelectedException e) {
            fail("AlreadySelectedException should not be thrown");
        }
    }

    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        boolean required = optionGroup.isRequired();
        assertTrue(required);
        
        optionGroup.setRequired(false);
        required = optionGroup.isRequired();
        assertFalse(required);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option1");
        Option option2 = new Option("b", "option2");
        
        option1.setDescription("Description for option1");
        option2.setDescription("Description for option2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[ -a Description for option1, -b Description for option2 ]";
        String result = optionGroup.toString();
        
        assertEquals(expected, result);
    }
}