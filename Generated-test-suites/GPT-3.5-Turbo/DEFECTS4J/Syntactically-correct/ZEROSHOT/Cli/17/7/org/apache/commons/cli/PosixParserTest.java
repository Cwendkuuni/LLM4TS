import org.junit.*;
import static org.junit.Assert.*;

public class PosixParserTest {

  @Test
  public void testInit() {
    PosixParser posixParser = new PosixParser();
    posixParser.init();
    // Assert initial values of members
    assertFalse(posixParser.eatTheRest);
    assertEquals(0, posixParser.tokens.size());
    assertNull(posixParser.currentOption);
  }

  @Test
  public void testFlatten() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    String[] arguments = {"--test", "value", "-i", "input.txt"};
    String[] flattenedArguments = posixParser.flatten(options, arguments, false);
    // Assert flattened arguments
    assertArrayEquals(arguments, flattenedArguments);
  }

  @Test
  public void testAddOption() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("t", "test", false, "test option");
    options.addOption(option);
    // Assert added option
    assertSame(option, options.getOption("t"));
  }

  @Test
  public void testProcessSingleHyphen() {
    PosixParser posixParser = new PosixParser();
    posixParser.processSingleHyphen("-");
    // Assert hyphen is added to the tokens list
    assertEquals(1, posixParser.tokens.size());
    assertEquals("-", posixParser.tokens.get(0));
  }

  @Test
  public void testProcessOptionToken() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("t", "test", false, "test option");
    options.addOption(option);
    posixParser.options = options;

    posixParser.processOptionToken("-t", false);
    // Assert option is added to the tokens list
    assertEquals(1, posixParser.tokens.size());
    assertEquals("-t", posixParser.tokens.get(0));
    // Assert currentOption is set to the added option
    assertSame(option, posixParser.currentOption);
  }

  @Test
  public void testProcessOptionTokenWithStopAtNonOption() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("t", "test", false, "test option");
    options.addOption(option);
    posixParser.options = options;

    posixParser.processOptionToken("-t", true);
    // Assert option is added to the tokens list
    assertEquals(1, posixParser.tokens.size());
    assertEquals("-t", posixParser.tokens.get(0));
    // Assert currentOption is set to the added option
    assertSame(option, posixParser.currentOption);
  }

  @Test
  public void testProcessOptionTokenWithNonExistingOption() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    posixParser.options = options;

    posixParser.processOptionToken("-x", false);
    // Assert eatTheRest is set to true
    assertTrue(posixParser.eatTheRest);
  }

  @Test
  public void testProcessOptionTokenWithNonExistingOptionAndStopAtNonOption() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    posixParser.options = options;

    posixParser.processOptionToken("-x", true);
    // Assert eatTheRest is set to true
    assertTrue(posixParser.eatTheRest);
  }

  @Test
  public void testProcess() {
    PosixParser posixParser = new PosixParser();
    Option option = new Option("t", "test", true, "test option");
    posixParser.currentOption = option;

    posixParser.process("value");
    // Assert value is added to the tokens list
    assertEquals(1, posixParser.tokens.size());
    assertEquals("value", posixParser.tokens.get(0));
    // Assert currentOption is set to null
    assertNull(posixParser.currentOption);
  }

  @Test
  public void testProcessWithStopAtNonOption() {
    PosixParser posixParser = new PosixParser();
    posixParser.currentOption = null;

    posixParser.process("value");
    // Assert tokens list contains "--" and value
    assertEquals(2, posixParser.tokens.size());
    assertEquals("--", posixParser.tokens.get(0));
    assertEquals("value", posixParser.tokens.get(1));
  }

  @Test
  public void testGobble() {
    PosixParser posixParser = new PosixParser();
    Iterator iter = Arrays.asList(new String[]{"value1", "value2"}).iterator();
    posixParser.eatTheRest = true;

    posixParser.gobble(iter);
    // Assert values are added to the tokens list
    assertEquals(2, posixParser.tokens.size());
    assertEquals("value1", posixParser.tokens.get(0));
    assertEquals("value2", posixParser.tokens.get(1));
  }

  @Test
  public void testBurstToken() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("t", "test", false, "test option");
    options.addOption(option);
    posixParser.options = options;

    posixParser.burstToken("-t", false);
    // Assert option is added to the tokens list
    assertEquals(1, posixParser.tokens.size());
    assertEquals("-t", posixParser.tokens.get(0));
    // Assert currentOption is set to the added option
    assertSame(option, posixParser.currentOption);
  }

  @Test
  public void testBurstTokenWithStopAtNonOption() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    Option option = new Option("t", "test", false, "test option");
    options.addOption(option);
    posixParser.options = options;

    posixParser.burstToken("-t", true);
    // Assert tokens list contains "--" and "t"
    assertEquals(2, posixParser.tokens.size());
    assertEquals("--", posixParser.tokens.get(0));
    assertEquals("t", posixParser.tokens.get(1));
  }

  @Test
  public void testBurstTokenWithNonExistingOption() {
    PosixParser posixParser = new PosixParser();
    Options options = new Options();
    posixParser.options = options;

    posixParser.burstToken("-x", false);
    // Assert tokens list contains "-x"
    assertEquals(1, posixParser.tokens.size());
    assertEquals("-x", posixParser.tokens.get(0));
  }
}