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

  @Test
  public void testAddOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option = new Option("t", "test", false, "Testing option");
    group.addOption(option);
    Options result = options.addOptionGroup(group);
    assertEquals(options, result);
    assertTrue(options.getOptionGroups().contains(group));
  }

  @Test
  public void testAddOption() {
    Option option = new Option("t", "test", false, "Testing option");
    Options result = options.addOption(option);
    assertEquals(options, result);
    assertTrue(options.getOptions().contains(option));
    assertTrue(options.hasOption("t"));
  }

  @Test
  public void testHasOption() {
    assertFalse(options.hasOption("t"));
  }

  @Test
  public void testHasLongOption() {
    Option option = new Option("t", "test", false, "Testing option");
    options.addOption(option);
    assertTrue(options.hasLongOption("test"));
  }

  @Test
  public void testHasShortOption() {
    Option option = new Option("t", "test", false, "Testing option");
    options.addOption(option);
    assertTrue(options.hasShortOption("t"));
  }

  @Test
  public void testGetOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option = new Option("t", "test", false, "Testing option");
    group.addOption(option);
    options.addOptionGroup(group);
    assertEquals(group, options.getOptionGroup(option));
  }

  @Test
  public void testToString() {
    Option option1 = new Option("t", "test", false, "Testing option");
    Option option2 = new Option("o", "option", false, "Testing option");
    options.addOption(option1);
    options.addOption(option2);
    String expected = "[ Options: [ short {t=option, o=option} ] [ long {test=option, option=option} ]";
    assertEquals(expected, options.toString());
  }
}