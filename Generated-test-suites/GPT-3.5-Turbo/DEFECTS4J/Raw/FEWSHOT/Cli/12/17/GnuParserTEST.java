import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class GnuParserTest {
    private GnuParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new GnuParser();
        options = new Options();
    }

    @Test
    public void testFlattenWithNoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(arguments.length, result.length);
        assertEquals(arguments[0], result[0]);
        assertEquals(arguments[1], result[1]);
        assertEquals(arguments[2], result[2]);
    }

    @Test
    public void testFlattenWithOptionAtStart() {
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);
        
        String[] arguments = {"-a", "arg1", "arg2", "arg3"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(arguments.length, result.length);
        assertFalse(result[0].startsWith("-"));
        assertEquals(option.getOpt(), result[0]);
        assertEquals(arguments[1], result[1]);
        assertEquals(arguments[2], result[2]);
        assertEquals(arguments[3], result[3]);
    }

    @Test
    public void testFlattenWithInvalidOptionAtStart() {
        String[] arguments = {"-x", "arg1", "arg2", "arg3"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(arguments.length, result.length);
        assertTrue(result[0].startsWith("-"));
        assertEquals(arguments[0], result[0]);
        assertEquals(arguments[1], result[1]);
        assertEquals(arguments[2], result[2]);
        assertEquals(arguments[3], result[3]);
    }

    @Test
    public void testFlattenWithOptionPropertyValue() {
        Option option = new Option("D", "property", true, "Property");
        options.addOption(option);
        
        String[] arguments = {"-Dproperty=value", "arg1", "arg2", "arg3"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(arguments.length, result.length);
        assertEquals(arguments[0], result[0]);
        assertEquals(arguments[1], result[1]);
        assertEquals(arguments[2], result[2]);
        assertEquals(arguments[3], result[3]);
        assertEquals(option.getOpt(), result[4]);
        assertEquals("value", result[5]);
    }

    @Test
    public void testFlattenWithOptionAndStopAtNonOption() {
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);
        
        String[] arguments = {"-a", "arg1", "-b", "arg2", "arg3"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(4, result.length);
        assertFalse(result[0].startsWith("-"));
        assertEquals(option.getOpt(), result[0]);
        assertEquals(arguments[1], result[1]);
        assertEquals("-b", result[2]);
        assertEquals(arguments[3], result[3]);
    }

    @Test
    public void testFlattenWithSeparator() {
        String[] arguments = {"-a", "--", "-b"};

        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(3, result.length);
        assertFalse(result[0].startsWith("-"));
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
    }
}