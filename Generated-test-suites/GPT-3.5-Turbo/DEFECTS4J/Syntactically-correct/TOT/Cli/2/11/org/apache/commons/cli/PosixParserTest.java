import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption(Option.builder("a").required().hasArg().build());
        options.addOption(Option.builder("b").optionalArg(true).build());
        options.addOption(Option.builder("c").hasArgs().build());
        options.addOption(Option.builder("d").longOpt("debug").desc("Enable debug mode").build());
    }

    @Test
    public void testFlatten_singleOption_noArgument() throws ParseException {
        String[] arguments = new String[]{"-a"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_singleOption_withArgument() throws ParseException {
        String[] arguments = new String[]{"-a", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_singleOption_optionalArgument() throws ParseException {
        String[] arguments = new String[]{"-b", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_singleOption_optionalArgumentNoValue() throws ParseException {
        String[] arguments = new String[]{"-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_multipleOptions() throws ParseException {
        String[] arguments = new String[]{"-a", "-d", "--", "-c", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-d", result[1]);
        assertEquals("--", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("arg1", result[4]);
    }

    @Test
    public void testFlatten_stopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"-a", "arg1", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_noStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"-a", "arg1", "nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_invalidOption() throws ParseException {
        String[] arguments = new String[]{"-x"};

        parser.flatten(options, arguments, true);
    }

    @Test
    public void testFlatten_multipleCharactersOption() throws ParseException {
        String[] arguments = new String[]{"-cd", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-c", result[0]);
        assertEquals("-d", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_multipleCharactersOptionWithArgument() throws ParseException {
        String[] arguments = new String[]{"-ca", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-c", result[0]);
        assertEquals("a", result[1]);
    }

    @Test
    public void testFlatten_multipleCharactersOptionWithExtraCharacters() throws ParseException {
        String[] arguments = new String[]{"-cabcd", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-c", result[0]);
        assertEquals("abcd", result[1]);
    }

    @Test
    public void testFlatten_multipleCharactersOptionWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"-cxyz", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-c", result[0]);
        assertEquals("-x", result[1]);
        assertEquals("yz", result[2]);
    }

    @Test
    public void testFlatten_longOption() throws ParseException {
        String[] arguments = new String[]{"--debug"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--debug", result[0]);
    }

    @Test
    public void testFlatten_longOptionWithValue() throws ParseException {
        String[] arguments = new String[]{"--debug", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--debug", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_longOptionWithEqualsValue() throws ParseException {
        String[] arguments = new String[]{"--debug=arg1"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--debug", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_longOptionWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"--abc"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("--abc", result[0]);
    }
}