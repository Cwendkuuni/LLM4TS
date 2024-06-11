import org.junit.Test;

public class PosixParserTest {
  
  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    // TODO: Add assertions
  }
  
  @Test
  public void testFlatten() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    
    // TODO: Add options to the options object
    
    String[] arguments = new String[] {"-a", "arg1", "--option", "--", "arg2"};
    boolean stopAtNonOption = false;
    
    parser.flatten(options, arguments, stopAtNonOption);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testGobble() {
    PosixParser parser = new PosixParser();
    
    // TODO: Create iterator with remaining tokens
    
    parser.gobble(iterator);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testProcess() {
    PosixParser parser = new PosixParser();
    String value = "value";
    
    parser.process(value);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testProcessSingleHyphen() {
    PosixParser parser = new PosixParser();
    String hyphen = "-";
    
    parser.processSingleHyphen(hyphen);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testProcessOptionToken() {
    PosixParser parser = new PosixParser();
    String token = "-o";
    boolean stopAtNonOption = false;
    
    parser.processOptionToken(token, stopAtNonOption);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testBurstToken() {
    PosixParser parser = new PosixParser();
    String token = "-abc";
    boolean stopAtNonOption = false;
    
    parser.burstToken(token, stopAtNonOption);
    
    // TODO: Add assertions
  }
}