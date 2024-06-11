package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_basic() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--", "-b", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "arg1", "--", "-b", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_multipleOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] arguments = {"-a", "arg1", "-b", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "arg1", "-b", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialPropertyOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Property Option");
        String[] arguments = {"-Dproperty=value"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-D", "property=value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_unrecognizedOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-x", "arg1"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-x", "arg1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_multipleArgsAfterStop() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "--", "-b", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "arg1", "--", "-b", "arg2", "arg3"};
        assertArrayEquals(expected, result);
    }
}

