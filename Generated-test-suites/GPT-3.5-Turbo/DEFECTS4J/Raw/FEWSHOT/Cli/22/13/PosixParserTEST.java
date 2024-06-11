import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"--foo", "bar", "-baz", "--", "arg1", "arg2"};
        boolean stopAtNonOption = true;

        String[] result = PosixParser.flatten(options, arguments, stopAtNonOption);

        assertEquals(6, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("-baz", result[2]);
        assertEquals("--", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        String value = "foo";

        parser.processNonOptionToken(value, tokens);

        assertEquals(2, tokens.size());
        assertEquals("--", tokens.get(0));
        assertEquals("foo", tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        String token = "-f";
        boolean stopAtNonOption = true;

        parser.processOptionToken(token, tokens, stopAtNonOption);

        assertEquals(1, tokens.size());
        assertEquals("-f", tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        List<String> tokens = new ArrayList<>();
        String token = "-foo";
        boolean stopAtNonOption = true;

        parser.burstToken(token, options, tokens, stopAtNonOption);

        assertEquals(1, tokens.size());
        assertEquals("-foo", tokens.get(0));
    }
    
    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        List<String> tokens = new ArrayList<>();
        Iterator iter = Arrays.asList("arg1", "arg2").iterator();

        parser.gobble(iter, tokens);

        assertEquals(2, tokens.size());
        assertEquals("arg1", tokens.get(0));
        assertEquals("arg2", tokens.get(1));
    }

}