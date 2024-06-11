package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser posixParser;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b with argument");
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = { "arg1", "arg2" };
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = { "-" };
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionWithArgument() {
        String[] arguments = { "-b", "value" };
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals("-b", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_DoubleHyphenOption() {
        String[] arguments = { "--option", "value" };
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_DoubleHyphenOptionWithValueContainingEqualSign() {
        String[] arguments = { "--option=value" };
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_InvalidOptionWithStopAtNonOptionTrue() {
        String[] arguments = { "-c", "value" };
        String[] result = posixParser.flatten(options, arguments, true);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_InvalidOptionWithStopAtNonOptionFalse() {
        String[] arguments = { "-c", "value" };
        String[] result = posixParser.flatten(options, arguments, false);
        assertEquals("-c", result[0]);
        assertEquals("value", result[1]);
    }
}