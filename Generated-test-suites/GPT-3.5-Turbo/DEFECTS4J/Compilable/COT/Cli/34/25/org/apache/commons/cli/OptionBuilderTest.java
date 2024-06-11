package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder builder = OptionBuilder.withLongOpt("testLongOpt");
    Option option = OptionBuilder.create();
    
    assertEquals("testLongOpt", option.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder builder = OptionBuilder.hasArg();
    Option option = OptionBuilder.create();
    
    assertEquals(1, option.getArgs());
  }
  
  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder builder = OptionBuilder.hasArg(true);
    Option option = OptionBuilder.create();
    
    assertEquals(1, option.getArgs());
    
    builder = OptionBuilder.hasArg(false);
    option = OptionBuilder.create();
    
    assertEquals(Option.UNINITIALIZED, option.getArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder builder = OptionBuilder.withArgName("testArgName");
    Option option = OptionBuilder.create();
    
    assertEquals("testArgName", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder builder = OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder builder = OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    
    assertEquals(':', option.getValueSeparator());
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder builder = OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    
    assertEquals('=', option.getValueSeparator());
  }
  
  @Test
  public void testIsRequiredWithBoolean() {
    OptionBuilder builder = OptionBuilder.isRequired(true);
    Option option = OptionBuilder.create();
    
    assertTrue(option.isRequired());
    
    builder = OptionBuilder.isRequired(false);
    option = OptionBuilder.create();
    
    assertFalse(option.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder builder = OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  @Test
  public void testHasArgsWithNum() {
    OptionBuilder builder = OptionBuilder.hasArgs(2);
    Option option = OptionBuilder.create();
    
    assertEquals(2, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder builder = OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create();
    
    assertEquals(1, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder builder = OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create();
    
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgsWithNumArgs() {
    OptionBuilder builder = OptionBuilder.hasOptionalArgs(2);
    Option option = OptionBuilder.create();
    
    assertEquals(2, option.getArgs());
    assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder builder = OptionBuilder.withType(Integer.class);
    Option option = OptionBuilder.create();
    
    assertEquals(Integer.class, option.getType());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder builder = OptionBuilder.withDescription("testDescription");
    Option option = OptionBuilder.create();
    
    assertEquals("testDescription", option.getDescription());
  }
  
  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('t');
    
    assertEquals('t', option.getOpt());
  }
  
  @Test
  public void testCreate() {
    try {
      Option option = OptionBuilder.create();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
  
  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("test");
    
    assertEquals("test", option.getOpt());
  }

}