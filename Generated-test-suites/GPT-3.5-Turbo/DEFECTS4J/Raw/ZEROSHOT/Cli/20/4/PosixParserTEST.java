import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        // Test case 1: stopAtNonOption is false
        Options options1 = new Options();
        options1.addOption("a", false, "option a");
        options1.addOption("b", true, "option b");
        options1.addOption("c", true, "option c");

        String[] arguments1 = {"--a", "value1", "-b", "value2", "-c"};
        String[] expected1 = {"--a", "value1", "-b", "value2", "-c"};
        assertArrayEquals(expected1, parser.flatten(options1, arguments1, false));

        // Test case 2: stopAtNonOption is true
        Options options2 = new Options();
        options2.addOption("x", false, "option x");
        options2.addOption("y", true, "option y");

        String[] arguments2 = {"-x", "-y", "value"};
        String[] expected2 = {"-x", "-y", "value"};
        assertArrayEquals(expected2, parser.flatten(options2, arguments2, true));
    }

    @Test
    public void testInit() {
        parser.init();

        assertEquals(false, parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        // Test case 1: option exists
        Options options1 = new Options();
        options1.addOption("a", false, "option a");

        parser.options = options1;

        String token1 = "-a";
        boolean stopAtNonOption1 = false;

        parser.processOptionToken(token1, stopAtNonOption1);
        assertEquals("a", parser.currentOption.getOpt());
        assertEquals("-a", parser.tokens.get(0));

        // Test case 2: option does not exist, stopAtNonOption is true
        Options options2 = new Options();

        parser.options = options2;

        String token2 = "-x";
        boolean stopAtNonOption2 = true;

        parser.processOptionToken(token2, stopAtNonOption2);
        assertEquals(null, parser.currentOption);
        assertEquals(true, parser.eatTheRest);
        assertEquals("-x", parser.tokens.get(1));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        parser.options = options;

        String token1 = "-ab";
        boolean stopAtNonOption1 = false;

        parser.burstToken(token1, stopAtNonOption1);
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));

        String token2 = "-xy";
        boolean stopAtNonOption2 = true;

        parser.burstToken(token2, stopAtNonOption2);
        assertEquals("-x", parser.tokens.get(2));
        assertEquals("-y", parser.tokens.get(3));
    }
}