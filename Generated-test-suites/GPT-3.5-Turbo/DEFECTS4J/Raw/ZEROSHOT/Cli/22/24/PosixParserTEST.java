import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertEquals(parser.tokens.size(), 0);
        assertFalse(parser.eatTheRest);
        assertNull(parser.options);
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--foo", "bar", "-t", "--", "--baz", "qux"};

        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertEquals(flattenedArguments.length, 7);
        assertEquals(flattenedArguments[0], "--foo");
        assertEquals(flattenedArguments[1], "bar");
        assertEquals(flattenedArguments[2], "-t");
        assertEquals(flattenedArguments[3], "--");
        assertEquals(flattenedArguments[4], "--baz");
        assertEquals(flattenedArguments[5], "qux");
        assertEquals(flattenedArguments[6], "--");
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator<String> iterator = Arrays.asList("--foo", "bar", "-t").iterator();
        parser.eatTheRest = true;

        parser.gobble(iterator);

        assertEquals(parser.tokens.size(), 3);
        assertEquals(parser.tokens.get(0), "--foo");
        assertEquals(parser.tokens.get(1), "bar");
        assertEquals(parser.tokens.get(2), "-t");
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        parser.processNonOptionToken("value");

        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "--");
        assertEquals(parser.tokens.get(1), "value");
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        parser.options = options;

        parser.processOptionToken("-f", true);

        assertEquals(parser.tokens.size(), 1);
        assertEquals(parser.tokens.get(0), "-f");
        assertFalse(parser.eatTheRest);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg(true).build());
        options.addOption(Option.builder("b").build());
        parser.options = options;

        parser.burstToken("-ab", true);

        assertEquals(parser.tokens.size(), 2);
        assertEquals(parser.tokens.get(0), "-a");
        assertEquals(parser.tokens.get(1), "-b");
    }
}