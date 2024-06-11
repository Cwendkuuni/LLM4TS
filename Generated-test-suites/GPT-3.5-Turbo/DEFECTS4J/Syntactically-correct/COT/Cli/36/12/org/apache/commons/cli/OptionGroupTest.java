
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {
    private OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add an option");
        OptionGroup result = optionGroup.addOption(option);
        assertNotNull(result);
        assertEquals(1, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option));
    }
    
    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "add", false, "Add an option");
        Option option2 = new Option("r", "remove", false, "Remove an option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        assertNotNull(names);
        assertEquals(2, names.size());
        assertTrue(names.contains("a"));
        assertTrue(names.contains("r"));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add an option");
        Option option2 = new Option("r", "remove", false, "Remove an option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<Option> options = optionGroup.getOptions();
        assertNotNull(options);
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
    
    @Test
    public void testSetSelected() {
        Option option = new Option("a", "add", false, "Add an option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals(option.getKey(), optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        Option option1 = new Option("a", "add", false, "Add an option");
        Option option2 = new Option("r", "remove", false, "Remove an option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        Option option = new Option("a", "add", false, "Add an option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        assertEquals(option.getKey(), optionGroup.getSelected());
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
        Option option1 = new Option("a", "add", false, "Add an option");
        Option option2 = new Option("r", "remove", false, "Remove an option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-a Add an option, -r Remove an option]";
        String result = optionGroup.toString();
        assertEquals(expected, result);
    }
}
