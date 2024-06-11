import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
    }

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", "c", false, "Option c");

        String[] arguments = {"-a", "value1", "-b", "value2", "--", "value3"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals(7, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("-b", flattened[2]);
        assertEquals("value2", flattened[3]);
        assertEquals("--", flattened[4]);
        assertEquals("value3", flattened[5]);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String token = "-abc";
        parser.burstToken(token, false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-abc", parser.tokens.get(0));
    }
}