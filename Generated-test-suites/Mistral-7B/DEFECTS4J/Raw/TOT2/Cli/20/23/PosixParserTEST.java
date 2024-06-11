import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] arguments = {"--foo", "-o", "out.txt", "--bar", "-c", "input.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList(
                "--foo",
                "--",
                "-o",
                "--bar",
                "--",
                "-c",
                "--",
                "input.txt"
        );
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--foo", "-o", "out.txt", "--bar", "-c", "input.txt", "file.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList(
                "--foo",
                "--",
                "-o",
                "--bar",
                "--",
                "-c",
                "--",
                "input.txt",
                "--",
                "file.txt"
        );
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--invalidOption", "--foo", "-o", "out.txt"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNoOptions() {
        Options options = new Options();
        String[] arguments = {"file.txt"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        String[] arguments = {"--foo"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
        Assert.assertEquals("Foo option", parser.currentOption.getValue());
    }

    @Test
    public void testProcessNoOption() {
        Options options = new Options();
        String[] arguments = {"--option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenSingleOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-o", false);
        List<String> expected = Arrays.asList("-o");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testBurstTokenSimpleOptionWithArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-o=output.txt", false);
        List<String> expected = Arrays.asList("-o", "--", "output.txt");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testBurstTokenComplexOptionWithArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--option=value", false);
        List<String> expected = Arrays.asList("--option", "--", "value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testBurstTokenComplexOptionWithoutArg() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--option", false);
        List<String> expected = Arrays.asList("--option");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testBurstTokenInvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--invalidOption", false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("--", false, true);
        List<String> expected = Arrays.asList("--");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}