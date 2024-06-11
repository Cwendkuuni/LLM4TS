import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

  private PosixParser parser;

  @Before
  public void setUp() {
    parser = new PosixParser();
  }

  @Test
  public void testFlatten() {
    String[] args = {"--foo", "bar"};
    String[] result = parser.flatten(null, args, false);
    assertArrayEquals(result, args);
  }

  @Test
  public void testInit() {

  }

  @Test
  public void testProcessNonOptionToken() {

  }

  @Test
  public void testProcessOptionToken() {

  }

  @Test
  public void testBurstToken() {

  }

  @Test
  public void testGobble() {

  }

  @Test
  public void testMerge() {

  }

}