package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    // Test with valid long opt
    OptionBuilder.withLongOpt("test");
    assertNotNull(OptionBuilder.create());
    
    // Test with null long opt
    OptionBuilder.withLongOpt(null);
    try {
      OptionBuilder.create();
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      // Exception should be thrown
    }
  }

  @Test
  public void testHasArg() {
    // Test hasArg() method
    OptionBuilder.hasArg();
    assertEquals(1, OptionBuilder.create().getArgs());

    // Test hasArg(boolean hasArg) method with true
    OptionBuilder.hasArg(true);
    assertEquals(1, OptionBuilder.create().getArgs());

    // Test hasArg(boolean hasArg) method with false
    OptionBuilder.hasArg(false);
    assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("test");
    assertNotNull(OptionBuilder.create());
  }

  @Test
  public void testIsRequired() {
    // Test isRequired() method
    OptionBuilder.isRequired();
    assertTrue(OptionBuilder.create().isRequired());

    // Test isRequired(boolean newRequired) method with true
    OptionBuilder.isRequired(true);
    assertTrue(OptionBuilder.create().isRequired());

    // Test isRequired(boolean newRequired) method with false
    OptionBuilder.isRequired(false);
    assertFalse(OptionBuilder.create().isRequired());
  }

  @Test
  public void testWithOptionalArg() {
    OptionBuilder.hasOptionalArg();
    assertEquals(1, OptionBuilder.create().getArgs());
    assertTrue(OptionBuilder.create().hasOptionalArg());
  }

  @Test
  public void testWithOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    assertTrue(OptionBuilder.create().hasOptionalArg());
  }

  @Test
  public void testWithOptionalArgsInt() {
    OptionBuilder.hasOptionalArgs(2);
    assertEquals(2, OptionBuilder.create().getArgs());
    assertTrue(OptionBuilder.create().hasOptionalArg());
  }

  @Test
  public void testWithType() {
    OptionBuilder.withType(String.class);
    assertEquals(String.class, OptionBuilder.create().getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("Test Option");
    assertEquals("Test Option", OptionBuilder.create().getDescription());
  }

  @Test
  public void testCreateChar() {
    // Test with valid char
    Option option = OptionBuilder.create('t');
    assertEquals("t", option.getOpt());
    assertNull(option.getLongOpt());

    // Test with invalid char
    try {
      OptionBuilder.create('$');
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      // Exception should be thrown
    }
  }

  @Test
  public void testCreateString() {
    // Test with valid string
    Option option = OptionBuilder.create("test");
    assertEquals("test", option.getOpt());
    assertNull(option.getLongOpt());

    // Test with null string
    OptionBuilder.withLongOpt("test");
    assertNotNull(OptionBuilder.create(null));
    
    // Test with null longOpt
    OptionBuilder.withLongOpt(null);
    try {
      OptionBuilder.create("test");
      fail("IllegalArgumentException should be thrown");
    } catch (IllegalArgumentException e) {
      // Exception should be thrown
    }
  }
}

