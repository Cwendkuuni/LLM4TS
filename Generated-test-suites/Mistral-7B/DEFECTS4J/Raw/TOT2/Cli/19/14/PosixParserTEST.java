import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimpleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] args = {"-a", "--b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "--b"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("long-option", true, "Long Option");

        String[] args = {"--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--long-option",
                "value"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options();
        options.addOption("option", true, "Option with arg");

        String[] args = {"-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-option",
                "value"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenLongOptionWithArg() {
        Options options = new Options();
        options.addOption("long-option", true, "Long Option with arg");

        String[] args = {"--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--long-option",
                "value"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] args = {"-a", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "-b"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenOptionWithArgAndOtherOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] args = {"-a", "value", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "value",
                "-b"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();

        String[] args = {"-invalid-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenOptionWithInvalidArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-a", "invalid_arg"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidLongOption() {
        Options options = new Options();

        String[] args = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] args = {"-a", "-b", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "-a",
                "-b",
                "non-option"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--",
                "value"
        );
        Assert.assertArrayEquals(expected.toArray(), result);
    }
}