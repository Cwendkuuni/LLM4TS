import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class PosixParserTest {

    private PosixParser parser = new PosixParser();

    @Test
    public void testFlatten_ZeroArguments_ReturnsEmptyArray() {
        Options options = new Options();
        String[] arguments = new String[0];
        String[] expected = new String[0];
        
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionEnabled_ReturnsArgumentsBeforeNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] arguments = {"-a", "arg1", "nonOption", "-b", "arg2"};
        String[] expected = {"-a", "arg1"};
        
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOptionDisabled_ReturnsAllArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] arguments = {"-a", "arg1", "nonOption", "-b", "arg2"};
        String[] expected = {"-a", "arg1", "nonOption", "-b", "arg2"};
        
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_ValidTokenWithArguments_ReturnsBurstTokensWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String token = "-abvalues";
        String[] expected = {"-a", "values", "-b", "values"};
        
        parserOptions(parser, options);
        parserBurstToken(parser, token, false);

        String[] result = parser.getTokens();

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_ValidTokenWithoutArguments_ReturnsBurstTokensWithoutArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String token = "-ab";
        String[] expected = {"-a", "-b"};
        
        parserOptions(parser, options);
        parserBurstToken(parser, token, false);

        String[] result = parser.getTokens();

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_InvalidToken_StopAtNonOptionEnabled_ReturnsSpecialTokenAndRest() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String token = "-c";
        String[] expected = {"--", "-c"};
        
        parserOptions(parser, options);
        parserBurstToken(parser, token, true);

        String[] result = parser.getTokens();

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_InvalidToken_StopAtNonOptionDisabled_ReturnsInvalidToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String token = "-c";
        String[] expected = {"-c"};
        
        parserOptions(parser, options);
        parserBurstToken(parser, token, false);

        String[] result = parser.getTokens();

        assertArrayEquals(expected, result);
    }

    private void parserOptions(PosixParser parser, Options options) {
        try {
            CommandLineParser cmdLineParser = new PosixParser();
            CommandLine cmdLine = cmdLineParser.parse(options, new String[0]);
            parser.setOptions(cmdLine.getOptions());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    private void parserBurstToken(PosixParser parser, String token, boolean stopAtNonOption) {
        try {
            parser.burstToken(token, stopAtNonOption);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
}