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
    Option option1 = new Option("a", "option1", true, "Option 1");
    Option option2 = new Option("b", "option2", true, "Option 2");
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);
    assertTrue(options.hasOption("a"));
    assertTrue(options.hasOption("b"));
    assertEquals(group, options.getOptionGroup(option1));
    assertEquals(group, options.getOptionGroup(option2));
  }
  
  @Test
  public void testAddOptionWithShortNameOnly() {
    options.addOption("a", "Option a");
    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
    assertEquals("Option a", options.getOption("a").getDescription());
  }
  
  @Test
  public void testAddOptionWithShortNameAndHasArg() {
    options.addOption("a", true, "Option with arg");
    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
    assertTrue(options.getOption("a").hasArg());
    assertEquals("Option with arg", options.getOption("a").getDescription());
  }
  
  @Test
  public void testAddOptionWithShortNameAndLongName() {
    options.addOption("a", "option1", false, "Option with short name and long name");
    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
    assertFalse(options.hasOption("option1"));
    assertEquals("Option with short name and long name", options.getOption("a").getDescription());
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("a", "option1", true, "Option 1");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
    assertEquals(option, options.getOption("a"));
  }
  
  @Test
  public void testGetOptions() {
    Option option1 = new Option("a", "option1", true, "Option 1");
    Option option2 = new Option("b", "option2", true, "Option 2");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(2, options.getOptions().size());
    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
  }
  
  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("a", "option1", true, "Option 1");
    Option option2 = new Option("ab", "option2", true, "Option 2");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(2, options.getMatchingOptions("o").size());
    assertTrue(options.getMatchingOptions("o").contains("option1"));
    assertTrue(options.getMatchingOptions("o").contains("option2"));
    assertEquals(1, options.getMatchingOptions("op").size());
    assertTrue(options.getMatchingOptions("op").contains("option2"));
  }
  
  @Test
  public void testHasOption() {
    Option option = new Option("a", "option1", true, "Option 1");
    options.addOption(option);
    assertTrue(options.hasOption("a"));
    assertFalse(options.hasOption("b"));
    assertFalse(options.hasOption("option1"));
  }
  
  @Test
  public void testHasLongOption() {
    Option option = new Option("a", "option1", true, "Option 1");
    options.addOption(option);
    assertTrue(options.hasLongOption("option1"));
    assertFalse(options.hasLongOption("a"));
  }
  
  @Test
  public void testHasShortOption() {
    Option option = new Option("a", "option1", true, "Option 1");
    options.addOption(option);
    assertTrue(options.hasShortOption("a"));
    assertFalse(options.hasShortOption("option1"));
  }
  
  @Test
  public void testGetOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option = new Option("a", "option1", true, "Option 1");
    group.addOption(option);
    options.addOptionGroup(group);
    assertEquals(group, options.getOptionGroup(option));
  }
  
  @Test
  public void testToString() {
    Option option1 = new Option("a", "option1", true, "Option 1");
    Option option2 = new Option("b", "option2", true, "Option 2");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals("[ Options: [ short {a=Option 1, b=Option 2} ] [ long {option1=Option 1, option2=Option 2} ]", options.toString());
  }
}