import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-f", "--foo", "value1", "-abc", "value2", "arg1", "arg2"};
        String[] flattened = parser.flatten(options, arguments, true);
        String[] expected = {"-f", "--foo", "value1", "-a", "-b", "-c", "value2", "--", "arg1", "arg2"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_stopAtNonOptionFalse() {
        String[] arguments = {"-f", "--foo", "value1", "-abc", "value2", "arg1", "arg2"};
        String[] flattened = parser.flatten(options, arguments, false);
        String[] expected = {"-f", "--foo", "value1", "-a", "-b", "-c", "value2", "arg1", "arg2"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_noOptions() {
        String[] arguments = {"value1", "value2"};
        String[] flattened = parser.flatten(options, arguments, true);
        String[] expected = {"value1", "value2"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_noArguments() {
        String[] arguments = {};
        String[] flattened = parser.flatten(options, arguments, true);
        String[] expected = {};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_stopAtNonOptionTrue_noRemainingOptions() {
        String[] arguments = {"-f", "--foo", "value1"};
        String[] flattened = parser.flatten(options, arguments, true);
        String[] expected = {"-f", "--foo", "value1"};
        assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlatten_stopAtNonOptionFalse_noRemainingOptions() {
        String[] arguments = {"-f", "--foo", "value1"};
        String[] flattened = parser.flatten(options, arguments, false);
        String[] expected = {"-f", "--foo", "value1"};
        assertArrayEquals(expected, flattened);
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}