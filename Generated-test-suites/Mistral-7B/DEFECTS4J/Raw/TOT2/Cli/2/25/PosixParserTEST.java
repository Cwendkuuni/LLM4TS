import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {

        String[] args = {"--foo", "--bar=test", "-a", "-b", "--baz=qux", "-c", "--", "-d", "e"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("baz", true, "z");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--foo", "-f", "--bar", "test", "-a", "-b", "--baz", "qux"};
        assertEquals(Arrays.asList(expected), Arrays.asList(result));
    }
}