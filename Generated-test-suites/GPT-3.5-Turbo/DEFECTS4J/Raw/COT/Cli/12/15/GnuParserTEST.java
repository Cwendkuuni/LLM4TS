import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

  @Test
  public void testFlatten() {
    GnuParser parser = new GnuParser();
    Options options = new Options();
    String[] arguments = {"-Dproperty=value", "input.txt"};

    String[] result = parser.flatten(options, arguments, true);

    // Test if the result contains the correct number of tokens
    assertEquals(3, result.length);
    
    // Test if the options are correctly parsed
    assertEquals("-D", result[0]);
    assertEquals("property=value", result[1]);

    // Test if the non-option argument is correctly parsed
    assertEquals("input.txt", result[2]);
  }

}