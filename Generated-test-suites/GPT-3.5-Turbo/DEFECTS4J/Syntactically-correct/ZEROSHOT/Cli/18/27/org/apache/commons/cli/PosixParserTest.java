import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
  
  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    
    assertFalse(parser.eatTheRest);
    assertNotNull(parser.tokens);
    assertEquals(0, parser.tokens.size());
    assertNull(parser.currentOption);
  }
  
  @Test
  public void testFlatten() {
    Options options = new Options();
    options.addOption("a", "optionA", false, "Option A description");
    options.addOption("b", "optionB", true, "Option B description");
    options.addOption("c", "optionC", true, "Option C description");
    
    String[] arguments = new String[] {"-a", "--", "-b", "valueB", "arg1", "-c", "valueC"};
    
    PosixParser parser = new PosixParser();
    String[] flattenedArguments = parser.flatten(options, arguments, true);
    
    assertEquals(8, flattenedArguments.length);
    assertEquals("-a", flattenedArguments[0]);
    assertEquals("--", flattenedArguments[1]);
    assertEquals("-b", flattenedArguments[2]);
    assertEquals("valueB", flattenedArguments[3]);
    assertEquals("arg1", flattenedArguments[4]);
    assertEquals("-c", flattenedArguments[5]);
    assertEquals("valueC", flattenedArguments[6]);
    assertEquals("--", flattenedArguments[7]);
  }
  
  @Test
  public void testGobble() {
    PosixParser parser = new PosixParser();
    parser.init();
    
    Iterator<String> iter = Arrays.asList(new String[] {"arg1", "arg2"}).iterator();
    parser.gobble(iter);
    
    List<String> tokens = parser.tokens;
    assertEquals(2, tokens.size());
    assertEquals("arg1", tokens.get(0));
    assertEquals("arg2", tokens.get(1));
  }
  
  @Test
  public void testProcess() {
    PosixParser parser = new PosixParser();
    parser.init();
    
    Option optionWithArg = new Option("a", "optionA", true, "Option A description");
    parser.currentOption = optionWithArg;
    parser.process("valueA");
    
    List<String> tokens = parser.tokens;
    assertEquals(1, tokens.size());
    assertEquals("valueA", tokens.get(0));
    assertNull(parser.currentOption);
    
    Option optionWithoutArg = new Option("b", "optionB", false, "Option B description");
    parser.currentOption = optionWithoutArg;
    parser.process("valueB");
    
    tokens = parser.tokens;
    assertEquals(2, tokens.size());
    assertEquals("--", tokens.get(0));
    assertEquals("valueB", tokens.get(1));
    assertNull(parser.currentOption);
  }
  
  @Test
  public void testProcessSingleHyphen() {
    PosixParser parser = new PosixParser();
    parser.init();
    
    parser.processSingleHyphen("-");
    
    List<String> tokens = parser.tokens;
    assertEquals(1, tokens.size());
    assertEquals("-", tokens.get(0));
  }
  
  @Test
  public void testProcessOptionToken() {
    Options options = new Options();
    options.addOption("a", "optionA", false, "Option A description");
    
    PosixParser parser = new PosixParser();
    parser.options = options;
    
    parser.processOptionToken("a", true);
    
    List<String> tokens = parser.tokens;
    assertEquals(1, tokens.size());
    assertEquals("-a", tokens.get(0));
    assertNotNull(parser.currentOption);
    
    parser.processOptionToken("b", true);
    
    tokens = parser.tokens;
    assertEquals(1, tokens.size());
    assertEquals("--", tokens.get(0));
    assertNull(parser.currentOption);
  }
  
  @Test
  public void testBurstToken() {
    Options options = new Options();
    options.addOption("a", "optionA", false, "Option A description");
    
    PosixParser parser = new PosixParser();
    parser.options = options;
    
    parser.burstToken("-ab", true);
    
    List<String> tokens = parser.tokens;
    assertEquals(1, tokens.size());
    assertEquals("-ab", tokens.get(0));
    
    parser.burstToken("-cd", true);
    
    tokens = parser.tokens;
    assertEquals(2, tokens.size());
    assertEquals("-ab", tokens.get(0));
    assertEquals("-cd", tokens.get(1));
    
    parser.burstToken("-ef", false);
    
    tokens = parser.tokens;
    assertEquals(3, tokens.size());
    assertEquals("-ab", tokens.get(0));
    assertEquals("-cd", tokens.get(1));
    assertEquals("-ef", tokens.get(2));
  }
}