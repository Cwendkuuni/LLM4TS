package org.apache.commons.cli;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

  private CommandLine commandLine;

  @Before
  public void setUp() {
    commandLine = new CommandLine();
  }

  @Test
  public void testHasOption() {
    assertFalse(commandLine.hasOption("abc"));
    assertFalse(commandLine.hasOption('x'));
  }

  @Test
  public void testGetOptionObject() {
    assertNull(commandLine.getOptionObject("abc"));
    assertNull(commandLine.getOptionObject('x'));
  }

  @Test
  public void testGetOptionValue() {
    assertNull(commandLine.getOptionValue("abc"));
    assertNull(commandLine.getOptionValue('x'));
  }

  @Test
  public void testGetOptionValues() {
    assertNull(commandLine.getOptionValues("abc"));
    assertNull(commandLine.getOptionValues('x'));
  }

  @Test
  public void testGetArgs() {
    String[] args = commandLine.getArgs();
    assertEquals(0, args.length);
  }

  @Test
  public void testAddOption() {
    Option option = new Option("abc", "abc", true, "abc description");
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption("abc"));
    assertEquals(option, commandLine.getOptionObject("abc"));
    assertEquals("abc value", commandLine.getOptionValue("abc", "default"));
    assertEquals(new String[]{"abc value"}, commandLine.getOptionValues("abc"));
    assertEquals(1, commandLine.getArgs().length);
    assertEquals(option.toString(), commandLine.getArgs()[0]);
  }
}

