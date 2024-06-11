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
    group.addOption(new Option("opt1", false, "option 1"));
    group.addOption(new Option("opt2", true, "option 2"));
    options.addOptionGroup(group);

    assertTrue(options.getOptionGroups().contains(group));
  }

  @Test
  public void testAddOption() {
    options.addOption("opt", "option", false, "option description");

    assertTrue(options.hasOption("opt"));
    assertTrue(options.hasLongOption("option"));
    assertFalse(options.hasShortOption("opt"));
  }

  @Test
  public void testGetOption() {
    options.addOption("opt", "option", false, "option description");

    Option option = options.getOption("opt");

    assertNotNull(option);
    assertEquals("opt", option.getOpt());
    assertEquals("option", option.getLongOpt());
    assertFalse(option.hasArg());
    assertEquals("option description", option.getDescription());
  }

  @Test
  public void testGetMatchingOptions() {
    options.addOption("opt1", "option1", false, "option 1");
    options.addOption("opt2", "option2", false, "option 2");
    options.addOption("opt3", "option3", false, "option 3");

    assertEquals(1, options.getMatchingOptions("opt").size());
    assertEquals("option1", options.getMatchingOptions("opt").get(0));
    assertEquals(2, options.getMatchingOptions("option").size());
    assertTrue(options.getMatchingOptions("option").contains("option1"));
    assertTrue(options.getMatchingOptions("option").contains("option2"));
  }

  @Test
  public void testHasOption() {
    options.addOption("opt", "option", false, "option description");

    assertTrue(options.hasOption("opt"));
    assertTrue(options.hasOption("option"));
    assertFalse(options.hasOption("invalid"));
  }

  @Test
  public void testHasLongOption() {
    options.addOption("opt", "option", false, "option description");

    assertTrue(options.hasLongOption("option"));
    assertFalse(options.hasLongOption("invalid"));
  }

  @Test
  public void testHasShortOption() {
    options.addOption("opt", "option", false, "option description");

    assertTrue(options.hasShortOption("opt"));
    assertFalse(options.hasShortOption("invalid"));
  }

  @Test
  public void testGetOptionGroup() {
    OptionGroup group = new OptionGroup();
    group.addOption(new Option("opt1", false, "option 1"));
    group.addOption(new Option("opt2", true, "option 2"));
    options.addOptionGroup(group);

    Option option1 = new Option("opt1", false, "option 1");
    Option option2 = new Option("opt2", true, "option 2");

    assertEquals(group, options.getOptionGroup(option1));
    assertEquals(group, options.getOptionGroup(option2));
    assertNull(options.getOptionGroup(new Option("invalid", false, "")));
  }
}