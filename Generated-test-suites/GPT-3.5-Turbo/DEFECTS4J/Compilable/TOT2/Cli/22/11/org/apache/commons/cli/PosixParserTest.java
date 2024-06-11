package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Options;

public class PosixParserTest {

    @Test
    public void testFlattenWithNormalOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithLongOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"--long", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("--long", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithMultipleShortOptions() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-abc"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlattenWithNonOptionStopTrue() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "value", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("--", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlattenWithNonOptionStopFalse() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-a", "value", "nonOption"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlattenWithSpecialCharacterOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-+", "value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-+", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithBurstOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = {"-abc"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }
}