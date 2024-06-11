package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");

        String[] args = {"--a", "--b=1", "--c", "--d=4", "--e=5", "-c", "-d", "arg"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--a", "--b", "--c", "--d", "--e", "-c", "-d", "arg"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenWithStopAtNonOptionTrue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--a", "--b", "arg1", "arg2"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--a", "--b", "arg1", "arg2"}, result);
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)} with a valid option.
     */
    @Test
    public void testProcessOptionTokenWithValidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-a", false);

        assertEquals("Option A", parser.currentOption.getLongOpt());
        assertTrue(parser.currentOption.hasArg());
        assertEquals(0, parser.tokens.size());
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)} with an invalid option.
     */
    @Test
    public void testProcessOptionTokenWithInvalidOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-x", false);

        assertNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals("-x", parser.tokens.get(0));
    }

    /**
     * Test method for {@link PosixParser#processOptionToken(String, boolean)} with stopAtNonOption set to true.
     */
    @Test
    public void testProcessOptionTokenWithStopAtNonOptionTrue() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-x", true);

        assertNull(parser.currentOption);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-x", parser.tokens.get(1));
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)} with a valid option.
     */
    @Test
    public void testBurstTokenWithValidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        PosixParser parser = new PosixParser();
        parser.burstToken("-ab", false);

        assertEquals(3, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertTrue(parser.currentOption == null || parser.currentOption.hasArg());
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)} with an invalid option.
     */
    @Test
    public void testBurstTokenWithInvalidOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.burstToken("-x", false);

        assertEquals(1, parser.tokens.size());
        assertEquals("-x", parser.tokens.get(0));
        assertNull(parser.currentOption);
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)} with an invalid option and stopAtNonOption set to true.
     */
    @Test
    public void testBurstTokenWithInvalidOptionAndStopAtNonOptionTrue() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.burstToken("-x", true);

        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("-x", parser.tokens.get(1));
    }

    /**
     * Test method for {@link PosixParser#burstToken(String, boolean)} with a valid option and an argument.
     */
    @Test
    public void testBurstTokenWithValidOptionAndArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        PosixParser parser = new PosixParser();
        parser.burstToken("-ab=value", false);

        assertEquals(4, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));
        assertTrue(parser.currentOption == null || parser.currentOption.hasArg());
    }
}