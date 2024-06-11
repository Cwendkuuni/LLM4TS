import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
  
  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    
    assertFalse(parser.eatTheRest);
    assertEquals(0, parser.tokens.size());
    assertNull(parser.currentOption);
  }
  
  @Test
  public void testFlatten() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    String[] arguments = { "-a", "--option=value", "-bc", "argument1", "argument2" };
    
    String[] result = parser.flatten(options, arguments, true);
    
    assertEquals(8, result.length);
    
    assertEquals("-", result[0]);
    assertEquals("a", result[1]);
    assertEquals("--", result[2]);
    assertEquals("option", result[3]);
    assertEquals("value", result[4]);
    assertEquals("-b", result[5]);
    assertEquals("-c", result[6]);
    assertEquals("argument1", result[7]);
  }
  
  @Test
  public void testGobble() {
    PosixParser parser = new PosixParser();
    Iterator iter = Arrays.asList("token1", "token2", "token3").iterator();
    
    parser.eatTheRest = true;
    parser.tokens.add("processed");
    
    parser.gobble(iter);
    
    assertEquals(4, parser.tokens.size());
    assertEquals("processed", parser.tokens.get(0));
    assertEquals("token1", parser.tokens.get(1));
    assertEquals("token2", parser.tokens.get(2));
    assertEquals("token3", parser.tokens.get(3));
  }
  
  @Test
  public void testProcess() {
    PosixParser parser = new PosixParser();
    parser.currentOption = new Option("option", true, "option description");
    
    parser.process("value");
    
    assertEquals(1, parser.tokens.size());
    assertEquals("value", parser.tokens.get(0));
    assertNull(parser.currentOption);
    
    parser.currentOption = new Option("option", false, "option description");
    
    parser.process("value");
    
    assertEquals(1, parser.tokens.size());
    assertEquals("value", parser.tokens.get(0));
    assertNull(parser.currentOption);
    
    parser.currentOption = null;
    
    parser.process("value");
    
    assertEquals(3, parser.tokens.size());
    assertEquals("--", parser.tokens.get(0));
    assertEquals("value", parser.tokens.get(1));
    assertEquals("value", parser.tokens.get(2));
  }
  
  @Test
  public void testProcessOptionToken() {
    Options options = new Options();
    Option option = new Option("option", false, "option description");
    options.addOption(option);
    
    PosixParser parser = new PosixParser();
    parser.options = options;
    
    parser.processOptionToken("option", true);
    
    assertEquals(option, parser.currentOption);
    assertEquals(1, parser.tokens.size());
    assertEquals("option", parser.tokens.get(0));
    
    parser.currentOption = null;
    parser.tokens.clear();
    
    parser.processOptionToken("option", false);
    
    assertNull(parser.currentOption);
    assertEquals(1, parser.tokens.size());
    assertEquals("option", parser.tokens.get(0));
    
    parser.currentOption = null;
    parser.tokens.clear();
    
    parser.processOptionToken("unknown", true);
    
    assertNull(parser.currentOption);
    assertEquals(1, parser.tokens.size());
    assertEquals("unknown", parser.tokens.get(0));
    
    parser.currentOption = null;
    parser.tokens.clear();
    
    parser.processOptionToken("unknown", false);
    
    assertNull(parser.currentOption);
    assertEquals(1, parser.tokens.size());
    assertEquals("u", parser.tokens.get(0));
  }
  
  @Test
  public void testBurstToken() {
    Options options = new Options();
    Option option1 = new Option("a", false, "option a");
    Option option2 = new Option("b", false, "option b");
    options.addOption(option1);
    options.addOption(option2);
    
    PosixParser parser = new PosixParser();
    parser.options = options;
    
    parser.burstToken("-abc", true);
    
    assertEquals(4, parser.tokens.size());
    assertEquals("-", parser.tokens.get(0));
    assertEquals("a", parser.tokens.get(1));
    assertEquals("b", parser.tokens.get(2));
    assertEquals("c", parser.tokens.get(3));
    
    parser.tokens.clear();
    
    parser.burstToken("-abc", false);
    
    assertEquals(1, parser.tokens.size());
    assertEquals("-", parser.tokens.get(0));
    
    parser.tokens.clear();
    
    parser.burstToken("-abcd", true);
    
    assertEquals(4, parser.tokens.size());
    assertEquals("-", parser.tokens.get(0));
    assertEquals("a", parser.tokens.get(1));
    assertEquals("b", parser.tokens.get(2));
    assertEquals("cd", parser.tokens.get(3));
    
    parser.tokens.clear();
    
    parser.burstToken("-abcd", false);
    
    assertEquals(1, parser.tokens.size());
    assertEquals("-", parser.tokens.get(0));
    
    parser.tokens.clear();
    
    parser.burstToken("-u", true);
    
    assertEquals(3, parser.tokens.size());
    assertEquals("-", parser.tokens.get(0));
    assertEquals("u", parser.tokens.get(1));
    assertEquals("u", parser.tokens.get(2));
    
    parser.tokens.clear();
    
    parser.burstToken("-u", false);
    
    assertEquals(1, parser.tokens.size());
    assertEquals("-", parser.tokens.get(0));
  }
  
}