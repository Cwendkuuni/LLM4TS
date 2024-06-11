import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flattening with basic options.
     */
    @Test
    public void testFlatteningBasicOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", "value", "Option C with a string argument");

        String[] arguments = {"-a", "--b", "1", "-c", "value"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "--b", "1", "-c", "value"}, parsedArguments);
    }

    /**
     * Test flattening with long options.
     */
    @Test
    public void testFlatteningLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", 1, "Long option B with an integer argument");

        String[] arguments = {"--longOptionA", "--longOptionB=1"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--longOptionA", "--longOptionB=1"}, parsedArguments);
    }

    /**
     * Test flattening with multiple options.
     */
    @Test
    public void testFlatteningMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", "value", "Option C with a string argument");

        String[] arguments = {"-a", "--b=1", "-c", "value"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "--b=1", "-c", "value"}, parsedArguments);
    }

    /**
     * Test flattening with no options.
     */
    @Test
    public void testFlatteningNoOptions() throws ParseException {
        Options options = new Options();

        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[0], parsedArguments);
    }

    /**
     * Test flattening with an invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test flattening with stopAtNonOption.
     */
    @Test
    public void testFlatteningStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "--stopNonOption", "argument"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "--stopNonOption", "argument"}, parsedArguments);
    }

    /**
     * Test flattening with bursting.
     */
    @Test
    public void testFlatteningBursting() throws ParseException {
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");

        String[] arguments = {"-oA--oB"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-oA", "--oB"}, parsedArguments);
    }
}