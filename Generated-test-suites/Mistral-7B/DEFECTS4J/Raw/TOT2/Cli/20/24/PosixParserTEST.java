import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.ParsedPosixArgument;
import org.apache.commons.cli.PosixParser;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = new String[]{"--foo", "-a", "--bar", "value"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", false, "Bar option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-a", "--bar", "value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        String[] args = new String[]{"--long-option=value"};
        Options options = new Options();
        options.addOption("long-option", true, "Long option with value");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option=value"}, result);
    }

    @Test
    public void testFlattenMultipleLongOptions() {
        String[] args = new String[]{"--foo", "--bar=value"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar=value"}, result);
    }

    @Test
    public void testFlattenShortOptions() {
        String[] args = new String[]{"-a", "-b", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlattenMixedOptions() {
        String[] args = new String[]{"--foo", "-a", "--bar=value", "-c"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("a", false, "Option A");
        options.addOption("bar", true, "Option Bar");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-a", "--bar=value", "-c"}, result);
    }

    @Test
    public void testFlattenWithArg() {
        String[] args = new String[]{"--foo=arg"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo with argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertEquals("arg", result[0]);
    }

    @Test
    public void testFlattenWithMultipleArgs() {
        String[] args = new String[]{"--foo=arg1", "--foo=arg2"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo with argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertEquals("arg1", result[0]);
        Assert.assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlattenWithInvalidShortOption() {
        String[] args = new String[]{"-x"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testFlattenWithInvalidLongOption() {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testFlattenWithRemainingArguments() {
        String[] args = new String[]{"--foo", "-a", "--bar=value", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", true, "Option Bar");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-a", "--bar=value", "arg1", "arg2"}, result);
    }
}