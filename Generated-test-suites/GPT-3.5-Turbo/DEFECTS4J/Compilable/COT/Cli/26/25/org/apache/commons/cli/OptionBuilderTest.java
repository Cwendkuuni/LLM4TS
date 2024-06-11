package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    Option option = OptionBuilder.withLongOpt("test").create();
    assertEquals("test", option.getLongOpt());
  }

  @Test
  public void testHasArg() {
    Option option = OptionBuilder.hasArg().create();
    assertTrue(option.hasArg());
    assertEquals(1, option.getArgs());
  }

  @Test
  public void testHasArgWithFalse() {
    Option option = OptionBuilder.hasArg(false).create();
    assertFalse(option.hasArg());
    assertEquals(Option.UNINITIALIZED, option.getArgs());
  }

  @Test
  public void testWithArgName() {
    Option option = OptionBuilder.withArgName("argName").create();
    assertEquals("argName", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    Option option = OptionBuilder.isRequired().create();
    assertTrue(option.isRequired());
  }

  @Test
  public void testWithOptionalArg() {
    Option option = OptionBuilder.hasOptionalArg().create();
    assertTrue(option.hasOptionalArg());
    assertEquals(1, option.getArgs());
  }

  @Test
  public void testHasOptionalArgs() {
    Option option = OptionBuilder.hasOptionalArgs().create();
    assertTrue(option.hasOptionalArg());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testHasOptionalArgsWithNum() {
    Option option = OptionBuilder.hasOptionalArgs(2).create();
    assertTrue(option.hasOptionalArg());
    assertEquals(2, option.getArgs());
  }

  @Test
  public void testWithValueSeparator() {
    Option option = OptionBuilder.withValueSeparator(':').create();
    assertEquals(':', option.getValueSeparator());
  }

  @Test
  public void testWithDescription() {
    Option option = OptionBuilder.withDescription("description").create();
    assertEquals("description", option.getDescription());
  }

  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidChar() {
    OptionBuilder.create('$');
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
  
}


