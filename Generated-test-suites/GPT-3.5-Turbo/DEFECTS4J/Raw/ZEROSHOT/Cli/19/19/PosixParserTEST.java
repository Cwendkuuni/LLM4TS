import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a", "-b", "value", "--option", "--arg=value", "argument"};
        boolean stopAtNonOption = false;
        String[] expected = new String[]{"-a", "-b", "value", "--option", "--arg", "value", "argument"};

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();

        parser.init();

        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        String token = "-a";
        boolean stopAtNonOption = false;
        String[] expected = new String[]{"-a"};

        parser.options = options;
        parser.processOptionToken(token, stopAtNonOption);

        assertEquals(options.getOption("a"), parser.currentOption);
        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));

        token = "-b";
        expected = new String[]{"-a", "-b"};

        parser.processOptionToken(token, stopAtNonOption);

        assertEquals(options.getOption("b"), parser.currentOption);
        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()])) ;
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String token = "-abc";
        boolean stopAtNonOption = false;
        String[] expected = new String[]{"-a", "-b", "-c"};

        parser.options = options;
        parser.burstToken(token, stopAtNonOption);

        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));

        stopAtNonOption = true;
        token = "-abcd";
        expected = new String[]{"-a", "-b", "-c", "-abcd"};

        parser.init();
        parser.burstToken(token, stopAtNonOption);

        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));

        stopAtNonOption = false;
        token = "-abc";
        expected = new String[]{"-a", "-b", "-c"};

        parser.init();
        parser.burstToken(token, stopAtNonOption);

        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()])); 
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");
        String token = "-a";
        String[] expected = new String[]{"-a"};

        parser.options = options;
        parser.currentOption = options.getOption("a");
        parser.process(token);

        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));

        token = "value";
        expected = new String[]{"-a"};

        parser.currentOption = options.getOption("b");
        parser.process(token);

        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));

        token = "value";
        expected = new String[]{"--", "value"};

        parser.currentOption = null;
        parser.eatTheRest = true;
        parser.process(token);

        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()])); 
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList(new String[]{"token1", "token2", "token3"}).iterator();
        String[] expected = new String[]{"token1", "token2", "token3"};

        parser.eatTheRest = false;
        parser.tokens.clear();
        parser.gobble(iter);

        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));

        parser.eatTheRest = true;
        parser.tokens.clear();
        parser.gobble(iter);

        assertArrayEquals(expected, parser.tokens.toArray(new String[parser.tokens.size()]));
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = new String[]{"-a", "-b", "value", "--option", "--arg=value", "argument"};
        boolean stopAtNonOption = true;
        String[] expected = new String[]{"-a", "-b", "value", "--option", "--arg=value", "argument"};

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertArrayEquals(expected, result);
    }

}