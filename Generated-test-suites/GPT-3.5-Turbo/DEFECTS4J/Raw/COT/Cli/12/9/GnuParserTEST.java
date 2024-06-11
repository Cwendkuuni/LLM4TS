import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new GnuParser();
        options = new Options();
    }

    @Test
    public void testFlatten_noOptions() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, false);
        assertTrue(result.length == 0);
    }

    @Test
    public void testFlatten_singleOption() {
        options.addOption("a", false, "option a");
        String[] arguments = {"-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertTrue(result.length == 1);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_multipleOptions() {
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        String[] arguments = {"-a", "-b", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertTrue(result.length == 3);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] arguments = {"-a", "nonoption", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertTrue(result.length == 1);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_doubleDash() {
        String[] arguments = {"--", "-a"};
        String[] result = parser.flatten(options, arguments, false);
        assertTrue(result.length == 2);
        assertEquals("--", result[0]);
        assertEquals("-a", result[1]);
    }

    @Test
    public void testFlatten_singleHyphen() {
        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);
        assertTrue(result.length == 1);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_specialPropertiesOption() {
        options.addOption("D", true, "property");
        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertTrue(result.length == 2);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

}