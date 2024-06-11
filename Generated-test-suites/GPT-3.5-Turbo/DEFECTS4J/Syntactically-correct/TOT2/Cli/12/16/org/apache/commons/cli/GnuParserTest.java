import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;

    @Before
    public void setUp() {
        parser = new GnuParser();
    }

    @Test
    public void testFlatten_basicFlatten() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Option alpha");
        options.addOption("b", "bravo", true, "Option bravo");

        String[] arguments = {"-a", "arg1", "--", "-b", "arg2", "-c", "arg3"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("Unexpected length of flattened arguments", 5, result.length);
        assertEquals("Incorrect parsing of option -a", "-a", result[0]);
        assertEquals("Incorrect parsing of option -b", "arg2", result[3]);
        assertEquals("Incorrect parsing of non-option token", "-c", result[4]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();

        String[] arguments = {"-a", "arg1", "--", "-b", "arg2", "-c", "arg3"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("Unexpected length of flattened arguments", 3, result.length);
        assertEquals("Incorrect parsing of option -a", "-a", result[0]);
        assertEquals("Incorrect parsing of non-option token", "-", result[1]);
        assertEquals("Incorrect parsing of non-option token", "arg1", result[2]);
    }

    @Test
    public void testFlatten_specialPropertiesOption() {
        Options options = new Options();
        options.addOption("D", true, "Special properties option");

        String[] arguments = {"-Dproperty=value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("Unexpected length of flattened arguments", 2, result.length);
        assertEquals("Incorrect parsing of special properties option", "-D", result[0]);
        assertEquals("Incorrect parsing of special properties value", "property=value", result[1]);
    }
}