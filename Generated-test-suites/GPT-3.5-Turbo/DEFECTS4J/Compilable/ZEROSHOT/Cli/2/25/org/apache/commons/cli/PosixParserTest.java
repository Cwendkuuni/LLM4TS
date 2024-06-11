package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        options.addOption("d", false, "Option d");
        options.addOption("e", false, "Option e");
        options.addOption("f", false, "Option f");

        String[] arguments = { "-a", "-b", "value", "-c", "value1", "value2", "-def" };
        String[] expected = { "-a", "-b", "value", "-c", "value1", "value2", "-d", "-e", "-f" };

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        options.addOption("d", false, "Option d");
        options.addOption("e", false, "Option e");
        options.addOption("f", false, "Option f");

        String[] arguments = { "-a", "-b", "value", "-c", "value1", "value2", "-def" };
        String[] expected = { "-a", "-b", "value", "-c", "value1", "value2", "-def" };

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        String[] arguments = { "value1", "value2", "value3" };
        String[] expected = { "value1", "value2", "value3" };

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {};
        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}