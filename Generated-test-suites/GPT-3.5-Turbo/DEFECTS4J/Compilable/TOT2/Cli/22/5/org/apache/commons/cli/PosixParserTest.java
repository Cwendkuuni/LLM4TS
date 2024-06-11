package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = {"--option1", "value1", "-o", "value2"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertNotNull(flattened);
        assertEquals(4, flattened.length);
        assertEquals("--option1", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("-o", flattened[2]);
        assertEquals("value2", flattened[3]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = {"--option1", "value1", "nonOption", "--option2", "value2"};
        String[] flattened = parser.flatten(options, arguments, true);

        assertNotNull(flattened);
        assertEquals(4, flattened.length);
        assertEquals("--option1", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("nonOption", flattened[2]);
        assertEquals("--option2", flattened[3]);
    }

    @Test
    public void testFlattenBurstToken() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] arguments = {"-abc", "value1", "-o", "value2"};
        String[] flattened = parser.flatten(options, arguments, false);

        assertNotNull(flattened);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("-c", flattened[2]);
        assertEquals("value1", flattened[3]);
    }

}