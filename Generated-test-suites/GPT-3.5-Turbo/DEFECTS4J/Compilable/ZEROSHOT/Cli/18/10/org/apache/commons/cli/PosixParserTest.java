package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
  
  private Options options;
  private PosixParser parser;
  
  @Before
  public void setUp() {
    options = new Options();
    parser = new PosixParser();
  }
  
  @Test
  public void testFlatten() {
    // Test case 1: No options or arguments
    String[] args = {};
    String[] result = parser.flatten(options, args, false);
    assertEquals(0, result.length);
    
    // Test case 2: Single option with no argument
    options.addOption("a", false, "Option a");
    args = new String[] {"-a"};
    result = parser.flatten(options, args, false);
    assertEquals(1, result.length);
    assertEquals("-a", result[0]);
    
    // Test case 3: Single option with argument
    options.addOption("b", true, "Option b with argument");
    args = new String[] {"-b", "arg"};
    result = parser.flatten(options, args, false);
    assertEquals(2, result.length);
    assertEquals("-b", result[0]);
    assertEquals("arg", result[1]);
    
    // Test case 4: Multiple options with and without argument
    options.addOption("c", "c-option", false, "Option c");
    args = new String[] {"-a", "-b", "arg", "--c-option"};
    result = parser.flatten(options, args, false);
    assertEquals(5, result.length);
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("arg", result[2]);
    assertEquals("--c-option", result[3]);
    assertEquals("c-option", result[4]);
    
    // Test case 5: Stop at non-option
    args = new String[] {"-a", "-b", "arg", "non-option", "--c-option"};
    result = parser.flatten(options, args, true);
    assertEquals(4, result.length);
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("arg", result[2]);
    assertEquals("non-option", result[3]);
  }
}