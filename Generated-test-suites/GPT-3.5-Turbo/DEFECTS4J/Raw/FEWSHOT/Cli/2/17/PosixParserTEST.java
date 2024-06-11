import static org.junit.Assert.*;
import org.apache.commons.cli.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PosixParserTest {
    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertEquals(new ArrayList(), parser.tokens);
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        PosixParser parser = new PosixParser();
        String[] arguments = {"-ab", "-c", "value"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("--", result[2]);
        assertEquals("-c", result[3]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[] {"arg1", "arg2"}).iterator();

        parser.gobble(iter);
        assertEquals(0, parser.tokens.size());

        parser.eatTheRest = true;
        parser.gobble(iter);
        assertEquals(2, parser.tokens.size());
        assertEquals("arg1", parser.tokens.get(0));
        assertEquals("arg2", parser.tokens.get(1));
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.currentOption = Option.builder("a").hasArg().build();
        parser.process("value");
        assertEquals("value", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.currentOption = Option.builder("a").hasArgs().build();
        parser.process("value");
        assertEquals("value", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));
        assertEquals("value", parser.tokens.get(3));
        assertNull(parser.currentOption);

        parser.currentOption = null;
        parser.process("value");
        assertEquals("--", parser.tokens.get(5));
        assertEquals("value", parser.tokens.get(6));
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.processOptionToken("a", true);
        assertEquals("a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);

        parser.processOptionToken("b", true);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.burstToken("-ab", true);
        assertEquals("-a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
        assertEquals("-b", parser.tokens.get(1));

        parser.burstToken("-abc", true);
        assertEquals("-a", parser.tokens.get(2));
        assertNotNull(parser.currentOption);
        assertEquals("-b", parser.tokens.get(3));
        assertEquals("-c", parser.tokens.get(4));

        parser.burstToken("-abc", false);
        assertEquals("-a", parser.tokens.get(5));
        assertNotNull(parser.currentOption);
        assertEquals("-b", parser.tokens.get(6));
        assertEquals("-c", parser.tokens.get(7));
    }
}