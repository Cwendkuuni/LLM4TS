import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

  // Test case for addOptionGroup(OptionGroup group) method
  @Test
  public void testAddOptionGroup() {
    Option option1 = new Option("a", "option1", false, "option1 description");
    Option option2 = new Option("b", "option2", false, "option2 description");
    OptionGroup optionGroup = new OptionGroup();
    optionGroup.setRequired(true);
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);

    Options options = new Options();
    options.addOptionGroup(optionGroup);

    assertTrue(options.getRequiredOptions().contains(option1.getKey()));
    assertTrue(options.getRequiredOptions().contains(option2.getKey()));
    assertTrue(options.getOptionGroups().contains(optionGroup));
    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
  }

  // Test cases for addOption(String opt, String description) method
  @Test
  public void testAddOptionStringString() {
    Options options = new Options();
    options.addOption("a", "option1", false, "option1 description");

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option1"));

    Option option = options.getOption("a");
    assertEquals("a", option.getKey());
    assertEquals("option1", option.getLongOpt());
    assertFalse(option.isRequired());
    assertFalse(option.hasArg());
    assertEquals("option1 description", option.getDescription());
  }

  // Test cases for addOption(String opt, boolean hasArg, String description) method
  @Test
  public void testAddOptionStringBooleanString() {
    Options options = new Options();
    options.addOption("a", true, "option1 description");

    assertTrue(options.hasOption("a"));

    Option option = options.getOption("a");
    assertEquals("a", option.getKey());
    assertNull(option.getLongOpt());
    assertFalse(option.isRequired());
    assertTrue(option.hasArg());
    assertEquals("option1 description", option.getDescription());
  }

  // Test cases for addOption(String opt, String longOpt, boolean hasArg, String description) method
  @Test
  public void testAddOptionStringStringBooleanString() {
    Options options = new Options();
    options.addOption("a", "option1", true, "option1 description");

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option1"));

    Option option = options.getOption("a");
    assertEquals("a", option.getKey());
    assertEquals("option1", option.getLongOpt());
    assertFalse(option.isRequired());
    assertTrue(option.hasArg());
    assertEquals("option1 description", option.getDescription());
  }

  // Test case for addOption(Option opt) method
  @Test
  public void testAddOption() {
    Options options = new Options();
    Option option = new Option("a", "option1", true, "option1 description");
    options.addOption(option);

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option1"));

    Option retrievedOption = options.getOption("a");
    assertSame(option, retrievedOption);
  }

  // Test case for getOptions() method
  @Test
  public void testGetOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", true, "option1 description");
    Option option2 = new Option("b", "option2", false, "option2 description");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(2, options.getOptions().size());
    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
  }

  // Test case for getRequiredOptions() method
  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", true, "option1 description");
    Option option2 = new Option("b", "option2", false, "option2 description");
    options.addOption(option1);
    options.addOption(option2);
    options.addOptionGroup(new OptionGroup().addOption(option1));

    assertEquals(1, options.getRequiredOptions().size());
    assertTrue(options.getRequiredOptions().contains(option1.getKey()));
  }

  // Test case for getOption(String opt) method
  @Test
  public void testGetOption() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", true, "option1 description");
    Option option2 = new Option("b", "option2", false, "option2 description");
    options.addOption(option1);
    options.addOption(option2);

    Option retrievedOption1 = options.getOption("a");
    Option retrievedOption2 = options.getOption("option2");
    assertSame(option1, retrievedOption1);
    assertSame(option2, retrievedOption2);
  }

  // Test case for getMatchingOptions(String opt) method
  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", true, "option1 description");
    Option option2 = new Option("b", "option1b", false, "option2 description");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(2, options.getMatchingOptions("option1").size());
    assertTrue(options.getMatchingOptions("option1").contains("option1"));
    assertTrue(options.getMatchingOptions("option1").contains("option1b"));

    assertEquals(0, options.getMatchingOptions("c").size());
  }

  // Test case for hasOption(String opt) method
  @Test
  public void testHasOption() {
    Options options = new Options();
    Option option = new Option("a", "option1", true, "option1 description");
    options.addOption(option);

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("option1"));
    assertFalse(options.hasOption("b"));
  }

  // Test case for hasLongOption(String opt) method
  @Test
  public void testHasLongOption() {
    Options options = new Options();
    Option option = new Option("a", "option1", true, "option1 description");
    options.addOption(option);

    assertTrue(options.hasLongOption("option1"));
    assertFalse(options.hasLongOption("option2"));
  }

  // Test case for hasShortOption(String opt) method
  @Test
  public void testHasShortOption() {
    Options options = new Options();
    Option option = new Option("a", "option1", true, "option1 description");
    options.addOption(option);

    assertTrue(options.hasShortOption("a"));
    assertFalse(options.hasShortOption("b"));
  }

  // Test case for getOptionGroup(Option opt) method
  @Test
  public void testGetOptionGroup() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", true, "option1 description");
    Option option2 = new Option("b", "option2", false, "option2 description");
    OptionGroup optionGroup = new OptionGroup().addOption(option1).setRequired(true);
    options.addOption(option1);
    options.addOption(option2);
    options.addOptionGroup(optionGroup);

    assertSame(optionGroup, options.getOptionGroup(option1));
    assertNull(options.getOptionGroup(option2));
  }

  // Test case for toString() method
  @Test
  public void testToString() {
    Options options = new Options();
    Option option1 = new Option("a", "option1", true, "option1 description");
    Option option2 = new Option("b", "option2", false, "option2 description");
    options.addOption(option1);
    options.addOption(option2);

    String expectedString = "[ Options: [ short {a=option1, b=option2} ] [ long {option1=" + option1.toString() + ", option2=" + option2.toString() + "} ]";
    assertEquals(expectedString, options.toString());
  }

}