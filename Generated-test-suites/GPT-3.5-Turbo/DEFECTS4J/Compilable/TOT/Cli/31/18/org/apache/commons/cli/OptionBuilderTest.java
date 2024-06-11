package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("testOpt");
    Option option = OptionBuilder.create();
    assertEquals("testOpt", option.getLongOpt());
  }

  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("testName");
    Option option = OptionBuilder.create();
    assertEquals("testName", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }

  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    assertEquals(':', option.getValueSeparator());
  }

  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
  }

  @Test
  public void testIsRequiredBoolean() {
    OptionBuilder.isRequired(true);
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }

  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    assertEquals(Integer.MAX_VALUE, option.getArgs());
  }

  @Test
  public void testHasArgsInt() {
    OptionBuilder.hasArgs(3);
    Option option = OptionBuilder.create();
    assertEquals(3, option.getArgs());
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create();
    assertEquals(Integer.MAX_VALUE, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgsInt() {
    OptionBuilder.hasOptionalArgs(2);
    Option option = OptionBuilder.create();
    assertEquals(2, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testWithType() {
    OptionBuilder.withType(Integer.class);
    Option option = OptionBuilder.create();
    assertEquals(Integer.class, option.getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("testDescription");
    Option option = OptionBuilder.create();
    assertEquals("testDescription", option.getDescription());
  }

  @Test
  public void testCreateWithOptionChar() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
  }

  @Test
  public void testCreateWithOptionString() {
    Option option = OptionBuilder.create("testOpt");
    assertEquals("testOpt", option.getOpt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutOption() {
    OptionBuilder.create();
  }
}