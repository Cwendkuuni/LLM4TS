package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_validOptions() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));

        String[] arguments = {"-a"};

        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_invalidOption() {
        Options options = new Options();

        String[] arguments = {"-b"};

        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_multipleOptionsWithValues() {
        Options options = new Options();
        options.addOption(new Option("a", "option a with value"));

        String[] arguments = {"-a=value"};

        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));

        String[] arguments = {"-a", "nonOption"};

        GnuParser parser = new GnuParser();

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption", result[1]);
    }
}