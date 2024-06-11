import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
  
  private OptionGroup optionGroup;
  private Option option1;
  private Option option2;
  
  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
    option1 = new Option("a", "option1", false, "Option 1");
    option2 = new Option("b", "option2", true, "Option 2");
  }
  
  @Test
  public void testAddOption() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    assertTrue(optionGroup.getOptions().contains(option1));
    assertTrue(optionGroup.getOptions().contains(option2));
  }
  
  @Test
  public void testGetNames() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    assertTrue(optionGroup.getNames().contains("a"));
    assertTrue(optionGroup.getNames().contains("b"));
  }
  
  @Test
  public void testGetOptions() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    assertTrue(optionGroup.getOptions().contains(option1));
    assertTrue(optionGroup.getOptions().contains(option2));
  }
  
  @Test
  public void testSetSelected() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    try {
      optionGroup.setSelected(option1);
      assertEquals("a", optionGroup.getSelected());
      
      optionGroup.setSelected(option2);
      assertEquals("b", optionGroup.getSelected());
    } catch (AlreadySelectedException e) {
      fail("Should not throw AlreadySelectedException");
    }
  }
  
  @Test(expected = AlreadySelectedException.class)
  public void testSetSelected_AlreadySelectedException() throws AlreadySelectedException {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }
  
  @Test
  public void testGetSelected() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    try {
      optionGroup.setSelected(option1);
      assertEquals("a", optionGroup.getSelected());
      
      optionGroup.setSelected(option2);
      assertEquals("b", optionGroup.getSelected());
    } catch (AlreadySelectedException e) {
      fail("Should not throw AlreadySelectedException");
    }
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
    String expected = "[--option1 Option 1, -b Option 2]";
    
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    assertEquals(expected, optionGroup.toString());
  }
}