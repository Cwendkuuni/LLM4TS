import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");

        String[] args = {"--a", "--b=1", "--c=1,2,3", "d", "--d=4", "-e", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--a");
        expected.add("--b");
        expected.add("1");
        expected.add("--c");
        expected.add("1");
        expected.add("2");
        expected.add("3");
        expected.add("d");
        expected.add("--d");
        expected.add("4");
        expected.add("-e");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--a", "arg1", "nonOption", "--b=1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("--a");
        expected.add("arg1");
        expected.add("nonOption");
        expected.add("--b");
        expected.add("1");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();

        String[] args = {"-acd"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("c");
        expected.add("d");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();

        String[] args = {"-abc"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("a");
        expected.add("b");
        expected.add("c");

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testBurstTokenWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"-abc=1"};
        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("b");
        expected.add("1");

        Assert.assertArrayEquals(expected.toArray(), result);
    }
}