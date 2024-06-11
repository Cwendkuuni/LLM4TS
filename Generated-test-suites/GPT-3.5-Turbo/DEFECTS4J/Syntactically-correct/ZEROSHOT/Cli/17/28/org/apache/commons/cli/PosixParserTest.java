import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

import static org.junit.Assert.*;

public class PosixParserTest {
  
  private PosixParser parser;
  private Options options;
  
  @Before
  public void setup() {
    parser = new PosixParser();
    options = new Options();
  }
  
  @Test
  public void testFlatten() {
    String[] args = {"-a", "-b", "value", "--option", "value"};
    String[] expected = {"-a", "-b", "value", "--option", "value"};
    String[] result = parser.flatten(options, args, true);
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testProcess() {
    String value = "value";
    parser.process(value);
    
    assertFalse(parser.eatTheRest);
    assertArrayEquals(new String[] {"--", value}, parser.tokens.toArray());
    
    // Test with currentOption set
    Option option = new Option("a", "option");
    option.setArgName("value");
    options.addOption(option);
    
    parser.currentOption = option;
    parser.process(value);
    
    assertNull(parser.currentOption);
    assertArrayEquals(new String[] {"--", value}, parser.tokens.toArray());
  }
  
  @Test
  public void testProcessSingleHyphen() {
    String hyphen = "-";
    parser.processSingleHyphen(hyphen);
    
    assertArrayEquals(new String[] {hyphen}, parser.tokens.toArray());
  }
  
  @Test
  public void testProcessOptionToken() {
    String token = "a";
    boolean stopAtNonOption = false;
    
    Option option = new Option(token, "");
    options.addOption(option);
    
    parser.processOptionToken(token, stopAtNonOption);
    
    assertSame(option, parser.currentOption);
    assertTrue(parser.tokens.contains(token));
    
    // Test with unknown option and stopAtNonOption true
    token = "b";
    stopAtNonOption = true;
    
    parser.currentOption = null;
    parser.init();
    parser.processOptionToken(token, stopAtNonOption);
    
    assertNull(parser.currentOption);
    assertTrue(parser.eatTheRest);
  }
  
  @Test
  public void testBurstToken() {
    String token = "-abc";
    boolean stopAtNonOption = false;
    
    Option optionA = new Option("a", "");
    Option optionB = new Option("b", "");
    Option optionC = new Option("c", "");
    options.addOption(optionA);
    options.addOption(optionB);
    options.addOption(optionC);
    
    parser.burstToken(token, stopAtNonOption);
    
    assertTrue(parser.tokens.contains("-a"));
    assertTrue(parser.tokens.contains("-b"));
    assertTrue(parser.tokens.contains("-c"));
    
    // Test with unknown option and stopAtNonOption true
    token = "-def";
    stopAtNonOption = true;
    
    parser.init();
    parser.burstToken(token, stopAtNonOption);
    
    assertTrue(parser.tokens.contains("--"));
    assertTrue(parser.tokens.contains("def"));
  }
  
}