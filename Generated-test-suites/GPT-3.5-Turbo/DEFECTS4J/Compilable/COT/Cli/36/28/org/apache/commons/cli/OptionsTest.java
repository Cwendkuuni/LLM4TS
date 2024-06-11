package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

  private Options options;

  @Before
  public void setup() {
    options = new Options();
  }

  @Test
  public void testAddOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("o1", "option1", false, "Option 1");
    Option option2 = new Option("o2", "option2", false, "Option 2");
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);

    assertTrue(options.getOptionGroups().contains(group));
    assertTrue(options.getOption("o1") != null);
    assertTrue(options.getOption("o2") != null);
  }

  @Test
  public void testAddOption() {
    Option option = new Option("o", "option", false, "Option");
    options.addOption(option);

    assertTrue(options.getOption("o") != null);
    assertTrue(options.getOption("option") != null);
  }

  @Test
  public void testAddOptionWithArgument() {
    Option option = new Option("o", "option", true, "Option with argument");
    options.addOption(option);

    assertTrue(options.getOption("o") != null);
    assertTrue(options.getOption("option") != null);
  }

  @Test
  public void testGetOptions() {
    Option option1 = new Option("o1", "option1", false, "Option 1");
    Option option2 = new Option("o2", "option2", false, "Option 2");
    options.addOption(option1);
    options.addOption(option2);

    assertTrue(options.getOptions().contains(option1));
    assertTrue(options.getOptions().contains(option2));
  }

  @Test
  public void testGetRequiredOptions() {
    Option option = new Option("o", "option", false, "Option");
    option.setRequired(true);
    options.addOption(option);

    assertTrue(options.getRequiredOptions().contains(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("o", "option", false, "Option");
    options.addOption(option);

    assertEquals(option, options.getOption("o"));
    assertEquals(option, options.getOption("option"));
  }

  @Test
  public void testGetMatchingOptions() {
    Option option1 = new Option("o1", "option1", false, "Option 1");
    Option option2 = new Option("o2", "option2", false, "Option 2");
    Option option3 = new Option("o3", "option3", false, "Option 3");
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);

    assertEquals(1, options.getMatchingOptions("o1").size());
    assertTrue(options.getMatchingOptions("o1").contains("o1"));
    assertEquals(2, options.getMatchingOptions("o").size());
    assertTrue(options.getMatchingOptions("o").contains("o1"));
    assertTrue(options.getMatchingOptions("o").contains("o2"));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("o", "option", false, "Option");
    options.addOption(option);

    assertTrue(options.hasOption("o"));
    assertTrue(options.hasOption("option"));
  }

  @Test
  public void testHasLongOption() {
    Option option = new Option("o", "option", false, "Option");
    options.addOption(option);

    assertTrue(options.hasLongOption("option"));
  }

  @Test
  public void testHasShortOption() {
    Option option = new Option("o", "option", false, "Option");
    options.addOption(option);

    assertTrue(options.hasShortOption("o"));
  }

  @Test
  public void testGetOptionGroup() {
    OptionGroup group = new OptionGroup();
    Option option1 = new Option("o1", "option1", false, "Option 1");
    Option option2 = new Option("o2", "option2", false, "Option 2");
    group.addOption(option1);
    group.addOption(option2);
    options.addOptionGroup(group);

    assertEquals(group, options.getOptionGroup(option1));
    assertEquals(group, options.getOptionGroup(option2));
  }

}