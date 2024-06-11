
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
  
  // Test init() method
  @Test
  public void testInit() {
    PosixParser posixParser = new PosixParser();
    posixParser.init();
    assertEquals(0, posixParser.tokens.size());
    assertEquals(false, posixParser.eatTheRest);
    assertNull(posixParser.currentOption);
  }
  
  // Test process() method when currentOption is null and stopAtNonOption is false
  @Test
  public void testProcessWithNullOptionAndStopAtNonOptionFalse() {
    PosixParser posixParser = new PosixParser();
    posixParser.currentOption = null;
    posixParser.eatTheRest = false;
    
    posixParser.process("value");
    assertEquals(1, posixParser.tokens.size());
    assertEquals("value", posixParser.tokens.get(0));
  }
  
  // Test process() method when currentOption hasArg() is true
  @Test
  public void testProcessWithOptionAndHasArgTrue() {
    PosixParser posixParser = new PosixParser();
    Option option = new Option("a", true, "Test Option");
    posixParser.currentOption = option;
    
    posixParser.process("value");
    assertEquals(1, posixParser.tokens.size());
    assertEquals("value", posixParser.tokens.get(0));
    assertNull(posixParser.currentOption);
  }
  
  // Test process() method when currentOption hasArgs() is true
  @Test
  public void testProcessWithOptionAndHasArgsTrue() {
    PosixParser posixParser = new PosixParser();
    Option option = new Option("a", true, "Test Option");
    option.setArgs(true);
    posixParser.currentOption = option;
    
    posixParser.process("value");
    assertEquals(1, posixParser.tokens.size());
    assertEquals("value", posixParser.tokens.get(0));
    assertEquals(option, posixParser.currentOption);
  }
  
  // Test process() method when currentOption is null and stopAtNonOption is true
  @Test
  public void testProcessWithNullOptionAndStopAtNonOptionTrue() {
    PosixParser posixParser = new PosixParser();
    posixParser.currentOption = null;
    posixParser.eatTheRest = true;

    posixParser.process("value");
    assertEquals(3, posixParser.tokens.size());
    assertEquals("--", posixParser.tokens.get(0));
    assertEquals("value", posixParser.tokens.get(1));
    assertEquals("value", posixParser.tokens.get(2));
  }
  
  // Test processSingleHyphen() method
  @Test
  public void testProcessSingleHyphen() {
    PosixParser posixParser = new PosixParser();
    posixParser.processSingleHyphen("-");
    
    assertEquals(1, posixParser.tokens.size());
    assertEquals("-", posixParser.tokens.get(0));
  }
  
  // Test processOptionToken() method when option exists
  @Test
  public void testProcessOptionTokenWithOptionExists() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("a", true, "Test Option");
    options.addOption(option);
    posixParser.options = options;
    
    posixParser.processOptionToken("a", false);
    
    assertEquals(1, posixParser.tokens.size());
    assertEquals("a", posixParser.tokens.get(0));
    assertEquals(option, posixParser.currentOption);
  }
  
  // Test processOptionToken() method when option does not exist and stopAtNonOption is false
  @Test
  public void testProcessOptionTokenWithOptionNotExistsAndStopAtNonOptionFalse() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("a", true, "Test Option");
    options.addOption(option);
    posixParser.options = options;
    
    posixParser.processOptionToken("b", false);
    
    assertEquals(0, posixParser.tokens.size());
    assertNull(posixParser.currentOption);
  }
  
  // Test processOptionToken() method when option does not exist and stopAtNonOption is true
  @Test
  public void testProcessOptionTokenWithOptionNotExistsAndStopAtNonOptionTrue() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("a", true, "Test Option");
    options.addOption(option);
    posixParser.options = options;
    
    posixParser.processOptionToken("b", true);
    
    assertEquals(true, posixParser.eatTheRest);
  }
  
  // Test burstToken() method
  @Test
  public void testBurstToken() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("a", true, "Test Option");
    options.addOption(option);
    posixParser.options = options;
    posixParser.currentOption = option;
    
    posixParser.burstToken("-abcd", false);
    
    assertEquals(3, posixParser.tokens.size());
    assertEquals("-a", posixParser.tokens.get(0));
    assertEquals("-b", posixParser.tokens.get(1));
    assertEquals("-cd", posixParser.tokens.get(2));
  }
  
}
