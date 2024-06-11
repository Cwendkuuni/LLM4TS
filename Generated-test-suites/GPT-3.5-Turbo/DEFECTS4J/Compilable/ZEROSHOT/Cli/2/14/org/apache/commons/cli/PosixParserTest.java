package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class PosixParserTest {

    @Test
    public void testFlatten_ValidInput_ReturnsArray() {
        Options options = new Options();
        String[] arguments = {"-a", "value"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsArray() {
        Options options = new Options();
        String[] arguments = {"-a", "value", "nonOption"};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlatten_NoOptions_ReturnsArray() {
        Options options = new Options();
        String[] arguments = {"value"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }

    @Test
    public void testFlatten_SpecialToken_ReturnsArray() {
        Options options = new Options();
        String[] arguments = {"--option=value"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_SingleHyphen_ReturnsArray() {
        Options options = new Options();
        String[] arguments = {"-"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_InvalidOption_ReturnsArray() {
        Options options = new Options();
        String[] arguments = {"-x"};
        boolean stopAtNonOption = false;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_ReturnsArray() {
        Options options = new Options();
        String[] arguments = {"-x", "value", "nonOption"};
        boolean stopAtNonOption = true;

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-x", result[0]);
        assertEquals("value", result[1]);
        assertEquals("nonOption", result[2]);
    }
}