import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

  private Options options;

  @Test
  public void testAddOptionGroup() {
    Option option1 = new Option("a", "add", false, "Add option.");
    Option option2 = new Option("r", "remove", false, "Remove option.");
    OptionGroup optionGroup = new OptionGroup();
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    options.addOptionGroup(optionGroup);
    assertTrue(options.getOptionGroups().contains(optionGroup));
  }

  @Test
  public void testAddOption() {
    Option option = new Option("a", "add", false, "Add option.");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("a", "add", false, "Add option.");
    Option option2 = new Option("r", "remove", false, "Remove option.");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(2, options.getOptions().size());
  }

  @Test
  public void testGetRequiredOptions() {
    Option option1 = new Option("a", "add", false, "Add option.");
    Option option2 = new Option("r", "remove", false, "Remove option.");
    options.addOption(option1);
    options.addOption(option2);
    options.getOption("a").setRequired(true);
    assertEquals(1, options.getRequiredOptions().size());
  }

  @Test
  public void testGetOption() {
    Option option = new Option("a", "add", false, "Add option.");
    options.addOption(option);
    assertNotNull(options.getOption("a"));
    assertNull(options.getOption("b"));
  }

  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("a", "add", false, "Add option.");
    Option option2 = new Option("r", "remove", false, "Remove option.");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(2, options.getMatchingOptions("a").size());
    assertEquals(1, options.getMatchingOptions("r").size());
    assertEquals(0, options.getMatchingOptions("b").size());
  }

  @Test
  public void testHasOption() {
    Option option = new Option("a", "add", false, "Add option.");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
  }

  @Test
  public void testHasLongOption() {
    Option option = new Option("a", "add", false, "Add option.");
    options.addOption(option);
    assertTrue(options.hasLongOption("add"));
    assertFalse(options.hasLongOption("remove"));
  }

  @Test
  public void testHasShortOption() {
    Option option = new Option("a", "add", false, "Add option.");
    options.addOption(option);
    assertTrue(options.hasShortOption("a"));
    assertFalse(options.hasShortOption("r"));
  }

  @Test
  public void testGetOptionGroup() {
    Option option1 = new Option("a", "add", false, "Add option.");
    Option option2 = new Option("r", "remove", false, "Remove option.");
    OptionGroup optionGroup1 = new OptionGroup();
    optionGroup1.setRequired(true);
    optionGroup1.addOption(option1);
    OptionGroup optionGroup2 = new OptionGroup();
    optionGroup2.addOption(option2);
    options.addOptionGroup(optionGroup1);
    options.addOptionGroup(optionGroup2);
    assertEquals(optionGroup1, options.getOptionGroup(option1));
    assertEquals(optionGroup2, options.getOptionGroup(option2));
    assertNull(options.getOptionGroup(new Option("c")));
  }

  @Test
  public void testToString() {
    Option option1 = new Option("a", "add", false, "Add option.");
    Option option2 = new Option("r", "remove", false, "Remove option.");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals("[ Options: [ short {a=Option@a, r=Option@r} ] [ long {add=Option@add, remove=Option@remove} ]", options.toString());
  }
}