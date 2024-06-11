
import org.junit.*;
import static org.junit.Assert.*;

public class PosixParserTest {

  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    // Verify that all the member variables are reset to their initial state
    assertFalse(parser.eatTheRest);
    assertEquals(0, parser.tokens.size());
    assertNull(parser.currentOption);
  }

  @Test
  public void testFlatten() {
    Options options = new Options();
    options.addOption("a", false, "Option a");
    options.addOption("b", true, "Option b");

    PosixParser parser = new PosixParser();
    String[] arguments = {"-a", "--b=value", "extra"};
    String[] flattened = parser.flatten(options, arguments, false);

    // Verify that the arguments are correctly flattened
    assertEquals("-a", flattened[0]);
    assertEquals("--b=value", flattened[1]);
    assertEquals("extra", flattened[2]);
  }

  @Test
  public void testGobble() {
    PosixParser parser = new PosixParser();
    parser.tokens.add("token1");
    parser.tokens.add("token2");
    parser.eatTheRest = true;

    Iterator iter = Arrays.asList("token3", "token4").iterator();
    parser.gobble(iter);

    // Verify that the remaining tokens are added to the list
    assertEquals("token1", parser.tokens.get(0));
    assertEquals("token2", parser.tokens.get(1));
    assertEquals("token3", parser.tokens.get(2));
    assertEquals("token4", parser.tokens.get(3));
  }

  @Test
  public void testProcess() {
    Options options = new Options();
    options.addOption("a", false, "Option a");
    options.addOption("b", true, "Option b");

    PosixParser parser = new PosixParser();
    parser.currentOption = options.getOption("b");
    parser.tokens.add("token1");

    parser.process("value");

    // Verify that the value is added to the tokens list
    assertEquals("token1", parser.tokens.get(0));
    assertEquals("value", parser.tokens.get(1));
    assertNull(parser.currentOption);
  }

  @Test
  public void testProcessOptionToken() {
    Options options = new Options();
    options.addOption("a", false, "Option a");
    options.addOption("b", true, "Option b");

    PosixParser parser = new PosixParser();
    parser.tokens.add("token1");

    parser.processOptionToken("-a", false);

    // Verify that the option is added to the tokens list
    assertEquals("token1", parser.tokens.get(0));
    assertEquals("-a", parser.tokens.get(1));
    assertEquals(options.getOption("a"), parser.currentOption);
  }

  @Test
  public void testBurstToken() {
    Options options = new Options();
    options.addOption("a", false, "Option a");
    options.addOption("b", true, "Option b");

    PosixParser parser = new PosixParser();
    parser.tokens.add("token1");

    parser.burstToken("--bc", false);

    // Verify that the option is added to the tokens list
    assertEquals("token1", parser.tokens.get(0));
    assertEquals("-b", parser.tokens.get(1));
    assertEquals("-c", parser.tokens.get(2));
    assertEquals(options.getOption("b"), parser.currentOption);
  }

}
