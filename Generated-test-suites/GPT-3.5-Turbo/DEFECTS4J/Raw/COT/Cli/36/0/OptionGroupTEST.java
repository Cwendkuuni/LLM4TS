import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.*;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("a", "add", false, "Add option");
    optionGroup.addOption(option);
    
    Assert.assertEquals(1, optionGroup.getOptions().size());
    Assert.assertEquals(option, optionGroup.getOptions().iterator().next());
  }
  
  @Test
  public void testGetNames() {
    OptionGroup optionGroup = new OptionGroup();
    optionGroup.addOption(new Option("a", "add", false, "Add option"));
    optionGroup.addOption(new Option("r", "remove", false, "Remove option"));

    Assert.assertEquals(2, optionGroup.getNames().size());
    Assert.assertTrue(optionGroup.getNames().contains("a"));
    Assert.assertTrue(optionGroup.getNames().contains("r"));
  }
  
  @Test
  public void testGetOptions() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    Assert.assertEquals(2, optionGroup.getOptions().size());
    Assert.assertTrue(optionGroup.getOptions().contains(option1));
    Assert.assertTrue(optionGroup.getOptions().contains(option2));
  }
  
  @Test(expected=AlreadySelectedException.class)
  public void testSetSelectedAlreadySelectedException() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
  }
  
  @Test
  public void testSetSelected() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    
    optionGroup.setSelected(option1);
    
    Assert.assertEquals("a", optionGroup.getSelected());
  }
  
  @Test
  public void testGetSelected() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    optionGroup.setSelected(option1);
    
    Assert.assertEquals("a", optionGroup.getSelected());
  }
  
  @Test
  public void testSetRequired() {
    OptionGroup optionGroup = new OptionGroup();
    optionGroup.setRequired(true);
    
    Assert.assertTrue(optionGroup.isRequired());
  }
  
  @Test
  public void testToString() {
    OptionGroup optionGroup = new OptionGroup();
    optionGroup.addOption(new Option("a", "add", false, "Add option"));
    optionGroup.addOption(new Option("r", "remove", false, "Remove option"));
    
    Assert.assertEquals("[add, --remove Remove option]", optionGroup.toString());
  }
}