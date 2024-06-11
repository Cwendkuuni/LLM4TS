import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
  
  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    assertFalse(parser.eatTheRest);
    assertEquals(0, parser.tokens.size());
  }
  
  @Test
  public void testFlatten() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    options.addOption("c", false, "option c");
    
    String[] arguments = {"-a", "--b=value", "--c", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    
    assertEquals(6, result.length);
    assertEquals("-a", result[0]);
    assertEquals("--b", result[1]);
    assertEquals("value", result[2]);
    assertEquals("--c", result[3]);
    assertEquals("arg1", result[4]);
    assertEquals("arg2", result[5]);
  }
  
  @Test
  public void testGobble() {
    PosixParser parser = new PosixParser();
    Iterator iter = Arrays.asList("arg1", "arg2", "arg3").iterator();
    
    parser.eatTheRest = true;
    parser.tokens.add("token");
    
    parser.gobble(iter);
    
    assertFalse(parser.eatTheRest);
    assertEquals(4, parser.tokens.size());
    assertEquals("token", parser.tokens.get(0));
    assertEquals("arg1", parser.tokens.get(1));
    assertEquals("arg2", parser.tokens.get(2));
    assertEquals("arg3", parser.tokens.get(3));
  }
  
  @Test
  public void testProcessNonOptionToken() {
    PosixParser parser = new PosixParser();
    parser.processNonOptionToken("value");
    
    assertTrue(parser.eatTheRest);
    assertEquals(2, parser.tokens.size());
    assertEquals("--", parser.tokens.get(0));
    assertEquals("value", parser.tokens.get(1));
  }
  
  @Test
  public void testProcessOptionToken() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    options.addOption("c", false, "option c");
    parser.options = options;
    
    parser.processOptionToken("a", false);
    
    assertFalse(parser.eatTheRest);
    assertEquals(1, parser.tokens.size());
    assertEquals("a", parser.tokens.get(0));
  }
  
  @Test
  public void testBurstToken() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    options.addOption("c", false, "option c");
    parser.options = options;
    
    parser.burstToken("-abc", false);
    
    assertFalse(parser.eatTheRest);
    assertEquals(3, parser.tokens.size());
    assertEquals("-a", parser.tokens.get(0));
    assertEquals("-b", parser.tokens.get(1));
    assertEquals("-c", parser.tokens.get(2));
  }
  
}