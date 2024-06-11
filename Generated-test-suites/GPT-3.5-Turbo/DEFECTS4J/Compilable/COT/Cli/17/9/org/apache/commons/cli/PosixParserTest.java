package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // test method's functionality
        Options options = new Options();
        String[] arguments = {"-a", "value", "--option=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(4, result.length);
        assertEquals("-", result[0]);
        assertEquals("a", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--option=value", result[3]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        // test method's functionality with stopAtNonOption = true
        Options options = new Options();
        String[] arguments = {"-a", "value", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(5, result.length);
        assertEquals("-", result[0]);
        assertEquals("a", result[1]);
        assertEquals("value", result[2]);
        assertEquals("--", result[3]);
        assertEquals("non-option", result[4]);
    }

    @Test
    public void testFlattenHyphen() {
        // test method's functionality with hyphen token
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlattenNonOption() {
        // test method's functionality with non-option token
        Options options = new Options();
        String[] arguments = {"non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(1, result.length);
        assertEquals("non-option", result[0]);
    }
}