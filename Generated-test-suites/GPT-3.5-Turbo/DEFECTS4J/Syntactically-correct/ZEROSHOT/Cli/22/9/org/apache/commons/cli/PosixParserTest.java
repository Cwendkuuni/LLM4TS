import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
  
  @Test
  public void testFlatten() {
    Options options = new Options();
    
    PosixParser parser = new PosixParser();
    
    String[] arguments = {"--foo", "bar", "-abc", "test", "--", "arg1", "arg2"};
    String[] expected = {"--foo", "bar", "-a", "-b", "-c", "test", "--", "arg1", "arg2"};
    
    String[] flattened = parser.flatten(options, arguments, true);
    
    assertArrayEquals(expected, flattened);
  }
  
  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    
    parser.init();
    
    assertTrue(parser.tokens.isEmpty());
    assertFalse(parser.eatTheRest);
  }
  
  @Test
  public void testProcessNonOptionToken() {
    PosixParser parser = new PosixParser();
    
    parser.tokens = new ArrayList();
    
    parser.processNonOptionToken("value");
    
    assertEquals("--", parser.tokens.get(0));
    assertEquals("value", parser.tokens.get(1));
  }
  
  @Test
  public void testProcessOptionToken() {
    PosixParser parser = new PosixParser();
    
    parser.tokens = new ArrayList();
    parser.options = new Options();
    parser.options.addOption("a", false);
    parser.options.addOption("b", true);
    
    parser.processOptionToken("-a", true);
    
    assertEquals("-a", parser.tokens.get(0));
    
    parser.tokens = new ArrayList();
    
    parser.processOptionToken("-b", true);
    
    assertEquals("-b", parser.tokens.get(0));
    
    parser.tokens = new ArrayList();
    
    parser.processOptionToken("-c", true);
    
    assertTrue(parser.eatTheRest);
    
    parser.tokens = new ArrayList();
    
    parser.processOptionToken("-c", false);
    
    assertEquals("-c", parser.tokens.get(0));
  }
  
  @Test
  public void testBurstToken() {
    PosixParser parser = new PosixParser();
    
    parser.tokens = new ArrayList();
    parser.options = new Options();
    parser.options.addOption("a", false);
    parser.options.addOption("b", true);
    
    parser.burstToken("-abc", true);
    
    assertEquals("-a", parser.tokens.get(0));
    assertEquals("-b", parser.tokens.get(1));
    assertEquals("-c", parser.tokens.get(2));
    
    parser.tokens = new ArrayList();
    
    parser.burstToken("-abcd", false);
    
    assertEquals("-a", parser.tokens.get(0));
    assertEquals("-b", parser.tokens.get(1));
    assertEquals("-c", parser.tokens.get(2));
    assertEquals("d", parser.tokens.get(3));
    
    parser.tokens = new ArrayList();
    
    parser.burstToken("-abcd", true);
    
    assertEquals("--", parser.tokens.get(0));
    assertEquals("d", parser.tokens.get(1));
  }
}