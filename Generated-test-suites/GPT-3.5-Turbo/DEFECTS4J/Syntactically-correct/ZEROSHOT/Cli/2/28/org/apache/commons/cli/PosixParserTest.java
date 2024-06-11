import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] args = {"-a", "-b", "value"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testProcessSingleHyphen() {
        String hyphen = "-";
        parser.processSingleHyphen(hyphen);

        String[] result = parser.flatten(new Options(), new String[]{}, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String token = "-a";
        parser.processOptionToken(token, true);

        String[] result = parser.flatten(options, new String[]{}, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String token = "-abc";
        parser.burstToken(token, true);

        String[] result = parser.flatten(options, new String[]{}, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-bc", result[1]);
    }

}