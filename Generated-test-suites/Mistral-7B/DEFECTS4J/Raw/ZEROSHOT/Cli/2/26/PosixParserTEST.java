import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlattenBasic() {
        String[] args = {"--foo", "-a", "-b", "--bar=baz", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        Option fooOption = new Option("foo", true, false, "Foo option");
        Option barOption = new Option("bar", true, false, "Bar option");
        options.addOption(fooOption);
        options.addOption(barOption);

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--foo", "-a", "-b", "--bar", "baz", "-c", "--", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--foo", "-a", "-b", "--bar=baz", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        Option fooOption = new Option("foo", true, false, "Foo option");
        Option barOption = new Option("bar", true, false, "Bar option");
        options.addOption(fooOption);
        options.addOption(barOption);

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"--foo", "-a", "-b", "--bar", "baz", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] args = {"-", "--foo"};
        Options options = new Options();
        Option fooOption = new Option("foo", true, false, "Foo option");
        options.addOption(fooOption);

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-", "--foo"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenInvalidOption() {
        String[] args = {"--invalid", "-a", "-b", "--bar=baz", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        Option fooOption = new Option("foo", true, false, "Foo option");
        Option barOption = new Option("bar", true, false, "Bar option");
        options.addOption(fooOption);
        options.addOption(barOption);

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--invalid", "-a", "-b", "--bar", "baz", "-c", "--", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenInvalidOptionStopAtNonOption() {
        String[] args = {"--invalid", "-a", "-b", "--bar=baz", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        Option fooOption = new Option("foo", true, false, "Foo option");
        Option barOption = new Option("bar", true, false, "Bar option");
        options.addOption(fooOption);
        options.addOption(barOption);

        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"--invalid", "-a", "-b", "--bar", "baz", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArgs() {
        Options options = new Options();
        Parser parser = new PosixParser();
        parser.flatten(options, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullOptions() {
        String[] args = {"--foo"};
        Parser parser = new PosixParser();
        parser.flatten(null, args, false);
    }
}