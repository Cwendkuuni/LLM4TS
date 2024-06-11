package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new GnuParser();
        options = new Options();
        options.addOption("h", "help", false, "Print help message");
        options.addOption("v", "verbose", false, "Enable verbose output");
        options.addOption("o", "output", true, "Specify output file");
    }

    @Test
    public void testFlatten_WithNormalArguments_ReturnsFlattenedArguments() {
        String[] arguments = new String[]{
                "-v", "--output", "file.txt", "arg1", "arg2"
        };

        List<String> expected = Arrays.asList("-v", "--output", "file.txt", "arg1", "arg2");
        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected.toArray(), actual);
    }

    @Test
    public void testFlatten_WithStopAtNonOption_ReturnsFlattenedArgumentsUntilNonOptionIsEncountered() {
        String[] arguments = new String[]{
                "-v", "--output", "file.txt", "arg1", "arg2", "nonOption"
        };

        List<String> expected = Arrays.asList("-v", "--output", "file.txt", "arg1", "arg2", "nonOption");
        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected.toArray(), actual);
    }

    @Test
    public void testFlatten_WithDoubleHyphenSeparator_ReturnsFlattenedArgumentsIncludingSeparator() {
        String[] arguments = new String[]{
                "-v", "--output", "file.txt", "--", "arg1", "arg2"
        };

        List<String> expected = Arrays.asList("-v", "--output", "file.txt", "--", "arg1", "arg2");
        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected.toArray(), actual);
    }

    @Test
    public void testFlatten_WithInvalidOption_ReturnsFlattenedArgumentsIncludingInvalidOption() {
        String[] arguments = new String[]{
                "-v", "--output", "file.txt", "-x", "arg1", "arg2"
        };

        List<String> expected = Arrays.asList("-v", "--output", "file.txt", "-x", "arg1", "arg2");
        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected.toArray(), actual);
    }

    @Test
    public void testFlatten_WithPropertiesOption_ReturnsFlattenedArgumentsWithSeparatedProperty() {
        String[] arguments = new String[]{
                "-Dproperty=value"
        };

        List<String> expected = Arrays.asList("-D", "property=value");
        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected.toArray(), actual);
    }

}