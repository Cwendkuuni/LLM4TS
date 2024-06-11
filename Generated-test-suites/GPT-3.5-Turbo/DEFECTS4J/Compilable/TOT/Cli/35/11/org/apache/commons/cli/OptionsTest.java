package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionsTest {

  // Test for addOptionGroup(OptionGroup group)
  @Test
  public void testAddOptionGroup() {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    group.addOption(new Option("a", "apple", false, "Apple option"));
    group.addOption(new Option("b", "banana", true, "Banana option"));

    options.addOptionGroup(group);

    assertNotNull(options.getOptionGroup(new Option("a", "apple", false, "")));
    assertNotNull(options.getOptionGroup(new Option("b", "banana", true, "")));
  }

  // Test for addOption(String opt, String description)
  @Test
  public void testAddOption1() {
    Options options = new Options();

    options.addOption("a", "Apple option");

    assertNotNull(options.getOption("a"));
  }

  // Test for addOption(String opt, boolean hasArg, String description)
  @Test
  public void testAddOption2() {
    Options options = new Options();

    options.addOption("a", true, "Apple option");

    assertNotNull(options.getOption("a"));
    assertTrue(options.getOption("a").hasArg());
  }

  // Test for addOption(String opt, String longOpt, boolean hasArg, String description)
  @Test
  public void tetAddOption3() {
    Options options = new Options();

    options.addOption("a", "apple", true, "Apple option");

    assertNotNull(options.getOption("a"));
    assertTrue(options.getOption("a").hasArg());
  }

  // Test for addOption(Option opt)
  @Test
  public void testAddOption4() {
    Options options = new Options();
    Option option = new Option("a", "apple", true, "Apple option");

    options.addOption(option);

    assertNotNull(options.getOption("a"));
    assertTrue(options.getOption("a").hasArg());
  }

  // Test for getOptions()
  @Test
  public void testGetOptions() {
    Options options = new Options();
    options.addOption("a", "Apple option");
    options.addOption("b", "Banana option");

    assertEquals(2, options.getOptions().size());
  }

  // Test for getRequiredOptions
  @Test
  public void testGetRequiredOptions() {
    Options options = new Options();
    options.addOption("a", "Apple option");
    options.addOption("b", true, "Banana option");

    assertEquals(1, options.getRequiredOptions().size());
  }

  // Test for getOption(String opt)
  @Test
  public void testGetOption() {
    Options options = new Options();
    options.addOption("a", "Apple option");
    options.addOption("b", true, "Banana option");

    assertNotNull(options.getOption("a"));
    assertNotNull(options.getOption("b"));
    assertNull(options.getOption("c"));
  }

  // Test for getMatchingOptions(String opt)
  @Test
  public void testGetMatchingOptions() {
    Options options = new Options();
    options.addOption("apple", true, "Apple option");
    options.addOption("banana", false, "Banana option");

    assertEquals(1, options.getMatchingOptions("a").size());
    assertEquals(1, options.getMatchingOptions("apple").size());
    assertEquals(0, options.getMatchingOptions("c").size());
  }

  // Test for hasOption(String opt)
  @Test
  public void testHasOption() {
    Options options = new Options();
    options.addOption("a", "Apple option");

    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
  }

  // Test for hasLongOption(String opt)
  @Test
  public void testHasLongOption() {
    Options options = new Options();
    options.addOption("a", "Apple option");

    assertTrue(options.hasLongOption("apple"));
    assertFalse(options.hasLongOption("banana"));
  }

  // Test for hasShortOption(String opt)
  @Test
  public void testHasShortOption() {
    Options options = new Options();
    options.addOption("a", "Apple option");

    assertTrue(options.hasShortOption("a"));
    assertFalse(options.hasShortOption("b"));
  }

  // Test for getOptionGroup(Option opt)
  @Test
  public void testGetOptionGroup() {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    group.addOption(new Option("a", "apple", true, "Apple option"));

    options.addOptionGroup(group);

    assertNotNull(options.getOptionGroup(new Option("a", "apple", true, "")));
    assertNull(options.getOptionGroup(new Option("b", "banana", false, "")));
  }

  // Test for toString()
  @Test
  public void testToString() {
    Options options = new Options();
    options.addOption("a", "Apple option");
    options.addOption("b", true, "Banana option");

    assertTrue(options.toString().contains("[ Options: [ short"));
    assertTrue(options.toString().contains("[ Options: [ long"));
  }

}