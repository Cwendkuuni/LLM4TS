package org.apache.commons.cli;

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
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a");
    Option option2 = new Option("b");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    options.addOptionGroup(optionGroup);

    assertTrue(options.getOptionGroups().contains(optionGroup));
  }

  @Test
  public void testAddOption() {
    Option option1 = new Option("a", true, "option a");
    Option option2 = new Option("b", "option b");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));

    assertTrue(options.getOption("a").isRequired());
    assertFalse(options.getOption("b").isRequired());
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("a");
    Option option2 = new Option("b");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
  }

  @Test
  public void testGetRequiredOptions() {
    Option option1 = new Option("a");
    option1.setRequired(true);
    OptionGroup optionGroup = new OptionGroup();
    Option option2 = new Option("b");
    optionGroup.addOption(option2);
    options.addOption(option1);
    options.addOptionGroup(optionGroup);

    assertTrue(options.getRequiredOptions().contains(option1));
    assertFalse(options.getRequiredOptions().contains(option2));
  }

  @Test
  public void testGetOption() {
    Option option1 = new Option("a");
    Option option2 = new Option("b");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(option1, options.getOption("a"));
    assertEquals(option2, options.getOption("b"));
  }

  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("apple");
    Option option2 = new Option("banana");
    options.addOption(option1);
    options.addOption(option2);

    assertEquals(1, options.getMatchingOptions("ap").size());
    assertTrue(options.getMatchingOptions("ap").contains("apple"));
  }

  @Test
  public void testHasOption() {
    Option option1 = new Option("a");
    Option option2 = new Option("b");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));
    assertFalse(options.hasOption("c"));
  }

  @Test
  public void testHasLongOption() {
    Option option1 = new Option("a", "apple");
    Option option2 = new Option("b", "banana");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.hasLongOption("apple"));
    assertTrue(options.hasLongOption("banana"));
    assertFalse(options.hasLongOption("grape"));
  }

  @Test
  public void testHasShortOption() {
    Option option1 = new Option("a");
    Option option2 = new Option("b");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.hasShortOption("a"));
    assertTrue(options.hasShortOption("b"));
    assertFalse(options.hasShortOption("c"));
  }

  @Test
  public void testGetOptionGroup() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("a");
    Option option2 = new Option("b");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    options.addOptionGroup(optionGroup);

    assertEquals(optionGroup, options.getOptionGroup(option1));
    assertEquals(optionGroup, options.getOptionGroup(option2));
  }

  @Test
  public void testToString() {
    Option option1 = new Option("a");
    Option option2 = new Option("b");
    options.addOption(option1);
    options.addOption(option2);

    String expected = "[ Options: [ short {a=Option: opt:[a] } ] [ long {}]";

    assertEquals(expected, options.toString());
  }
}