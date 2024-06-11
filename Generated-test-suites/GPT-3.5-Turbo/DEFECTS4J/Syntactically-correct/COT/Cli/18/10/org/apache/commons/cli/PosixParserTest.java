import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-abc", "value1", "-d", "--", "value2"};
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals("-abc", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-d", result[2]);
        assertEquals("--", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();

        parser.processSingleHyphen("-");

        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("a", true, "option a");
        options.addOption(option);

        parser.options = options;
        parser.processOptionToken("-a", false);

        assertEquals("-a", parser.tokens.get(0));
        assertEquals(option, parser.currentOption);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option = new Option("a", true, "option a");
        option.setArgs(2);
        options.addOption(option);

        parser.options = options;
        parser.process("value");

        assertEquals("value", parser.tokens.get(0));
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option optionA = new Option("a", true, "option a");
        optionA.setArgs(2);
        options.addOption(optionA);
        Option optionB = new Option("b", true, "option b");
        optionB.setArgs(2);
        options.addOption(optionB);

        parser.options = options;
        parser.burstToken("-abc", false);

        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));
        assertEquals(optionA, parser.currentOption);
    }
}