package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();

        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "value", "--", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(result.length, 6);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "-b");
        assertEquals(result[2], "value");
        assertEquals(result[3], "--");
        assertEquals(result[4], "arg1");
        assertEquals(result[5], "arg2");
    }

    @Test
    public void testFlattenWithNoOptions() {
        GnuParser parser = new GnuParser();

        Options options = new Options();

        String[] arguments = {"arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(result.length, 2);
        assertEquals(result[0], "arg1");
        assertEquals(result[1], "arg2");
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        GnuParser parser = new GnuParser();

        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "--", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(result.length, 4);
        assertEquals(result[0], "-a");
        assertEquals(result[1], "-b");
        assertEquals(result[2], "--");
        assertEquals(result[3], "arg1");
    }

    @Test
    public void testFlattenWithNoOptionsAndStopAtNonOption() {
        GnuParser parser = new GnuParser();

        Options options = new Options();

        String[] arguments = {"arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(result.length, 2);
        assertEquals(result[0], "arg1");
        assertEquals(result[1], "arg2");
    }
}