package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.util.*;

public class OptionTest {

  private Option option;

  @Before
  public void setUp() throws Exception {
    option = new Option("t", "test", true, "Test option");
  }

  @After
  public void tearDown() throws Exception {
    option = null;
  }

  @Test
  public void testGetOpt() {
    assertEquals("t", option.getOpt());
  }

  @Test
  public void testGetLongOpt() {
    assertEquals("test", option.getLongOpt());
  }

  @Test
  public void testSetLongOpt() {
    option.setLongOpt("test");
    assertEquals("test", option.getLongOpt());
  }

  @Test
  public void testSetOptionalArg() {
    option.setOptionalArg(true);
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasLongOpt() {
    assertTrue(option.hasLongOpt());
  }

  @Test
  public void testHasArg() {
    assertTrue(option.hasArg());
  }

  @Test
  public void testGetDescription() {
    assertEquals("Test option", option.getDescription());
  }

  @Test
  public void testSetDescription() {
    option.setDescription("New description");
    assertEquals("New description", option.getDescription());
  }

  @Test
  public void testIsRequired() {
    assertFalse(option.isRequired());
  }

  @Test
  public void testSetRequired() {
    option.setRequired(true);
    assertTrue(option.isRequired());
  }

  @Test
  public void testSetArgName() {
    option.setArgName("new arg");
    assertEquals("new arg", option.getArgName());
  }

  @Test
  public void testHasArgName() {
    assertFalse(option.hasArgName());
  }

  @Test
  public void testHasArgs() {
    assertFalse(option.hasArgs());
  }

  @Test
  public void testSetArgs() {
    option.setArgs(2);
    assertTrue(option.hasArgs());
  }

  @Test
  public void testSetValueSeparator() {
    option.setValueSeparator('|');
    assertEquals('|', option.getValueSeparator());
  }

  @Test
  public void testHasValueSeparator() {
    assertFalse(option.hasValueSeparator());
  }

  @Test
  public void testGetArgs() {
    assertEquals(-1, option.getArgs());
  }

  @Test(expected = RuntimeException.class)
  public void testAddValueForProcessingException() {
    option.addValueForProcessing("value");
  }

  @Test
  public void testHasNoValues() {
    assertTrue(option.hasNoValues());
  }

  @Test
  public void testEquals() {
    Option option2 = new Option("t", "test", true, "Test option");
    assertTrue(option.equals(option2));
  }

  @Test
  public void testHashCode() {
    Option option2 = new Option("t", "test", true, "Test option");
    assertEquals(option.hashCode(), option2.hashCode());
  }

  @Test
  public void testClone() {
    Option option2 = (Option) option.clone();
    assertEquals(option.getOpt(), option2.getOpt());
    assertEquals(option.getLongOpt(), option2.getLongOpt());
    assertEquals(option.hasOptionalArg(), option2.hasOptionalArg());
    assertEquals(option.getDescription(), option2.getDescription());
    assertEquals(option.isRequired(), option2.isRequired());
    assertEquals(option.getArgName(), option2.getArgName());
    assertEquals(option.hasArgName(), option2.hasArgName());
    assertEquals(option.hasArgs(), option2.hasArgs());
    assertEquals(option.getValueSeparator(), option2.getValueSeparator());
    assertEquals(option.getArgs(), option2.getArgs());
    assertEquals(option.hasNoValues(), option2.hasNoValues());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAddValue() {
    option.addValue("value");
  }
}