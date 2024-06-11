import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "-b=value", "--option", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("Incorrect number of tokens", 6, flattened.length);

        assertEquals("Token 1 is incorrect", "-a", flattened[0]);
        assertEquals("Token 2 is incorrect", "-b", flattened[1]);
        assertEquals("Token 3 is incorrect", "value", flattened[2]);
        assertEquals("Token 4 is incorrect", "--option", flattened[3]);
        assertEquals("Token 5 is incorrect", "arg1", flattened[4]);
        assertEquals("Token 6 is incorrect", "arg2", flattened[5]);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.burstToken("-abc", false);

        String[] flattened = parser.flatten(options, new String[0], false);

        assertEquals("Incorrect number of tokens", 3, flattened.length);

        assertEquals("Token 1 is incorrect", "-a", flattened[0]);
        assertEquals("Token 2 is incorrect", "-b", flattened[1]);
        assertEquals("Token 3 is incorrect", "-c", flattened[2]);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.processOptionToken("-a", false);

        String[] flattened = parser.flatten(options, new String[0], false);

        assertEquals("Incorrect number of tokens", 1, flattened.length);

        assertEquals("Token is incorrect", "-a", flattened[0]);
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.process("-a");

        String[] flattened = parser.flatten(options, new String[0], true);

        assertEquals("Incorrect number of tokens", 2, flattened.length);

        assertEquals("Token 1 is incorrect", "--", flattened[0]);
        assertEquals("Token 2 is incorrect", "-a", flattened[1]);
    }
}