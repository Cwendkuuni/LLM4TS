import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_BasicUsage() {
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "-b", "-c=value", "--", "-d", "-e"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            List<String> expected = new ArrayList<>();
            expected.add("--foo");
            expected.add("--");
            expected.add("-a");
            expected.add("-b");
            expected.add("-c");
            expected.add("value");
            expected.add("--");
            expected.add("-d");
            expected.add("-e");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "-b", "--", "-c", "-d"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, true);
            List<String> expected = new ArrayList<>();
            expected.add("--foo");
            expected.add("-a");
            expected.add("-b");
            expected.add("--");
            expected.add("-c");
            expected.add("-d");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        Options options = new Options();
        String[] arguments = {"--foo", "-x", "--"}; // 'x' is an invalid option
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MissingArgument() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        String[] arguments = {"--foo", "-a"}; // missing argument for option 'a'
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        Options options = new Options();
        String[] arguments = {"--longOption=value", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            List<String> expected = new ArrayList<>();
            expected.add("--longOption");
            expected.add("value");
            expected.add("--");
            expected.add("arg1");
            expected.add("arg2");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MultipleLongOptions() {
        Options options = new Options();
        String[] arguments = {"--longOption1", "--longOption2=value", "--"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            List<String> expected = new ArrayList<>();
            expected.add("--longOption1");
            expected.add("--longOption2");
            expected.add("value");
            expected.add("--");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MultipleShortOptions() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "-c", "--"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            List<String> expected = new ArrayList<>();
            expected.add("-a");
            expected.add("-b");
            expected.add("-c");
            expected.add("--");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_OptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "a help message", 3);
        String[] arguments = {"--foo", "-a", "arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            List<String> expected = new ArrayList<>();
            expected.add("--foo");
            expected.add("-a");
            expected.add("arg1");
            expected.add("arg2");
            expected.add("arg3");
            expected.add("--");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_StopAtNonOptionWithOption() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        String[] arguments = {"--foo", "-a", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, true);
            List<String> expected = new ArrayList<>();
            expected.add("--foo");
            expected.add("-a");
            expected.add("--");
            expected.add("arg1");
            expected.add("arg2");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_LongOptionWithArgumentAndTrailingDash() {
        Options options = new Options();
        String[] arguments = {"--longOption=value--", "--"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            List<String> expected = new ArrayList<>();
            expected.add("--longOption");
            expected.add("value");
            expected.add("--");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MixedShortAndLongOptions() {
        Options options = new Options();
        options.addOption("a", true, "a help message");
        options.addOption("l", "list", "list all options");
        String[] arguments = {"-a", "--longOption=value", "--", "-b"};
        PosixParser parser = new PosixParser();

        try {
            String[] flattened = parser.flatten(options, arguments, false);
            List<String> expected = new ArrayList<>();
            expected.add("-a");
            expected.add("--longOption");
            expected.add("value");
            expected.add("--");
            expected.add("-b");

            Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}