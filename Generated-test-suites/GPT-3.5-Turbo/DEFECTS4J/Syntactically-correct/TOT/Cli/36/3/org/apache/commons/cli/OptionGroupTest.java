import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class OptionGroupTest {
  
  private OptionGroup optionGroup;
  
  @Before
  public void setUp() {
    optionGroup = new OptionGroup();
  }
  
  @After
  public void tearDown() {
    optionGroup = null;
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("a", true, "option a");
    optionGroup.addOption(option);
    assertEquals(1, optionGroup.getOptions().size());
  }
  
  @Test
  public void testGetNames() {
    Option option1 = new Option("a", true, "option a");
    Option option2 = new Option("b", true, "option b");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    assertEquals(2, optionGroup.getNames().size());
  }
  
  @Test
  public void testGetOptions() {
    Option option1 = new Option("a", true, "option a");
    Option option2 = new Option("b", true, "option b");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    assertEquals(2, optionGroup.getOptions().size());
  }
  
  @Test
  public void testSetSelected() {
    Option option1 = new Option("a", true, "option a");
    Option option2 = new Option("b", true, "option b");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    optionGroup.setSelected(option1);
    assertEquals("a", optionGroup.getSelected());
    
    optionGroup.setSelected(option2);
    assertEquals("b", optionGroup.getSelected());
  }
  
  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedAlreadySelectedException() {
    Option option1 = new Option("a", true, "option a");
    Option option2 = new Option("b", true, "option b");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2); // should throw AlreadySelectedException
  }
  
  @Test
  public void testGetSelected() {
    Option option1 = new Option("a", true, "option a");
    Option option2 = new Option("b", true, "option b");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    optionGroup.setSelected(option1);
    assertEquals("a", optionGroup.getSelected());
    
    optionGroup.setSelected(option2);
    assertEquals("b", optionGroup.getSelected());
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
    Option option1 = new Option("a", true, "option a");
    Option option2 = new Option("b", true, "option b");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    String expected = "[--a option a, --b option b]";
    assertEquals(expected, optionGroup.toString());
  }
}