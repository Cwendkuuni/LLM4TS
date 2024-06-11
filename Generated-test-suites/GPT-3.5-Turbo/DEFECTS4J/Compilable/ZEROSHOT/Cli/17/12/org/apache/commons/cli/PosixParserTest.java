package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
  
  private PosixParser posixParser;
  private Options options;
  
  @Before
  public void setUp() {
    posixParser = new PosixParser();
    options = new Options();
  }
  
  @Test
  public void testFlatten_StopAtNonOptionFalse() {
    String[] arguments = {"-a", "arg1", "-b", "arg2", "nonOption"};
    options.addOption("a", false, "Option A");
    options.addOption("b", true, "Option B");
    
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(result.length, 5);
    assertEquals(result[0], "-a");
    assertEquals(result[1], "arg1");
    assertEquals(result[2], "-b");
    assertEquals(result[3], "arg2");
    assertEquals(result[4], "nonOption");
  }
  
  @Test
  public void testFlatten_StopAtNonOptionTrue() {
    String[] arguments = {"-a", "arg1", "-b", "arg2", "nonOption"};
    options.addOption("a", false, "Option A");
    options.addOption("b", true, "Option B");
    
    String[] result = posixParser.flatten(options, arguments, true);
    
    assertEquals(result.length, 2);
    assertEquals(result[0], "-a");
    assertEquals(result[1], "arg1");
  }
  
  @Test
  public void testFlatten_SingleHyphen() {
    String[] arguments = {"-"};
    options.addOption("a", false, "Option A");
    
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(result.length, 1);
    assertEquals(result[0], "-");
  }
  
  @Test
  public void testFlatten_LongOptionWithValue() {
    String[] arguments = {"--option=value"};
    
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(result.length, 2);
    assertEquals(result[0], "--option");
    assertEquals(result[1], "value");
  }
  
  @Test
  public void testFlatten_LongOptionWithoutValue() {
    String[] arguments = {"--option"};
    
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(result.length, 1);
    assertEquals(result[0], "--option");
  }
  
  @Test
  public void testFlatten_ShortOptionWithSingleCharacter() {
    String[] arguments = {"-o"};
    options.addOption("o", false, "Option O");
    
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(result.length, 1);
    assertEquals(result[0], "-o");
  }
  
  @Test
  public void testFlatten_ShortOptionWithMultipleCharacters() {
    String[] arguments = {"-ab"};
    options.addOption("a", false, "Option A");
    options.addOption("b", true, "Option B");
    
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(result.length, 2);
    assertEquals(result[0], "-a");
    assertEquals(result[1], "-b");
  }
  
  @Test
  public void testFlatten_ShortOptionWithValue() {
    String[] arguments = {"-o=value"};
    options.addOption("o", true, "Option O");
    
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(result.length, 2);
    assertEquals(result[0], "-o");
    assertEquals(result[1], "value");
  }
  
  @Test
  public void testFlatten_ShortOptionWithNonOptionStop() {
    String[] arguments = {"-oaob"};
    options.addOption("o", false, "Option O");
    options.addOption("a", false, "Option A");
    options.addOption("b", true, "Option B");
    
    String[] result = posixParser.flatten(options, arguments, true);
    
    assertEquals(result.length, 2);
    assertEquals(result[0], "-o");
    assertEquals(result[1], "aob");
  }
  
  @Test
  public void testFlatten_ShortOptionWithNonOptionNoStop() {
    String[] arguments = {"-oaob"};
    options.addOption("o", false, "Option O");
    options.addOption("a", false, "Option A");
    options.addOption("b", true, "Option B");
    
    String[] result = posixParser.flatten(options, arguments, false);
    
    assertEquals(result.length, 4);
    assertEquals(result[0], "-o");
    assertEquals(result[1], "a");
    assertEquals(result[2], "-o");
    assertEquals(result[3], "b");
  }
}