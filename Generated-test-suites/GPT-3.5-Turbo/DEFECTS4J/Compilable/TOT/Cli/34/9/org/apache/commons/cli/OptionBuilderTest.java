package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

  // Test case for withLongOpt(String newLongopt) method
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("testLongOpt");
    
    Option option = OptionBuilder.create();
    
    assertEquals("testLongOpt", option.getLongOpt());
  }
  
  // Test case for hasArg() method
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.hasArgs());
  }
  
  // Test case for hasArg(boolean hasArg) method
  
  @Test
  public void testHasArgWithTrue() {
    OptionBuilder.hasArg(true);
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.hasArgs());
  }
  
  @Test
  public void testHasArgWithFalse() {
    OptionBuilder.hasArg(false);
    
    Option option = OptionBuilder.create();
    
    assertFalse(option.hasArgs());
  }
  
  // Test case for withArgName(String name) method
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("testArgName");
    
    Option option = OptionBuilder.create();
    
    assertEquals("testArgName", option.getArgName());
  }
  
  // Test case for isRequired() method
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.isRequired());
  }
  
  // Test case for withValueSeparator(char sep) method
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator(':');
    
    Option option = OptionBuilder.create();
    
    assertEquals(':', option.getValueSeparator());
  }
  
  // Test case for withValueSeparator() method
  
  @Test
  public void testWithValueSeparatorDefault() {
    OptionBuilder.withValueSeparator();
    
    Option option = OptionBuilder.create();
    
    assertEquals('=', option.getValueSeparator());
  }
  
  // Test case for isRequired(boolean newRequired) method
  
  @Test
  public void testIsRequireWithTrue() {
    OptionBuilder.isRequired(true);
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testIsRequireWithFalse() {
    OptionBuilder.isRequired(false);
    
    Option option = OptionBuilder.create();
    
    assertFalse(option.isRequired());
  }
  
  // Test case for hasArgs() method
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.hasArgs());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  // Test case for hasArgs(int num) method
  
  @Test
  public void testHasArgsWithPositiveNum() {
    OptionBuilder.hasArgs(3);
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.hasArgs());
    assertEquals(3, option.getArgs());
  }
  
  @Test
  public void testHasArgsWithZeroNum() {
    OptionBuilder.hasArgs(0);
    
    Option option = OptionBuilder.create();
    
    assertFalse(option.hasArgs());
    assertEquals(0, option.getArgs());
  }
  
  @Test
  public void testHasArgsWithNegativeNum() {
    OptionBuilder.hasArgs(-3);
    
    Option option = OptionBuilder.create();
    
    assertFalse(option.hasArgs());
    assertEquals(Option.UNINITIALIZED, option.getArgs());
  }
  
  // Test case for hasOptionalArg() method
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.hasOptionalArg());
    assertTrue(option.hasArgs());
    assertEquals(1, option.getArgs());
  }
  
  // Test case for hasOptionalArgs() method
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.hasOptionalArg());
    assertTrue(option.hasArgs());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  // Test case for hasOptionalArgs(int numArgs) method
  
  @Test
  public void testHasOptionalArgsWithPositiveNum() {
    OptionBuilder.hasOptionalArgs(3);
    
    Option option = OptionBuilder.create();
    
    assertTrue(option.hasOptionalArg());
    assertTrue(option.hasArgs());
    assertEquals(3, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArgsWithZeroNum() {
    OptionBuilder.hasOptionalArgs(0);
    
    Option option = OptionBuilder.create();
    
    assertFalse(option.hasOptionalArg());
    assertFalse(option.hasArgs());
    assertEquals(0, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArgsWithNegativeNum() {
    OptionBuilder.hasOptionalArgs(-3);
    
    Option option = OptionBuilder.create();
    
    assertFalse(option.hasOptionalArg());
    assertFalse(option.hasArgs());
    assertEquals(Option.UNINITIALIZED, option.getArgs());
  }
  
  // Test case for withType(Class newType) method
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(Integer.class);
    
    Option option = OptionBuilder.create();
    
    assertEquals(Integer.class, option.getType());
  }
  
  // Test case for withDescription(String newDescription) method
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("testDescription");
    
    Option option = OptionBuilder.create();
    
    assertEquals("testDescription", option.getDescription());
  }
  
  // Test case for create(char opt) method
  
  @Test
  public void testCreateWithValidOptChar() {
    Option option = OptionBuilder.create('t');
    
    assertNotNull(option);
    assertEquals("t", option.getOpt());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidOptChar() {
    OptionBuilder.create('&');
  }
  
  // Test case for create() method
  
  @Test
  public void testCreateWithValidLongOpt() {
    OptionBuilder.withLongOpt("testLongOpt");
    
    Option option = OptionBuilder.create();
    
    assertNotNull(option);
    assertEquals("testLongOpt", option.getLongOpt());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
  
  // Test case for create(String opt) method
  
  @Test
  public void testCreateWithValidOpt() {
    Option option = OptionBuilder.create("testOpt");
    
    assertNotNull(option);
    assertEquals("testOpt", option.getOpt());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutOpt() {
    OptionBuilder.withLongOpt("testLongOpt");
    
    OptionBuilder.create(null);
  }
}