import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    PosixParser parser;
    Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"--foo", "bar", "-x", "-abc", "--d", "e", "-f", "--", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(10, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("-x", result[2]);
        assertEquals("-abc", result[3]);
        assertEquals("--d", result[4]);
        assertEquals("e", result[5]);
        assertEquals("-f", result[6]);
        assertEquals("--", result[7]);
        assertEquals("arg1", result[8]);
        assertEquals("arg2", result[9]);
    }

    @Test
    public void testProcessOptionToken() {
        boolean stopAtNonOption = true;

        String token1 = "--foo";
        parser.processOptionToken(token1, stopAtNonOption);
        assertEquals("--foo", parser.currentOption.getOpt());
        assertEquals("--foo", parser.tokens.get(0));

        String token2 = "-x";
        parser.processOptionToken(token2, stopAtNonOption);
        assertEquals("-x", parser.currentOption.getOpt());
        assertEquals("-x", parser.tokens.get(1));

        String token3 = "-abc";
        parser.processOptionToken(token3, stopAtNonOption);
        assertEquals("-abc", parser.currentOption.getOpt());
        assertEquals("-abc", parser.tokens.get(2));

        String token4 = "-d";
        parser.processOptionToken(token4, stopAtNonOption);
        assertNull(parser.currentOption);
        assertEquals("--", parser.tokens.get(3));
        assertEquals("-d", parser.tokens.get(4));
    }

    @Test
    public void testProcess() {
        String value1 = "bar";
        parser.currentOption = options.addOption("foo", true, "Test option");
        parser.process(value1);
        assertNull(parser.currentOption);
        assertEquals(value1, parser.tokens.get(0));

        String value2 = "value";
        parser.currentOption = options.addOption("abc", false, "Test option");
        parser.process(value2);
        assertEquals(value2, parser.tokens.get(1));

        String value3 = "value1";
        parser.currentOption = options.addOption("abc", true, "Test option");
        parser.process(value3);
        assertNull(parser.currentOption);
        assertEquals(value3, parser.tokens.get(2));

        String value4 = "value2";
        parser.process(value4);
        assertNull(parser.currentOption);
        assertEquals("--", parser.tokens.get(3));
        assertEquals(value4, parser.tokens.get(4));
    }

    @Test
    public void testBurstToken() {
        boolean stopAtNonOption = true;

        String token1 = "-xyz";
        parser.burstToken(token1, stopAtNonOption);
        assertEquals("-x", parser.tokens.get(0));
        assertEquals("-y", parser.tokens.get(1));
        assertEquals("-z", parser.tokens.get(2));

        String token2 = "-abc";
        parser.burstToken(token2, stopAtNonOption);
        assertEquals("-a", parser.tokens.get(3));
        assertEquals("-b", parser.tokens.get(4));
        assertEquals("-c", parser.tokens.get(5));

        parser.currentOption = options.addOption("d", true, "Test option");
        String token3 = "-def";
        parser.burstToken(token3, stopAtNonOption);
        assertEquals("-d", parser.tokens.get(6));
        assertEquals("ef", parser.tokens.get(7));

        String token4 = "-ghi";
        parser.burstToken(token4, stopAtNonOption);
        assertNull(parser.currentOption);
        assertEquals("-ghi", parser.tokens.get(8));

        String token5 = "-j";
        parser.burstToken(token5, stopAtNonOption);
        assertNull(parser.currentOption);
        assertEquals("-j", parser.tokens.get(9));
    }
}