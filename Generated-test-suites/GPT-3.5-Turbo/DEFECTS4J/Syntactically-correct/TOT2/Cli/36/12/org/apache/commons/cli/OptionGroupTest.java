import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "description");
        
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "description");
        
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getNames().contains("o"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "description");
        
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals("o2", optionGroup.getSelected());

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
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
        Option option1 = new Option("o1", "option1", false, "description1");
        Option option2 = new Option("o2", "option2", false, "description2");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[-o1 description1, -o2 description2]";
        assertEquals(expected, optionGroup.toString());
    }
}