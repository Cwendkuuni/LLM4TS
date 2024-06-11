import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);

        assertTrue(optionGroup.getNames().contains("a"));
        assertFalse(optionGroup.getNames().contains("b"));
        
        optionGroup.addOption(option2);
        assertTrue(optionGroup.getNames().contains("b"));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            assertEquals("a", optionGroup.getSelected());
            optionGroup.setSelected(option2);
            assertEquals("b", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option1);
            fail("Expected AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "alpha", false, "Option Alpha");
        Option option2 = new Option("b", "beta", false, "Option Beta");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[ -a Option Alpha, -b Option Beta]", optionGroup.toString());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

}