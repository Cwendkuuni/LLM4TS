import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
  
  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    assertEquals(false, parser.eatTheRest);
    assertEquals(0, parser.tokens.size());
    assertNull(parser.currentOption);
  }
  
  @Test
  public void testFlatten() {
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    options.addOption("c", true, "option c");
    
    PosixParser parser = new PosixParser();
    
    String[] arguments = {"-a", "arg1", "-b", "arg2", "-c", "arg3"};
    String[] flattenedArgs = parser.flatten(options, arguments, false);
    
    assertEquals("-a", flattenedArgs[0]);
    assertEquals("arg1", flattenedArgs[1]);
    assertEquals("-b", flattenedArgs[2]);
    assertEquals("arg2", flattenedArgs[3]);
    assertEquals("-c", flattenedArgs[4]);
    assertEquals("arg3", flattenedArgs[5]);
  }
  
  @Test
  public void testGobble() {
    PosixParser parser = new PosixParser();
    
    Iterator iter = Arrays.asList("arg1", "arg2", "arg3").iterator();
    
    parser.eatTheRest = true;
    parser.gobble(iter);
    
    assertEquals("arg1", parser.tokens.get(0));
    assertEquals("arg2", parser.tokens.get(1));
    assertEquals("arg3", parser.tokens.get(2));
  }
  
  @Test
  public void testProcess() {
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    options.addOption("c", true, "option c");
    
    PosixParser parser = new PosixParser();
    parser.options = options;
    
    parser.currentOption = options.getOption("a");
    parser.currentOption.setArg(false);
    
    parser.process("arg1");
    assertEquals("arg1", parser.tokens.get(0));
    
    parser.currentOption = options.getOption("b");
    parser.currentOption.setArg(true);
    
    parser.process("arg2");
    assertEquals("arg2", parser.tokens.get(1));
    
    parser.currentOption = options.getOption("c");
    parser.currentOption.setArg(true);
    parser.currentOption.setArgs(true);
    
    parser.process("arg3");
    assertEquals("arg3", parser.tokens.get(2));
    
    parser.currentOption = null;
    parser.eatTheRest = false;
    
    parser.process("arg4");
    parser.process("arg5");
    
    assertEquals("--", parser.tokens.get(3));
    assertEquals("arg4", parser.tokens.get(4));
    assertEquals("arg5", parser.tokens.get(5));
  }
  
  @Test
  public void testProcessSingleHyphen() {
    PosixParser parser = new PosixParser();
    parser.processSingleHyphen("-");
    
    assertEquals("-", parser.tokens.get(0));
  }
  
  @Test
  public void testProcessOptionToken() {
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    options.addOption("c", true, "option c");
    
    PosixParser parser = new PosixParser();
    parser.options = options;
    
    String token = "-a";
    
    parser.processOptionToken(token, false);
    assertEquals("-a", parser.tokens.get(0));
    
    token = "-d";
    
    parser.processOptionToken(token, false);
    assertEquals("-d", parser.tokens.get(1));
    
    parser.processOptionToken(token, true);
    assertEquals(true, parser.eatTheRest);
  }
  
  @Test
  public void testBurstToken() {
    Options options = new Options();
    options.addOption("a", false, "option a");
    options.addOption("b", true, "option b");
    options.addOption("c", true, "option c");
    
    PosixParser parser = new PosixParser();
    parser.options = options;
    
    String token = "-ab";
    parser.burstToken(token, false);
    
    assertEquals("-a", parser.tokens.get(0));
    assertEquals("-b", parser.tokens.get(1));
    
    token = "-d";
    parser.burstToken(token, false);
    assertEquals("-d", parser.tokens.get(2));
    
    parser.burstToken(token, true);
    assertEquals(true, parser.eatTheRest);
  }
}