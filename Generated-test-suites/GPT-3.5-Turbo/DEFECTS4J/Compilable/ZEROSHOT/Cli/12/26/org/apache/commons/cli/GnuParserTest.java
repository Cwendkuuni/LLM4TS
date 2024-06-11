package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;

    @Before
    public void setUp() {
        parser = new GnuParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("h", "help", false, "Display help message");

        String[] args = {"-h", "input.txt"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-h", result[0]);
        assertEquals("input.txt", result[1]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "Specify file name");
        options.addOption("p", "port", true, "Specify port number");

        String[] args = {"-f", "config.txt", "-p", "8080", "input.txt"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("config.txt", result[1]);
        assertEquals("-p", result[2]);
    }

    @Test
    public void testFlattenWithNoOptions() {
        Options options = new Options();

        String[] args = {"input.txt"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(1, result.length);
        assertEquals("input.txt", result[0]);
    }

    @Test
    public void testFlattenWithMultipleOptionsStartWithHyphen() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        String[] args = {"-ab"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }

    @Test
    public void testFlattenWithDoubleHyphen() {
        Options options = new Options();
        options.addOption("f", "file", true, "Specify file name");

        String[] args = {"--", "input.txt"};

        String[] result = parser.flatten(options, args, true);

        assertEquals(2, result.length);
        assertEquals("--", result[0]);
        assertEquals("input.txt", result[1]);
    }

}