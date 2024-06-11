import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        optionGroup.addOption(option1);
        assertTrue(optionGroup.getOptions().contains(option1));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        optionGroup.addOption(option1);
        assertTrue(optionGroup.getNames().contains("a"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        optionGroup.addOption(option1);
        assertTrue(optionGroup.getOptions().contains(option1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a");
        Option option2 = new Option("b");
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
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", null, false, "Option A");
        Option option2 = new Option(null, "aa", false, "Option AA");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[ -a Option A, --aa Option AA]", optionGroup.toString());
    }
}