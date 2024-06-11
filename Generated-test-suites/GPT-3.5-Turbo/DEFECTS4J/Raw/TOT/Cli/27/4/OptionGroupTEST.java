import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {
  
  Option option1;
  Option option2;
  Option option3;
  OptionGroup optionGroup;
  
  @Before
  public void setUp() {
    option1 = new Option("a", "option1", false, "Option 1");
    option2 = new Option("b", "option2", false, "Option 2");
    option3 = new Option("c", "option3", false, "Option 3");
    optionGroup = new OptionGroup();
  }
  
  @Test
  public void testAddOption() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    optionGroup.addOption(option3);

    assertTrue(optionGroup.getOptions().contains(option1));
    assertTrue(optionGroup.getOptions().contains(option2));
    assertTrue(optionGroup.getOptions().contains(option3));
  }
  
  @Test
  public void testGetNames() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    optionGroup.addOption(option3);
    
    assertEquals(3, optionGroup.getNames().size());
    assertTrue(optionGroup.getNames().contains(option1.getKey()));
    assertTrue(optionGroup.getNames().contains(option2.getKey()));
    assertTrue(optionGroup.getNames().contains(option3.getKey()));
  }
  
  @Test
  public void testGetOptions() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    optionGroup.addOption(option3);
    
    assertEquals(3, optionGroup.getOptions().size());
    assertTrue(optionGroup.getOptions().contains(option1));
    assertTrue(optionGroup.getOptions().contains(option2));
    assertTrue(optionGroup.getOptions().contains(option3));
  }
  
  @Test
  public void testSetSelected() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    optionGroup.addOption(option3);
    
    try {
      optionGroup.setSelected(option1);
      assertEquals(option1.getOpt(), optionGroup.getSelected());
    } catch (AlreadySelectedException e) {
      fail("Unexpected AlreadySelectedException");
    }
  }
  
  @Test(expected=AlreadySelectedException.class)
  public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }
  
  @Test
  public void testSetSelectedNull() {
    optionGroup.addOption(option1);
    optionGroup.setSelected(option1);
    
    optionGroup.setSelected(null);
    assertNull(optionGroup.getSelected());
  }
  
  @Test
  public void testGetSelected() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    assertNull(optionGroup.getSelected());
    
    optionGroup.setSelected(option1);
    assertEquals(option1.getOpt(), optionGroup.getSelected());
    
    optionGroup.setSelected(null);
    assertNull(optionGroup.getSelected());
  }
  
  @Test
  public void testSetRequired() {
    assertFalse(optionGroup.isRequired());
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());
  }
  
  @Test
  public void testIsRequired() {
    assertFalse(optionGroup.isRequired());
    
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());
    
    optionGroup.setRequired(false);
    assertFalse(optionGroup.isRequired());
  }
  
  @Test
  public void testToString() {
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    optionGroup.addOption(option3);
    
    String expected = "[" + "-" + option1.getOpt() + " " + option1.getDescription() +
                      ", " + "-" + option2.getOpt() + " " + option2.getDescription() +
                      ", " + "-" + option3.getOpt() + " " + option3.getDescription() + "]";
    
    assertEquals(expected, optionGroup.toString());
  }
}