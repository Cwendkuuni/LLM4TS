
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("D", true, "property=value"));
        options.addOption(new Option("f", false, "flag"));

        String[] arguments = { "-Dproperty=value", "-f", "--", "-foo", "bar", "baz" };
        String[] expected = { "-Dproperty=value", "-f", "--", "-foo", "bar", "baz" };

        GnuParser parser = new GnuParser();
        String[] actual = parser.flatten(options, arguments, true);

        assertNotNull(actual);
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("D", true, "property=value"));
        options.addOption(new Option("f", false, "flag"));

        String[] arguments = { "-Dproperty=value", "-f", "--", "-foo", "bar", "baz" };
        String[] expected = { "-Dproperty=value", "-f" };

        GnuParser parser = new GnuParser();
        String[] actual = parser.flatten(options, arguments, false);

        assertNotNull(actual);
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        options.addOption(new Option("D", true, "property=value"));
        options.addOption(new Option("f", false, "flag"));

        String[] arguments = {};
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] actual = parser.flatten(options, arguments, true);

        assertNotNull(actual);
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testFlattenNullArguments() {
        Options options = new Options();
        options.addOption(new Option("D", true, "property=value"));
        options.addOption(new Option("f", false, "flag"));

        String[] arguments = null;
        String[] expected = null;

        GnuParser parser = new GnuParser();
        String[] actual = parser.flatten(options, arguments, true);

        assertEquals(expected, actual);
    }
}
