import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "This is option a");

        String[] args = {"-a", "file1", "file2"};
        String[] result = posixParser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("file1", result[1]);
        assertEquals("file2", result[2]);
    }

    @Test
    public void testFlatten_doNotStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "This is option a");

        String[] args = {"-a", "file1", "file2"};
        String[] result = posixParser.flatten(options, args, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("file1", result[1]);
        assertEquals("file2", result[2]);
        assertEquals("--", result[3]);
    }

    @Test
    public void testProcess() {
        posixParser.init();
        posixParser.process("value");

        List<String> tokens = posixParser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("value", tokens.get(0));
        assertNull(posixParser.getCurrentOption());
        assertTrue(posixParser.isEatTheRest());
    }

    @Test
    public void testProcessSingleHyphen() {
        posixParser.init();
        posixParser.processSingleHyphen("-");

        List<String> tokens = posixParser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("-", tokens.get(0));
        assertNull(posixParser.getCurrentOption());
        assertFalse(posixParser.isEatTheRest());
    }

    @Test
    public void testProcessOptionToken_found() {
        Options options = new Options();
        Option option = new Option("a", false, "This is option a");
        options.addOption(option);

        posixParser.init();
        posixParser.setOptions(options);
        posixParser.processOptionToken("-a", true);

        List<String> tokens = posixParser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals(option, posixParser.getCurrentOption());
    }

    @Test
    public void testProcessOptionToken_notFound_stopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", false, "This is option a");
        options.addOption(option);

        posixParser.init();
        posixParser.setOptions(options);
        posixParser.processOptionToken("-b", true);

        List<String> tokens = posixParser.getTokens();
        assertEquals(0, tokens.size());
        assertNull(posixParser.getCurrentOption());
        assertTrue(posixParser.isEatTheRest());
    }

    @Test
    public void testProcessOptionToken_notFound_doNotStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", false, "This is option a");
        options.addOption(option);

        posixParser.init();
        posixParser.setOptions(options);
        posixParser.processOptionToken("-b", false);

        List<String> tokens = posixParser.getTokens();
        assertEquals(1, tokens.size());
        assertEquals("-b", tokens.get(0));
        assertNull(posixParser.getCurrentOption());
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option optionA = new Option("a", false, "This is option a");
        Option optionB = new Option("b", false, "This is option b");
        options.addOption(optionA);
        options.addOption(optionB);

        posixParser.init();
        posixParser.setOptions(options);
        posixParser.burstToken("-ab", true);

        List<String> tokens = posixParser.getTokens();
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertNull(posixParser.getCurrentOption());
        assertTrue(posixParser.isEatTheRest());
    }

    @Test
    public void testBurstToken_stopAtNonOption() {
        Options options = new Options();
        Option optionA = new Option("a", false, "This is option a");
        Option optionB = new Option("b", false, "This is option b");
        options.addOption(optionA);
        options.addOption(optionB);

        posixParser.init();
        posixParser.setOptions(options);
        posixParser.burstToken("-ab", true);

        List<String> tokens = posixParser.getTokens();
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertNull(posixParser.getCurrentOption());
        assertTrue(posixParser.isEatTheRest());
    }

    @Test
    public void testGobble() {
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3");

        posixParser.init();
        posixParser.setEatTheRest(true);
        posixParser.gobble(arguments.iterator());

        List<String> tokens = posixParser.getTokens();
        assertEquals(3, tokens.size());
        assertEquals("arg1", tokens.get(0));
        assertEquals("arg2", tokens.get(1));
        assertEquals("arg3", tokens.get(2));
    }
}