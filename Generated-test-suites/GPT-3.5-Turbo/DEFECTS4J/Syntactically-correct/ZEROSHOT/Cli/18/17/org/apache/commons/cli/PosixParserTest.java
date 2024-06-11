import org.junit.Before;
import org.junit.Test;
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
    String[] arguments = new String[]{"-a", "arg1", "--option", "--", "arg2"};
    String[] expected = new String[]{"-a", "arg1", "--option", "--", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenStopAtNonOption() {
    String[] arguments = new String[]{"-a", "arg1", "--option", "--", "arg2"};
    String[] expected = new String[]{"-a", "arg1"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenSingleHyphen() {
    String[] arguments = new String[]{"-"};
    String[] expected = new String[]{"-"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenOptionToken() {
    String[] arguments = new String[]{"-a"};
    String[] expected = new String[]{"-a"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenOptionTokenStopAtNonOption() {
    String[] arguments = new String[]{"-a"};
    String[] expected = new String[]{"-a"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenBurstToken() {
    String[] arguments = new String[]{"-abc"};
    String[] expected = new String[]{"-a", "-b", "-c"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenBurstTokenStopAtNonOption() {
    String[] arguments = new String[]{"-abc"};
    String[] expected = new String[]{"-abc"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenNonOption() {
    String[] arguments = new String[]{"arg1"};
    String[] expected = new String[]{"arg1"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenNonOptionStopAtNonOption() {
    String[] arguments = new String[]{"arg1"};
    String[] expected = new String[]{"arg1"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlattenEatTheRest() {
    String[] arguments = new String[]{"-a", "arg1", "--option", "--", "arg2"};
    String[] expected = new String[]{"--", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }
}