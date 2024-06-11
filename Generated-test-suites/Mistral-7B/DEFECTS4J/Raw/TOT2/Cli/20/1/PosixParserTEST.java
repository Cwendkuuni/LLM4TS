import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flatten method with basic usage.
     */
    @Test
    public void testBasicUsage() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C");

        String[] args = {"-a", "-b", "1", "--c", "3", "4", "5"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "--c", "3"}, flattened);
    }

    /**
     * Test flatten method with stopAtNonOption=true.
     */
    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] args = {"--foo", "-a", "-b", "1", "4", "5"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--foo", "-a", "-b", "1", "4", "5"}, flattened);
    }

    /**
     * Test flatten method with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        String[] args = {"--x", "-a", "-b", "1", "4", "5"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test flatten method with long option.
     */
    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");

        String[] args = {"--foo", "bar"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "bar"}, flattened);
    }

    /**
     * Test flatten method with long option with argument.
     */
    @Test
    public void testLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", 2, "Option Bar");

        String[] args = {"--foo", "arg1", "--bar", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "arg1", "--bar", "arg2"}, flattened);
    }

    /**
     * Test flatten method with multiple long options.
     */
    @Test
    public void testLongOptions() {
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", true, "Option Bar");

        String[] args = {"--foo", "--bar"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "--bar"}, flattened);
    }

    /**
     * Test flatten method with multiple long options with arguments.
     */
    @Test
    public void testLongOptionsWithArguments() {
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", true, "Option Bar");
        options.addOption("baz", 3, "Option Baz");

        String[] args = {"--foo", "arg1", "--bar", "arg2", "--baz", "arg3"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "arg1", "--bar", "arg2", "--baz", "arg3"}, flattened);
    }

    /**
     * Test flatten method with multiple long options with one having arguments.
     */
    @Test
    public void testLongOptionsWithMixedArguments() {
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", 2, "Option Bar");

        String[] args = {"--foo", "arg1", "--bar", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "arg1", "--bar", "arg2", "arg3"}, flattened);
    }

    /**
     * Test flatten method with multiple long options with multiple arguments.
     */
    @Test
    public void testLongOptionsWithMultipleArguments() {
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", 2, "Option Bar");
        options.addOption("baz", 3, "Option Baz");

        String[] args = {"--foo", "arg1", "--bar", "arg2", "arg3", "--baz", "arg4", "arg5"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "arg1", "--bar", "arg2", "arg3", "--baz", "arg4", "arg5"}, flattened);
    }
}