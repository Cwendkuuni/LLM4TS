import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        // given
        String[] args = {"--foo=bar", "--qux=baz", "-a", "-b", "-c", "arg1", "-d=arg2"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("qux", true, "Qux option");
        options.addOption("a", true, "A option");
        options.addOption("b", false, "B option");
        options.addOption("c", false, "C option");
        options.addOption("d", true, "D option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // when
        String[] expected = {"--foo", "bar", "--qux", "baz", "-a", "-b", "-c", "arg1", "--", "d", "arg2"};

        // then
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testStopAtNonOption() throws ParseException {
        // given
        String[] args = {"--foo", "bar", "-", "invalidOption"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        PosixParser parser = new PosixParser();

        // when
        parser.flatten(options, args, true);

        // then
        // ParseException is expected to be thrown
    }

    @Test
    public void testBurstToken() throws ParseException {
        // given
        String[] args = {"--foo-bar", "--qux-baz=xyz"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("qux", true, "Qux option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // when
        String[] expected = {"--foo", "bar", "--qux", "baz", "xyz"};

        // then
        Assert.assertArrayEquals(expected, result);
    }
}