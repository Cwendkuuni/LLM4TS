import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionsTest {

  private Options options;

  @Before
  public void setUp() {
    options = new Options();
  }

  // Test addOptionGroup method with required and non-required options
  @Test
  public void testAddOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "description1");
    Option option2 = new Option("b", "description2");
    group.addOption(option1);
    group.addOption(option2);
    group.setRequired(true);
    options.addOptionGroup(group);

    assertTrue(options.getOptionGroups().contains(group));
    assertTrue(options.getRequiredOptions().contains(group));

    assertTrue(options.getOptions().contains(option1));
    assertFalse(option1.isRequired());

    assertTrue(options.getOptions().contains(option2));
    assertFalse(option2.isRequired());
  }

  // Test addOption method with short name only
  @Test
  public void testAddOption1() {
    options.addOption("a", "description");

    Option option = options.getOption("a");

    assertNotNull(option);
    assertEquals("a", option.getOpt());
    assertNull(option.getLongOpt());
    assertFalse(option.hasArg());
    assertEquals("description", option.getDescription());
    assertFalse(option.isRequired());
  }

  // Test addOption method with short name and hasArg set to true
  @Test
  public void testAddOption2() {
    options.addOption("a", true, "description");

    Option option = options.getOption("a");

    assertNotNull(option);
    assertEquals("a", option.getOpt());
    assertNull(option.getLongOpt());
    assertTrue(option.hasArg());
    assertEquals("description", option.getDescription());
    assertFalse(option.isRequired());
  }

  // Test addOption method with short name, long name, and hasArg set to true
  @Test
  public void testAddOption3() {
    options.addOption("a", "alpha", true, "description");

    Option option = options.getOption("a");

    assertNotNull(option);
    assertEquals("a", option.getOpt());
    assertEquals("alpha", option.getLongOpt());
    assertTrue(option.hasArg());
    assertEquals("description", option.getDescription());
    assertFalse(option.isRequired());
  }

  // Test addOption method with required option
  @Test
  public void testAddOption4() {
    Option option = new Option("a", "description");
    option.setRequired(true);
    options.addOption(option);

    assertTrue(options.getRequiredOptions().contains(option));
    assertTrue(options.getOptions().contains(option));
    assertTrue(option.isRequired());
  }

  // Test getOptions method to retrieve read-only collection
  @Test
  public void testGetOptions() {
    Option option1 = new Option("a", "description1");
    Option option2 = new Option("b", "description2");

    options.addOption(option1);
    options.addOption(option2);

    Collection<Option> retrievedOptions = options.getOptions();

    assertNotNull(retrievedOptions);
    assertEquals(2, retrievedOptions.size());
    assertTrue(retrievedOptions.contains(option1));
    assertTrue(retrievedOptions.contains(option2));

    try {
      retrievedOptions.add(new Option("c", "description3"));
      fail("UnsupportedOperationException should be thrown");
    } catch (UnsupportedOperationException e) {
      // do nothing
    }
  }

  // Test getRequiredOptions method to retrieve read-only list
  @Test
  public void testGetRequiredOptions() {
    Option option1 = new Option("a", "description1");
    Option option2 = new Option("b", "description2");

    option1.setRequired(true);
    option2.setRequired(true);

    options.addOption(option1);
    options.addOption(option2);

    List<Option> retrievedOptions = options.getRequiredOptions();

    assertNotNull(retrievedOptions);
    assertEquals(2, retrievedOptions.size());
    assertTrue(retrievedOptions.contains(option1));
    assertTrue(retrievedOptions.contains(option2));

    try {
      retrievedOptions.add(new Option("c", "description3"));
      fail("UnsupportedOperationException should be thrown");
    } catch (UnsupportedOperationException e) {
      // do nothing
    }
  }

  // Test getOption method with short name
  @Test
  public void testGetOption1() {
    Option option = new Option("a", "description");
    options.addOption(option);

    Option retrievedOption = options.getOption("a");

    assertNotNull(retrievedOption);
    assertSame(option, retrievedOption);
  }

  // Test getOption method with long name
  @Test
  public void testGetOption2() {
    Option option = new Option("a", "alpha", true, "description");
    options.addOption(option);

    Option retrievedOption = options.getOption("alpha");

    assertNotNull(retrievedOption);
    assertSame(option, retrievedOption);
  }

  // Test getMatchingOptions method with partial name match
  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("a", "description1");
    Option option2 = new Option("abc", "description2");
    Option option3 = new Option("aac", "description3");
    Option option4 = new Option("aacd", "description4");

    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);
    options.addOption(option4);

    List<String> matchingOptions = options.getMatchingOptions("a");

    assertNotNull(matchingOptions);
    assertEquals(4, matchingOptions.size());
    assertTrue(matchingOptions.contains("a"));
    assertTrue(matchingOptions.contains("abc"));
    assertTrue(matchingOptions.contains("aac"));
    assertTrue(matchingOptions.contains("aacd"));
  }

  // Test hasOption method with existing option
  @Test
  public void testHasOption1() {
    Option option = new Option("a", "description");
    options.addOption(option);

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("alpha"));
  }

  // Test hasOption method with non-existing option
  @Test
  public void testHasOption2() {
    assertFalse(options.hasOption("a"));
  }

  // Test hasLongOption method with existing long option
  @Test
  public void testHasLongOption() {
    Option option = new Option("a", "alpha", true, "description");
    options.addOption(option);

    assertTrue(options.hasLongOption("alpha"));
    assertFalse(options.hasLongOption("a"));
  }

  // Test hasShortOption method with existing short option
  @Test
  public void testHasShortOption() {
    Option option = new Option("a", "alpha", true, "description");
    options.addOption(option);

    assertFalse(options.hasShortOption("alpha"));
    assertTrue(options.hasShortOption("a"));
  }

  // Test getOptionGroup method with existing option group
  @Test
  public void testGetOptionGroup1() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "description1");
    Option option2 = new Option("b", "description2");
    group.addOption(option1);
    group.addOption(option2);

    options.addOptionGroup(group);

    assertEquals(group, options.getOptionGroup(option1));
    assertEquals(group, options.getOptionGroup(option2));
  }

  // Test getOptionGroup method with non-existing option group
  @Test
  public void testGetOptionGroup2() {
    Option option = new Option("a", "description");
    assertNull(options.getOptionGroup(option));
  }

  // Test toString method to confirm correct string representation
  @Test
  public void testToString() {
    Option option1 = new Option("a", "description1");
    Option option2 = new Option("b", "description2");

    options.addOption(option1);
    options.addOption(option2);

    String expected = "[ Options: [ short {a=Option: a } ] [ long {description1=Option: a } ]]";
    assertEquals(expected, options.toString());
  }

}