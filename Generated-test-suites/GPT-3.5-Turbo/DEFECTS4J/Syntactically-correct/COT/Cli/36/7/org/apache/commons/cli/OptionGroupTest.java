import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    // Create a new OptionGroup
    OptionGroup optionGroup = new OptionGroup();
    
    // Create an Option to add to the OptionGroup
    Option option = new Option("a", "optionA", false, "Option A");
    
    // Add the Option to the OptionGroup
    optionGroup.addOption(option);
    
    // Get the names of the options in the OptionGroup
    Collection<String> optionNames = optionGroup.getNames();
    
    // Check that the option was added to the OptionGroup
    assertTrue(optionNames.contains("a"));
  }
  
  @Test
  public void testGetNames() {
    // Create a new OptionGroup
    OptionGroup optionGroup = new OptionGroup();
    
    // Create an Option to add to the OptionGroup
    Option option = new Option("a", "optionA", false, "Option A");
    
    // Add the Option to the OptionGroup
    optionGroup.addOption(option);
    
    // Get the names of the options in the OptionGroup
    Collection<String> optionNames = optionGroup.getNames();
    
    // Check that the correct option names are returned
    assertTrue(optionNames.contains("a"));
    assertFalse(optionNames.contains("b"));
  }
  
  @Test
  public void testGetOptions() {
    // Create a new OptionGroup
    OptionGroup optionGroup = new OptionGroup();
    
    // Create an Option to add to the OptionGroup
    Option option = new Option("a", "optionA", false, "Option A");
    
    // Add the Option to the OptionGroup
    optionGroup.addOption(option);
    
    // Get the options in the OptionGroup
    Collection<Option> options = optionGroup.getOptions();
    
    // Check that the correct options are returned
    assertTrue(options.contains(option));
  }
  
  @Test
  public void testSetSelected() throws AlreadySelectedException {
    // Create a new OptionGroup
    OptionGroup optionGroup = new OptionGroup();
    
    // Create two Options to add to the OptionGroup
    Option option1 = new Option("a", "optionA", false, "Option A");
    Option option2 = new Option("b", "optionB", false, "Option B");
    
    // Add the Options to the OptionGroup
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    // Set the selected option of the OptionGroup to option1
    optionGroup.setSelected(option1);
    
    // Check that the selected option is option1
    assertEquals(option1.getKey(), optionGroup.getSelected());
    
    // Set the selected option of the OptionGroup to option2
    optionGroup.setSelected(option2);
    
    // Check that the selected option is option2
    assertEquals(option2.getKey(), optionGroup.getSelected());
    
    // Attempt to set the selected option of the OptionGroup to null
    optionGroup.setSelected(null);
    
    // Check that the selected option is null
    assertNull(optionGroup.getSelected());
  }
  
  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedAlreadySelectedException() throws AlreadySelectedException {
    // Create a new OptionGroup
    OptionGroup optionGroup = new OptionGroup();
    
    // Create two Options to add to the OptionGroup
    Option option1 = new Option("a", "optionA", false, "Option A");
    Option option2 = new Option("b", "optionB", false, "Option B");
    
    // Add the Options to the OptionGroup
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    // Set the selected option of the OptionGroup to option1
    optionGroup.setSelected(option1);
    
    // Attempt to set the selected option of the OptionGroup to option2
    optionGroup.setSelected(option2);
  }
  
  @Test
  public void testIsRequired() {
    // Create a new OptionGroup
    OptionGroup optionGroup = new OptionGroup();
    
    // Set the required flag of the OptionGroup to true
    optionGroup.setRequired(true);
    
    // Check that the OptionGroup is required
    assertTrue(optionGroup.isRequired());
    
    // Set the required flag of the OptionGroup to false
    optionGroup.setRequired(false);
    
    // Check that the OptionGroup is not required
    assertFalse(optionGroup.isRequired());
  }
  
  @Test
  public void testToString() {
    // Create a new OptionGroup
    OptionGroup optionGroup = new OptionGroup();
    
    // Create two Options to add to the OptionGroup
    Option option1 = new Option("a", "optionA", false, "Option A");
    Option option2 = new Option("b", "optionB", false, "Option B");
    
    // Add the Options to the OptionGroup
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    // Get the string representation of the OptionGroup
    String optionGroupString = optionGroup.toString();
    
    // Check that the correct string representation is returned
    assertEquals("[a, b]", optionGroupString);
  }
}