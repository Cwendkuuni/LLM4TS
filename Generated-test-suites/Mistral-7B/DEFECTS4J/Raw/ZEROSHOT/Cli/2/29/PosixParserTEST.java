import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");

        String[] arguments = {"-a", "--b=test", "value1", "-c", "value2", "value3"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List expected = Arrays.asList(new String[]{"-a", "--b", "test", "-c", "value2", "value3"});
        Assert.assertArrayEquals(expected.toArray(), flattened);

        // Test with stopAtNonOption set to true
        String[] flattenedStopAtNonOption = parser.flatten(options, arguments, true);
        List expectedStopAtNonOption = Arrays.asList(new String[]{"-a", "--b=test", "value1", "-c", "value2", "value3"});
        Assert.assertArrayEquals(expectedStopAtNonOption.toArray(), flattenedStopAtNonOption);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] arguments = {"-z", "--b=test"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenNoOptions() throws ParseException {
        Options options = new Options();

        String[] arguments = {"value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List expected = Arrays.asList(new String[]{"--", "value1", "value2"});
        Assert.assertArrayEquals(expected.toArray(), flattened);
    }

    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        Options options = new Options();

        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List expected = Arrays.asList(new String[]{});
        Assert.assertArrayEquals(expected.toArray(), flattened);
    }
}