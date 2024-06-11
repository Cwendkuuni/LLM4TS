import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten_Basic() {
        String[] args = {"--foo=bar", "-h", "-a", "-b=qux", "-c", "-d=quux"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("h", false, "Help option");
        options.addOption("a", false, "Another option");
        options.addOption("b", true, "Bar option");
        options.addOption("c", false, "C option");
        options.addOption("d", true, "D option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--foo",
                "bar",
                "--h",
                "-a",
                "-b",
                "qux",
                "--c",
                "--d",
                "quux"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_SimpleOption() {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", false, "A option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_NoOption() {
        String[] args = {"--qux"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }

        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--qux"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"--foo", "bar", "-a", "qux", "--d", "quux"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("a", false, "Another option");
        options.addOption("d", true, "D option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--foo",
                "bar",
                "--d",
                "quux"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_EmptyArgs() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[0], false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--long-option-name"};
        Options options = new Options();
        options.addOption("longOptionName", false, "Long option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-name"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] args = {"--long-option-name=value"};
        Options options = new Options();
        options.addOption("longOptionName", true, "Long option with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-name", "value"}, result);
    }

    @Test
    public void testFlatten_LongOptionWithMultipleValues() {
        String[] args = {"--long-option-name=value1", "--long-option-name=value2"};
        Options options = new Options();
        options.addOption("longOptionName", true, "Long option with multiple values");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-name", "value1", "--long-option-name", "value2"}, result);
    }
}