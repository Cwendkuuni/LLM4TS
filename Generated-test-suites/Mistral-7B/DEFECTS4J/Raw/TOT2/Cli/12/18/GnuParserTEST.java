import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlattenWithSingleArgument() {
        String[] arguments = new String[]{"arg"};
        String[] expected = new String[]{"arg"};
        Options options = new Options();

        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }
}