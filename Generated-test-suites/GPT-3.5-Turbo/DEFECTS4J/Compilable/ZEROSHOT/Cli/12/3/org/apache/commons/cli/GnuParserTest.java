package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value1", "--option2", "value2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("--option2", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlattenWithStopAtNonOptionFalse() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value1", "--option2", "value2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("--option2", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlattenWithNoOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "value1", "--option2", "value2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("--option2", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "--", "--option2", "value2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("--", result[2]);
        assertEquals("--option2", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlattenWithSingleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "-b", "-", "--option2", "value2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-", result[2]);
        assertEquals("--option2", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlattenWithOptionSubstring() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("opt", true, "Option");

        String[] arguments = {"-optvalue"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-optvalue", result[0]);
    }

    @Test
    public void testFlattenWithUnknownOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-a"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlattenWithNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-a", "-b", "value1", "--option2", "value2", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value1", result[2]);
        assertEquals("--option2", result[3]);
        assertEquals("value2", result[4]);
        assertEquals("nonOption", result[5]);
    }

}