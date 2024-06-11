import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test flatten method with simple options.
     */
    @Test
    public void testSimpleOptions() {
        Options options = new Options();
        options.addOption("a", true, "An option with a single letter id and no argument.");
        options.addOption("b", 1, "An option with a single letter id and an integer value.");
        options.addOption("c", 2, "An option with a single letter id and a short integer value.");
        options.addOption("d", "arg", "An option with a single letter id and a string argument.");
        options.addOption("e", "arg1 arg2", "An option with a single letter id and multiple string arguments.");

        String[] arguments = {"-a", "-b", "1", "-c", "2", "--d", "arg", "--e", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "--d", "arg", "--e", "arg1", "arg2"}, result);
    }

    /**
     * Test flatten method with options and arguments.
     */
    @Test
    public void testOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", true, "An option with a single letter id and no argument.");
        options.addOption("b", 1, "An option with a single letter id and an integer value.");
        options.addOption("c", 2, "An option with a single letter id and a short integer value.");
        options.addOption("d", "arg", "An option with a single letter id and a string argument.");
        options.addOption("e", "arg1 arg2", "An option with a single letter id and multiple string arguments.");
        options.addOption("f", "arg1 arg2 arg3 arg4 arg5 arg6 arg7 arg8 arg9", "An option with a long id and multiple string arguments.");

        String[] arguments = {"--a", "--b=1", "--c=2", "--d=arg", "--e=arg1 arg2", "--f=arg1 arg2 arg3 arg4 arg5 arg6 arg7 arg8 arg9"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b", "1", "--c", "2", "--d", "arg", "--e", "arg1", "arg2", "--f", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", "arg8", "arg9"}, result);
    }

    /**
     * Test flatten method with optional and required arguments.
     */
    @Test
    public void testOptionalAndRequiredArguments() {
        Options options = new Options();
        options.addOption("a", true, "An option with a single letter id and no argument.");
        options.addOption("b", 1, "An option with a single letter id and an integer value.");
        options.addOption("c", 2, "An option with a single letter id and a short integer value.");
        options.addOption("d", "arg1 arg2", "An option with a single letter id and multiple string arguments.");
        options.addOption("e", "arg1 arg2", "An option with a single letter id and multiple string arguments and a required argument.");

        String[] arguments = {"-a", "-b", "1", "--c", "2", "--d=arg1 arg2", "--e=arg1 arg2 arg3"};

        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("Missing required option '--e' with arg 'arg3'", e.getMessage());
        }

        options.addOption("e", "arg1 arg2 arg3", "An option with a single letter id and multiple string arguments and optional arguments.");

        String[] arguments2 = {"-a", "-b", "1", "--c", "2", "--d=arg1 arg2", "--e=arg1 arg2"};
        PosixParser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options, arguments2, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "1", "--c", "2", "--d", "arg1", "arg2", "--e", "arg1", "arg2"}, result2);
    }

    /**
     * Test flatten method with unhandled tokens.
     */
    @Test
    public void testUnhandledTokens() {
        Options options = new Options();
        options.addOption("a", true, "An option with a single letter id and no argument.");
        options.addOption("b", 1, "An option with a single letter id and an integer value.");

        String[] arguments = {"-a", "-b=1", "--unknown_option", "1", "--option_with_arg=arg", "--option_with_args=arg1 arg2 arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "1", "--unknown_option", "--option_with_arg", "arg", "--option_with_args", "arg1", "arg2", "arg3"}, result);
    }

    /**
     * Test flatten method with stopAtNonOption set to true.
     */
    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "An option with a single letter id and no argument.");
        options.addOption("b", 1, "An option with a single letter id and an integer value.");

        String[] arguments = {"-a", "-b=1", "non_option", "-c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "1", "non_option", "-c"}, result);
    }

    /**
     * Test flatten method with negative indices.
     */
    @Test
    public void testNegativeIndices() {
        Options options = new Options();

        String[] arguments = {"--a", "--b=-1", "--c=-2"};

        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Index: -1", e.getMessage());
        }

        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Index: -2", e.getMessage());
        }
    }

    /**
     * Test flatten method with very long arguments.
     */
    @Test
    public void testLongArguments() {
        Options options = new Options();
        options.addOption("a", true, "An option with a single letter id and no argument.");

        String[] arguments = {"--a=long_argument_longer_than_max_array_length_in_java"};
        PosixParser parser = new PosixParser();
        try {
            String[] result = parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("Array size exceeds limit", e.getMessage());
        }
    }
}