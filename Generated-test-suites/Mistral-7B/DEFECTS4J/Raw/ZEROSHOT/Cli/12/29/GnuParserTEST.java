import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten_emptyArgs() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[0];

        String[] expected = new String[0];
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_simpleOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        String[] arguments = {"-A"};

        String[] expected = {"-A"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_twoSimpleOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        options.addOption("B", true, "Option B");
        String[] arguments = {"-A", "-B"};

        String[] expected = {"-A", "-B"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_longOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption"};

        String[] expected = {"--longOption"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_complexOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("C", true, "Option C");
        options.addOption("D", true, "Option D");
        String[] arguments = {"--C=value", "-Dvalue"};

        String[] expected = {"--C", "value", "-D", "value"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_invalidOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-X"};

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_stopAtNonOption_simpleOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("A", true, "Option A");
        options.addOption("B", true, "Option B");
        String[] arguments = {"-A", "-B", "argument"};

        String[] expected = {"-A", "-B"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_stopAtNonOption_longOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        String[] arguments = {"--longOption", "argument"};

        String[] expected = {"--longOption"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_stopAtNonOption_complexOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("C", true, "Option C");
        options.addOption("D", true, "Option D");
        String[] arguments = {"--C=value", "-Dvalue", "argument"};

        String[] expected = {"--C", "value"};
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_doubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"--"};

        String[] expected = {"--"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_doubleDashWithArgument() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"--", "argument"};

        String[] expected = {"--", "argument"};
        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_doubleDashStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"--", "argument"};

        String[] expected = new String[0];
        String[] actual = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_emptyOptions() {
        GnuParser parser = new GnuParser();
        String[] arguments = {"arg1", "arg2"};

        String[] expected = {"arg1", "arg2"};
        String[] actual = parser.flatten(new Options(), arguments, false);

        Assert.assertArrayEquals(expected, actual);
    }
}