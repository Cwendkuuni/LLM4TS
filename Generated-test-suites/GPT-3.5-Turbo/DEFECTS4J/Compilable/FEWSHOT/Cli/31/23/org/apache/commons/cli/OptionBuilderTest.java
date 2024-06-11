package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test");
    Option opt = OptionBuilder.create();
    
    assertEquals("test", opt.getLongOpt());
  }
  
  @Test 
  public void testHasArg() {
    OptionBuilder.hasArg();
    Option opt = OptionBuilder.create();
    
    assertEquals(1, opt.getArgs());
  }
  
  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
    Option opt = OptionBuilder.create();
    
    assertEquals(1, opt.getArgs());
    
    OptionBuilder.hasArg(false);
    opt = OptionBuilder.create();
    
    assertEquals(-1, opt.getArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("test");
    Option opt = OptionBuilder.create();
    
    assertEquals("test", opt.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option opt = OptionBuilder.create();
    
    assertTrue(opt.isRequired());
  }
    
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator(':');
    Option opt = OptionBuilder.create();
    
    assertEquals(':', opt.getValueSeparator());
  }
  
  @Test
  public void testWithDefaultValueSeparator() {
    OptionBuilder.withValueSeparator();
    Option opt = OptionBuilder.create();
    
    assertEquals('=', opt.getValueSeparator());
  }
  
  @Test
  public void testIsRequiredWithBoolean() {
    OptionBuilder.isRequired(true);
    Option opt = OptionBuilder.create();
    
    assertTrue(opt.isRequired());
    
    OptionBuilder.isRequired(false);
    opt = OptionBuilder.create();
    
    assertFalse(opt.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    Option opt = OptionBuilder.create();
    
    assertEquals(Integer.MAX_VALUE, opt.getArgs());
  }
  
  @Test
  public void testHasArgsWithNum() {
    OptionBuilder.hasArgs(3);
    Option opt = OptionBuilder.create();
    
    assertEquals(3, opt.getArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    Option opt = OptionBuilder.create();
    
    assertEquals(1, opt.getArgs());
    assertTrue(opt.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    Option opt = OptionBuilder.create();
    
    assertEquals(Integer.MAX_VALUE, opt.getArgs());
    assertTrue(opt.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsWithNumArgs() {
    OptionBuilder.hasOptionalArgs(5);
    Option opt = OptionBuilder.create();
    
    assertEquals(5, opt.getArgs());
    assertTrue(opt.hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(String.class);
    Option opt = OptionBuilder.create();
    
    assertEquals(String.class, opt.getType());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test");
    Option opt = OptionBuilder.create();
    
    assertEquals("test", opt.getDescription());
  }
  
  @Test
  public void testCreate() {
    OptionBuilder.withLongOpt("test");
    OptionBuilder.hasArg();
    OptionBuilder.withArgName("test");
    OptionBuilder.isRequired();
    OptionBuilder.withValueSeparator(':');
    OptionBuilder.hasArgs();
    OptionBuilder.hasOptionalArgs();
    OptionBuilder.withType(String.class);
    OptionBuilder.withDescription("test");
    
    Option opt = OptionBuilder.create();
    
    assertEquals("test", opt.getLongOpt());
    assertEquals(1, opt.getArgs());
    assertEquals("test", opt.getArgName());
    assertTrue(opt.isRequired());
    assertEquals(':', opt.getValueSeparator());
    assertEquals(Integer.MAX_VALUE, opt.getArgs());
    assertTrue(opt.hasOptionalArg());
    assertEquals(String.class, opt.getType());
    assertEquals("test", opt.getDescription());
  }
  
}