package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();

        String[] arguments = { "-a", "-b", "value", "-c", "arg1", "arg2" };
        String[] expected = { "-a", "-b", "value", "-c", "arg1", "arg2" };

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", false, "option c");

        PosixParser parser = new PosixParser();

        String[] arguments = { "-abc", "--", "arg1", "arg2" };
        String[] expected = { "-a", "-b", "-c", "--", "arg1", "arg2" };

        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }
}