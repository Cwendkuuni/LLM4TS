package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-a", "value1", "arg1"};
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "value1", "arg1"};
        boolean stopAtNonOption = true;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlattenWithMultiCharacterOptions() {
        Options options = new Options();
        options.addOption("abc", false, "Option abc");
        String[] arguments = {"-abc", "value1", "arg1"};
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(3, result.length);
        assertEquals("-abc", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlattenWithUnknownOptionAndStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value1", "arg1"};
        boolean stopAtNonOption = true;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }
}