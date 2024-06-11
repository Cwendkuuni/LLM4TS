import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;

public class GnuParserTest {

    private Options options;
    private GnuParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new GnuParser();
    }

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = {"-a", "value1", "-b", "value2", "--option", "value3"};
        String[] expected = {"-a", "value1", "-b", "value2", "--option", "value3"};

        CommandLine cmd = parser.parse(options, arguments);
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] arguments = {"-a", "value1", "-b", "value2", "--option", "value3", "nonOption"};
        String[] expected = {"-a", "value1", "-b", "value2", "--option", "value3", "nonOption"};

        CommandLine cmd = parser.parse(options, arguments);
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDoubleDash() throws ParseException {
        String[] arguments = {"-a", "value1", "nonOption", "--", "value2"};
        String[] expected = {"-a", "value1", "nonOption", "--", "value2"};

        CommandLine cmd = parser.parse(options, arguments);
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithDashOnly() throws ParseException {
        String[] arguments = {"-", "value1"};
        String[] expected = {"-", "value1"};

        CommandLine cmd = parser.parse(options, arguments);
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithExistingOption() throws ParseException {
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "value1", "-b", "value2", "--option", "value3"};
        String[] expected = {"-a", "value1", "-b", "value2", "--option", "value3"};

        CommandLine cmd = parser.parse(options, arguments);
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithPartialOption() throws ParseException {
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-c", "value1", "-b", "value2", "--option", "value3"};
        String[] expected = {"-c", "value1", "-b", "value2", "--option", "value3"};

        CommandLine cmd = parser.parse(options, arguments);
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithPropertiesOption() throws ParseException {
        options.addOption("D", true, "Properties Option");

        String[] arguments = {"-Dproperty=value", "-b", "value2", "--option", "value3"};
        String[] expected = {"-D", "property=value", "-b", "value2", "--option", "value3"};

        CommandLine cmd = parser.parse(options, arguments);
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNonOption() throws ParseException {
        String[] arguments = {"-a", "value1", "-b", "value2", "--option", "value3", "nonOption"};
        String[] expected = {"-a", "value1", "-b", "value2", "--option", "value3", "nonOption"};

        CommandLine cmd = parser.parse(options, arguments);
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

}