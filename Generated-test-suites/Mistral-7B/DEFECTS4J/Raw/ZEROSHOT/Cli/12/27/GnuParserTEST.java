import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.util.ArgList;
import org.apache.commons.cli.util.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GnuParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("D", "debug", false, "Print debug information.");
        options.addOption("f", "file", true, "Path to input file.");
        options.addOption("h", "help", false, "Print help information.");
        options.addOption("i", "input", true, "Input to process.");
        options.addOption("o", "output", true, "Output file.");
        options.addOption("v", "verbose", false, "Print verbose output.");
    }

    @Test
    public void testFlattenSimple() {
        String[] arguments = new String[]{"-D", "debug", "-f", "/path/to/file.txt", "-i", "input", "-o", "output"};
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-D", "debug", "-f", "/path/to/file.txt", "-i", "input", "-o", "output"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDash() {
        String[] arguments = new String[]{"--", "-h"};
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"--", "-h"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        String[] arguments = new String[]{"--", "--help"};
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"--", "--help"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleCharacterOption() {
        String[] arguments = new String[]{"-h"};
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-h"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenTwoCharacterOption() {
        String[] arguments = new String[]{"-D", "debug"};
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-D", "debug"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] arguments = new String[]{"-X", "invalid"};
        Parser parser = new GnuParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenMultiArgumentOption() {
        String[] arguments = new String[]{"-i", "arg1", "arg2", "arg3"};
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-i", "arg1", "arg2", "arg3"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = new String[]{"-D", "debug", "arg1", "arg2"};
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);
        String[] expected = new String[]{"-D", "debug"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsAndArguments() {
        String[] arguments = new String[]{"-D", "debug", "-f", "/path/to/file.txt", "-i", "arg1", "arg2", "-o", "output"};
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[]{"-D", "debug", "-f", "/path/to/file.txt", "-i", "arg1", "arg2", "-o", "output"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = new String[0];
        Parser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);
        String[] expected = new String[0];
        Assert.assertArrayEquals(expected, result);
    }
}