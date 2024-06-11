import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        Collection names = optionGroup.getNames();
        assertTrue(names.contains("a"));
        assertTrue(names.contains("b"));
        
        Collection options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());

        // Test AlreadySelectedException for option2
        try {
            optionGroup.setSelected(option2);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // Exception expected
        }
        
        // Test resetting the selected option
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
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
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-a Option 1, -b Option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}