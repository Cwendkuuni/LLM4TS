import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option with an argument");
        options.addOption("n", "no-option");

        String[] arguments = {"--option", "value", "--no-option", "-o", "arg", "--option=value2"};

        Parser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--option", "value", "--no-option", "-o", "o", "arg", "--option=value2"}, flattened);

        String[] flattenedStopAtNonOption = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"--option", "value", "--no-option", "-o", "--", "arg", "--option=value2"}, flattenedStopAtNonOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullOptions() {
        String[] arguments = {"--option", "value"};
        new PosixParser().flatten(null, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArguments() {
        Options options = new Options();
        new PosixParser().flatten(options, null, false);
    }
}