import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setup() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = new Options();
        String[] args = {"-a", "value1", "arg1"};
        String[] result = posixParser.flatten(options, args, true);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlatten_DoNotStopAtNonOption() {
        Options options = new Options();
        String[] args = {"-a", "value1", "arg1"};
        String[] result = posixParser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SpecialToken() {
        Options options = new Options();
        String[] args = {"--option=value"};
        String[] result = posixParser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testProcessOptionToken_ValidOption() {
        Options options = new Options();
        Option option = new Option("a", "option");
        options.addOption(option);

        posixParser.processOptionToken("-a", false);

        assertEquals(option, posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("-a", posixParser.getTokens().get(0));
    }

    @Test
    public void testProcessOptionToken_InvalidOption_StopAtNonOption() {
        Options options = new Options();

        posixParser.processOptionToken("-a", true);

        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("-a", posixParser.getTokens().get(0));
        assertTrue(posixParser.isEatTheRest());
    }

    @Test
    public void testProcessOptionToken_InvalidOption_DoNotStopAtNonOption() {
        Options options = new Options();

        posixParser.processOptionToken("-a", false);

        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("-a", posixParser.getTokens().get(0));
        assertFalse(posixParser.isEatTheRest());
    }

    @Test
    public void testProcess_ArgumentWithOption() {
        Options options = new Options();
        Option option = new Option("a", "option");
        options.addOption(option);
        posixParser.setCurrentOption(option);

        posixParser.process("value1");

        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("value1", posixParser.getTokens().get(0));
    }

    @Test
    public void testProcess_ArgumentWithoutOption_StopAtNonOption() {
        Options options = new Options();

        posixParser.process("arg1");

        assertNull(posixParser.getCurrentOption());
        assertEquals(2, posixParser.getTokens().size());
        assertEquals("--", posixParser.getTokens().get(0));
        assertEquals("arg1", posixParser.getTokens().get(1));
        assertTrue(posixParser.isEatTheRest());
    }

    @Test
    public void testProcess_ArgumentWithoutOption_DoNotStopAtNonOption() {
        Options options = new Options();

        posixParser.process("arg1");

        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("arg1", posixParser.getTokens().get(0));
        assertFalse(posixParser.isEatTheRest());
    }

    @Test
    public void testBurstToken_ValidOptions() {
        Options options = new Options();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        options.addOption(optionA);
        options.addOption(optionB);

        posixParser.burstToken("-ab", false);

        assertEquals(2, posixParser.getTokens().size());
        assertEquals("-a", posixParser.getTokens().get(0));
        assertEquals("-b", posixParser.getTokens().get(1));
    }

    @Test
    public void testBurstToken_InvalidOptions_StopAtNonOption() {
        Options options = new Options();

        posixParser.burstToken("-ab", true);

        assertNull(posixParser.getCurrentOption());
        assertEquals(2, posixParser.getTokens().size());
        assertEquals("--", posixParser.getTokens().get(0));
        assertEquals("b", posixParser.getTokens().get(1));
        assertTrue(posixParser.isEatTheRest());
    }

    @Test
    public void testBurstToken_InvalidOptions_DoNotStopAtNonOption() {
        Options options = new Options();

        posixParser.burstToken("-ab", false);

        assertNull(posixParser.getCurrentOption());
        assertEquals(1, posixParser.getTokens().size());
        assertEquals("-ab", posixParser.getTokens().get(0));
        assertFalse(posixParser.isEatTheRest());
    }

    // TODO: Add more test cases for other methods in the PosixParser class

}