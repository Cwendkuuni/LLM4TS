import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "option description");
        
        OptionGroup result = optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
        assertSame(optionGroup, result);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "option description");
        
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getNames().contains("o"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "option description");
        
        optionGroup.addOption(option);
        
        assertTrue(optionGroup.getOptions().contains(option));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "option description");
        
        optionGroup.addOption(option);
        
        optionGroup.setSelected(option);
        
        assertEquals("o", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_withAlreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "option1 description");
        Option option2 = new Option("o2", "option2", false, "option2 description");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "option description");
        
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        
        assertEquals("o", optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        
        optionGroup.setRequired(true);
        
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString_oneOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", false, "option description");
        optionGroup.addOption(option);
        
        String expected = "[-o option description]";
        
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToString_multipleOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "option1 description");
        Option option2 = new Option("o2", "option2", false, "option2 description");
        
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        String expected = "[-o1 option1 description, -o2 option2 description]";
        
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToString_noOptions() {
        OptionGroup optionGroup = new OptionGroup();
        
        String expected = "[]";
        
        assertEquals(expected, optionGroup.toString());
    }
}