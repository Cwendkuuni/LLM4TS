import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--option", "arg2"};

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("-a", flattened[0]);
        assertEquals("arg1", flattened[1]);
        assertEquals("--option", flattened[2]);
        assertEquals("arg2", flattened[3]);
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();

        parser.processNonOptionToken("arg1");

        List tokens = parser.getTokens();

        assertEquals("--", tokens.get(0));
        assertEquals("arg1", tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");

        parser.setOptions(options);

        parser.processOptionToken("-a", false);

        List tokens = parser.getTokens();

        assertEquals("-a", tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");

        parser.setOptions(options);

        parser.burstToken("-ab", false);

        List tokens = parser.getTokens();

        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
    }

    @Test
    public void testBurstTokenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");

        parser.setOptions(options);

        parser.burstToken("-ab", true);

        List tokens = parser.getTokens();

        assertEquals("--", tokens.get(0));
        assertEquals("-ab", tokens.get(1));
    }

}