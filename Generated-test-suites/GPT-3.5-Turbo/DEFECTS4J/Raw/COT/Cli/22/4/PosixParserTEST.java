import org.junit.*;
import static org.junit.Assert.*;

public class PosixParserTest {

  @Test
  public void testFlatten() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    String[] arguments = {"--foo", "value", "--bar"};

    String[] result = parser.flatten(options, arguments, true);

    assertEquals(result.length, 4);
    assertEquals(result[0], "--");
    assertEquals(result[1], "--foo");
    assertEquals(result[2], "value");
    assertEquals(result[3], "--bar");
  }

  @Test
  public void testProcessNonOptionToken() {
    PosixParser parser = new PosixParser();
    parser.processNonOptionToken("value");

    List tokens = parser.getTokens();
    assertEquals(tokens.size(), 2);
    assertEquals(tokens.get(0), "--");
    assertEquals(tokens.get(1), "value");
  }

  @Test
  public void testProcessOptionToken() {
    PosixParser parser = new PosixParser();
    parser.processOptionToken("--foo", true);

    List tokens = parser.getTokens();
    assertEquals(tokens.size(), 1);
    assertEquals(tokens.get(0), "--foo");
  }

  @Test
  public void testBurstToken() {
    PosixParser parser = new PosixParser();
    parser.initOptions();
    parser.burstToken("-abc", true);

    List tokens = parser.getTokens();
    assertEquals(tokens.size(), 3);
    assertEquals(tokens.get(0), "-a");
    assertEquals(tokens.get(1), "-b");
    assertEquals(tokens.get(2), "-c");
  }

  @Test
  public void testGobble() {
    PosixParser parser = new PosixParser();
    Iterator iterator = Arrays.asList("value1", "value2").iterator();
    parser.setEatTheRest(true);
    parser.gobble(iterator);

    List tokens = parser.getTokens();
    assertEquals(tokens.size(), 2);
    assertEquals(tokens.get(0), "value1");
    assertEquals(tokens.get(1), "value2");
  }

}