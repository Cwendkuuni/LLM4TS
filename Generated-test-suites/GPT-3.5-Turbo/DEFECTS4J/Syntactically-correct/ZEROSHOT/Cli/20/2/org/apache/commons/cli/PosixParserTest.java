import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

  @Test
  public void testInit() {
    PosixParser pp = new PosixParser();
    pp.init();
    assertEquals(false, pp.eatTheRest);
    assertEquals(0, pp.tokens.size());
    assertEquals(null, pp.currentOption);
  }

  @Test
  public void testFlatten() {
    PosixParser pp = new PosixParser();
    Options opts = new Options();
    String[] args = new String[]{"-a", "--foo", "bar"};
    String[] result = pp.flatten(opts, args, true);
    assertEquals(3, result.length);
    assertEquals("-a", result[0]);
    assertEquals("--foo", result[1]);
    assertEquals("bar", result[2]);
  }

  @Test
  public void testProcess() {
    PosixParser pp = new PosixParser();
    pp.currentOption = new Option("a", false);
    pp.currentOption.setArg(true);
    pp.process("value");
    assertEquals(1, pp.tokens.size());
    assertEquals("value", pp.tokens.get(0));
    assertEquals(null, pp.currentOption);

    pp.currentOption = null;
    pp.process("value");
    assertEquals(2, pp.tokens.size());
    assertEquals("--", pp.tokens.get(0));
    assertEquals("value", pp.tokens.get(1));
    assertEquals(true, pp.eatTheRest);
  }

  @Test
  public void testProcessOptionToken() {
    PosixParser pp = new PosixParser();
    pp.options = new Options();
    pp.options.addOption(new Option("a", false));

    pp.processOptionToken("-a", true);
    assertEquals(1, pp.tokens.size());
    assertEquals("-a", pp.tokens.get(0));
    assertEquals(null, pp.currentOption);

    pp.processOptionToken("-b", true);
    assertEquals(1, pp.tokens.size());
    assertEquals("-b", pp.tokens.get(0));
    assertEquals(true, pp.eatTheRest);
  }

  @Test
  public void testBurstToken() {
    PosixParser pp = new PosixParser();
    pp.options = new Options();
    pp.options.addOption(new Option("a", false));
    pp.options.addOption(new Option("b", false));

    pp.burstToken("-ab", true);
    assertEquals(1, pp.tokens.size());
    assertEquals("-ab", pp.tokens.get(0));
    assertEquals(null, pp.currentOption);

    pp.burstToken("-ab", false);
    assertEquals(1, pp.tokens.size());
    assertEquals("-a", pp.tokens.get(0));
  }
}