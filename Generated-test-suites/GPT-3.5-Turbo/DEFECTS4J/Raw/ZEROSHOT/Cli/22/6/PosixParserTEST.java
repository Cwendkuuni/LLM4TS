import org.junit.*;
import static org.junit.Assert.*;

public class PosixParserTest {

  private PosixParser parser;
  private Options options;
  
  @Before
  public void setUp() {
    parser = new PosixParser();
    options = new Options();
    options.addOption("a", false, "Option a");
    options.addOption("b", true, "Option b");
  }
  
  @Test
  public void testFlatten_stopAtNonOptionTrue() {
    String[] args = {"-a", "test", "-b", "value", "nonOption"};
    String[] result = parser.flatten(options, args, true);
    assertEquals(3, result.length);
    assertEquals("-a", result[0]);
    assertEquals("test", result[1]);
    assertEquals("nonOption", result[2]);
  }
  
  @Test
  public void testFlatten_stopAtNonOptionFalse() {
    String[] args = {"-a", "test", "-b", "value", "nonOption"};
    String[] result = parser.flatten(options, args, false);
    assertEquals(5, result.length);
    assertEquals("-a", result[0]);
    assertEquals("test", result[1]);
    assertEquals("-b", result[2]);
    assertEquals("value", result[3]);
    assertEquals("nonOption", result[4]);
  }
  
  @Test
  public void testFlatten_longOption() {
    String[] args = {"--a", "--b=value"};
    String[] result = parser.flatten(options, args, false);
    assertEquals(2, result.length);
    assertEquals("--a", result[0]);
    assertEquals("--b=value", result[1]);
  }
  
  @Test
  public void testFlatten_singleHyphen() {
    String[] args = {"-"};
    String[] result = parser.flatten(options, args, false);
    assertEquals(1, result.length);
    assertEquals("-", result[0]);
  }
  
  @Test
  public void testFlatten_shortOption() {
    String[] args = {"-a", "test", "-b=value"};
    String[] result = parser.flatten(options, args, false);
    assertEquals(3, result.length);
    assertEquals("-a", result[0]);
    assertEquals("test", result[1]);
    assertEquals("-b=value", result[2]);
  }
  
  @Test
  public void testFlatten_burstToken() {
    String[] args = {"-ab", "value"};
    String[] result = parser.flatten(options, args, false);
    assertEquals(2, result.length);
    assertEquals("-a", result[0]);
    assertEquals("-b=value", result[1]);
  }
  
  @Test
  public void testFlatten_noOption() {
    String[] args = {"test"};
    String[] result = parser.flatten(options, args, false);
    assertEquals(1, result.length);
    assertEquals("test", result[0]);
  }
  
  @After
  public void tearDown() {
    parser = null;
    options = null;
  }
}