import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "--bar=abc", "--baz", "-c", "value", "--"};
        Options options = new Options();
        options.addOption("foo", true, "Optional boolean");
        options.addOption("bar", true, "Optional string");
        options.addOption("baz", true, "Optional boolean with argument");
        options.addOption("c", true, "Optional boolean with long argument name");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(Arrays.asList("--foo", "--bar", "abc", "--baz", "-c", "value", "--"), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] args = {"--invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        fail("Expected ParseException but it didn't occur");
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"--foo", "--bar=abc", "--baz", "-c", "value", "--", "nonOption", "moreArgs"};
        Options options = new Options();
        options.addOption("foo", true, "Optional boolean");
        options.addOption("bar", true, "Optional string");
        options.addOption("baz", true, "Optional boolean with argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(Arrays.asList("--foo", "--bar=abc", "--baz", "nonOption", "moreArgs"), result);
    }
}