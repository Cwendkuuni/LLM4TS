package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test");
    assertEquals("test", OptionBuilder.create().getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    assertEquals(1, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testHasArgWithTrue() {
    OptionBuilder.hasArg(true);
    assertEquals(1, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testHasArgWithFalse() {
    OptionBuilder.hasArg(false);
    assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("test");
    assertEquals("test", OptionBuilder.create().getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    assertTrue(OptionBuilder.create().isRequired());
  }
  
  @Test
  public void testWithOptionalArg() {
    OptionBuilder.hasOptionalArg();
    assertTrue(OptionBuilder.create().hasOptionalArg());
    assertEquals(1, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testWithOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    assertTrue(OptionBuilder.create().hasOptionalArg());
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testWithOptionalArgsNum() {
    OptionBuilder.hasOptionalArgs(2);
    assertTrue(OptionBuilder.create().hasOptionalArg());
    assertEquals(2, OptionBuilder.create().getArgs());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test");
    assertEquals("test", OptionBuilder.create().getDescription());
  }
  
  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithEmptyOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithOpt() {
    Option option = OptionBuilder.create("test");
    assertEquals("test", option.getOpt());
  }
  
  @Test
  public void testReset() {
    OptionBuilder.reset();
    assertNull(OptionBuilder.create().getDescription());
    assertNull(OptionBuilder.create().getArgName());
    assertNull(OptionBuilder.create().getLongOpt());
    assertNull(OptionBuilder.create().getType());
    assertFalse(OptionBuilder.create().isRequired());
    assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    assertFalse(OptionBuilder.create().hasOptionalArg());
    assertEquals((char)0, OptionBuilder.create().getValueSeparator());
  }
}