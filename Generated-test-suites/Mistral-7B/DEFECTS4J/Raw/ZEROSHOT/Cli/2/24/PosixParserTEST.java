import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        String[] args = new String[]{"--foo", "-a", "-b", "--bar=baz", "-c", "--quux=quux", "quux2", "quux3"};
        Options options = new Options();
        options.addOption("foo", false, null, "Print foobar");
        options.addOption("bar", true, "baz", "Print bar with value baz");
        options.addOption("quux", 1, "quux", "Print quux with value quux");

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--foo",
                "-a",
                "-b",
                "--bar",
                "baz",
                "-c",
                "--quux",
                "quux",
                "quux2",
                "quux3"
        );

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);

        // test with stopAtNonOption = true
        String[] argsWithNonOption = new String[]{"--foo", "-a", "-b", "quux", "quux2", "quux3"};
        String[] resultWithStopAtNonOption = parser.flatten(options, argsWithNonOption, true);

        List<String> expectedWithStopAtNonOption = Arrays.asList(
                "--foo",
                "-a",
                "-b",
                "quux",
                "quux2",
                "quux3"
        );

        Assert.assertArrayEquals(expectedWithStopAtNonOption.toArray(new String[0]), resultWithStopAtNonOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = new String[]{"--foo", "-a", "-b", "--bar=baz", "-x", "-c", "--quux=quux", "quux2", "quux3"};
        Options options = new Options();
        options.addOption("foo", false, null, "Print foobar");
        options.addOption("bar", true, "baz", "Print bar with value baz");
        options.addOption("quux", 1, "quux", "Print quux with value quux");

        Parser parser = new PosixParser();
        parser.flatten(options, args, false);

        // the test should fail with an IllegalArgumentException
    }
}