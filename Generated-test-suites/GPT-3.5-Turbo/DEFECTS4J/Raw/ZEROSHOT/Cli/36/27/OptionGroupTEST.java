import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import java.util.Collection;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "option a");
        OptionGroup result = optionGroup.addOption(option);
        assertTrue(result.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Failed to set selected option: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetSelected_AlreadySelectedException() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSetSelected_NullOption() {
        Option option1 = new Option("a", "option a");
        optionGroup.addOption(option1);
        try {
            optionGroup.setSelected(null);
            assertNull(optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Failed to set selected option: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSelected() {
        Option option = new Option("a", "option a");
        optionGroup.addOption(option);
        try {
            optionGroup.setSelected(option);
            assertEquals("a", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Failed to set selected option: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a option a, -b option b]";
        assertEquals(expected, optionGroup.toString());
    }
}