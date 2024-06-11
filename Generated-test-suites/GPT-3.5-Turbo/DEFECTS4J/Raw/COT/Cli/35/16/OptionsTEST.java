import org.junit.Test;
import org.junit.Assert;

public class OptionsTest {
  
  @Test
  public void testAddOptionGroup() {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    group.addOption(new Option("a", "aaa", true, "option a"));
    group.addOption(new Option("b", "bbb", false, "option b"));
    options.addOptionGroup(group);
    
    Assert.assertEquals(2, options.getOptions().size());
    Assert.assertEquals(1, options.getOptionGroups().size());
  }
  
  @Test
  public void testAddOption() {
    Options options = new Options();
    options.addOption(new Option("c", "ccc", true, "option c"));
    options.addOption("d", false, "option d");
    options.addOption("e", "eee", true, "option e");
    
    Assert.assertEquals(3, options.getOptions().size());
  }
  
  @Test
  public void testGetOption() {
    Options options = new Options();
    options.addOption(new Option("f", "fff", true, "option f"));
    options.addOption("g", false, "option g");
    
    Option optionF = options.getOption("f");
    Assert.assertEquals("f", optionF.getOpt());
    
    Option optionG = options.getOption("g");
    Assert.assertEquals("g", optionG.getOpt());
  }
  
  @Test
  public void testHasOption() {
    Options options = new Options();
    options.addOption(new Option("h", "hhh", true, "option h"));
    
    Assert.assertTrue(options.hasOption("h"));
    Assert.assertFalse(options.hasOption("i"));
  }
  
  @Test
  public void testGetOptionGroup() {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    group.addOption(new Option("j", "jjj", true, "option j"));
    group.addOption(new Option("k", "kkk", false, "option k"));
    options.addOptionGroup(group);
    
    Option optionJ = group.getOptions().get(0);
    OptionGroup resultGroup = options.getOptionGroup(optionJ);
    
    Assert.assertEquals(group, resultGroup);
  }
  
  @Test
  public void testToString() {
    Options options = new Options();
    options.addOption(new Option("l", "lll", true, "option l"));
    options.addOption(new Option("m", "mmm", false, "option m"));
    
    String optionsString = options.toString();
    String expectedString = "[ Options: [ short {l=option l, m=option m} ] [ long {} ]";
    
    Assert.assertEquals(expectedString, optionsString);
  }
}