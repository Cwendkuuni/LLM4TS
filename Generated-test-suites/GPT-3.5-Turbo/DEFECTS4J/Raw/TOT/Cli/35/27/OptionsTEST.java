import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {
  
  @Test
  public void testAddOptionGroup() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    
    options.addOptionGroup(group);
    
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("add"));
    assertTrue(options.hasOption("r"));
    assertTrue(options.hasOption("remove"));
    
    assertEquals(option1, options.getOption("a"));
    assertEquals(option1, options.getOption("add"));
    
    assertEquals(option2, options.getOption("r"));
    assertEquals(option2, options.getOption("remove"));
  }
  
  @Test
  public void testAddOption() {
    Options options = new Options();
    
    Option option = new Option("t", "test", true, "Test option");
    
    options.addOption(option);
    
    assertTrue(options.hasOption("t"));
    assertTrue(options.hasOption("test"));
    
    assertEquals(option, options.getOption("t"));
    assertEquals(option, options.getOption("test"));
  }
  
  @Test
  public void testGetOptions() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    assertEquals(2, options.getOptions().size());
    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
  }
  
  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    option1.setRequired(true);
    option2.setRequired(true);
    
    options.addOption(option1);
    options.addOption(option2);
    
    assertEquals(2, options.getRequiredOptions().size());
    assertTrue(options.getRequiredOptions().contains(option1));
    assertTrue(options.getRequiredOptions().contains(option2));
  }
  
  @Test
  public void testGetOption() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    assertEquals(option1, options.getOption("a"));
    assertEquals(option1, options.getOption("add"));
    assertEquals(option2, options.getOption("r"));
    assertEquals(option2, options.getOption("remove"));
  }
  
  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", true, "Add option");
    Option option2 = new Option("r", "remove", true, "Remove option");
    Option option3 = new Option("c", "clear", true, "Clear option");
    
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);
    
    assertEquals(2, options.getMatchingOptions("a").size());
    assertTrue(options.getMatchingOptions("a").contains("add"));
    assertTrue(options.getMatchingOptions("a").contains("clear"));
    
    assertEquals(1, options.getMatchingOptions("r").size());
    assertTrue(options.getMatchingOptions("r").contains("remove"));
    
    assertEquals(1, options.getMatchingOptions("c").size());
    assertTrue(options.getMatchingOptions("c").contains("clear"));
  }
  
  @Test
  public void testHasOption() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("add"));
    assertTrue(options.hasOption("r"));
    assertTrue(options.hasOption("remove"));
    
    assertFalse(options.hasOption("t"));
  }
  
  @Test
  public void testHasLongOption() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    assertTrue(options.hasLongOption("add"));
    assertTrue(options.hasLongOption("remove"));
    
    assertFalse(options.hasLongOption("t"));
  }
  
  @Test
  public void testHasShortOption() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    assertTrue(options.hasShortOption("a"));
    assertTrue(options.hasShortOption("r"));
    
    assertFalse(options.hasShortOption("t"));
  }
  
  @Test
  public void testGetOptionGroup() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    
    options.addOptionGroup(group);
    
    assertEquals(group, options.getOptionGroup(option1));
    assertEquals(group, options.getOptionGroup(option2));
  }
  
  @Test
  public void testToString() {
    Options options = new Options();
    
    Option option1 = new Option("a", "add", false, "Add option");
    Option option2 = new Option("r", "remove", false, "Remove option");
    
    options.addOption(option1);
    options.addOption(option2);
    
    assertEquals("[ Options: [ short {a=Option: {[ short a , long add , description Add option , is required? false , is argument? false ]}} ] [ long {add=Option: {[ short a , long add , description Add option , is required? false , is argument? false ]}, remove=Option: {[ short r , long remove , description Remove option , is required? false , is argument? false ]}} ]", options.toString());
  }
}