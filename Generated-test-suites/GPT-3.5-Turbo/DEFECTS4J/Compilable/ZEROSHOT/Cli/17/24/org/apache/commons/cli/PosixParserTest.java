package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "value", "-b"};

        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_DoesNotStopAtNonOption() {
        String[] arguments = {"-a", "-b"};

        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlatten_ArgumentValue() {
        String[] arguments = {"-a", "value"};

        options.addOption("a", true, "Option a");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_MultipleArgumentValues() {
        String[] arguments = {"-a", "value1", "value2"};

        options.addOption("a", true, "Option a");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testFlatten_IgnoreSpecialToken() {
        String[] arguments = {"--"};

        options.addOption("a", false, "Option a");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlatten_SingleHyphenToken() {
        String[] arguments = {"-"};

        options.addOption("a", false, "Option a");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ValidOptionToken() {
        String[] arguments = {"-a"};

        options.addOption("a", false, "Option a");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_InvalidOptionToken_StopAtNonOption() {
        String[] arguments = {"-c"};

        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-c", result[0]);
    }

    @Test
    public void testFlatten_InvalidOptionToken_NoStopAtNonOption() {
        String[] arguments = {"-c"};

        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-c", result[0]);
    }

    @Test
    public void testFlatten_Bursting() {
        String[] arguments = {"-abc"};

        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlatten_Bursting_StopAtNonOption() {
        String[] arguments = {"-abc"};

        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-abc", result[0]);
    }

}