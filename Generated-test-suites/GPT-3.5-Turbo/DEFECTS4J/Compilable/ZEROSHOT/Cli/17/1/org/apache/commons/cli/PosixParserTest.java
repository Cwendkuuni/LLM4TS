package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "apple", false, "apple option");
        options.addOption("b", "banana", true, "banana option");

        PosixParser parser = new PosixParser();

        String[] args = {"-a", "-b", "value"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a", "-b", "value"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "apple", false, "apple option");
        options.addOption("b", "banana", true, "banana option");

        PosixParser parser = new PosixParser();

        String[] args = {"-a", "-b", "value", "extra"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"-a", "-b", "value", "extra"};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithSpecialToken() {
        Options options = new Options();
        options.addOption("a", "apple", false, "apple option");
        options.addOption("b", "banana", true, "banana option");

        PosixParser parser = new PosixParser();

        String[] args = {"--apple", "--banana=value"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--apple", "--banana", "value"};

        assertArrayEquals(expected, result);
    }
}