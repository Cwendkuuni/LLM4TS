import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "arg2", "-b", "--flag", "value"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        
        assertEquals(7, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("arg1", flattenedArguments[1]);
        assertEquals("arg2", flattenedArguments[2]);
        assertEquals("-b", flattenedArguments[3]);
        assertEquals("--", flattenedArguments[4]);
        assertEquals("flag", flattenedArguments[5]);
        assertEquals("value", flattenedArguments[6]);
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
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        parser.currentOption = null;
        parser.processOptionToken("-a", false);
        assertEquals("-a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);

        parser.currentOption = null;
        parser.processOptionToken("-c", false);
        assertNull(parser.currentOption);

        parser.currentOption = null;
        parser.processOptionToken("-c", true);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        parser.currentOption = options.getOption("a");
        parser.process("value");
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.currentOption = null;
        parser.process("value");
        assertTrue(parser.eatTheRest);
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = new ArrayList<String>().iterator();

        parser.eatTheRest = false;
        parser.gobble(iter);
        assertFalse(parser.eatTheRest);

        parser.eatTheRest = true;
        parser.gobble(iter);
        assertFalse(parser.eatTheRest);

        List<String> remainingTokens = new ArrayList<>();
        remainingTokens.add("arg1");
        remainingTokens.add("arg2");
        iter = remainingTokens.iterator();

        parser.eatTheRest = true;
        parser.gobble(iter);
        assertFalse(parser.eatTheRest);
        assertEquals("arg1", parser.tokens.get(0));
        assertEquals("arg2", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        List<String> tokens = new ArrayList<>();
        tokens.add("-abc");
        tokens.add("-d");

        parser.options = options;
        parser.currentOption = null;
        parser.burstToken("-abc", true);
        assertFalse(parser.eatTheRest);
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));

        parser.currentOption = null;
        parser.burstToken("-abc", false);
        assertFalse(parser.eatTheRest);
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("-c", parser.tokens.get(2));

        parser.currentOption = null;
        parser.burstToken("-d", true);
        assertTrue(parser.eatTheRest);
        assertEquals("-d", parser.tokens.get(0));

        parser.currentOption = null;
        parser.burstToken("-d", false);
        assertFalse(parser.eatTheRest);
        assertEquals("-d", parser.tokens.get(0));
    }
}