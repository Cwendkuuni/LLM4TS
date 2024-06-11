import org.junit.Before;
import org.junit.Test;
import java.util.Collection;

public class OptionGroupTest {
    
    private OptionGroup optionGroup;
    
    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
    }
    
    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add option");
        OptionGroup result = optionGroup.addOption(option);
        Collection<String> names = result.getNames();
        assertTrue(names.contains("a"));
    }
    
    @Test
    public void testGetNames() {
        Option option = new Option("b", "build", false, "Build option");
        optionGroup.addOption(option);
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("b"));
    }
    
    @Test
    public void testGetOptions() {
        Option option = new Option("c", "create", false, "Create option");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testSetSelected() {
        Option option = new Option("d", "delete", false, "Delete option");
        optionGroup.addOption(option);
        optionGroup.setSelected(option);
        String selected = optionGroup.getSelected();
        assertEquals("d", selected);
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedThrowsException() {
        Option option1 = new Option("e", "edit", false, "Edit option");
        optionGroup.addOption(option1);
        Option option2 = new Option("f", "find", false, "Find option");
        optionGroup.addOption(option2);
        
        // Both options cannot be selected at the same time
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testGetSelected() {
        Option option1 = new Option("g", "generate", false, "Generate option");
        optionGroup.addOption(option1);
        Option option2 = new Option("h", "help", false, "Help option");
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        String selected = optionGroup.getSelected();
        assertEquals("g", selected);
    }
    
    @Test
    public void testSetRequired() {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }
    
    @Test
    public void testToString() {
        Option option1 = new Option("i", "info", false, "Info option");
        optionGroup.addOption(option1);
        Option option2 = new Option("j", "java", false, "Java option");
        optionGroup.addOption(option2);
        String expected = "[--info Info option, --java Java option]";
        assertEquals(expected, optionGroup.toString());
    }
}