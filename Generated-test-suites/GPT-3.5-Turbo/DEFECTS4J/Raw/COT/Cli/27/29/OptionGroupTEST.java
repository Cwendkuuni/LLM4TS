import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;
import static org.junit.Assert.*;

public class OptionGroupTest {
  
  private OptionGroup optionGroup;
  
  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("f", "file", false, "File option");
    optionGroup.addOption(option);
    
    // Check if the option is added to the group
    assertTrue(optionGroup.getOptions().contains(option));
  }
  
  @Test
  public void testGetNames() {
    Option option = new Option("f", "file", false, "File option");
    optionGroup.addOption(option);
    
    // Get the names of the options in the group
    assertEquals(1, optionGroup.getNames().size());
    assertTrue(optionGroup.getNames().contains("f"));
  }
  
  @Test
  public void testGetOptions() {
    Option option = new Option("f", "file", false, "File option");
    optionGroup.addOption(option);
    
    // Get the options in the group
    assertEquals(1, optionGroup.getOptions().size());
    assertTrue(optionGroup.getOptions().contains(option));
  }
  
  @Test
  public void testSetSelected() throws AlreadySelectedException {
    Option option1 = new Option("f", "file", false, "File option");
    Option option2 = new Option("d", "directory", false, "Directory option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    // Set the selected option to option1
    optionGroup.setSelected(option1);
    
    // Check if the selected option is option1
    assertEquals("f", optionGroup.getSelected());
    
    // Set the selected option to option2
    optionGroup.setSelected(option2);
    
    // Check if the selected option is option2
    assertEquals("d", optionGroup.getSelected());
  }
  
  @Test(expected = AlreadySelectedException.class)
  public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
    Option option1 = new Option("f", "file", false, "File option");
    Option option2 = new Option("d", "directory", false, "Directory option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    // Set the selected option to option1
    optionGroup.setSelected(option1);
    
    // Set the selected option to option2, which should throw AlreadySelectedException
    optionGroup.setSelected(option2);
  }
  
  @Test
  public void testGetSelected() throws AlreadySelectedException {
    Option option1 = new Option("f", "file", false, "File option");
    Option option2 = new Option("d", "directory", false, "Directory option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    // Set the selected option to option1
    optionGroup.setSelected(option1);
    
    // Check if the selected option is option1
    assertEquals("f", optionGroup.getSelected());
    
    // Set the selected option to option2
    optionGroup.setSelected(option2);
    
    // Check if the selected option is option2
    assertEquals("d", optionGroup.getSelected());
  }
  
  @Test
  public void testSetRequired() {
    // Set the group as required
    optionGroup.setRequired(true);
    
    // Check if the group is required
    assertTrue(optionGroup.isRequired());
  }
  
  @Test
  public void testToString() {
    Option option1 = new Option("f", "file", false, "File option");
    Option option2 = new Option("d", "directory", false, "Directory option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    // Get the string representation of the group
    String expected = "[ -f File option, --directory Directory option ]";
    assertEquals(expected, optionGroup.toString());
  }
}