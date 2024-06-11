package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;

public class OptionsTest {

  //create an instance of the Options class to use in the test cases
  private Options options;

  //initialize the instance before each test case
  @Before
  public void setUp() {
    options = new Options();
  }

  //test addOptionGroup method
  //This method adds an OptionGroup to the Options instance
  @Test
  public void testAddOptionGroup() {
    OptionGroup group = new OptionGroup();
    options.addOptionGroup(group);
    assertEquals(1, options.getOptionGroups().size());
    assertTrue(options.getOptionGroups().contains(group));
  }

  //test addOption method with only short name
  //This method adds an option with only a short name to the Options instance
  @Test
  public void testAddOption_ShortName() {
    options.addOption("a", "description");
    assertTrue(options.hasOption("a"));
  }

  //test addOption method with short name and hasArg flag
  //This method adds an option with a short name and hasArg flag to the Options instance
  @Test
  public void testAddOption_ShortNameAndHasArg() {
    options.addOption("a", true, "description");
    assertTrue(options.hasOption("a"));
    Option option = options.getOption("a");
    assertTrue(option.hasArg());
  }

  //test addOption method with short name, long name, and hasArg flag
  //This method adds an option with a short name, long name, and hasArg flag to the Options instance
  @Test
  public void testAddOption_ShortNameLongNameAndHasArg() {
    options.addOption("a", "option", true, "description");
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option"));
    Option option = options.getOption("a");
    assertTrue(option.hasArg());
  }

  //test addOption method with Option instance
  //This method adds an option instance to the Options instance
  @Test
  public void testAddOption_OptionInstance() {
    Option option = new Option("a", false, "description");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
  }

  //test getOptions method
  //This method retrieves a collection of options in the Options instance
  @Test
  public void testGetOptions() {
    Option option = new Option("a", false, "description");
    options.addOption(option);
    assertEquals(1, options.getOptions().size());
    assertTrue(options.getOptions().contains(option));
  }

  //test getRequiredOptions method
  //This method retrieves a list of required options in the Options instance
  @Test
  public void testGetRequiredOptions() {
    Option option = new Option("a", false, "description");
    option.setRequired(true);
    options.addOption(option);
    assertEquals(1, options.getRequiredOptions().size());
    assertTrue(options.getRequiredOptions().contains(option));
  }

  //test getOption method
  //This method retrieves the option matching the specified short or long name
  @Test
  public void testGetOption() {
    Option option = new Option("a", false, "description");
    options.addOption(option);
    assertEquals(option, options.getOption("a"));
    assertEquals(option, options.getOption("description"));
  }

  //test getMatchingOptions method
  //This method retrieves the options with a long name starting with the specified name
  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("a", false, "description");
    Option option2 = new Option("abc", false, "description");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(1, options.getMatchingOptions("a").size());
    assertTrue(options.getMatchingOptions("a").contains("a"));
    assertEquals(2, options.getMatchingOptions("ab").size());
    assertTrue(options.getMatchingOptions("ab").contains("abc"));
  }

  //test hasOption method
  //This method checks if the named option is a member of the Options instance
  @Test
  public void testHasOption() {
    Option option = new Option("a", false, "description");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
  }

  //test hasLongOption method
  //This method checks if the named long option is a member of the Options instance
  @Test
  public void testHasLongOption() {
    Option option = new Option("a", false, "description");
    options.addOption(option);
    assertTrue(options.hasLongOption("description"));
  }

  //test hasShortOption method
  //This method checks if the named short option is a member of the Options instance
  @Test
  public void testHasShortOption() {
    Option option = new Option("a", false, "description");
    options.addOption(option);
    assertTrue(options.hasShortOption("a"));
  }

  //test getOptionGroup method
  //This method retrieves the OptionGroup the option belongs to
  @Test
  public void testGetOptionGroup() {
    Option option = new Option("a", false, "description");
    OptionGroup group = new OptionGroup();
    group.addOption(option);
    options.addOptionGroup(group);
    assertEquals(group, options.getOptionGroup(option));
  }

  //test toString method
  //This method converts the Options instance to a string representation
  @Test
  public void testToString() {
    Option option = new Option("a", false, "description");
    options.addOption(option);
    assertTrue(options.toString().contains("a"));
    assertTrue(options.toString().contains("description"));
  }

}