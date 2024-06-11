package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("newLongopt");
    Option option = OptionBuilder.create();
    assertEquals("newLongopt", option.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
  }
  
  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
    Option option = OptionBuilder.create();
    assertTrue(option.hasArg());
    
    OptionBuilder.hasArg(false);
    option = OptionBuilder.create();
    assertFalse(option.hasArg());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("name");
    Option option = OptionBuilder.create();
    assertEquals("name", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithValuesSeparator() {
    OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    assertEquals(':', option.getValueSeparator());
  }
  
  @Test
  public void testWithValuesSeparatorDefault() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    assertEquals('=', option.getValueSeparator());
  }
  
  @Test
  public void testIsRequiredWithBoolean() {
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
  public void testHasArgsWithInt() {
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
  public void testHasOptionalArgsWithInt() {
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
    OptionBuilder.withDescription("newDescription");
    Option option = OptionBuilder.create();
    assertEquals("newDescription", option.getDescription());
  }
  
  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('C');
    assertEquals("C", option.getOpt());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("COPT");
    assertEquals("COPT", option.getOpt());
  }
}