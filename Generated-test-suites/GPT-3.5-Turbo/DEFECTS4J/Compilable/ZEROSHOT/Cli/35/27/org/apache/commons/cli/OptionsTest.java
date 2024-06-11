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
    options.addOptionGroup(group);
    assertEquals(group, options.getOptionGroups().iterator().next());
  }
  
  @Test
  public void testAddOption() {
    Option option = new Option("o", true, "option");
    options.addOption(option);
    assertTrue(options.hasOption("o"));
    assertEquals(option, options.getOption("o"));
  }
  
  @Test
  public void testGetOptions() {
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(2, options.getOptions().size());
    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
  }
  
  @Test
  public void testGetRequiredOptions() {
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    options.addOption(option1);
    options.addOption(option2);
    option1.setRequired(true);
    assertEquals(1, options.getRequiredOptions().size());
    assertTrue(options.getRequiredOptions().contains(option1));
  }
  
  @Test
  public void testGetOption() {
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals(option1, options.getOption("o1"));
  }
  
  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    Option option3 = new Option("o3", true, "option3");
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);
    assertEquals(2, options.getMatchingOptions("o").size());
    assertTrue(options.getMatchingOptions("o").contains("o1"));
    assertTrue(options.getMatchingOptions("o").contains("o2"));
  }
  
  @Test
  public void testHasOption() {
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    options.addOption(option1);
    options.addOption(option2);
    assertTrue(options.hasOption("o1"));
    assertFalse(options.hasOption("o3"));
  }
  
  @Test
  public void testHasLongOption() {
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    options.addOption(option1);
    options.addOption(option2);
    assertTrue(options.hasLongOption("o1"));
    assertFalse(options.hasLongOption("o3"));
  }
  
  @Test
  public void testHasShortOption() {
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    options.addOption(option1);
    options.addOption(option2);
    assertTrue(options.hasShortOption("o1"));
    assertFalse(options.hasShortOption("o3"));
  }
  
  @Test
  public void testGetOptionGroup() {
    OptionGroup group1 = new OptionGroup();
    OptionGroup group2 = new OptionGroup();
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    group1.addOption(option1);
    group2.addOption(option2);
    options.addOptionGroup(group1);
    options.addOptionGroup(group2);
    assertEquals(group1, options.getOptionGroup(option1));
    assertEquals(group2, options.getOptionGroup(option2));
  }
  
  @Test
  public void testToString() {
    Option option1 = new Option("o1", true, "option1");
    Option option2 = new Option("o2", true, "option2");
    options.addOption(option1);
    options.addOption(option2);
    assertEquals("[ Options: [ short {o1=option1, o2=option2} ] [ long {} ]", options.toString());
  }
}