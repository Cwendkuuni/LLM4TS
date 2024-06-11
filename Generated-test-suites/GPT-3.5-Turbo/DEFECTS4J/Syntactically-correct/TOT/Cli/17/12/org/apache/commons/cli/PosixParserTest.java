import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", "c-option", false, "Option c");
        options.addOption("d", "d-option", true, "Option d");
        options.addOption("e", true, "Option e");
        options.addOption("f", true, "Option f");
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        String[] args = {"-a", "input.txt", "-b", "output.txt", "--", "-c-option"};

        String[] result = parser.flatten(options, args, true);

        assertEquals("-a", result[0]);
        assertEquals("input.txt", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("output.txt", result[3]);
        assertEquals("--", result[4]);
        assertEquals("-c-option", result[5]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        String[] args = {"-a", "input.txt", "-b", "output.txt", "--", "-c-option"};

        String[] result = parser.flatten(options, args, false);

        assertEquals("-a", result[0]);
        assertEquals("input.txt", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("output.txt", result[3]);
        assertEquals("--", result[4]);
        assertEquals("-c-option", result[5]);
    }

    @Test
    public void testBurstToken_ValidOptionId() {
        String token = "-cd";

        parser.burstToken(token, false);

        assertEquals("-c", parser.getTokens().get(0));
        assertEquals("-d", parser.getTokens().get(1));
    }

    @Test
    public void testBurstToken_InvalidOptionId_StopAtNonOptionTrue() {
        String token = "-xyz";

        parser.burstToken(token, true);

        assertEquals("--", parser.getTokens().get(0));
        assertEquals("-xyz", parser.getTokens().get(1));
    }

    @Test
    public void testBurstToken_InvalidOptionId_StopAtNonOptionFalse() {
        String token = "-xyz";

        parser.burstToken(token, false);

        assertEquals("-xyz", parser.getTokens().get(0));
    }

    @Test
    public void testProcess_SingleOption() {
        String value = "input.txt";

        parser.process(value);

        assertEquals(value, parser.getTokens().get(0));
    }

    @Test
    public void testProcess_InvalidOption_StopAtNonOptionTrue() {
        String value = "-xyz";

        parser.process(value);

        assertEquals("--", parser.getTokens().get(0));
        assertEquals(value, parser.getTokens().get(1));
    }

    @Test
    public void testProcess_InvalidOption_StopAtNonOptionFalse() {
        String value = "-xyz";

        parser.process(value);

        assertEquals("-xyz", parser.getTokens().get(0));
    }
}