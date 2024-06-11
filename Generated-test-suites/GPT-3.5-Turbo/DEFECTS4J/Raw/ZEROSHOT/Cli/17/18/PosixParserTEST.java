import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "value1", "-b", "value2", "nonOption"};
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        String[] result = parser.flatten(options, args, true);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlatten_ContinueAtNonOption() {
        String[] args = {"-a", "value1", "-b", "value2", "nonOption"};
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        String[] result = parser.flatten(options, args, false);

        assertNotNull(result);
        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
        assertEquals("nonOption", result[4]);
    }

    @Test
    public void testFlatten_WithSpecialToken() {
        String[] args = {"--option1", "value1", "--option2=value2", "-a=b"};
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("a", true, "Option a");

        String[] result = parser.flatten(options, args, false);

        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("--option1", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--option2", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlatten_ProcessSingleHyphen() {
        String[] args = {"-", "value1", "-a", "-"};
        options.addOption("a", true, "Option a");

        String[] result = parser.flatten(options, args, false);

        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("-", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-a", result[2]);
        assertEquals("-", result[3]);
    }

    @Test
    public void testFlatten_ProcessOptionToken() {
        String[] args = {"-a", "value1", "-b", "value2"};
        options.addOption("a", true, "Option a");

        String[] result = parser.flatten(options, args, true);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        String token = "-abc";
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        parser.burstToken(token, true);

        String[] result = parser.flatten(options, new String[]{token}, false);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("bc", result[1]);
    }

    @Test
    public void testBurstToken_ContinueAtNonOption() {
        String token = "-abc";
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        parser.burstToken(token, false);

        String[] result = parser.flatten(options, new String[]{token}, false);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(token, result[0]);
    }

    @Test
    public void testBurstToken_NoMatchingOption() {
        String token = "-xyz";
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        parser.burstToken(token, false);

        String[] result = parser.flatten(options, new String[]{token}, false);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(token, result[0]);
    }
}