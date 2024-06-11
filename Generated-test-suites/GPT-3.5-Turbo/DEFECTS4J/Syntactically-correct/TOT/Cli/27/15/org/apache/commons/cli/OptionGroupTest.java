import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    OptionGroup optionGroup = new OptionGroup();
    
    // Test adding valid options
    Option option1 = new Option("a", "option1", true, "Option 1");
    optionGroup.addOption(option1);
    assertEquals(1, optionGroup.getOptions().size());
    
    Option option2 = new Option("b", "option2", false, "Option 2");
    optionGroup.addOption(option2);
    assertEquals(2, optionGroup.getOptions().size());
    
    // Test adding null option
    try {
      optionGroup.addOption(null);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Option must not be null", e.getMessage());
    }
  }
  
  @Test
  public void testGetNames() {
    OptionGroup optionGroup = new OptionGroup();
    
    // Test empty group
    assertEquals(0, optionGroup.getNames().size());
    
    // Test group with options
    Option option1 = new Option("a", "option1", true, "Option 1");
    optionGroup.addOption(option1);
    
    Option option2 = new Option("b", "option2", false, "Option 2");
    optionGroup.addOption(option2);
    
    assertEquals(2, optionGroup.getNames().size());
    assertTrue(optionGroup.getNames().contains("a"));
    assertTrue(optionGroup.getNames().contains("b"));
  }
    
  @Test
  public void testGetOptions() {
    OptionGroup optionGroup = new OptionGroup();
    
    // Test empty group
    assertEquals(0, optionGroup.getOptions().size());
    
    // Test group with options
    Option option1 = new Option("a", "option1", true, "Option 1");
    optionGroup.addOption(option1);
    
    Option option2 = new Option("b", "option2", false, "Option 2");
    optionGroup.addOption(option2);
    
    assertEquals(2, optionGroup.getOptions().size());
    assertTrue(optionGroup.getOptions().contains(option1));
    assertTrue(optionGroup.getOptions().contains(option2));
  }
  
  @Test
  public void testSetSelected() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", true, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
    Option option3 = new Option("c", "option3", false, "Option 3");
    
    // Test setting selected option to null
    optionGroup.setSelected(null);
    assertNull(optionGroup.getSelected());
    
    // Test setting selected option to a valid option
    optionGroup.addOption(option1);
    optionGroup.setSelected(option1);
    assertEquals("a", optionGroup.getSelected());
    
    // Test setting selected option to an already selected option
    try {
      optionGroup.setSelected(option1);
      fail("Expected AlreadySelectedException to be thrown");
    } catch (AlreadySelectedException e) {
      assertEquals("The option 'option1' has already been selected.", e.getMessage());
    }
    
    // Test setting selected option to a different option
    optionGroup.addOption(option2);
    try {
      optionGroup.setSelected(option2);
      fail("Expected AlreadySelectedException to be thrown");
    } catch (AlreadySelectedException e) {
      assertEquals("The option 'option2' has already been selected.", e.getMessage());
    }
    
    // Test setting selected option after resetting
    optionGroup.setSelected(null);
    optionGroup.setSelected(option3);
    assertEquals("c", optionGroup.getSelected());
  }
  
  @Test
  public void testIsSelected() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", true, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
    
    // Test isSelected for unselected option group
    assertFalse(optionGroup.isSelected());
    
    // Test isSelected for selected option group
    optionGroup.addOption(option1);
    optionGroup.setSelected(option1);
    assertTrue(optionGroup.isSelected());
    
    // Test isSelected for reset option group
    optionGroup.setSelected(null);
    assertFalse(optionGroup.isSelected());
    
    // Test isSelected for required option group
    optionGroup.addOption(option2);
    optionGroup.setRequired(true);
    assertFalse(optionGroup.isSelected());
    
    // Test isSelected for required and selected option group
    optionGroup.setSelected(option2);
    assertTrue(optionGroup.isSelected());
  }
  
  @Test
  public void testSetRequired() {
    OptionGroup optionGroup = new OptionGroup();
    
    // Test setting required to true
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());
    
    // Test setting required to false
    optionGroup.setRequired(false);
    assertFalse(optionGroup.isRequired());
  }
  
  @Test
  public void testToString() {
    OptionGroup optionGroup = new OptionGroup();
    
    // Test toString for empty group
    String expectedEmptyString = "[]";
    assertEquals(expectedEmptyString, optionGroup.toString());
    
    // Test toString for group with options
    Option option1 = new Option("a", "option1", true, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    String expectedString = "[-a Option 1, -b Option 2]";
    assertEquals(expectedString, optionGroup.toString());
  }
}