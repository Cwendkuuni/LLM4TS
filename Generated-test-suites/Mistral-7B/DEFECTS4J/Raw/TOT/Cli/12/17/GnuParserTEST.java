import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlattenNormalCase() {
        String[] arguments = {"-a", "--optionB", "valueForOptionB", "-c", "valueForOptionC", "--"};
        Options options = new Options();
        GnuParser parser = new GnuParser();

        String[] expected = {"-a", "--optionB", "valueForOptionB", "-c", "valueForOptionC"};
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleCharacterOption() {
        String[] arguments = {"-D", "property=value"};
        Options options = new Options();

        String[] expected = {"-D", "property=value"};
        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingOption() {
        String[] arguments = {"-X", "missingOption"};
        Options options = new Options();

        new GnuParser().flatten(options, arguments, false);
    }

    @Test
    public void testFlattenDoubleDash() {
        String[] arguments = {"--", "value1", "--", "value2"};
        Options options = new Options();

        String[] expected = {"--", "value1", "--", "value2"};
        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "--optionB", "valueForOptionB", "-c", "valueForOptionC", "nonOption"};
        Options options = new Options();

        String[] expected = {"-a", "--optionB", "valueForOptionB"};
        String[] result = new GnuParser().flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        Options options = new Options();

        String[] expected = {};
        String[] result = new GnuParser().flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }
}