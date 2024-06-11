package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
  
  private PosixParser parser;
  private Options options;
  
  @Before
  public void setup() {
    parser = new PosixParser();
    options = new Options();
  }
  
  @Test
  public void testFlatten_stopAtNonOption_true() {
    options.addOption(new Option("a", true, "Test option a"));
    options.addOption(new Option("b", true, "Test option b"));
    options.addOption(new Option("c", true, "Test option c"));
    
    String[] arguments = new String[]{"-a", "valueA", "-b", "valueB"};
    String[] flattened = parser.flatten(options, arguments, true);
    
    assertEquals(2, flattened.length);
    assertEquals("-a", flattened[0]);
    assertEquals("valueA", flattened[1]);
  }
  
  @Test
  public void testFlatten_stopAtNonOption_false() {
    options.addOption(new Option("a", true, "Test option a"));
    options.addOption(new Option("b", true, "Test option b"));
    options.addOption(new Option("c", true, "Test option c"));
    
    String[] arguments = new String[]{"-a", "valueA", "-b", "valueB"};
    String[] flattened = parser.flatten(options, arguments, false);
    
    assertEquals(4, flattened.length);
    assertEquals("-a", flattened[0]);
    assertEquals("valueA", flattened[1]);
    assertEquals("-b", flattened[2]);
    assertEquals("valueB", flattened[3]);
  }
  
  @Test
  public void testFlatten_specialToken() {
    options.addOption(new Option("a", true, "Test option a"));
    options.addOption(new Option("b", true, "Test option b"));
    options.addOption(new Option("c", true, "Test option c"));
    
    String[] arguments = new String[]{"--test", "--special=value"};
    String[] flattened = parser.flatten(options, arguments, false);
    
    assertEquals(2, flattened.length);
    assertEquals("--test", flattened[0]);
    assertEquals("--special=value", flattened[1]);
  }
  
  @Test
  public void testFlatten_singleHyphen() {
    options.addOption(new Option("a", true, "Test option a"));
    options.addOption(new Option("b", true, "Test option b"));
    options.addOption(new Option("c", true, "Test option c"));
    
    String[] arguments = new String[]{"-"};
    String[] flattened = parser.flatten(options, arguments, false);
    
    assertEquals(1, flattened.length);
    assertEquals("-", flattened[0]);
  }
  
  @Test
  public void testFlatten_optionToken() {
    options.addOption(new Option("a", true, "Test option a"));
    options.addOption(new Option("b", true, "Test option b"));
    options.addOption(new Option("c", true, "Test option c"));
    
    String[] arguments = new String[]{"-a", "valueA", "-d"};
    String[] flattened = parser.flatten(options, arguments, false);
    
    assertEquals(2, flattened.length);
    assertEquals("-a", flattened[0]);
    assertEquals("valueA", flattened[1]);
  }
  
  @Test
  public void testFlatten_burstToken() {
    options.addOption(new Option("a", true, "Test option a"));
    options.addOption(new Option("b", true, "Test option b"));
    options.addOption(new Option("c", true, "Test option c"));
    
    String[] arguments = new String[]{"-abc"};
    String[] flattened = parser.flatten(options, arguments, false);
    
    assertEquals(3, flattened.length);
    assertEquals("-a", flattened[0]);
    assertEquals("-b", flattened[1]);
    assertEquals("-c", flattened[2]);
  }
  
  @Test
  public void testFlatten_noOption() {
    options.addOption(new Option("a", true, "Test option a"));
    options.addOption(new Option("b", true, "Test option b"));
    options.addOption(new Option("c", true, "Test option c"));
    
    String[] arguments = new String[]{"valueA", "valueB"};
    String[] flattened = parser.flatten(options, arguments, false);
    
    assertEquals(2, flattened.length);
    assertEquals("valueA", flattened[0]);
    assertEquals("valueB", flattened[1]);
  }

}