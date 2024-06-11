import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

  @Test
  public void testAddOptionGroup() {
    Options options = new Options();
    Option option1 = new Option("a", "test1", false, "test option 1");
    Option option2 = new Option("b", "test2", false, "test option 2");
    OptionGroup group = new OptionGroup();
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);

    assertTrue(options.getOptionGroups().contains(group));
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));
    assertEquals(group, options.getOptionGroup(option1));
    assertEquals(group, options.getOptionGroup(option2));
  }

  @Test
  public void testAddOption() {
    Options options = new Options();
    Option option1 = new Option("a", "test1", false, "test option 1");
    Option option2 = new Option("b", "test2", false, "test option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));
    assertEquals(option1, options.getOption("a"));
    assertEquals(option2, options.getOption("b")); 
  }

  @Test
  public void testAddOptionWithHasArg() {
    Options options = new Options();
    Option option = new Option("a", "test1", true, "test option 1");
    options.addOption(option);

    assertTrue(options.getOptions().contains(option));
    assertTrue(options.hasOption("a"));
    assertEquals(option, options.getOption("a"));
  }

  @Test
  public void testGetOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "test1", false, "test option 1");
    Option option2 = new Option("b", "test2", false, "test option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(2, options.getOptions().size());
  }

  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "test1", false, "test option 1");
    Option option2 = new Option("b", "test2", false, "test option 2");
    option1.setRequired(true);
    option2.setRequired(true);
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(2, options.getRequiredOptions().size());
    assertTrue(options.getRequiredOptions().contains("a"));
    assertTrue(options.getRequiredOptions().contains("b"));
  }

  @Test
  public void testGetOption() {
    Options options = new Options();
    Option option1 = new Option("a", "test1", false, "test option 1");
    Option option2 = new Option("b", "test2", false, "test option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(option1, options.getOption("a"));
    assertEquals(option2, options.getOption("b"));
  }

  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "test1", false, "test option 1");
    Option option2 = new Option("ab", "test2", false, "test option 2");
    Option option3 = new Option("abc", "test3", false, "test option 3");
    Option option4 = new Option("bc", "test4", false, "test option 4");
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);
    options.addOption(option4);

    assertEquals(2, options.getMatchingOptions("a").size());
    assertTrue(options.getMatchingOptions("a").contains("a"));
    assertTrue(options.getMatchingOptions("a").contains("ab"));
  }

  @Test
  public void testHasOption() {
    Options options = new Options();
    Option option = new Option("a", "test1", false, "test option 1");
    options.addOption(option);

    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
  }

  @Test
  public void testHasLongOption() {
    Options options = new Options();
    Option option = new Option("a", "test1", false, "test option 1");
    options.addOption(option);

    assertTrue(options.hasLongOption("test1"));
    assertFalse(options.hasLongOption("test2"));
  }

  @Test
  public void testHasShortOption() {
    Options options = new Options();
    Option option = new Option("a", "test1", false, "test option 1");
    options.addOption(option);

    assertTrue(options.hasShortOption("a"));
    assertFalse(options.hasShortOption("b"));
  }

  @Test
  public void testGetOptionGroup() {
    Options options = new Options();
    Option option1 = new Option("a", "test1", false, "test option 1");
    Option option2 = new Option("a", "test2", false, "test option 2");
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
    Option option1 = new Option("a", "test1", false, "test option 1");
    Option option2 = new Option("b", "test2", false, "test option 2");
    options.addOption(option1);
    options.addOption(option2);

    String expected = "[ Options: [ short {a=Option[ opt: a, long-opt: test1, has-arg: false, description: test option 1 ]"
        + ", b=Option[ opt: b, long-opt: test2, has-arg: false, description: test option 2 ] } ]";
    assertEquals(expected, options.toString());
  }

}