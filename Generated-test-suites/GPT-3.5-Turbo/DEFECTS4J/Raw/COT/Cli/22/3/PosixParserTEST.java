import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value", "--foo", "--bar=value", "input.txt"};

        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(7, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
        assertEquals("--foo", flattened[3]);
        assertEquals("--bar", flattened[4]);
        assertEquals("value", flattened[5]);
        assertEquals("input.txt", flattened[6]);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "-b", "value"};
        Iterator iter = Arrays.asList(arguments).iterator();
        List tokens = new ArrayList();

        parser.gobble(iter);

        assertEquals(0, tokens.size());

        tokens.add("-a");
        tokens.add("-b");
        tokens.add("value");

        parser.eatTheRest = true;
        parser.gobble(iter);

        assertEquals(3, tokens.size());
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        String value = "input.txt";
        List tokens = new ArrayList();

        parser.processNonOptionToken(value);

        assertEquals("--", tokens.get(0));
        assertEquals(value, tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "-a";
        List tokens = new ArrayList();
        Options options = new Options();
        options.addOption(new Option("a", "alpha"));

        parser.processOptionToken(token, false);

        assertEquals(token, tokens.get(0));
        assertEquals(1, tokens.size());

        tokens.clear();

        parser.options = options;
        parser.processOptionToken(token, true);

        assertEquals(token, tokens.get(0));
        assertEquals(1, tokens.size());

        tokens.clear();

        parser.options = new Options();
        parser.processOptionToken(token, false);

        assertEquals(token, tokens.get(0));
        assertEquals(1, tokens.size());
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String token = "-abf";
        Options options = new Options();
        options.addOption(new Option("a", "alpha"));
        options.addOption(new Option("b", "bravo"));
        options.addOption(new Option("f", false, "flag"));
        List tokens = new ArrayList();

        parser.burstToken(token, false);

        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertEquals("-f", tokens.get(2));
        assertEquals(3, tokens.size());

        tokens.clear();
        parser.burstToken(token, true);

        assertEquals("--", tokens.get(0));
        assertEquals("-abf", tokens.get(1));
        assertEquals(2, tokens.size());
    }
}