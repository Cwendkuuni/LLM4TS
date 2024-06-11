import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;

public class OptionGroupTest {
  
  @Test
  public void testAddOption() {
    OptionGroup group = new OptionGroup();
    Option option = new Option("t", "test", false, "test option");
    
    group.addOption(option);
    
    Collection options = group.getOptions();
    
    assertTrue(options.contains(option));
  }
  
  @Test
  public void testGetNames() {
    OptionGroup group = new OptionGroup();
    Option option = new Option("t", "test", false, "test option");
    
    group.addOption(option);
    
    Collection names = group.getNames();
    
    assertTrue(names.contains("t"));
  }
  
  @Test
  public void testGetOptions() {
    OptionGroup group = new OptionGroup();
    Option option = new Option("t", "test", false, "test option");
    
    group.addOption(option);
    
    Collection options = group.getOptions();
    
    assertTrue(options.contains(option));
  }
  
  @Test
  public void testSetSelected() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("t", "test1", false, "test option 1");
    Option option2 = new Option("u", "test2", false, "test option 2");
    
    group.addOption(option1);
    group.addOption(option2);
    
    group.setSelected(option1);
    
    assertEquals("t", group.getSelected());
  }
  
  @Test
  public void testIsSelectedWithAlreadySelectedException() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("t", "test1", false, "test option 1");
    Option option2 = new Option("u", "test2", false, "test option 2");
    
    group.addOption(option1);
    group.addOption(option2);
    
    group.setSelected(option1);
    
    try {
      group.setSelected(option2);
      fail("Expected AlreadySelectedException");
    } catch (AlreadySelectedException e) {
      // Expected exception
    }
  }
  
  @Test
  public void testSetRequiredAndGetRequired() {
    OptionGroup group = new OptionGroup();
    
    group.setRequired(true);
    
    assertTrue(group.isRequired());
  }

}