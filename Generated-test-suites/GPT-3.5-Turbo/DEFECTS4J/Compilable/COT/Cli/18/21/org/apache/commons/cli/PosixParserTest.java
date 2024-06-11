package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
  
  private PosixParser parser;
  private Options options;
  
  @Before
  public void setUp() {
    parser = new PosixParser();
    options = new Options();
  }
  
  @Test
  public void testFlatten() {
    String[] arguments = {"-a", "--option", "value"};
    
    String[] result = parser.flatten(options, arguments, false);
    
    assertEquals("-a", result[0]);
    assertEquals("--option", result[1]);
    assertEquals("value", result[2]);
  }
  
  @Test
  public void testFlattenStopAtNonOption() {
    String[] arguments = {"-a", "nonoption", "--option", "value"};
    
    String[] result = parser.flatten(options, arguments, true);
    
    assertEquals("-a", result[0]);
    assertEquals("nonoption", result[1]);
  }
  
  @Test
  public void testFlattenSingleHyphen() {
    String[] arguments = {"-"};
    
    String[] result = parser.flatten(options, arguments, false);
    
    assertEquals("-", result[0]);
  }
  
  @Test
  public void testFlattenOptionToken() {
    options.addOption("a", false, "Description of option a");
    
    String[] arguments = {"-a"};
    
    String[] result = parser.flatten(options, arguments, false);
    
    assertEquals("-a", result[0]);
  }
  
  @Test
  public void testFlattenOptionTokenStopAtNonOption() {
    options.addOption("a", false, "Description of option a");
    
    String[] arguments = {"-a", "nonoption"};
    
    String[] result = parser.flatten(options, arguments, true);
    
    assertEquals("-a", result[0]);
    assertEquals("nonoption", result[1]);
  }
  
  @Test
  public void testFlattenBurstToken() {
    options.addOption("a", false, "Description of option a");
    options.addOption("b", true, "Description of option b");
    
    String[] arguments = {"-ab"};
    
    String[] result = parser.flatten(options, arguments, false);
    
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
  }
  
  @Test
  public void testFlattenBurstTokenStopAtNonOption() {
    options.addOption("a", false, "Description of option a");
    options.addOption("b", true, "Description of option b");
    
    String[] arguments = {"-ab", "nonoption"};
    
    String[] result = parser.flatten(options, arguments, true);
    
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("nonoption", result[2]);
  }
  
  @Test
  public void testFlattenIgnoreNonOption() {
    options.addOption("a", false, "Description of option a");
    
    String[] arguments = {"-a", "nonoption"};
    
    String[] result = parser.flatten(options, arguments, false);
    
    assertEquals("-a", result[0]);
  }
  
  @Test
  public void testFlattenIgnoreNonOptionStopAtNonOption() {
    options.addOption("a", false, "Description of option a");
    
    String[] arguments = {"-a", "nonoption"};
    
    String[] result = parser.flatten(options, arguments, true);
    
    assertEquals("-a", result[0]);
  }
  
  @Test
  public void testFlattenEatTheRest() {
    options.addOption("a", false, "Description of option a");
    
    String[] arguments = {"-a", "nonoption1", "nonoption2"};
    
    String[] result = parser.flatten(options, arguments, false);
    
    assertEquals("-a", result[0]);
    assertEquals("--", result[1]);
    assertEquals("nonoption1", result[2]);
    assertEquals("nonoption2", result[3]);
  }
  
  @Test
  public void testFlattenEatTheRestStopAtNonOption() {
    options.addOption("a", false, "Description of option a");
    
    String[] arguments = {"-a", "nonoption1", "nonoption2"};
    
    String[] result = parser.flatten(options, arguments, true);
    
    assertEquals("-a", result[0]);
    assertEquals("nonoption1", result[1]);
    assertEquals("nonoption2", result[2]);
  }
  
}