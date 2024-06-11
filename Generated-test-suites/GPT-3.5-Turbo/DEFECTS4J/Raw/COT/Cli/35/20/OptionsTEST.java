import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

  @Test
  public void testAddOptionGroup() {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);

    assertTrue(options.getOptionGroups().contains(group));
  }

  @Test
  public void testAddOption_String_String_Boolean_String() {
    Options options = new Options();
    options.addOption("a", "apple", false, "This is option 1");

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasLongOption("apple"));
  }

  @Test
  public void testAddOption_String_Boolean_String() {
    Options options = new Options();
    options.addOption("a", false, "This is option 1");

    assertTrue(options.hasOption("a"));
  }

  @Test
  public void testAddOption_String_String_Boolean_String() {
    Options options = new Options();
    options.addOption("a", "apple", true, "This is option 1");

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasLongOption("apple"));
    assertTrue(options.getOption("a").hasArg());
  }

  @Test
  public void testAddOption_Option() {
    Options options = new Options();
    Option option = new Option("a", "apple", false, "This is option 1");
    options.addOption(option);

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasLongOption("apple"));
  }

  @Test
  public void testGetOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(2, options.getOptions().size());
    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
  }

  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    options.addOption(option1);
    options.addOption(option2);
    options.addOptionGroup(new OptionGroup().addOption(option1));

    assertEquals(1, options.getRequiredOptions().size());
    assertTrue(options.getRequiredOptions().contains(option1));
  }

  @Test
  public void testGetOption() {
    Options options = new Options();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(option1, options.getOption("a"));
    assertEquals(option2, options.getOption("b"));
    assertEquals(option1, options.getOption("--apple"));
    assertEquals(option2, options.getOption("--banana"));
  }

  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(1, options.getMatchingOptions("a").size());
    assertTrue(options.getMatchingOptions("a").contains("apple"));

    assertEquals(1, options.getMatchingOptions("b").size());
    assertTrue(options.getMatchingOptions("b").contains("banana"));
  }

  @Test
  public void testHasOption() {
    Options options = new Options();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));
    assertFalse(options.hasOption("c"));
  }

  @Test
  public void testHasLongOption() {
    Options options = new Options();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.hasLongOption("apple"));
    assertTrue(options.hasLongOption("banana"));
    assertFalse(options.hasLongOption("carrot"));
  }

  @Test
  public void testHasShortOption() {
    Options options = new Options();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.hasShortOption("a"));
    assertTrue(options.hasShortOption("b"));
    assertFalse(options.hasShortOption("c"));
  }

  @Test
  public void testGetOptionGroup() {
    Options options = new Options();

    OptionGroup group1 = new OptionGroup();
    Option option1 = new Option("a", "apple", false, "This is option 1");
    Option option2 = new Option("b", "banana", false, "This is option 2");
    group1.addOption(option1);
    group1.addOption(option2);

    OptionGroup group2 = new OptionGroup();
    Option option3 = new Option("c", "carrot", false, "This is option 3");
    group2.addOption(option3);

    options.addOptionGroup(group1);
    options.addOptionGroup(group2);

    assertEquals(group1, options.getOptionGroup(option1));
    assertEquals(group1, options.getOptionGroup(option2));
    assertEquals(group2, options.getOptionGroup(option3));
  }

}