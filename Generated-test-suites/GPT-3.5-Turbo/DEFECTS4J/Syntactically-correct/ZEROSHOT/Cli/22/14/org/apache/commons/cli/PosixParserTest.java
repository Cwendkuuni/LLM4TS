import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testFlatten_longOptionWithoutValue_success() {
        String[] arguments = {"--foo"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("foo", result[1]);
    }

    @Test
    public void testFlatten_longOptionWithValue_success() {
        options.addOption(Option.builder("f").longOpt("foo").hasArg().build());
        String[] arguments = {"--foo=bar"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("--", result[0]);
        assertEquals("foo", result[1]);
        assertEquals("=", result[2]);
        assertEquals("bar", result[3]);
    }

    @Test
    public void testFlatten_singleHyphen_success() {
        String[] arguments = {"-"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_shortOption_success() {
        options.addOption(Option.builder("f").longOpt("foo").build());
        String[] arguments = {"-f"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_shortOptionWithoutValue_success() {
        options.addOption(Option.builder("f").longOpt("foo").build());
        String[] arguments = {"-f"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_shortOptionWithValue_success() {
        options.addOption(Option.builder("f").longOpt("foo").hasArg().build());
        String[] arguments = {"-fbar"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlatten_nonOption_stopAtNonOptionSet_success() {
        String[] arguments = {"nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("nonOption", result[0]);
    }

    @Test
    public void testFlatten_nonOption_stopAtNonOptionNotSet_success() {
        String[] arguments = {"nonOption"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("nonOption", result[0]);
    }

    @Test
    public void testBurstToken_shortOption_success() throws Exception {
        options.addOption(Option.builder("f").longOpt("foo").build());
        String token = "-f";

        parser.burstToken(token, true);

        assertEquals(1, tokens.size());
        assertEquals("-f", tokens.get(0));
    }

    @Test
    public void testBurstToken_shortOptionInvalid_success() throws Exception {
        String token = "-x";

        parser.burstToken(token, true);

        assertEquals(1, tokens.size());
        assertEquals("-x", tokens.get(0));
    }

    @Test
    public void testBurstToken_shortOptionWithValue_success() throws Exception {
        options.addOption(Option.builder("f").longOpt("foo").hasArg().build());
        String token = "-fbar";

        parser.burstToken(token, true);

        assertEquals(2, tokens.size());
        assertEquals("-f", tokens.get(0));
        assertEquals("bar", tokens.get(1));
    }

    @Test
    public void testBurstToken_stopAtNonOption_success() throws Exception {
        String token = "-abc";

        parser.burstToken(token, true);

        assertEquals(1, tokens.size());
        assertEquals("--abc", tokens.get(0));
    }

    @Test
    public void testBurstToken_stopAtNonOptionNotSet_success() throws Exception {
        String token = "-abc";

        parser.burstToken(token, false);

        assertEquals(3, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertEquals("-c", tokens.get(2));
    }
}