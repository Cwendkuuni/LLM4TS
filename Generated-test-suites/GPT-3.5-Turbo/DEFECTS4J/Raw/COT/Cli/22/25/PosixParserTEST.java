
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser = new PosixParser();

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"--foo", "-b", "bar", "-abc", "value", "-def"};
        String[] expected = {"--foo", "-b", "bar", "-a", "-b", "-c", "value", "-d", "-e", "-f"};
        String[] actual = posixParser.flatten(options, arguments, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGobble() {
        Iterator iter = new ArrayList<String>().iterator();
        posixParser.gobble(iter);
    }

    @Test
    public void testProcessNonOptionToken() {
        String value = "nonOption";
        posixParser.processNonOptionToken(value);
        assertTrue(posixParser.getEatTheRest());
        assertEquals(2, posixParser.getTokens().size());
        assertEquals("--", posixParser.getTokens().get(0));
        assertEquals(value, posixParser.getTokens().get(1));
    }

    @Test
    public void testProcessOptionToken() {
        String token = "-a";
        boolean stopAtNonOption = true;
        posixParser.setOptions(new Options());
        posixParser.processOptionToken(token, stopAtNonOption);
        assertEquals(1, posixParser.getTokens().size());
        assertEquals(token, posixParser.getTokens().get(0));
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        boolean stopAtNonOption = true;
        posixParser.setOptions(new Options());
        posixParser.burstToken(token, stopAtNonOption);
        assertEquals(4, posixParser.getTokens().size());
        assertEquals("-a", posixParser.getTokens().get(0));
        assertEquals("-b", posixParser.getTokens().get(1));
        assertEquals("-c", posixParser.getTokens().get(2));
        assertEquals(token, posixParser.getTokens().get(3));
    }

}

