import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

  private PosixParser parser;

  @Before
  public void setUp() {
    parser = new PosixParser();
  }

  @Test
  public void testInit() {
    parser.init();
    assertFalse(parser.eatTheRest);
    assertEquals(0, parser.tokens.size());
    assertNull(parser.currentOption);
  }

  @Test
  public void testFlatten() {
    Options options = new Options();
    String[] arguments = new String[] {"-a", "valueA", "-bc", "valueB", "valueC"};
    boolean stopAtNonOption = false;

    String[] result = parser.flatten(options, arguments, stopAtNonOption);
    assertEquals(7, result.length);

    assertEquals("-a", result[0]);
    assertEquals("valueA", result[1]);
    assertEquals("-b", result[2]);
    assertEquals("-c", result[3]);
    assertEquals("valueB", result[4]);
    assertEquals("valueC", result[5]);
    assertEquals("--", result[6]);
  }

  @Test
  public void testGobble() {
    Iterator<String> iter = Arrays.asList("-d", "valueD", "-e", "valueE").iterator();
    parser.eatTheRest = true;

    parser.gobble(iter);
    assertEquals(4, parser.tokens.size());
    assertEquals("-d", parser.tokens.get(0));
    assertEquals("valueD", parser.tokens.get(1));
    assertEquals("-e", parser.tokens.get(2));
    assertEquals("valueE", parser.tokens.get(3));
  }

  @Test
  public void testProcess_ValidOptionWithArg() {
    parser.currentOption = new Option("f", true, "option f");
    parser.process("valueF");

    assertEquals(1, parser.tokens.size());
    assertEquals("valueF", parser.tokens.get(0));
    assertNull(parser.currentOption);
  }

  @Test
  public void testProcess_ValidOptionWithArgs() {
    parser.currentOption = new Option("g", true, "option g");
    parser.process("valueG");

    assertEquals(1, parser.tokens.size());
    assertEquals("valueG", parser.tokens.get(0));
    assertNull(parser.currentOption);
  }

  @Test
  public void testProcess_NoCurrentOption_StopAtNonOption() {
    String value = "nonOptionValue";

    parser.process(value);

    assertTrue(parser.eatTheRest);
    assertEquals("--", parser.tokens.get(0));
    assertEquals(value, parser.tokens.get(1));
  }

  @Test
  public void testProcess_NoCurrentOption_NoStopAtNonOption() {
    String value = "nonOptionValue";

    parser.process(value);

    assertFalse(parser.eatTheRest);
    assertEquals(value, parser.tokens.get(0));
  }

  @Test
  public void testProcessSingleHyphen() {
    String hyphen = "-";

    parser.processSingleHyphen(hyphen);

    assertEquals(hyphen, parser.tokens.get(0));
  }

  @Test
  public void testProcessOptionToken_ValidOptionExists() {
    String token = "-h";
    boolean stopAtNonOption = true;

    Option option = new Option("h", "help", false, "display help");
    Options options = new Options();
    options.addOption(option);

    parser.options = options;

    parser.processOptionToken(token, stopAtNonOption);

    assertEquals(1, parser.tokens.size());
    assertEquals(token, parser.tokens.get(0));
    assertEquals(option, parser.currentOption);
  }

  @Test
  public void testProcessOptionToken_ValidOptionDoesNotExist_StopAtNonOption() {
    String token = "-i";
    boolean stopAtNonOption = true;

    parser.processOptionToken(token, stopAtNonOption);

    assertTrue(parser.eatTheRest);
  }

  @Test
  public void testProcessOptionToken_ValidOptionDoesNotExist_NoStopAtNonOption() {
    String token = "-j";
    boolean stopAtNonOption = false;

    parser.processOptionToken(token, stopAtNonOption);

    assertEquals(1, parser.tokens.size());
    assertEquals(token, parser.tokens.get(0));
  }

  @Test
  public void testBurstToken_ValidOptionExists_NoStopAtNonOption() {
    String token = "-k";
    boolean stopAtNonOption = false;

    Option option = new Option("k", false, "option k");
    Options options = new Options();
    options.addOption(option);

    parser.options = options;

    parser.burstToken(token, stopAtNonOption);

    assertEquals(1, parser.tokens.size());
    assertEquals("-k", parser.tokens.get(0));
    assertEquals(option, parser.currentOption);
  }

  @Test
  public void testBurstToken_ValidOptionExists_StopAtNonOption() {
    String token = "-l";
    boolean stopAtNonOption = true;

    Option option = new Option("l", false, "option l");
    Options options = new Options();
    options.addOption(option);

    parser.options = options;

    parser.burstToken(token, stopAtNonOption);

    assertEquals(1, parser.tokens.size());
    assertEquals("-l", parser.tokens.get(0));
    assertEquals(option, parser.currentOption);
  }

  @Test
  public void testBurstToken_InvalidOption_StopAtNonOption() {
    String token = "-m";
    boolean stopAtNonOption = true;

    parser.burstToken(token, stopAtNonOption);

    assertEquals(1, parser.tokens.size());
    assertEquals("-m", parser.tokens.get(0));
  }
}