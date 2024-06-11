import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"--opt1", "-opt2", "arg1", "--", "-arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(5, result.length);
        assertEquals("--opt1", result[0]);
        assertEquals("-opt2", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("--", result[3]);
        assertEquals("-arg2", result[4]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"--opt1", "--", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("--opt1", result[0]);
        assertEquals("--", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlattenNoOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
        assertEquals("arg3", result[2]);
    }

    @Test
    public void testFlattenOnlyOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"--opt1", "-opt2", "--opt3"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("--opt1", result[0]);
        assertEquals("-opt2", result[1]);
        assertEquals("--opt3", result[2]);
    }

    @Test
    public void testFlattenSpecialPropertiesOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"-Dproperty=value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlattenSpecialPropertiesOptionWithHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"-D-property=value"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-D", result[0]);
        assertEquals("-property", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlattenStopAtNonOptionWithHyphen() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = new String[]{"-arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-arg1", result[0]);
        assertEquals("arg2", result[1]);
    }
}