
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "value", "-b", "--", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(6, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("--", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
    }

    @Test
    public void testFlatten_noOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "value", "arg1", "--", "-b"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlatten_specialPropertiesOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-Dproperty=value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }
}
