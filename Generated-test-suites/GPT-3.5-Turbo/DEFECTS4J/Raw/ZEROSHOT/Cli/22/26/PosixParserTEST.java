import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

  private PosixParser parser;
  
  @Test
  public void testInit() {
    parser = new PosixParser();
    parser.init();
    // add test case here
  }
  
  @Test
  public void testFlatten() {
    parser = new PosixParser();
    Options options = new Options();
    String[] arguments = new String[] {"-o", "option"};

    String[] flattened = parser.flatten(options, arguments, true);
    
    // add test cases here
  }
  
  @Test
  public void testProcessNonOptionToken() {
    parser = new PosixParser();
    parser.init();
    String value = "value";
    
    parser.processNonOptionToken(value);
    
    // add test cases here
  }
  
  @Test
  public void testProcessOptionToken() {
    parser = new PosixParser();
    parser.init();
    String token = "-o";
    boolean stopAtNonOption = true;
    
    parser.processOptionToken(token, stopAtNonOption);
    
    // add test cases here
  }
  
  @Test
  public void testBurstToken() {
    parser = new PosixParser();
    parser.init();
    String token = "-abc";
    boolean stopAtNonOption = true;
    
    parser.burstToken(token, stopAtNonOption);
    
    // add test cases here
  }
}