package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_basic() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "--", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "--", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_specialPropertiesOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Special properties option");

        String[] arguments = {"-Dproperty=value"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-D", "property=value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_dashOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"-"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_doubleDashOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"--"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_emptyArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {};
        assertArrayEquals(expected, result);
    }
}