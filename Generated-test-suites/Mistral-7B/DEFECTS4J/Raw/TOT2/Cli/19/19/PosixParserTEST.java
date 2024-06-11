import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlattenNormalCase() {
        String[] args = {"-a", "--optionB=valueB", "-c", "valueC", "--", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-a", "--optionB", "valueB", "-c", "valueC", "--optionD", "valueD"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoOptionsCase() {
        String[] args = {"--", "argument1", "argument2", "argument3"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"--argument1", "--argument2", "--argument3"};
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"-x", "--optionB=valueB", "-c", "valueC", "--", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        assertEquals("Unknown option: x", parser.getLastError());
    }

    @Test
    public void testBurstTokenSingleOption() {
        String token = "-a";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        String[] expected = {"-a"};
        assertArrayEquals(expected, parser.getTokensToTest());
    }

    @Test
    public void testBurstTokenLongOption() {
        String token = "-longOption";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        String[] expected = {"-longOption"};
        assertArrayEquals(expected, parser.getTokensToTest());
    }

    @Test
    public void testBurstTokenLongOptionWithValue() {
        String token = "--longOption=value";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        String[] expected = {"--longOption", "value"};
        assertArrayEquals(expected, parser.getTokensToTest());
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        String token = "-a -b --longOption value";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        String[] expected = {"-a", "-b", "--longOption", "value"};
        assertArrayEquals(expected, parser.getTokensToTest());
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        String token = "-a -b --";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);

        String[] expected = {"-a", "-b", "--"};
        assertArrayEquals(expected, parser.getTokensToTest());
    }

    private String[] getTokensToTest() {
        PosixParser parser = new PosixParser();
        parser.burstToken("", false);
        return (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
    }

}