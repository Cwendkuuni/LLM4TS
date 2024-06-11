package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class GnuParserTest {

    @Test
    public void testFlatten_withOptions() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlatten_withNonOptionStop() {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "arg1", "nonOption", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("arg1", result[1]);
    }

    @Test
    public void testFlatten_withSpecialPropertiesOption() {
        Options options = new Options();
        options.addOption("D", true, "special properties option");

        GnuParser parser = new GnuParser();
        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlatten_withDoubleDash() {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "--", "arg1"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("arg1", result[2]);
    }

    @Test
    public void testFlatten_withSingleDash() {
        Options options = new Options();
        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "-", "arg1"};
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-", result[1]);
        assertEquals("arg1", result[2]);
    }

}