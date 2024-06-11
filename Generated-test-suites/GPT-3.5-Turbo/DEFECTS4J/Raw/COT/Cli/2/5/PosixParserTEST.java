import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    PosixParser parser = new PosixParser();
    Options options = new Options();

    //Test Method: init()
    @Test
    public void testInit() {
        parser.init();
        assertNotNull(parser.tokens);
        assertEquals(0, parser.tokens.size());
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
    }

    //Test Method: flatten(Options, String[], boolean)
    @Test
    public void testFlatten() {

        options.addOption("a", false, "a sample option");
        options.addOption("b", true, "another sample option");
        options.addOption(Option.builder("c").hasArgs().build());

        String[] args = {"-a", "-b", "value", "-c", "arg1", "arg2"};

        String[] result = parser.flatten(options, args, false);
        assertNotNull(result);
        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
        assertEquals("--", result[6]);
    }

    //Test Method: gobble(Iterator)
    @Test
    public void testGobble() {

        parser.tokens.add("token1");
        parser.tokens.add("token2");

        String[] input = {"arg1", "arg2"};
        parser.gobble(Arrays.asList(input).iterator());

        assertEquals(4, parser.tokens.size());
        assertEquals("token1", parser.tokens.get(0));
        assertEquals("token2", parser.tokens.get(1));
        assertEquals("arg1", parser.tokens.get(2));
        assertEquals("arg2", parser.tokens.get(3));
    }

    //Test Method: process(String)
    @Test
    public void testProcess() {

        options.addOption("a", false, "a sample option");
        options.addOption("b", true, "another sample option");
        options.addOption(Option.builder("c").hasArgs().build());

        parser.currentOption = options.getOption("b");
        parser.tokens.clear();

        parser.process("value");

        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));

        parser.currentOption = options.getOption("a");
        parser.tokens.clear();

        parser.process("value");

        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);

        parser.currentOption = null;
        parser.tokens.clear();

        parser.process("value");

        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));

    }

    //Test Method: processSingleHyphen(String)
    @Test
    public void testProcessSingleHyphen() {

        parser.tokens.clear();
        parser.processSingleHyphen("-");

        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }

    //Test Method: processOptionToken(String, boolean)
    @Test
    public void testProcessOptionToken() {

        options.addOption("a", false, "a sample option");
        options.addOption("b", true, "another sample option");
        options.addOption(Option.builder("c").hasArgs().build());

        parser.currentOption = null;
        parser.tokens.clear();

        parser.processOptionToken("a", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("a", parser.tokens.get(0));
        assertNotNull(parser.currentOption);
        assertEquals("a", parser.currentOption.getOpt());

        parser.currentOption = null;
        parser.tokens.clear();

        parser.processOptionToken("d", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-d", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.currentOption = null;
        parser.tokens.clear();

        parser.processOptionToken("a", true);

        assertTrue(parser.eatTheRest);
        assertNull(parser.currentOption);

    }

    //Test Method: burstToken(String, boolean)
    @Test
    public void testBurstToken() {

        options.addOption("a", false, "a sample option");

        parser.tokens.clear();
        parser.currentOption = null;

        parser.burstToken("-ab", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.tokens.clear();
        parser.currentOption = null;

        parser.burstToken("-ab", true);

        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertNull(parser.currentOption);

        parser.tokens.clear();
        parser.currentOption = null;

        parser.burstToken("-xyz", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-x", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.tokens.clear();
        parser.currentOption = null;

        parser.burstToken("-xyz", true);

        assertEquals(3, parser.tokens.size());
        assertEquals("-x", parser.tokens.get(0));
        assertEquals("-y", parser.tokens.get(1));
        assertEquals("-z", parser.tokens.get(2));
        assertNull(parser.currentOption);

    }

}