import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionGroupTest {
  
  @Test
  public void testAddOption() {
    OptionGroup group = new OptionGroup();
    
    Option option = new Option("a", true, "Option A");
    OptionGroup result = group.addOption(option);
    
    assertEquals(group, result);
    assertTrue(group.getOptions().contains(option));
  }
  
  @Test
  public void testGetNames() {
    OptionGroup group = new OptionGroup();
    
    Option option = new Option("a", true, "Option A");
    group.addOption(option);
    
    Collection names = group.getNames();
    
    assertEquals(1, names.size());
    assertTrue(names.contains("a"));
  }
  
  @Test
  public void testGetOptions() {
    OptionGroup group = new OptionGroup();
    
    Option option = new Option("a", true, "Option A");
    group.addOption(option);
    
    Collection options = group.getOptions();
    
    assertEquals(1, options.size());
    assertTrue(options.contains(option));
  }
  
  @Test
  public void testSetSelected() throws AlreadySelectedException {
    OptionGroup group = new OptionGroup();
    
    Option option1 = new Option("a", true, "Option A");
    Option option2 = new Option("b", true, "Option B");
    
    group.addOption(option1);
    group.addOption(option2);
    
    group.setSelected(option2);
    
    assertEquals("b", group.getSelected());
  }
  
  @Test(expected = AlreadySelectedException.class)
  public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
    OptionGroup group = new OptionGroup();
    
    Option option1 = new Option("a", true, "Option A");
    Option option2 = new Option("b", true, "Option B");
    
    group.addOption(option1);
    group.addOption(option2);
    
    group.setSelected(option1);
    group.setSelected(option2);
  }
  
  @Test
  public void testSetSelected_NullOption() throws AlreadySelectedException {
    OptionGroup group = new OptionGroup();
    
    Option option1 = new Option("a", true, "Option A");
    Option option2 = new Option("b", true, "Option B");
    
    group.addOption(option1);
    group.addOption(option2);
    
    group.setSelected(option1);
    group.setSelected(null);
    
    assertNull(group.getSelected());
  }
  
  @Test
  public void testIsRequired() {
    OptionGroup group = new OptionGroup();
    
    group.setRequired(true);
    
    assertTrue(group.isRequired());
  }
  
  @Test
  public void testToString() {
    OptionGroup group = new OptionGroup();
    
    Option option1 = new Option("a", true, "Option A");
    Option option2 = new Option("b", true, "Option B");
    
    group.addOption(option1);
    group.addOption(option2);
    
    String result = group.toString();
    
    assertEquals("[ -a Option A, -b Option B ]", result);
  }
  
}