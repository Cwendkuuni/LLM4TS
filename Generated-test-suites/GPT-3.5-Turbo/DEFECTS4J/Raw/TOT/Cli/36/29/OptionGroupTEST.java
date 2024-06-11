import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    OptionGroup group = new OptionGroup();
    
    // Test adding an option to the group
    Option option1 = new Option("a", "aaa", false, "Option A");
    group.addOption(option1);
    assertEquals(group.getOptions().size(), 1);
    
    // Test adding a duplicate option to the group
    Option option2 = new Option("b", "bbb", false, "Option B");
    group.addOption(option2);
    group.addOption(option2);
    assertEquals(group.getOptions().size(), 2);
  }

  @Test
  public void testGetNames() {
    OptionGroup group = new OptionGroup();
    
    // Test getting names when no options are added
    assertTrue(group.getNames().isEmpty());
    
    // Test getting names when options are added
    Option option1 = new Option("a", "aaa", false, "Option A");
    Option option2 = new Option("b", "bbb", false, "Option B");
    group.addOption(option1);
    group.addOption(option2);
    assertEquals(group.getNames().size(), 2);
    assertTrue(group.getNames().contains("a"));
    assertTrue(group.getNames().contains("b"));
  }

  @Test
  public void testGetOptions() {
    OptionGroup group = new OptionGroup();
    
    // Test getting options when no options are added
    assertTrue(group.getOptions().isEmpty());
    
    // Test getting options when options are added
    Option option1 = new Option("a", "aaa", false, "Option A");
    Option option2 = new Option("b", "bbb", false, "Option B");
    group.addOption(option1);
    group.addOption(option2);
    assertEquals(group.getOptions().size(), 2);
    assertTrue(group.getOptions().contains(option1));
    assertTrue(group.getOptions().contains(option2));
  }

  @Test(expected = AlreadySelectedException.class)
  public void testSetSelected() {
    OptionGroup group = new OptionGroup();
    
    // Test setting a selected option when no options are added
    group.setSelected(null);
    assertNull(group.getSelected());
    
    // Test selecting an option
    Option option1 = new Option("a", "aaa", false, "Option A");
    Option option2 = new Option("b", "bbb", false, "Option B");
    group.addOption(option1);
    group.addOption(option2);
    group.setSelected(option1);
    assertEquals(group.getSelected(), option1.getKey());
    
    // Test selecting an option that is already selected
    group.setSelected(option1);
    
    // Test selecting a different option
    group.setSelected(option2);
    assertEquals(group.getSelected(), option2.getKey());
    
    // Test selecting a different option when an option is already selected
    group.setSelected(option1);
    assertEquals(group.getSelected(), option1.getKey());
  }

  @Test
  public void testSetRequired() {
    OptionGroup group = new OptionGroup();
    
    // Test setting required flag to true
    group.setRequired(true);
    assertTrue(group.isRequired());
    
    // Test setting required flag to false
    group.setRequired(false);
    assertFalse(group.isRequired());
  }

  @Test
  public void testToString() {
    OptionGroup group = new OptionGroup();
    
    // Test toString when no options are added
    assertEquals(group.toString(), "[]");
    
    // Test toString when options are added
    Option option1 = new Option("a", "aaa", false, "Option A");
    Option option2 = new Option("b", "bbb", false, "Option B");
    group.addOption(option1);
    group.addOption(option2);
    assertEquals(group.toString(), "[-a Option A, -b Option B]");
  }

}