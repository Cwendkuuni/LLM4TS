import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-a", "-b"};
        String[] expected = {"-a", "-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionsWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", 1, "Option C");

        String[] args = {"-a", "argA", "--c", "argC"};
        String[] expected = {"-a", "argA", "-c", "argC"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = {"-x", "--", "-c", "argC"}; // invalid option 'x'
        PosixParser parser = new PosixParser();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_LongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");

        String[] args = {"--longOptionA"};
        String[] expected = {"--longOptionA"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionsWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");

        String[] args = {"--longOptionA=arg1", "--longOptionB=arg2"};
        String[] expected = {"--longOptionA=arg1", "--longOptionB=arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }
}