import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 3, "Option C with three arguments");
    }

    @After
    public void tearDown() {
        options = null;
    }

    @Test
    public void testEmptyArgs() throws ParseException {
        String[] args = {};
        String[] expected = {};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }

    @Test
    public void testSingleArg() throws ParseException {
        String[] args = {"arg1"};
        String[] expected = {"arg1"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }

    @Test
    public void testSingleOption() throws ParseException {
        String[] args = {"-a"};
        String[] expected = {"-a"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }

    @Test
    public void testStringOptionWithArg() throws ParseException {
        String[] args = {"-a", "arg1"};
        String[] expected = {"-a", "arg1"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }

    @Test
    public void testStringOptionWithArgAndMoreArgs() throws ParseException {
        String[] args = {"-a", "arg1", "arg2", "arg3"};
        String[] expected = {"-a", "arg1", "arg2", "arg3"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }

    @Test
    public void testStringOptionWithArgAndAnotherOption() throws ParseException {
        String[] args = {"-a", "arg1", "-b", "arg2"};
        String[] expected = {"-a", "arg1", "-b", "arg2"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"-z"};
        String[] expected = {};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
        assertNull(options.getOption("z"));
    }

    @Test
    public void testOptionWithThreeArgs() throws ParseException {
        String[] args = {"-c", "arg1", "arg2", "arg3"};
        String[] expected = {"-c", "arg1", "arg2", "arg3"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
        assertSame(options.getOption("c"), result[0]);
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"arg1", "-a", "arg2", "-b"};
        String[] expected = {"arg1", "-a", "arg2", "-b"};
        String[] result = new PosixParser().flatten(options, args, true);
        assertEquals(expected, result);
    }

    @Test
    public void testDoubleDash() throws ParseException {
        String[] args = {"--", "arg1", "arg2"};
        String[] expected = {"--", "arg1", "arg2"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDoubleDashWithArg() throws ParseException {
        String[] args = {"--arg"};
        String[] expected = {"--arg"};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }

    @Test
    public void testSingleDash() throws ParseException {
        String[] args = {"-"};
        String[] expected = {};
        String[] result = new PosixParser().flatten(options, args, false);
        assertEquals(expected, result);
    }
}