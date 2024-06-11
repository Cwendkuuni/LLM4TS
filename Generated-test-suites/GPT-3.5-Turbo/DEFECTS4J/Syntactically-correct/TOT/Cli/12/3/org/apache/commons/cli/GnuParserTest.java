import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        options.addOption("a", false, "Option A");
    }

    @Test
    public void testFlatten_OnlyOptions() {
        String[] arguments = {"-a", "-b", "--", "-c"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }
    
    @Test
    public void testFlatten_OptionAndValue() {
        String[] arguments = {"-a", "value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "value", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("nonOption", result[2]);
    }
    
    @Test
    public void testFlatten_HyphenArgument() {
        String[] arguments = {"-", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-", result[0]);
        assertEquals("-b", result[1]);
    }
    
    @Test
    public void testFlatten_DoubleHyphenArgument() {
        String[] arguments = {"--", "-b"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("-b", result[1]);
    }
    
    @Test
    public void testFlatten_NoLeadingHyphen() {
        String[] arguments = {"value"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }
    
    @Test
    public void testFlatten_NoLeadingHyphen_StopAtNonOption() {
        String[] arguments = {"value", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("value", result[0]);
        assertEquals("nonOption", result[1]);
    }
}