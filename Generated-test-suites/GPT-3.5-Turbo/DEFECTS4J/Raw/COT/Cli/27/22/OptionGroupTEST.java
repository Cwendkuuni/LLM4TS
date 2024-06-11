import org.junit.Test;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.AlreadySelectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", false, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
  
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    assertEquals(optionGroup.getOptions().size(), 2);
  }
  
  @Test
  public void testGetNames() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", false, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
  
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    assertEquals(optionGroup.getNames().size(), 2);
    assertTrue(optionGroup.getNames().contains("a"));
    assertTrue(optionGroup.getNames().contains("b"));
  }
  
  @Test
  public void testGetOptions() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", false, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
  
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    assertEquals(optionGroup.getOptions().size(), 2);
    assertTrue(optionGroup.getOptions().contains(option1));
    assertTrue(optionGroup.getOptions().contains(option2));
  }
  
  @Test
  public void testSetSelected() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", false, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
  
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    assertEquals(optionGroup.getSelected(), "a");
  }
  
  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedAlreadySelected() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", false, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
  
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }
  
  @Test
  public void testGetSelected() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", false, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
  
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    assertEquals(optionGroup.getSelected(), "a");
  }
  
  @Test
  public void testSetRequired() {
    OptionGroup optionGroup = new OptionGroup();
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());
    optionGroup.setRequired(false);
    assertFalse(optionGroup.isRequired());
  }
  
  @Test
  public void testToString() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "option1", false, "Option 1");
    Option option2 = new Option("b", "option2", false, "Option 2");
  
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    String expected = "[--option1 Option 1, --option2 Option 2]";
    String actual = optionGroup.toString();
    assertEquals(expected, actual);
  }
}