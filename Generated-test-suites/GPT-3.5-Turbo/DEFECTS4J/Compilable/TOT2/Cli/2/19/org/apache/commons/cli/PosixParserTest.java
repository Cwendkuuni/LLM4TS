package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b with argument");
        options.addOption("c", true, "option c with multiple arguments");

        String[] arguments = {"-a", "-b", "argumentB", "--", "-c", "arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("Flattened array size should be 7", 7, flattened.length);
        assertEquals("First element should be -a", "-a", flattened[0]);
        assertEquals("Second element should be -b", "-b", flattened[1]);
        assertEquals("Third element should be argumentB", "argumentB", flattened[2]);
        assertEquals("Fourth element should be --", "--", flattened[3]);
        assertEquals("Fifth element should be -c", "-c", flattened[4]);
        assertEquals("Sixth element should be arg1", "arg1", flattened[5]);
        assertEquals("Seventh element should be arg2", "arg2", flattened[6]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b with argument");

        String[] arguments = {"-a", "nonOption", "-b", "argumentB"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        assertEquals("Flattened array size should be 4", 4, flattened.length);
        assertEquals("First element should be -a", "-a", flattened[0]);
        assertEquals("Second element should be nonOption", "nonOption", flattened[1]);
        assertEquals("Third element should be -b", "-b", flattened[2]);
        assertEquals("Fourth element should be argumentB", "argumentB", flattened[3]);
    }

    @Test
    public void testFlattenWithInvalidOptions() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = {"-a", "-d"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        assertEquals("Flattened array size should be 2", 2, flattened.length);
        assertEquals("First element should be -a", "-a", flattened[0]);
        assertEquals("Second element should be -d", "-d", flattened[1]);
    }
}