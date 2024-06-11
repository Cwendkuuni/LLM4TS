import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D with argument");

        String[] arguments = {"--a", "--b", "--c", "--d=value"};
        String[] expected = {"--a", "--b", "--c", "--d", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D with argument");

        String[] arguments = {"--a", "--b", "--c", "--d=value", "nonOption"};
        String[] expected = {"--a", "--b", "--c", "--d", "value", "nonOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSpecialToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D with argument");

        String[] arguments = {"--a", "--b", "--", "value"};
        String[] expected = {"--a", "--b", "--", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstSingleHyphen() {
        Options options = new Options();

        String[] arguments = {"-"};
        String[] expected = {"--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        Assert.assertArrayEquals(expected, Arrays.asList(result).toArray());
    }

    @Test
    public void testBurstOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D with argument");

        String[] arguments = {"-abcd"};
        String[] expected = {"--a", "--b", "--c", "--d"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        Assert.assertArrayEquals(expected, Arrays.asList(result).toArray());
    }

    @Test
    public void testBurstOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D with argument");

        String[] arguments = {"-ad=value"};
        String[] expected = {"--a", "--d", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        Assert.assertArrayEquals(expected, Arrays.asList(result).toArray());
    }

    @Test
    public void testBurstMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D with argument");

        String[] arguments = {"-abc"};
        String[] expected = {"--a", "--c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        Assert.assertArrayEquals(expected, Arrays.asList(result).toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstUnknownOption() {
        Options options = new Options();

        String[] arguments = {"-x"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false)[0];

        Assert.fail("Expected IllegalArgumentException");
    }
}