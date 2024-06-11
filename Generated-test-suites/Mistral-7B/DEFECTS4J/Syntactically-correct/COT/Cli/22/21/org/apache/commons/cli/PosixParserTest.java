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
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");

        String[] arguments = {"--a", "-b", "--c", "-d=4", "e", "--", "f", "g"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList(
                "--a", "-c", "--d", "4", "--", "f", "g"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);
    }

    @Test
    public void testFlattenWithLongValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--a=long-value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList(
                "--a", "long-value"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithNoOptions() throws ParseException {
        Options options = new Options();

        String[] arguments = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList(
                "--", "arg1", "arg2"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"arg1", "--a", "arg3"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList(
                "--a", "arg3"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArgs);
    }
}