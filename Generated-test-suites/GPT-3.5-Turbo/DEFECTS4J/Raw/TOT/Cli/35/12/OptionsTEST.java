import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class OptionsTest {

  private Options options;

  @Before
  public void setUp() {
    options = new Options();
  }

  // Testing addOptionGroup()
  @Test
  public void testAddOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);

    List<OptionGroup> optionGroups = new ArrayList<>(options.getOptionGroups());
    Assert.assertEquals(1, optionGroups.size());
    Assert.assertEquals(group, optionGroups.get(0));
    
    // Testing required options
    List requiredOptions = options.getRequiredOptions();
    Assert.assertFalse(requiredOptions.contains(option1.getKey()));
    Assert.assertFalse(requiredOptions.contains(option2.getKey()));
  }

  // Testing addOption(String opt, String description)
  @Test
  public void testAddOptionWithShortNameAndDescription() {
    String opt = "a";
    String description = "This is option a";
    options.addOption(opt, description);

    // Getting the added option
    Option addedOption = options.getOption(opt);
    Assert.assertNotNull(addedOption);
    Assert.assertEquals(opt, addedOption.getOpt());
    Assert.assertEquals(description, addedOption.getDescription());

    // Testing required options
    List requiredOptions = options.getRequiredOptions();
    Assert.assertFalse(requiredOptions.contains(opt));
  }

  // Testing addOption(String opt, boolean hasArg, String description)
  @Test
  public void testAddOptionWithShortNameAndHasArg() {
    String opt = "a";
    boolean hasArg = true;
    String description = "This is option a with argument";
    options.addOption(opt, hasArg, description);

    // Getting the added option
    Option addedOption = options.getOption(opt);
    Assert.assertNotNull(addedOption);
    Assert.assertEquals(opt, addedOption.getOpt());
    Assert.assertEquals(hasArg, addedOption.hasArg());
    Assert.assertEquals(description, addedOption.getDescription());

    // Testing required options
    List requiredOptions = options.getRequiredOptions();
    Assert.assertFalse(requiredOptions.contains(opt));
  }

  // Testing addOption(String opt, String longOpt, boolean hasArg, String description)
  @Test
  public void testAddOptionWithShortNameLongNameAndHasArg() {
    String opt = "a";
    String longOpt = "optionA";
    boolean hasArg = true;
    String description = "This is option a with argument";
    options.addOption(opt, longOpt, hasArg, description);

    // Getting the added option
    Option addedOption = options.getOption(opt);
    Assert.assertNotNull(addedOption);
    Assert.assertEquals(opt, addedOption.getOpt());
    Assert.assertEquals(longOpt, addedOption.getLongOpt());
    Assert.assertEquals(hasArg, addedOption.hasArg());
    Assert.assertEquals(description, addedOption.getDescription());

    // Testing required options
    List requiredOptions = options.getRequiredOptions();
    Assert.assertFalse(requiredOptions.contains(opt));
  }

  // Testing getOptions()
  @Test
  public void testGetOptions() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    options.addOption(option1);
    options.addOption(option2);

    Collection<Option> optionList = options.getOptions();
    Assert.assertEquals(2, optionList.size());
    Assert.assertTrue(optionList.contains(option1));
    Assert.assertTrue(optionList.contains(option2));
  }

  // Testing getRequiredOptions()
  @Test
  public void testGetRequiredOptions() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    option1.setRequired(true);
    option2.setRequired(true);
    options.addOption(option1);
    options.addOption(option2);

    List requiredOptions = options.getRequiredOptions();
    Assert.assertEquals(2, requiredOptions.size());
    Assert.assertTrue(requiredOptions.contains(option1.getKey()));
    Assert.assertTrue(requiredOptions.contains(option2.getKey()));
  }

  // Testing getOption(String opt)
  @Test
  public void testGetOptionWithShortName() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    options.addOption(option1);
    options.addOption(option2);

    Option retrievedOption = options.getOption("a");
    Assert.assertEquals(option1, retrievedOption);
  }

  // Testing getOption(String opt)
  @Test
  public void testGetOptionWithLongName() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    options.addOption(option1);
    options.addOption(option2);

    Option retrievedOption = options.getOption("first option");
    Assert.assertEquals(option1, retrievedOption);
  }

  // Testing getMatchingOptions(String opt)
  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("ab", "abc option");
    Option option3 = new Option("b", "second option");
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);

    List<String> matchingOptions = options.getMatchingOptions("a");
    Assert.assertEquals(2, matchingOptions.size());
    Assert.assertTrue(matchingOptions.contains("first option"));
    Assert.assertTrue(matchingOptions.contains("abc option"));

    matchingOptions = options.getMatchingOptions("b");
    Assert.assertEquals(1, matchingOptions.size());
    Assert.assertTrue(matchingOptions.contains("second option"));

    matchingOptions = options.getMatchingOptions("c");
    Assert.assertTrue(matchingOptions.isEmpty());
  }

  // Testing hasOption(String opt)
  @Test
  public void testHasOptionWithShortName() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    options.addOption(option1);
    options.addOption(option2);

    boolean hasOption = options.hasOption("a");
    Assert.assertTrue(hasOption);

    hasOption = options.hasOption("c");
    Assert.assertFalse(hasOption);
  }

  // Testing hasLongOption(String opt)
  @Test
  public void testHasLongOption() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    options.addOption(option1);
    options.addOption(option2);

    boolean hasLongOption = options.hasLongOption("first option");
    Assert.assertTrue(hasLongOption);

    hasLongOption = options.hasLongOption("abc");
    Assert.assertFalse(hasLongOption);
  }

  // Testing hasShortOption(String opt)
  @Test
  public void testHasShortOption() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    options.addOption(option1);
    options.addOption(option2);

    boolean hasShortOption = options.hasShortOption("a");
    Assert.assertTrue(hasShortOption);

    hasShortOption = options.hasShortOption("c");
    Assert.assertFalse(hasShortOption);
  }

  // Testing getOptionGroup(Option opt)
  @Test
  public void testGetOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);

    OptionGroup retrievedGroup = options.getOptionGroup(option1);
    Assert.assertEquals(group, retrievedGroup);

    retrievedGroup = options.getOptionGroup(option2);
    Assert.assertEquals(group, retrievedGroup);
  }

  // Testing toString()
  @Test
  public void testToString() {
    Option option1 = new Option("a", "first option");
    Option option2 = new Option("b", "second option");
    options.addOption(option1);
    options.addOption(option2);

    String expectedString = "[ Options: [ short {a=first option, b=second option} ] [ long {} ]";
    Assert.assertEquals(expectedString, options.toString());
  }
}