package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    String longOpt = "test";
    Option option = OptionBuilder.withLongOpt(longOpt).create();
    assertEquals(option.getLongOpt(), longOpt);
  }

  @Test
  public void testHasArg() {
    Option option = OptionBuilder.hasArg().create();
    assertTrue(option.hasArgs());
    assertEquals(option.getArgs(), 1);
  }

  @Test
  public void testHasArgBoolean() {
    Option option = OptionBuilder.hasArg(false).create();
    assertFalse(option.hasArgs());
    assertEquals(option.getArgs(), Option.UNINITIALIZED);
  }

  @Test
  public void testWithArgName() {
    String argName = "test";
    Option option = OptionBuilder.withArgName(argName).create();
    assertEquals(option.getArgName(), argName);
  }

  @Test
  public void testIsRequired() {
    Option option = OptionBuilder.isRequired().create();
    assertTrue(option.isRequired());
  }

  @Test
  public void testWithValueSeparatorChar() {
    char sep = ':';
    Option option = OptionBuilder.withValueSeparator(sep).create();
    assertEquals(option.getValueSeparator(), sep);
  }

  @Test
  public void testWithValueSeparator() {
    Option option = OptionBuilder.withValueSeparator().create();
    assertEquals(option.getValueSeparator(), '=');
  }

  @Test
  public void testIsRequiredBoolean() {
    Option option = OptionBuilder.isRequired(true).create();
    assertTrue(option.isRequired());
  }

  @Test
  public void testHasArgs() {
    Option option = OptionBuilder.hasArgs().create();
    assertTrue(option.hasArgs());
    assertEquals(option.getArgs(), Option.UNLIMITED_VALUES);
  }

  @Test
  public void testHasArgsInt() {
    int num = 2;
    Option option = OptionBuilder.hasArgs(num).create();
    assertTrue(option.hasArgs());
    assertEquals(option.getArgs(), num);
  }

  @Test
  public void testHasOptionalArg() {
    Option option = OptionBuilder.hasOptionalArg().create();
    assertTrue(option.hasArgs());
    assertEquals(option.getArgs(), 1);
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgs() {
    Option option = OptionBuilder.hasOptionalArgs().create();
    assertTrue(option.hasArgs());
    assertEquals(option.getArgs(), Option.UNLIMITED_VALUES);
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgsInt() {
    int num = 2;
    Option option = OptionBuilder.hasOptionalArgs(num).create();
    assertTrue(option.hasArgs());
    assertEquals(option.getArgs(), num);
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testWithType() {
    Object type = new Integer(1);
    Option option = OptionBuilder.withType(type).create();
    assertEquals(option.getType(), type);
  }

  @Test
  public void testWithDescription() {
    String desc = "test";
    Option option = OptionBuilder.withDescription(desc).create();
    assertEquals(option.getDescription(), desc);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithCharInvalidOpt() {
    char opt = '#';
    OptionBuilder.create(opt);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNoLongOpt() {
    OptionBuilder.create();
  }

  @Test
  public void testCreateWithChar() {
    char opt = 't';
    Option option = OptionBuilder.create(opt);
    assertEquals(option.getOpt(), String.valueOf(opt));
  }

  @Test
  public void testCreateWithString() {
    String opt = "test";
    Option option = OptionBuilder.create(opt);
    assertEquals(option.getOpt(), opt);
  }
}