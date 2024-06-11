import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

  private Options options;

  @Before
  public void setUp() {
    options = new Options();
  }

  @Test
  public void testAddOptionGroup() {
    OptionGroup group = new OptionGroup();
    options.addOptionGroup(group);
    assertEquals(1, options.getOptionGroups().size());
  }

  @Test
  public void testAddOption() {
    Option option = new Option("a", "apple", false, "description");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasLongOption("apple"));
    assertFalse(options.hasShortOption("a"));

    option = new Option("b", true, "description");
    options.addOption(option);
    assertTrue(options.hasOption("b"));
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("a", "apple", false, "description");
    Option option2 = new Option("b", "banana", true, "description");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(2, options.getOptions().size());
  }

  @Test
  public void testGetRequiredOptions() {
    Option option1 = new Option("a", "apple", false, "description");
    Option option2 = new Option("b", "banana", true, "description");
    option2.setRequired(true);
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(1, options.getRequiredOptions().size());
  }

  @Test
  public void testGetOption() {
    Option option1 = new Option("a", "apple", false, "description");
    options.addOption(option1);
    assertEquals(option1, options.getOption("a"));
    assertEquals(option1, options.getOption("apple"));
    assertNull(options.getOption("b"));
  }

  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("apple", false, "description");
    Option option2 = new Option("banana", false, "description");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(2, options.getMatchingOptions("a").size());
    assertEquals(1, options.getMatchingOptions("b").size());
  }

  @Test
  public void testHasOption() {
    Option option = new Option("a", "apple", false, "description");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("apple"));
    assertFalse(options.hasOption("b"));
  }

  @Test
  public void testHasLongOption() {
    Option option = new Option("a", "apple", false, "description");
    options.addOption(option);
    assertTrue(options.hasLongOption("apple"));
    assertFalse(options.hasLongOption("b"));
  }

  @Test
  public void testHasShortOption() {
    Option option = new Option("a", "apple", false, "description");
    options.addOption(option);
    assertTrue(options.hasShortOption("a"));
    assertFalse(options.hasShortOption("b"));
  }

  @Test
  public void testGetOptionGroup() {
    Option option = new Option("a", "apple", false, "description");
    OptionGroup group = new OptionGroup();
    group.addOption(option);
    options.addOptionGroup(group);
    assertEquals(group, options.getOptionGroup(option));
  }

  @Test
  public void testToString() {
    Option option = new Option("a", "apple", false, "description");
    options.addOption(option);
    String expected = "[ Options: [ short {a=Option{opt=a, longOpt=apple, hasArg=false, description=description}} ] [ long {apple=Option{opt=a, longOpt=apple, hasArg=false, description=description}} ]";
    assertEquals(expected, options.toString());
  }
}