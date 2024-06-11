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

        // Add some sample options
        Option option1 = new Option("a", "apple", false, "An option without argument");
        Option option2 = new Option("b", "banana", true, "An option with argument");
        Option option3 = new Option("c", "cherry", false, "An option without argument");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
    }

    // test flatten method with stopAtNonOption set to false
    @Test
    public void testFlatten_StopAtNonOptionFalse() {
        String[] args = {"-a", "--apple", "-b", "arg1", "-c", "--cherry"};

        String[] result = parser.flatten(options, args, false);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--apple", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("--cherry", result[5]);
    }

    // test flatten method with stopAtNonOption set to true
    @Test
    public void testFlatten_StopAtNonOptionTrue() {
        String[] args = {"-a", "--apple", "-b", "arg1", "-c", "--cherry"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--apple", result[1]);
    }

    // test processOptionToken method with valid option
    @Test
    public void testProcessOptionToken_ValidOption() {
        String token = "-a";
        boolean stopAtNonOption = false;

        parser.processOptionToken(token, stopAtNonOption);

        String[] result = (String[]) ((List) parser.getTokens()).toArray();
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    // test processOptionToken method with invalid option and stopAtNonOption set to true
    @Test
    public void testProcessOptionToken_InvalidOption_StopAtNonOptionTrue() {
        String token = "-x";
        boolean stopAtNonOption = true;

        parser.processOptionToken(token, stopAtNonOption);

        String[] result = (String[]) ((List) parser.getTokens()).toArray();
        assertEquals(0, result.length);
        assertEquals(true, parser.getEatTheRest());
    }

    // test processOptionToken method with invalid option and stopAtNonOption set to false
    @Test
    public void testProcessOptionToken_InvalidOption_StopAtNonOptionFalse() {
        String token = "-x";
        boolean stopAtNonOption = false;

        parser.processOptionToken(token, stopAtNonOption);

        String[] result = (String[]) ((List) parser.getTokens()).toArray();
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    // test burstToken method with valid option
    @Test
    public void testBurstToken_ValidOption() {
        String token = "-barg1";
        boolean stopAtNonOption = false;

        parser.burstToken(token, stopAtNonOption);

        String[] result = (String[]) ((List) parser.getTokens()).toArray();
        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("arg1", result[1]);
    }

    // test burstToken method with invalid option and stopAtNonOption set to true
    @Test
    public void testBurstToken_InvalidOption_StopAtNonOptionTrue() {
        String token = "-xarg1";
        boolean stopAtNonOption = true;

        parser.burstToken(token, stopAtNonOption);

        String[] result = (String[]) ((List) parser.getTokens()).toArray();
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("xarg1", result[1]);
    }

    // test burstToken method with invalid option and stopAtNonOption set to false
    @Test
    public void testBurstToken_InvalidOption_StopAtNonOptionFalse() {
        String token = "-xarg1";
        boolean stopAtNonOption = false;

        parser.burstToken(token, stopAtNonOption);

        String[] result = (String[]) ((List) parser.getTokens()).toArray();
        assertEquals(1, result.length);
        assertEquals("-xarg1", result[0]);
    }

    // test processNonOptionToken method
    @Test
    public void testProcessNonOptionToken() {
        String value = "value";

        parser.processNonOptionToken(value);

        String[] result = (String[]) ((List) parser.getTokens()).toArray();
        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("value", result[1]);
    }

}