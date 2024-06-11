import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testInit() {
        posixParser.init();
        assertEquals(false, posixParser.eatTheRest);
        assertEquals(0, posixParser.tokens.size());
        assertNull(posixParser.currentOption);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option = Option.builder("f").hasArg(false).build();
        options.addOption(option);

        posixParser.options = options;
        posixParser.processOptionToken("-f", false);

        assertNotNull(posixParser.currentOption);
        assertEquals(option, posixParser.currentOption);
    }

    @Test
    public void testProcess() {
        posixParser.currentOption = Option.builder("f").hasArg(false).build();
        posixParser.process("value");

        assertNull(posixParser.currentOption);
        assertEquals(1, posixParser.tokens.size());
        assertEquals("value", posixParser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option = Option.builder("f").hasArg(false).build();
        options.addOption(option);

        posixParser.options = options;
        posixParser.burstToken("-f", false);

        assertEquals(1, posixParser.tokens.size());
        assertEquals("-f", posixParser.tokens.get(0));

        option = Option.builder("a").hasArg(true).build();
        options.addOption(option);

        posixParser.options = options;
        posixParser.burstToken("-af", false);

        assertEquals(3, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
        assertEquals("-f", posixParser.tokens.get(1));
        assertEquals("f", posixParser.tokens.get(2));

        posixParser.init();
        posixParser.burstToken("-afvalue", false);

        assertEquals(2, posixParser.tokens.size());
        assertEquals("-a", posixParser.tokens.get(0));
        assertEquals("fvalue", posixParser.tokens.get(1));
    }

    @Test
    public void testGobble() {
        Iterator iter = Arrays.asList(new String[]{"token1", "token2"}).iterator();
        posixParser.eatTheRest = true;

        posixParser.gobble(iter);

        assertEquals(2, posixParser.tokens.size());
        assertEquals("token1", posixParser.tokens.get(0));
        assertEquals("token2", posixParser.tokens.get(1));
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option = Option.builder("f").hasArg(false).build();
        options.addOption(option);

        String[] arguments = new String[]{"-f", "--", "value1", "value2"};

        String[] flattened = posixParser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"-f", "--", "value1", "value2"}, flattened);
    }

    @After
    public void tearDown() {
        posixParser = null;
    }
}