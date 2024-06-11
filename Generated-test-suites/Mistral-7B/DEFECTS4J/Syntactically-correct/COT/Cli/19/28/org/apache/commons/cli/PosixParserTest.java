import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"test", "--foo=bar"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(new String[]{"test", "--foo", "bar"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"test", "--foo=bar", "--", "argument"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertEquals(new String[]{"test", "--foo=bar", "--", "argument"}, result);
    }

    @Test
    public void testFlattenSingleDash() {
        String[] args = {"-", "--foo=bar"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(new String[]{"--", "--foo", "bar"}, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        String[] args = {"--", "--foo=bar"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(new String[]{"--foo", "bar"}, result);
    }

    @Test
    public void testFlattenDoubleDashWithValue() {
        String[] args = {"--", "--foo=bar", "value"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertEquals(new String[]{"--foo", "bar", "value"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalid"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.flatten(options, args, false);
    }

    @Test
    public void testProcessOptionToken() {
        String[] args = {"--foo", "--bar=baz"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.processOptionToken("--foo", false);
        assertEquals(new String[]{"--foo"}, parser.tokens.toArray(new String[parser.tokens.size()]));

        parser.init();
        parser.processOptionToken("--bar=baz", false);
        assertEquals(new String[]{"--bar", "baz"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testProcessOptionTokenWithStopAtNonOption() {
        String[] args = {"--foo", "--bar=baz", "--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.processOptionToken("--foo", true);
        assertEquals(new String[]{"--foo", "--"}, parser.tokens.toArray(new String[parser.tokens.size()]));

        parser.init();
        parser.processOptionToken("--bar=baz", true);
        assertEquals(new String[]{"--", "--bar", "baz"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testProcessSingleDash() {
        String[] args = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.init();
        parser.process("-", false);
        assertEquals(new String[]{"--"}, parser.tokens.toArray(new String[parser.tokens.size()]));

        parser.init();
        parser.process("-", true);
        assertEquals(new String[]{"--", "--"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testProcessDoubleDash() {
        String[] args = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.init();
        parser.process("--", false);
        assertEquals(new String[]{"--foo"}, parser.tokens.toArray(new String[parser.tokens.size()]));

        parser.init();
        parser.process("--", true);
        assertEquals(new String[]{"--", "--"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenSingleOption() {
        String[] args = {"--foo"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("--foo", false);
        assertEquals(new String[]{"--foo"}, parser.tokens.toArray(new String[parser.tokens.size()]));

        parser.init();
        parser.burstToken("--foo", true);
        assertEquals(new String[]{"--foo"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenOptionWithValue() {
        String[] args = {"--foo=bar"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("--foo=bar", false);
        assertEquals(new String[]{"--foo", "bar"}, parser.tokens.toArray(new String[parser.tokens.size()]));

        parser.init();
        parser.burstToken("--foo=bar", true);
        assertEquals(new String[]{"--foo", "bar"}, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testBurstTokenInvalidOption() {
        String[] args = {"--invalid"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("--invalid", false);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);

        parser.init();
        parser.burstToken("--invalid", true);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenMultipleOptions() {
        String[] args = {"--foo=bar", "--bar=baz"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("--foo=bar", false);
        assertEquals(new String[]{"--foo", "bar"}, parser.tokens.toArray(new String[parser.tokens.size()]));
        parser.init();

        parser.burstToken("--bar=baz", false);
        assertEquals(new String[]{"--bar", "baz"}, parser.tokens.toArray(new String[parser.tokens.size()]));
        parser.init();
    }

    @Test
    public void testBurstTokenMultipleOptionsWithStopAtNonOption() {
        String[] args = {"--foo=bar", "--bar=baz", "--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("--foo=bar", true);
        assertEquals(new String[]{"--foo", "--"}, parser.tokens.toArray(new String[parser.tokens.size()]));
        parser.init();

        parser.burstToken("--bar=baz", true);
        assertEquals(new String[]{"--", "--bar", "baz"}, parser.tokens.toArray(new String[parser.tokens.size()]));
        parser.init();
    }
}