import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlattenNormalUsage() {
        String[] args = {"--foo", "-o", "bar", "arg1", "--baz=qux", "--"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "opt");
        options.addOption("baz", true, "b");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-o", "opt", "--baz=qux"}, result);
    }

    @Test
    public void testFlattenSpecialToken() {
        String[] args = {"--", "--foo=bar"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo=bar"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--xoo", "--", "arg1", "--baz=qux"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "opt");
        options.addOption("baz", true, "b");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}