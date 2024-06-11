import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Test
    public void testInit() {
        posixParser = new PosixParser();
        posixParser.init();
        List<String> tokens = posixParser.getTokens();
        assertTrue(tokens.isEmpty());
        assertFalse(posixParser.isEatTheRest());
    }

    @Test
    public void testFlatten() {
        posixParser = new PosixParser();
        Options options = new Options();
        options.addOption("f", "foo", false, "foo option");
        options.addOption("b", "bar", true, "bar option");

        String[] arguments = {"--foo", "-b", "testValue"};
        String[] flattenedArguments = posixParser.flatten(options, arguments, true);
        assertEquals(flattenedArguments.length, 4);
        assertEquals(flattenedArguments[0], "--");
        assertEquals(flattenedArguments[1], "--foo");
        assertEquals(flattenedArguments[2], "-b");
        assertEquals(flattenedArguments[3], "testValue");
    }

    @Test
    public void testGobble() {
        posixParser = new PosixParser();
        posixParser.init();
        List<String> tokens = posixParser.getTokens();
        tokens.add("token1");
        tokens.add("token2");
        tokens.add("token3");

        Iterator<String> iterator = Arrays.asList("token1", "token2", "token3", "token4").iterator();
        posixParser.gobble(iterator);

        assertEquals(tokens.size(), 5);
        assertEquals(tokens.get(4), "token4");
    }

    @Test
    public void testProcessNonOptionToken() {
        posixParser = new PosixParser();
        posixParser.init();
        List<String> tokens = posixParser.getTokens();
        posixParser.processNonOptionToken("testValue");

        assertEquals(tokens.size(), 2);
        assertEquals(tokens.get(0), "--");
        assertEquals(tokens.get(1), "testValue");
    }

    @Test
    public void testProcessOptionToken() {
        posixParser = new PosixParser();
        posixParser.init();
        List<String> tokens = posixParser.getTokens();
        Options options = new Options();
        options.addOption("f", "foo", false, "foo option");
        options.addOption("b", "bar", true, "bar option");

        posixParser.setOptions(options);
        posixParser.processOptionToken("--foo", true);
        posixParser.processOptionToken("-b", true);

        assertEquals(tokens.size(), 3);
        assertEquals(tokens.get(0), "--foo");
        assertEquals(tokens.get(1), "-b");
        assertEquals(tokens.get(2), "true");
    }

    @Test
    public void testBurstToken() {
        posixParser = new PosixParser();
        posixParser.init();
        List<String> tokens = posixParser.getTokens();
        Options options = new Options();
        options.addOption("f", "foo", false, "foo option");

        posixParser.setOptions(options);
        posixParser.burstToken("-fb", true);

        assertEquals(tokens.size(), 2);
        assertEquals(tokens.get(0), "-f");
        assertEquals(tokens.get(1), "b");
    }
}