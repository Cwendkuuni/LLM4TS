import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "optA", false, "Option A");
        Option option2 = new Option("b", "optB", false, "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "optA", false, "Option A");
        Option option2 = new Option("b", "optB", false, "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertTrue(optionGroup.getNames().contains("a"));
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "optA", false, "Option A");
        Option option2 = new Option("b", "optB", false, "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
        
        try {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "optA", false, "Option A");
        Option option2 = new Option("b", "optB", false, "Option B");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expectedString = "[-a Option A, -b Option B]";
        assertEquals(expectedString, optionGroup.toString());
    }
}