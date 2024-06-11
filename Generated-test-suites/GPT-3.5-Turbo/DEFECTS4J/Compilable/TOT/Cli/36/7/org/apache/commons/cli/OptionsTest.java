package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

  private Options options;

  @Before
  public void setUp() {
    options = new Options();
  }

  // Test for addOptionGroup(OptionGroup group)

  @Test
  public void testAddOptionGroup() {
    // Create OptionGroup
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "apple", false, "Description for option 1");
    Option option2 = new Option("b", "banana", false, "Description for option 2");
    group.addOption(option1);
    group.addOption(option2);
    
    // Add OptionGroup to Options
    options.addOptionGroup(group);

    // Verify that both options are added to Options and OptionGroup
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));
    assertTrue(options.hasOption("apple"));
    assertTrue(options.hasOption("banana"));

    assertNotNull(options.getOptionGroup(option1));
    assertNotNull(options.getOptionGroup(option2));
  }

  // Test for addOption(String opt, String description)

  @Test
  public void testAddOptionWithShortNameOnly() {
    options.addOption("a", "Description for option a");

    assertTrue(options.hasOption("a"));
    assertFalse(options.getOption("a").hasLongOpt());
    assertNull(options.getOption("a").getLongOpt());
    assertFalse(options.getOption("a").hasArg());
    assertNull(options.getOption("a").getDescription());

    assertEquals(1, options.getOptions().size());
    assertTrue(options.getRequiredOptions().isEmpty());
    assertNotNull(options.helpOptions());
  }

  // Test for addOption(String opt, boolean hasArg, String description)

  @Test
  public void testAddOptionWithShortNameAndHasArg() {
    options.addOption("a", true, "Description for option a with argument");

    assertTrue(options.hasOption("a"));
    assertFalse(options.getOption("a").hasLongOpt());
    assertNull(options.getOption("a").getLongOpt());
    assertTrue(options.getOption("a").hasArg());
    assertNull(options.getOption("a").getDescription());

    assertEquals(1, options.getOptions().size());
    assertTrue(options.getRequiredOptions().isEmpty());
    assertNotNull(options.helpOptions());
  }

  // Test for addOption(String opt, String longOpt, boolean hasArg, String description)

  @Test
  public void testAddOptionWithShortNameAndLongNameAndHasArg() {
    options.addOption("a", "apple", true, "Description for option a with argument");

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("apple"));

    assertTrue(options.getOption("a").hasLongOpt());
    assertEquals("apple", options.getOption("a").getLongOpt());

    assertTrue(options.getOption("a").hasArg());
    assertTrue(options.getOption("apple").hasArg());

    assertEquals("Description for option a with argument", options.getOption("a").getDescription());
    assertEquals("Description for option a with argument", options.getOption("apple").getDescription());

    assertEquals(1, options.getOptions().size());
    assertTrue(options.getRequiredOptions().isEmpty());
    assertNotNull(options.helpOptions());
  }

  // Test for getOption(String opt)

  @Test
  public void testGetOptionWithShortName() {
    options.addOption("a", "apple", true, "Description for option a with argument");

    Option option = options.getOption("a");

    assertNotNull(option);
    assertEquals("a", option.getOpt());
    assertEquals("apple", option.getLongOpt());
    assertTrue(option.hasArg());
    assertEquals("Description for option a with argument", option.getDescription());
  }

  // Test for getOption(String opt)

  @Test
  public void testGetOptionWithLongName() {
    options.addOption("a", "apple", true, "Description for option a with argument");

    Option option = options.getOption("apple");

    assertNotNull(option);
    assertEquals("a", option.getOpt());
    assertEquals("apple", option.getLongOpt());
    assertTrue(option.hasArg());
    assertEquals("Description for option a with argument", option.getDescription());
  }

  // Test for hasOption(String opt)

  @Test
  public void testHasOptionWithShortName() {
    options.addOption("a", true, "Description for option a with argument");

    assertTrue(options.hasOption("a"));
  }

  // Test for hasOption(String opt)

  @Test
  public void testHasOptionWithLongName() {
    options.addOption("a", "apple", true, "Description for option a with argument");

    assertTrue(options.hasOption("apple"));
  }

  // Test for getOptionGroup(Option opt)

  @Test
  public void testGetOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("a", "apple", false, "Description for option 1");
    group.addOption(option1);
    options.addOptionGroup(group);

    OptionGroup retrievedGroup = options.getOptionGroup(option1);

    assertNotNull(retrievedGroup);
    assertSame(group, retrievedGroup);
  }

  // Test for toString()

  @Test
  public void testToString() {
    options.addOption("a", "apple", true, "Description for option a with argument");

    String str = options.toString();

    assertNotNull(str);
    assertTrue(str.contains("[ short {a="));
    assertTrue(str.contains("} [ long {apple=Option"));
  }

}