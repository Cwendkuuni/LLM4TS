
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create();
    assertEquals("test", option.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
  }
  
  @Test
  public void testHasArgBoolean() {
    OptionBuilder.hasArg(true);
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
    
    OptionBuilder.hasArg(false);
    option = OptionBuilder.create();
    assertFalse(option.hasArg());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("argName");
    Option option = OptionBuilder.create();
    assertEquals("argName", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("description");
    Option option = OptionBuilder.create();
    assertEquals("description", option.getDescription());
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
    
    OptionBuilder.isRequired(false);
    option = OptionBuilder.create();
    assertFalse(option.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
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
    assertTrue(option.hasArg());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create();
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsInt() {
    OptionBuilder.hasOptionalArgs(3);
    Option option = OptionBuilder.create();
    assertEquals(3, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(Integer.class);
    Option option = OptionBuilder.create();
    assertEquals(Integer.class, option.getType());
  }
  
  @Test
  public void testCreateChar() {
    Option option = OptionBuilder.create('c');
    assertEquals("c", option.getOpt());
  }
  
  @Test
  public void testCreateString() {
    Option option = OptionBuilder.create("option");
    assertEquals("option", option.getOpt());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithLongOpt() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create();
    assertEquals("test", option.getLongOpt());
  }
  
  @Test
  public void testCreateWithLongOptNull() {
    Option option = OptionBuilder.create();
    assertNull(option.getLongOpt());
  }

  @Test
  public void testReset() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create();
    assertEquals("test", option.getLongOpt());
    
    OptionBuilder.reset();
    option = OptionBuilder.create();
    assertNull(option.getLongOpt());
  }

}
