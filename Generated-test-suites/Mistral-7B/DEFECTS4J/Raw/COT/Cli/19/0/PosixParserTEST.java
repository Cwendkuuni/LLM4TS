package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link PosixParser} class.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        // Given
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");
        options.addOption("i", "input", true, "input file");
        options.addOption("h", "help", false, "display this help message");

        String[] arguments = {"--output", "output_directory", "--input", "input_file", "-o", "new_output", "--help"};

        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertArrayEquals(new String[]{"--output", "output_directory", "--input", "input_file", "--o", "new_output", "--help"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        // Given
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");
        options.addOption("i", "input", true, "input file");
        options.addOption("h", "help", false, "display this help message");

        String[] arguments = {"--output", "output_directory", "--input", "input_file", "--", "new_option"};

        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, true);

        // Then
        assertArrayEquals(new String[]{"--output", "output_directory", "--input", "input_file", "--", "new_option"}, result);
    }

    /**
     * Test method for {@link PosixParser#process(String)}.
     */
    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();

        // When
        parser.process("--help");

        // Then
        assertNull(parser.getCurrentOption());

        parser.init();
        parser.process("o");
        parser.process("new_option");

        // Then
        assertEquals(parser.getCurrentOption(), options.getOption("o"));
        assertNull(parser.getCurrentOption(), "Current option should be set to 'new_option'.");

        parser.init();
        parser.process("--");
        parser.process("new_option_value");

        // Then
        assertEquals(parser.getCurrentOption(), null);
        assertArrayEquals(new String[]{"--", "new_option_value"}, (String[]) parser.getTokens().toArray());
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)}.
     */
    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();

        // Given
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        // When
        parser.processOptionToken("o", false);

        // Then
        assertEquals(parser.getCurrentOption(), options.getOption("o"));
        assertNull(parser.getCurrentOption(), "Current option should be set to 'o'.");

        parser.init();
        parser.processOptionToken("--", true);

        // Then
        assertNull(parser.getCurrentOption());
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)}.
     */
    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();

        // Given
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        // When & Then
        parser.burstToken("-o", false);
        assertEquals(parser.getTokens().size(), 1);
        assertEquals(parser.getTokens().get(0), "-o");

        parser.init();
        parser.burstToken("--output", false);
        assertEquals(parser.getTokens().size(), 2);
        assertEquals(parser.getTokens().get(0), "--output");
        assertEquals(parser.getTokens().get(1), null);

        parser.init();
        parser.burstToken("--output-directory", false);
        assertEquals(parser.getTokens().size(), 3);
        assertEquals(parser.getTokens().get(0), "--output");
        assertEquals(parser.getTokens().get(1), "--directory");
        assertEquals(parser.getTokens().get(2), null);

        parser.init();
        parser.burstToken("--output-directory=output", false);
        assertEquals(parser.getTokens().size(), 4);
        assertEquals(parser.getTokens().get(0), "--output");
        assertEquals(parser.getTokens().get(1), "--directory");
        assertEquals(parser.getTokens().get(2), "output");
        assertEquals(parser.getTokens().get(3), null);

        parser.init();
        parser.burstToken("--output-directory=output-value", true);
        assertEquals(parser.getTokens().size(), 2);
        assertEquals(parser.getTokens().get(0), "--output-directory=output-value");
        assertEquals(parser.getTokens().get(1), null);

        parser.init();
        parser.burstToken("--output-directory=output-value", false);
        assertEquals(parser.getTokens().size(), 3);
        assertEquals(parser.getTokens().get(0), "--output-directory");
        assertEquals(parser.getTokens().get(1), "output-value");
        assertEquals(parser.getTokens().get(2), null);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        // Given
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");

        String[] arguments = {"--invalid"};

        PosixParser parser = new PosixParser();

        // When
        parser.flatten(options, arguments, false);

        // Then
        // Exception should be thrown
    }
}