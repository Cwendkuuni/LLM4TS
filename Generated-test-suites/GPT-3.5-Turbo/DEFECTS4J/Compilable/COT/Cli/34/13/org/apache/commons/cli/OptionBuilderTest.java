package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
  
  // Extracted public methods and their signatures:
  /**
   * withLongOpt(String)
   * hasArg()
   * hasArg(boolean)
   * withArgName(String)
   * isRequired()
   * withValueSeparator(char)
   * withValueSeparator()
   * isRequired(boolean)
   * hasArgs()
   * hasArgs(int)
   * hasOptionalArg()
   * hasOptionalArgs()
   * hasOptionalArgs(int)
   * withType(Class)
   * withDescription(String)
   * create(char) throws IllegalArgumentException
   * create() throws IllegalArgumentException
   * create(String) throws IllegalArgumentException
   */
  
  // JUnit 4 test cases:
  
  @Test
  public void testWithLongOpt() {
    // Test basic functionality
    OptionBuilder.withLongOpt("testLongOpt");
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testHasArg() {
    // Test basic functionality
    OptionBuilder.hasArg();
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testHasArgWithBoolean() {
    // Test basic functionality
    OptionBuilder.hasArg(true);
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testWithArgName() {
    // Test basic functionality
    OptionBuilder.withArgName("testArgName");
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testIsRequired() {
    // Test basic functionality
    OptionBuilder.isRequired();
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testWithValueSeparatorChar() {
    // Test basic functionality
    OptionBuilder.withValueSeparator(':');
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testWithValueSeparator() {
    // Test basic functionality
    OptionBuilder.withValueSeparator();
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testIsRequiredWithBoolean() {
    // Test basic functionality
    OptionBuilder.isRequired(true);
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testHasArgs() {
    // Test basic functionality
    OptionBuilder.hasArgs();
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testHasArgsWithInt() {
    // Test basic functionality
    OptionBuilder.hasArgs(2);
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testHasOptionalArg() {
    // Test basic functionality
    OptionBuilder.hasOptionalArg();
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testHasOptionalArgs() {
    // Test basic functionality
    OptionBuilder.hasOptionalArgs();
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testHasOptionalArgsWithInt() {
    // Test basic functionality
    OptionBuilder.hasOptionalArgs(2);
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testWithType() {
    // Test basic functionality
    OptionBuilder.withType(Integer.class);
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testWithDescription() {
    // Test basic functionality
    OptionBuilder.withDescription("testDescription");
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testCreateWithChar() {
    // Test basic functionality
    OptionBuilder.create('t');
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testCreate() {
    // Test basic functionality
    OptionBuilder.create();
    
    // TODO: Add additional test cases here
  }
  
  @Test
  public void testCreateWithString() {
    // Test basic functionality
    OptionBuilder.create("testString");
    
    // TODO: Add additional test cases here
  }
  
}