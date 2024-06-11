package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_WithNoOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_WithSingleOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        String[] arguments = {"-a", "arg1"};
        String[] expected = {"-a", "arg1"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithMultipleOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");
        String[] arguments = {"-a", "-b", "arg1"};
        String[] expected = {"-a", "-b", "arg1"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithSpecialOptionFormat() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Special option format");
        String[] arguments = {"-Dproperty=value"};
        String[] expected = {"-D", "property=value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "nonOption", "-b"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_WithDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-a", "--", "-b"};
        String[] expected = {"-a", "--", "-b"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
}