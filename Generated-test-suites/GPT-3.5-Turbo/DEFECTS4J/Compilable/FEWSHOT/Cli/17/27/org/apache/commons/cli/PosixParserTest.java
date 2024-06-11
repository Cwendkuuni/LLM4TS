package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "enable-a", false, "Enable option A");
        options.addOption("b", "enable-b", false, "Enable option B");
        options.addOption("c", "enable-c", false, "Enable option C");
        options.addOption("d", "enable-d", false, "Enable option D");
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, new String[]{"-a", "value1", "text", "--", "-b", "value2"}, true);

        assertEquals("-a", tokens[0]);
        assertEquals("value1", tokens[1]);
        assertEquals("text", tokens[2]);
        assertEquals("--", tokens[3]);
        assertEquals("-b", tokens[4]);
        assertEquals("value2", tokens[5]);
    }

    @Test
    public void testFlattenContinue() {
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, new String[]{"-a", "value1", "text", "--", "-b", "value2"}, false);

        assertEquals("-a", tokens[0]);
        assertEquals("value1", tokens[1]);
        assertEquals("text", tokens[2]);
        assertEquals("--", tokens[3]);
        assertEquals("-b", tokens[4]);
        assertEquals("value2", tokens[5]);
    }

    @Test
    public void testFlattenSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, new String[]{"-"}, false);

        assertEquals("-", tokens[0]);
    }

    @Test
    public void testFlattenOptionToken() {
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, new String[]{"-a"}, false);

        assertEquals("-a", tokens[0]);
    }

    @Test
    public void testFlattenBurstToken() {
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, new String[]{"-abc"}, false);

        assertEquals("-a", tokens[0]);
        assertEquals("-b", tokens[1]);
        assertEquals("-c", tokens[2]);
    }

    @Test
    public void testFlattenNonOption() {
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, new String[]{"text"}, false);

        assertEquals("text", tokens[0]);
    }
}