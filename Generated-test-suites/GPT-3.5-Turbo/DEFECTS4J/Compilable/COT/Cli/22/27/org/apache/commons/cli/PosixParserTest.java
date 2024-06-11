package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "--b=value", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-a", result[0]);
        assertEquals("--b=value", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        PosixParser parser = new PosixParser();

        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "--", "-b", "value", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, true);

        assertEquals("-a", result[0]);
        assertEquals("--", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
    }
}

