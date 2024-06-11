import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_StopAtNonOption_True() {
        String[] args = {"-a", "arg1", "-b", "arg2", "nonOption"};
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, true);

        String[] expected = {"-a", "-b", "arg2", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        String[] args = {"-a", "arg1", "-b", "arg2", "nonOption"};
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        PosixParser parser = new PosixParser();

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "arg1", "-b", "arg2", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();

        parser.processNonOptionToken("nonOption");

        List<String> tokens = parser.getTokens();
        assertTrue(tokens.contains("--"));
        assertTrue(tokens.contains("nonOption"));
    }

    @Test
    public void testProcessOptionToken_Exists() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.setOptions(options);

        parser.processOptionToken("-a", true);

        List<String> tokens = parser.getTokens();
        assertTrue(tokens.contains("-a"));
    }

    @Test
    public void testProcessOptionToken_DoesNotExist_StopAtNonOption_True() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.setOptions(options);

        parser.processOptionToken("-b", true);

        List<String> tokens = parser.getTokens();
        assertEquals(0, tokens.size());
        assertTrue(parser.getEatTheRest());
    }

    @Test
    public void testProcessOptionToken_DoesNotExist_StopAtNonOption_False() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.setOptions(options);

        parser.processOptionToken("-b", false);

        List<String> tokens = parser.getTokens();
        assertTrue(tokens.contains("-b"));
    }

    @Test
    public void testBurstToken_OptionExists() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.setOptions(options);

        parser.burstToken("-abc", true);

        List<String> tokens = parser.getTokens();
        assertTrue(tokens.contains("-a"));
        assertTrue(tokens.contains("-b"));
        assertTrue(tokens.contains("-c"));
    }

    @Test
    public void testBurstToken_OptionExists_WithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option a");
        parser.setOptions(options);

        parser.burstToken("-aarg1", true);

        List<String> tokens = parser.getTokens();
        assertTrue(tokens.contains("-a"));
        assertTrue(tokens.contains("arg1"));
    }

    @Test
    public void testBurstToken_OptionDoesNotExist_StopAtNonOption_True() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.setOptions(options);

        parser.burstToken("-bcd", true);

        List<String> tokens = parser.getTokens();
        assertEquals(2, tokens.size());
        assertTrue(tokens.contains("--"));
        assertTrue(tokens.contains("bcd"));
    }

    @Test
    public void testBurstToken_OptionDoesNotExist_StopAtNonOption_False() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        parser.setOptions(options);

        parser.burstToken("-bcd", false);

        List<String> tokens = parser.getTokens();
        assertTrue(tokens.contains("-bcd"));
    }
}