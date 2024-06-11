import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "-b", "value", "--option=value"};
        String[] expected = {"-a", "-b", "value", "--", "--option=value"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "-b", "value", "--option=value", "arg1", "arg2"};
        String[] expected = {"-a", "-b", "value", "--", "--option=value", "arg1", "arg2"};
        String[] actual = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenNoOptions() {
        String[] arguments = {"arg1", "arg2"};
        String[] expected = {"arg1", "arg2"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        String[] expected = {};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenOnlyHyphens() {
        String[] arguments = {"-", "-"};
        String[] expected = {"-", "-"};
        String[] actual = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, actual);
    }
}