import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

  private PosixParser posixParser;
  private Options options;

  @Before
  public void setUp() {
    posixParser = new PosixParser();
    options = new Options();
  }

  @Test
  public void testFlatten() {
    String[] arguments = new String[] {"-a", "value1", "-bc", "--option=value2", "param1", "param2"};
    options.addOption("a", false, "Option a");
    options.addOption("b", false, "Option b");
    options.addOption("c", false, "Option c");
    String[] result = posixParser.flatten(options, arguments, true);
    String[] expected = new String[] {"-a", "-b", "-c", "--", "value1", "--option", "value2", "param1", "param2"};
    assertArrayEquals(expected, result);
  }

  @Test
  public void testInit() {
    posixParser.init();
    assertTrue(posixParser.tokens.isEmpty());
    assertFalse(posixParser.eatTheRest);
    assertNull(posixParser.currentOption);
  }

  @Test
  public void testProcess() {
    posixParser.currentOption = options.getOption("a");
    String token = "value";
    posixParser.process(token);
    assertTrue(posixParser.tokens.contains(token));
    assertNull(posixParser.currentOption);
  }

  @Test
  public void testProcessSingleHyphen() {
    String hyphen = "-";
    posixParser.processSingleHyphen(hyphen);
    assertTrue(posixParser.tokens.contains(hyphen));
  }

  @Test
  public void testProcessOptionToken() {
    options.addOption("a", false, "Option a");
    String token = "-a";
    boolean stopAtNonOption = false;
    posixParser.processOptionToken(token, stopAtNonOption);
    assertTrue(posixParser.tokens.contains(token));
    assertSame(options.getOption("a"), posixParser.currentOption);
  }

  @Test
  public void testBurstToken() {
    options.addOption("a", false, "Option a");
    options.addOption("b", true, "Option b");
    options.addOption("c", false, "Option c");
    String token = "-abc";
    boolean stopAtNonOption = true;
    posixParser.burstToken(token, stopAtNonOption);
    assertTrue(posixParser.tokens.contains("-a"));
    assertTrue(posixParser.tokens.contains("-b"));
    assertTrue(posixParser.tokens.contains("-c"));
    assertSame(options.getOption("c"), posixParser.currentOption);
  }

}