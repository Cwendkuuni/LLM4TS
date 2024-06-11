import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PosixParserTest {
  
  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    
    assertEquals(false, parser.eatTheRest);
    assertEquals(0, parser.tokens.size());
    assertEquals(null, parser.currentOption);
  }

  @Test
  public void testFlatten() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    
    String[] arguments = {"-a", "-b", "--c=3", "--d=4"};
    boolean stopAtNonOption = true;
    
    String[] result = parser.flatten(options, arguments, stopAtNonOption);
    
    assertEquals("-a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("--c", result[2]);
    assertEquals("3", result[3]);
    assertEquals("--d", result[4]);
    assertEquals("4", result[5]);
  }
  
  @Test
  public void testGobble() {
    PosixParser parser = new PosixParser();
    parser.eatTheRest = true;
    List tokens = new ArrayList();
    tokens.add("1");
    tokens.add("2");
    tokens.add("3");
    
    Iterator iter = tokens.iterator();
    parser.gobble(iter);
    
    assertEquals(3, parser.tokens.size());
    assertEquals("1", parser.tokens.get(0));
    assertEquals("2", parser.tokens.get(1));
    assertEquals("3", parser.tokens.get(2));
  }
  
  @Test
  public void testProcess() {
    PosixParser parser = new PosixParser();
    String value = "value";
    
    parser.process(value);
    
    assertEquals(true, parser.eatTheRest);
    assertEquals("--", parser.tokens.get(0));
    assertEquals("value", parser.tokens.get(1));
  }
  
  @Test
  public void testProcessOptionToken() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    Option option = new Option("a", "option a");
    options.addOption(option);
    
    String token = "-a";
    boolean stopAtNonOption = false;
    
    parser.processOptionToken(token, stopAtNonOption);
    
    assertEquals(option, parser.currentOption);
    assertEquals("-a", parser.tokens.get(0));
  }
  
  @Test
  public void testBurstToken() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    Option option = new Option("a", "option a");
    options.addOption(option);
    
    String token = "-abc";
    boolean stopAtNonOption = false;
    
    parser.burstToken(token, stopAtNonOption);
    
    assertEquals("-a", parser.tokens.get(0));
    assertEquals("-b", parser.tokens.get(1));
    assertEquals("-c", parser.tokens.get(2));
  }
}