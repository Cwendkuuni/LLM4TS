import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

  @Test
  public void testAddOption() {
    Options options = new Options();
    Option option = new Option("a", "add", true, "Add option");
    
    options.addOption(option);
    
    Assert.assertEquals(option, options.getOption("a"));
    Assert.assertEquals(option, options.getOption("add"));
  }
  
  @Test
  public void testHasOption() {
    Options options = new Options();
    Option option = new Option("a", "add", true, "Add option");
    
    options.addOption(option);
    
    Assert.assertTrue(options.hasOption("a"));
    Assert.assertTrue(options.hasOption("add"));
    Assert.assertFalse(options.hasOption("b"));
  }
  
  @Test
  public void testGetOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "add", true, "Add option");
    Option option2 = new Option("b", "build", false, "Build option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    Assert.assertEquals(2, options.getOptions().size());
    Assert.assertTrue(options.getOptions().contains(option1));
    Assert.assertTrue(options.getOptions().contains(option2));
  }
  
  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "add", true, "Add option");
    Option option2 = new Option("b", "build", false, "Build option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    Assert.assertEquals(2, options.getMatchingOptions("a").size());
    Assert.assertTrue(options.getMatchingOptions("a").contains("add"));
    Assert.assertTrue(options.getMatchingOptions("a").contains("add"));
    Assert.assertTrue(options.getMatchingOptions("b").contains("build"));
    Assert.assertEquals(0, options.getMatchingOptions("c").size());
  }
  
  @Test
  public void testGetOptionGroup() {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "add", true, "Add option");
    Option option2 = new Option("b", "build", false, "Build option");
    
    group.addOption(option1);
    group.addOption(option2);
    
    options.addOptionGroup(group);
    
    Assert.assertEquals(group, options.getOptionGroup(option1));
    Assert.assertEquals(group, options.getOptionGroup(option2));
  }
  
  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "add", true, "Add option");
    Option option2 = new Option("b", "build", false, "Build option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    Assert.assertEquals(0, options.getRequiredOptions().size());
    
    option1.setRequired(true);
    Assert.assertEquals(1, options.getRequiredOptions().size());
    Assert.assertTrue(options.getRequiredOptions().contains(option1));
  }
  
  @Test
  public void testToString() {
    Options options = new Options();
    
    Assert.assertEquals("[ Options: [ short {} ] [ long {} ]", options.toString());
    
    Option option1 = new Option("a", "add", true, "Add option");
    Option option2 = new Option("b", "build", false, "Build option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    Assert.assertEquals("[ Options: [ short {a=Add option, b=Build option} ] [ long {} ]", options.toString());
  }

}