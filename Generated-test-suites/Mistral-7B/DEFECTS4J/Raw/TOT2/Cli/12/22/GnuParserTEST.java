import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert;

public class GnuParserTest {

  @Test
  public void testFlatten() throws ParseException {
    String[] args = {"-Dproperty=value", "--", "-f", "-o", "output.txt"};
    Options options = new Options();

    GnuParser parser = new GnuParser();
    String[] result = parser.flatten(options, args, false);

    String[] expected = {"-D", "property", "value", "--", "-f", "-o", "output.txt"};

    Assert.assertArrayEquals(expected, result);
  }
}