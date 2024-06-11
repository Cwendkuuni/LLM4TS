package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_InputArgumentsWithLongOption_ReturnsFlattenedArguments() {
        String[] arguments = {"--verbose", "--input", "file.txt", "--output", "output.txt"};
        String[] expected = {"--verbose", "--input", "file.txt", "--output", "output.txt"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_InputArgumentsWithShortOption_ReturnsFlattenedArguments() {
        String[] arguments = {"-v", "-i", "file.txt", "-o", "output.txt"};
        String[] expected = {"-v", "-i", "file.txt", "-o", "output.txt"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_InputArgumentsWithUnknownOptionAndStopAtNonOptionFalse_ReturnsFlattenedArguments() {
        String[] arguments = {"-v", "-i", "file.txt", "-o", "output.txt", "-a"};
        String[] expected = {"-v", "-i", "file.txt", "-o", "output.txt", "-a"};

        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_InputArgumentsWithUnknownOptionAndStopAtNonOptionTrue_ReturnsFlattenedArgumentsWithoutUnknownOption() {
        String[] arguments = {"-v", "-i", "file.txt", "-o", "output.txt", "-a"};
        String[] expected = {"-v", "-i", "file.txt", "-o", "output.txt"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_InputArgumentsWithSingleHyphen_ReturnsFlattenedArguments() {
        String[] arguments = {"-"};
        String[] expected = {"-"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_InputArgumentsWithNonOption_ReturnsFlattenedArguments() {
        String[] arguments = {"input.txt", "-v", "output.txt"};
        String[] expected = {"input.txt", "-v", "output.txt"};

        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);
    }

}