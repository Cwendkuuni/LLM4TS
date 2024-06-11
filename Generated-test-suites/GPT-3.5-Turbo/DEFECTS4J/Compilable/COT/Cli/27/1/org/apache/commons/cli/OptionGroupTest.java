package org.apache.commons.cli;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class OptionGroupTest {

  @Test
  public void testAddOption() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("h", "help", false, "print help message");
    OptionGroup result = optionGroup.addOption(option);
    assertNotNull(result);
    assertTrue(result.getOptions().contains(option));
  }

  @Test
  public void testGetNames() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("h", "help", false, "print help message");
    optionGroup.addOption(option);
    assertTrue(optionGroup.getNames().contains("h"));
    assertTrue(optionGroup.getNames().contains("help"));
  }

  @Test
  public void testGetOptions() {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("h", "help", false, "print help message");
    optionGroup.addOption(option);
    assertTrue(optionGroup.getOptions().contains(option));
  }

  @Test
  public void testSetSelected() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("h", "help", false, "print help message");
    Option option2 = new Option("v", "version", false, "print version");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    optionGroup.setSelected(option1);
    assertEquals("h", optionGroup.getSelected());
    optionGroup.setSelected(option2);
    assertEquals("v", optionGroup.getSelected());
  }

  @Test(expected = AlreadySelectedException.class)
  public void testSetSelectedAlreadySelected() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("h", "help", false, "print help message");
    Option option2 = new Option("v", "version", false, "print version");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    optionGroup.setSelected(option1);
    optionGroup.setSelected(option2);
    optionGroup.setSelected(option1);
  }

  @Test
  public void testSetSelectedNull() throws AlreadySelectedException {
    OptionGroup optionGroup = new OptionGroup();
    Option option = new Option("h", "help", false, "print help message");
    optionGroup.addOption(option);
    optionGroup.setSelected(option);
    assertEquals("h", optionGroup.getSelected());
    optionGroup.setSelected(null);
    assertNull(optionGroup.getSelected());
  }

  @Test
  public void testIsRequired() {
    OptionGroup optionGroup = new OptionGroup();
    optionGroup.setRequired(true);
    assertTrue(optionGroup.isRequired());
    optionGroup.setRequired(false);
    assertFalse(optionGroup.isRequired());
  }

  @Test
  public void testToString() {
    OptionGroup optionGroup = new OptionGroup();
    Option option1 = new Option("h", "help", false, "print help message");
    Option option2 = new Option("v", "version", false, "print version");
    optionGroup.addOption(option1);
    optionGroup.addOption(option2);
    String expected = "[h -help, v -version]";
    assertEquals(expected, optionGroup.toString());
  }

}