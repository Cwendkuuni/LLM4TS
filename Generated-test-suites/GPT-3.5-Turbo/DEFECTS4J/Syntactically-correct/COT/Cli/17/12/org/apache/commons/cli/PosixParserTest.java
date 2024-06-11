
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    private PosixParser parser = new PosixParser();

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "--c=value", "arg1", "--", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(7, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("--c", result[2]);
        assertEquals("value", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("--", result[5]);
        assertEquals("arg2", result[6]);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "-d", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-d", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlattenWithStopAtNonOptionFalse() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "-d", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(5, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-d", result[2]);
        assertEquals("arg1", result[3]);
        assertEquals("arg2", result[4]);
    }

    @Test
    public void testFlattenWithHyphen() {
        Options options = new Options();
        String[] arguments = {"-a", "-"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-", result[1]);
    }
}
