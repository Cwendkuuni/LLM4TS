package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithNoOptions() {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2", "arg3"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
        assertEquals("arg3", result[2]);
    }

    @Test
    public void testFlattenWithSingleHyphenOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Option a");
        options.addOption(option);
        String[] arguments = new String[]{"-a", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertTrue(result[0].startsWith("-")); // check if option starts with "-"
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        Option option = new Option("a", "test", false, "Option a");
        options.addOption(option);
        String[] arguments = new String[]{"--test", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertTrue(result[0].startsWith("--")); // check if option starts with "--"
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlattenWithOptionAndArgument() {
        Options options = new Options();
        Option option = new Option("a", true, "Option a");
        options.addOption(option);
        String[] arguments = new String[]{"-a", "value"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertTrue(result[0].startsWith("-")); // check if option starts with "-"
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", false, "Option b");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = new String[]{"-a", "-b", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(3, result.length);
        assertTrue(result[0].startsWith("-")); // check if option starts with "-"
        assertTrue(result[1].startsWith("-")); // check if option starts with "-"
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlattenWithUnknownOptionAndStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertTrue(result[0].startsWith("-")); // check if option starts with "-"
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlattenWithUnknownOptionAndNotStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "arg1"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertTrue(result[0].startsWith("-")); // check if option starts with "-"
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlattenWithBurstTokenAndStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-abc", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(2, result.length);
        assertTrue(result[0].startsWith("-")); // check if option starts with "-"
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlattenWithBurstTokenAndNotStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"-abc", "arg1"};
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(2, result.length);
        assertTrue(result[0].startsWith("-")); // check if option starts with "-"
        assertEquals("arg1", result[1]);
    }
}