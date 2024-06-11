import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
        options.addOption("d", 3, "Option D with three arguments");

        String[] args = {"--a", "-b", "valueB", "-c", "valueC1", "valueC2", "--d", "valueD1", "valueD2", "valueD3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--a",
                "-b",
                "valueB",
                "-c",
                "--",
                "valueC1",
                "valueC2",
                "--d",
                "valueD1",
                "valueD2",
                "valueD3"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        // Test with stopAtNonOption = true
        result = parser.flatten(options, args, true);

        expected = Arrays.asList(
                "--a",
                "-b",
                "valueB",
                "-c",
                "--d",
                "valueD1",
                "valueD2",
                "valueD3"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithInvalidTokens() {
        Options options = new Options();

        String[] args = {"--", "--foo", "--bar=value", "-foo", "--", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--",
                "--foo",
                "--",
                "foo",
                "--",
                "value1",
                "value2"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        // Test with stopAtNonOption = true
        result = parser.flatten(options, args, true);

        expected = Arrays.asList(
                "--",
                "--foo",
                "foo",
                "--value1",
                "--value2"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalid", "--a"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOptionWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--invalid", "--a"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, true);
    }
}