import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser posixParser = new PosixParser();
        posixParser.init();
        assertEquals(false, posixParser.eatTheRest);
        assertEquals(0, posixParser.tokens.size());
        assertNull(posixParser.currentOption);
    }

    @Test
    public void testFlatten() {
        PosixParser posixParser = new PosixParser();
        Options options = new Options();
        options.addOption("a", "apple", false, "apple option");
        options.addOption("b", "banana", true, "banana option");

        String[] arguments = {"-a", "--", "-b", "value"};

        String[] result = posixParser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value", result[3]);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser posixParser = new PosixParser();
        posixParser.processSingleHyphen("-");
        assertEquals(1, posixParser.tokens.size());
        assertEquals("-", posixParser.tokens.get(0));
    }
}